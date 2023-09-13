package com.xuecheng.filesystem.service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.filesystem.dao.FileSystemRepository;
import com.xuecheng.framework.domain.filesystem.FileSystem;
import com.xuecheng.framework.domain.filesystem.response.FileSystemCode;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;

import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import org.apache.commons.lang3.StringUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.Cleaner;

import java.io.IOException;
import java.util.Map;

@Service
public class FileSystemService {
    //注入yml文件内容
    @Value("${xuecheng.fastdfs.tracker_servers}")
    String tracker_servers;
    @Value("${xuecheng.fastdfs.connect_timeout_in_seconds}")
    int connect_timeout_in_seconds;
    @Value("${xuecheng.fastdfs.network_timeout_in_seconds}")
    int network_timeout_in_seconds;
    @Value("${xuecheng.fastdfs.charset}")
    String charset;
    //注入Dao
    @Autowired
    FileSystemRepository fileSystemRepository;
    public UploadFileResult upload(MultipartFile multipartFileMultipartFile,
                                   String businesskey,
                                   String filetag,
                                   String metadata){
        if (multipartFileMultipartFile == null) {
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_FILEISNULL);
        }
    //文件上传FastDFS中，返回文件ID
        String fileId = uploadFastDFS(multipartFileMultipartFile);
        if (StringUtils .isEmpty(fileId)) {
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_SERVERFAIL);
        }
        //将ID和其他信息存到MongoDB中
        FileSystem fileSystem = new FileSystem();
        fileSystem.setFileId(fileId);
        fileSystem.setFilePath(fileId);
        fileSystem.setFiletag(filetag);
        fileSystem.setBusinesskey(businesskey);
        fileSystem.setFileName(multipartFileMultipartFile.getOriginalFilename());
        fileSystem.setFileType(multipartFileMultipartFile.getContentType());
        //metadata把JSON格式转换成Map
        if (StringUtils.isNotEmpty(metadata)) {
            try {
                Map map_metadata = JSON.parseObject(metadata, Map.class);
                fileSystem.setMetadata(map_metadata);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        fileSystemRepository.save(fileSystem);
        return new UploadFileResult(CommonCode.SUCCESS,fileSystem);
    }


    //文件上传FastDFS中，返回文件ID

    /**
     *
     * @param multipartFile 上传的文件
     * @return              Fast DFS返回的文件ID
     */
    private String uploadFastDFS(MultipartFile multipartFile){
        //初始化Fast DFS环境
        initFastDFSConfig();
        //创建trackerClient
        TrackerClient trackerClient = new TrackerClient();
        try {
            //tracker获取连接
            TrackerServer trackerServer = trackerClient.getConnection();
            //从trackerclient获取storage
            StorageServer storeStorage = trackerClient.getStoreStorage(trackerServer);
            //创建StorageClient来上传文件
            StorageClient1 storageClient1 = new StorageClient1(trackerServer,storeStorage);
            //上传文件
                //得到文件字节
                byte[] bytes = multipartFile.getBytes();
                //得到文件原始名称
                String originalFilename = multipartFile.getOriginalFilename();
                //得到拓展名
            String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            String fileID = storageClient1.upload_appender_file1(bytes,ext,null);
            return fileID;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private void initFastDFSConfig(){
//        ClientGlobal clientGlobal = new ClientGlobal();

        try {
            //初始化tracker服务地址
            ClientGlobal.initByTrackers(tracker_servers);
            ClientGlobal.setG_charset(charset);
            ClientGlobal.setG_connect_timeout(connect_timeout_in_seconds);
            ClientGlobal.setG_network_timeout(connect_timeout_in_seconds);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
    }
}

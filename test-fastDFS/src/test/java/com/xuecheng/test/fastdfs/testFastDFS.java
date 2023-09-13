package com.xuecheng.test.fastdfs;

import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Administrator
 * @version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class testFastDFS {

    //上传文件
    @Test
    public void testUpload(){
        try {
            //加载配置文件
            ClientGlobal.initByProperties("config/fastdfs-client.properties");
            //定义TrackerClient,请求Tracker服务
            TrackerClient trackerClient = new TrackerClient();
            //连接Tracker
            TrackerServer trackerServer = trackerClient.getConnection();
            //获取Storage
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);

            //向Storage上传文件--------StorageClient
            StorageClient1 storageClient = new StorageClient1(trackerServer,storageServer);
            //上传文件
            //本地文件路径、
            String filePath = "B:/1XueChengZaiXian/xcEduService01/test-fastDFS/src/main/resources/image/octocat-spinner-128.gif";
            //上传成功返回FileId
            String fileId = storageClient.upload_appender_file1(filePath, "gif", null);
            System.out.println(fileId);
            //group1/M00/00/00/wKhMgmTYsnGED_GPAAAAAIevy2g252.gif
            //group1/M00/00/00/wKhMg2TY5cyELiKPAAAAAIevy2g693.gif



        } catch (IOException e) {
            e.printStackTrace();
        }






        catch (MyException e) {
            e.printStackTrace();
        }

    }


    //下载文件
    @Test
    public void testDownload(){
        try {
            //加载fastdfs-client.properties配置文件
            ClientGlobal.initByProperties("config/fastdfs-client.properties");
            //定义TrackerClient，用于请求TrackerServer
            TrackerClient trackerClient = new TrackerClient();
            //连接tracker
            TrackerServer trackerServer = trackerClient.getConnection();
            //获取Stroage
            StorageServer storeStorage = trackerClient.getStoreStorage(trackerServer);
            //创建stroageClient
            StorageClient1 storageClient1 = new StorageClient1(trackerServer,storeStorage);
            //下载文件
            //文件id
            String fileId = "group1/M00/00/00/wKhMg2TUnd2EQaW3AAAAAIevy2g545.gif";
            byte[] bytes = storageClient1.download_file1(fileId);
            //使用输出流保存文件
            FileOutputStream fileOutputStream = new FileOutputStream(new File("B:/1XueChengZaiXian/xcEduService01/test-fastDFS/src/main/resources/image/logo.gif"));
            fileOutputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
    }



}

package com.xuecheng.api.filesystem;


import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Api(value = "文件管理接口",description = "文件管理接口")
public interface FileSystemControllerApi {
    //上传文件
    @ApiOperation("上传文件")
    public UploadFileResult upload(MultipartFile multipartFile,
                                   String businesskey,
                                   String filetag,
                                   String metadata);

}

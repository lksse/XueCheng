package com.xuecheng.framework.domain.course.response;

import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class CoursepublishResult extends ResponseResult {
    String previewUrl;
    public CoursepublishResult(ResultCode resultCode,String previewUrl){
        super(resultCode);
        this.previewUrl = previewUrl;

    }
}

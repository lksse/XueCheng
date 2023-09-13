package com.xuecheng.framework.domain.cms.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryPageRequest {
    //接收页面查询条件
    //站点ID
    @ApiModelProperty("站点id")
    private String siteId;
    //页面ID
    @ApiModelProperty("站点id")
    private String pageId;
    //页面名称
    @ApiModelProperty("页面名称")
    private String pageName;
    //别名
    @ApiModelProperty("别名")
    private String pageAliase;
    //模板ID
    @ApiModelProperty("模板ID")
    private String templateId;

}

package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "cms页面管理接口", description = "cms页面管理接口，提供页面的增删改查")
public interface CmsPageControllerApi {
    //页面查询
    @ApiOperation("分页查询列表页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页 码", required = true, paramType = "path", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页记录 数", required = true, paramType = "path", dataType = "int")})
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);
    //新增页面
    @ApiOperation("新增页面")
    public CmsPageResult add(CmsPage cmsPage);

    @ApiOperation("通过ID查找页面")
    public CmsPage findById(String id);

    @ApiOperation("修改页面")
    public CmsPageResult edit(String id,CmsPage cmsPage);

    @ApiOperation("删除页面")
    public ResponseResult delById(String id);

    //页面发布
    @ApiOperation("页面发布")
    public ResponseResult post(String id);

    //保存页面
    @ApiOperation("保存页面")
    public CmsPageResult save(CmsPage cmsPage);

    //页面一键发布
    @ApiOperation("页面一键发布")
    public CmsPostPageResult postPageQuick(CmsPage cmsPage);

}

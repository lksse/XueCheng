package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

// 对于MongoDB的Dao ，继承MongoRepository，其中CmsPage为模型，String为组件的类型
public interface CmsPageSiteRepository extends MongoRepository<CmsSite,String> {
                                            //关于实现在，在Test中，MongoRepository有很多可以直接使用的查询方法
         //根据页面名称查询


}


package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.course.CourseGrade;
import com.xuecheng.framework.domain.system.SysDictionary;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CmsSysRepository extends MongoRepository<SysDictionary,String> {
    CourseGrade findByDType(String dType);
}

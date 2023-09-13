package com.xuecheng.framework.domain.course;

import com.xuecheng.framework.domain.system.SysDictionaryValue;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.util.List;
@Data
@ToString
@Document(collection = "sys_dictionary")
public class CourseGrade {
    @Id
    private String id;
    @Field("d_name")
    private String dName;
    @Field("d_type")
    private String dType;
    @Field("d_value")
    private List<SysDictionaryValue> dValue;

}

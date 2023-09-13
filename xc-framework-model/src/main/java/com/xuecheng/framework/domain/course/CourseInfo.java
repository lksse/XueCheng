package com.xuecheng.framework.domain.course;

public class CourseInfo {
    private String id;
    private String name;

    public CourseInfo(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public CourseInfo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

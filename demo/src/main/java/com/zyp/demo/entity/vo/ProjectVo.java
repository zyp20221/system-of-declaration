package com.zyp.demo.entity.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProjectVo {
    private Long pid;
    private String projectName;
    private String projectType;
    private String projectYear;
    private Integer projectNumbers;
    private Integer projectStatus;
    private String projectNotice;
    private LocalDateTime endTime;
    private LocalDateTime createTime;
    private  LocalDateTime publishTime;
    private LocalDateTime updateTime;

}

package com.zyp.demo.entity.vo;

import lombok.Data;

@Data
public class DeclareVo {
    private Long bid;
    private Long projectId;
    private String declarationName;
    private String projectName;
    private String projectType;
    private String projectYear;
    private Integer projectLevel;
    private String username;
    private String adviser;
    private Integer modificationStatus;
    private Integer checkStatus;
    private String processStatus;

    private String nodeName;

    private String result;
    private Integer pingShen;
}

package com.zyp.demo.entity.vo;

import io.swagger.models.auth.In;
import lombok.Data;

@Data
public class ProjectCount {
    private Integer published;
    private Integer ongoing;
    private Integer stopped;
}

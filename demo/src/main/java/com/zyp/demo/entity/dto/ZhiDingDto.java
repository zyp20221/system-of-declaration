package com.zyp.demo.entity.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "用来为申报书指定评审老师的实体")
public class ZhiDingDto {
    private List<Long> ids;
    private List<Long> existingIds;
}

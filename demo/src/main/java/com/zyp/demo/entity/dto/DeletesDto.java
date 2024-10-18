package com.zyp.demo.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "批量删除实体")
public class DeletesDto {
    private List<Long> ids;
}

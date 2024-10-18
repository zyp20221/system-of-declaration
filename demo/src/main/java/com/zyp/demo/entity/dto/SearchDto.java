package com.zyp.demo.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "搜索实体")
public class SearchDto {
    @ApiModelProperty(value = "所属批次", required = true)
    private String selectedBatch;
    @ApiModelProperty(value = "项目名称", required = true)
    private String projectName;
    @ApiModelProperty(value = "项目编号", required = true)
    private String projectCode;
    @ApiModelProperty(value = "项目类型", required = true)
    private String selectedType;

}

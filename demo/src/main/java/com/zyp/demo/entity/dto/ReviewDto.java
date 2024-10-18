package com.zyp.demo.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "评审的实体")
public class ReviewDto {
    @ApiModelProperty(value = "指导意见", required = true)
    private String teachersAdvice;
    @ApiModelProperty(value = "评审意见", required = true)
    private String pingshenAdvice;


}

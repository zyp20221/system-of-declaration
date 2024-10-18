package com.zyp.demo.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel(description = "添加评审老师实体")
public class PingShenDto {
    @ApiModelProperty(value = "被选中的申报书编号",required = true)
    @NotNull(message = "不可以为空")
    private List<Long> ids;
    @ApiModelProperty(value = "评审实体",required = true)
    @NotNull(message = "不可以为空")
    private PingShenInformation reviewer;
}

package com.zyp.demo.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PingShenInformation{
    @ApiModelProperty(value = "工号", required = true)
    @NotNull(message = "不可以为空")
    private Long gongHao;
    @ApiModelProperty(value = "用户姓名", required = true)
    @NotNull(message = "不可以为空")
    private String username;
}

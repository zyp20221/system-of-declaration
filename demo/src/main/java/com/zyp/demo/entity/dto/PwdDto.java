package com.zyp.demo.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "修改密码的实体")
public class PwdDto {
    @ApiModelProperty(value = "旧密码", required = true)
    @NotNull(message = "旧密码不能为空")
    private String oldPassword;
    @ApiModelProperty(value = "新密码", required = true)
    @NotNull(message = "新密码不能为空")
    private String newPassword;
    @ApiModelProperty(value = "确认新密码", required = true)
    @NotNull(message = "不能为空")
    private String confirmPassword;
}

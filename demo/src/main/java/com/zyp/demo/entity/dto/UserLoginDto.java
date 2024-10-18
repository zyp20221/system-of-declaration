package com.zyp.demo.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotNull;
@Data
@ApiModel(description = "登录表单实体")
public class UserLoginDto {
    @ApiModelProperty(value = "工学号", required = true)
    @NotNull(message = "工学号不能为空")
    private String gxh;
    @ApiModelProperty(value = "邮箱", required = true)
    @NotNull(message = "邮箱不能为空")
    private String email;
    @ApiModelProperty(value = "电话号码", required = true)
    @NotNull(message = "电话号码不能为空")
    private String phoneNumber;
    @ApiModelProperty(value = "密码", required = true)
    @NotNull(message = "密码不能为空")
    private String pwd;
}

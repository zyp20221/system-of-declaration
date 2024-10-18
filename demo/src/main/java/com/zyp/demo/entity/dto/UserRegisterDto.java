package com.zyp.demo.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "注册表单实体")
public class UserRegisterDto {
    @ApiModelProperty(value = "姓名", required = true)
    @NotNull(message = "姓名不能为空")
    private String username;
    @ApiModelProperty(value = "工学号", required = true)
    @NotNull(message = "工学号不能为空")
    private String gxh;
    @ApiModelProperty(value = "电话号码", required = true)
    @NotNull(message = "电话号码不能为空")
    private String phoneNumber;
    @ApiModelProperty(value = "邮箱", required = true)
    @NotNull(message = "工学号不能为空")
    private String email;
    @ApiModelProperty(value = "身份", required = true)
    @NotNull(message = "身份不能为空")
    private String identify;
    @ApiModelProperty(value = "职称", required = true)
    @NotNull(message = "这个必须要identify为2时才不为空")
    private String zhicheng;
    @ApiModelProperty(value = "密码", required = true)
    @NotNull(message = "密码不能为空")
    private String pwd;
}

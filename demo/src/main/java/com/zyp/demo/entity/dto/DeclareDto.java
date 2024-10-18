package com.zyp.demo.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "申报书实体")
public class DeclareDto {
    @ApiModelProperty(value = "所属批次", required = true)
    @NotNull(message = "所属批次不能为空")
    private String projectYear;
    @ApiModelProperty(value = "申报书名称", required = true)
    @NotNull(message = "申报书名称不能为空")
    private String declarationName;
    @ApiModelProperty(value = "项目类型", required = true)
    @NotNull(message = "项目类型不能为空")
    private String projectType;
    @ApiModelProperty(value = "项目简介", required = true)
    @NotNull(message = "项目简介不能为空")
    private String projectBrief;
    @ApiModelProperty(value = "项目成员", required = true)
    @NotNull(message = "项目成员不能为空")
    private String projectMember;
    @ApiModelProperty(value = "指导老师", required = true)
    @NotNull(message = "指导老师不能为空")
    private String  adviserId;
    @ApiModelProperty(value = "申报内容", required = true)
    @NotNull(message = "申报内容不能为空")
    private String declarationContent;
    @ApiModelProperty(value = "申报书修改状态", required = true)
    private String modificationStatus;
    @ApiModelProperty(value = "申报书查询状态", required = true)
    private String checkStatus;
    @ApiModelProperty(value = "项目id", required = true)
    private Long projectId;
    @ApiModelProperty(value = "项目名称", required = true)
    private String projectName;
    @ApiModelProperty(value = "附件路径", required = true)
    private String filePath;
    @ApiModelProperty(value = "附件名称", required = true)
    private String fileName;
}

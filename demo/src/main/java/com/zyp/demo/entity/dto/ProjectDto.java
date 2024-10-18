package com.zyp.demo.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@ApiModel(description = "项目发布实体")
public class ProjectDto {
    @ApiModelProperty(value = "项目名称", required = true)
    @NotNull(message = "项目名称不能为空")
    private String projectName;
    @ApiModelProperty(value = "项目类型", required = true)
    @NotNull(message = "项目类型不能为空")
    private String projectType;
    @ApiModelProperty(value = "项目年份", required = true)
    @NotNull(message = "项目年份不能为空")
    private String projectYear;
    @ApiModelProperty(value = "结束时间", required = true)
    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endTime;
    @ApiModelProperty(value = "项目通知", required = true)
    @NotNull(message = "项目通知不能为空")
    private String projectNotice;
    @ApiModelProperty(value = "项目说明", required = true)
    @NotNull(message = "项目说明不能为空")
    private String projectIntroduce;
    @ApiModelProperty(value = "项目状态", required = true)
    @NotNull(message = "项目状态不能为空")
    private Integer projectStatus;
    @ApiModelProperty(value = "上次通知的数量", required = true)
    @NotNull(message = "可以为空")
    private Integer lastNotificationCount;
    @ApiModelProperty(value = "上次通知的数量", required = true)
    @NotNull(message = "可以为空")
    private Integer identifyRestriction;
}

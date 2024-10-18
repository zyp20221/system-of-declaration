package com.zyp.demo.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "进度条实体")
public class JinduDto {
    @ApiModelProperty(value = "学生进度", required = true)
    @NotNull(message = "可以为空")
    private Integer studentsJindu;
    @ApiModelProperty(value = "老师进度", required = true)
    @NotNull(message = "可以为空")
    private Integer teacherJindu;
    @ApiModelProperty(value = "学校专家进度", required = true)
    @NotNull(message = "可以为空")
    private Integer zhuanjiaJindu;
    @ApiModelProperty(value = "学院进度", required = true)
    @NotNull(message = "可以为空")
    private Integer colleageJindu;
    @ApiModelProperty(value = "学校进度", required = true)
    @NotNull(message = "可以为空")
    private Integer schoolJindu;
    @ApiModelProperty(value = "发布进度", required = true)
    @NotNull(message = "可以为空")
    private Integer fabuJindu;
    @ApiModelProperty(value = "项目编号", required = true)
    @NotNull(message = "可以为空")
    private Integer pId;
    @ApiModelProperty(value = "项目名称", required = true)
    @NotNull(message = "不可以为空")
    private String projectName;
}

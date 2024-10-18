package com.zyp.demo.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author author
 * @since 2024-08-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("project")
public class Project implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value = "pid", type = IdType.AUTO)
    private Long pid;

    private Long leaderId;

    private String projectYear;

    private String projectNotice;

    private String projectIntroduce;

    private String projectName;

    private String projectType;

    private Integer projectNumbers;

    private Integer projectStatus;

    private LocalDateTime createTime;

    private LocalDateTime endTime;
    private  LocalDateTime publishTime;
    private LocalDateTime updateTime;
    private Long createrId;

    private Long updaterId;

    private Integer lastNotificationCount;


    private Integer identifyRestriction;
}

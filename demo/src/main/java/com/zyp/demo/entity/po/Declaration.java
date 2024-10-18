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
@TableName("declaration")
public class Declaration implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value = "bid", type = IdType.AUTO)
    private Long bid;

    private Long declarantId;

    private String declarantEmail;

    private String projectType;

    private String projectYear;

    private String projectBrief;

    private String declarationName;

    private String projectMember;

    private Long adviserId;

    private String teachersAdvice;

    private String declarationContent;

    private Integer modificationStatus;

    private Integer checkStatus;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer pingShen;

    private Long projectId;

    private String projectName;

    private String fileName;

    private String filePath;

    private String pingshenAdvice;

}

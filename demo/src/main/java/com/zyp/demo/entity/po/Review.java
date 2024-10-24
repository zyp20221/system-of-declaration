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
@TableName("review")
public class Review implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value = "rid", type = IdType.AUTO)
    private Long rid;

    private Long declarationId;

    private String gxh;

    private String pingshenService;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}

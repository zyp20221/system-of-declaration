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
@TableName("users")
public class Users implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value = "uid", type = IdType.AUTO)
    private Long uid;

    private String username;

    private String gxh;

    private String pwd;

    private Integer identify;

    private String zhicheng;

    private Integer psLevel;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createrId;

    private Long updaterId;

    private String phoneNumber;

    private String email;


}

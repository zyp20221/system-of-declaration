package com.zyp.demo.service;

import com.zyp.demo.entity.dto.JinduDto;
import com.zyp.demo.entity.po.Jindu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zyp.demo.entity.vo.JinduVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2024-08-24
 */
public interface IJinduService extends IService<Jindu> {

    void add(Long id, JinduDto jinduDto);

    JinduVo get(Long authorization);

    void update2(Long id, JinduDto jinduDto);

    void add2(String a,JinduDto jinduDto);
}

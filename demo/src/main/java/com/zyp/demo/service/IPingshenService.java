package com.zyp.demo.service;

import com.zyp.demo.entity.dto.PingShenDto;
import com.zyp.demo.entity.po.Pingshen;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zyp.demo.entity.vo.PingShenVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2024-09-11
 */
public interface IPingshenService extends IService<Pingshen> {

    void add(PingShenDto pingShenDto);

    List<PingShenVo> getList(List<Long> id);

    void delete(Long id1, String gxh);
}

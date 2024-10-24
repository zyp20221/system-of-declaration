package com.zyp.demo.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zyp.demo.entity.dto.ReviewDto;
import com.zyp.demo.entity.po.Review;
import com.zyp.demo.entity.vo.DeclareVo;
import com.zyp.demo.entity.vo.ReviewVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2024-08-15
 */
public interface IReviewService extends IService<Review> {

    ReviewVo get(Long authorization);

    void add(Long id, ReviewDto reviewDto);

    void dahui(Long authorization);

    List<DeclareVo> getList(String authorization);

    List<DeclareVo> getList2(String authorization);

    void add2(Long id, ReviewDto reviewDto);
}

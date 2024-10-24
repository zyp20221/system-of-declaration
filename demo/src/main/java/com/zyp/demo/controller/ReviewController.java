package com.zyp.demo.controller;


import com.zyp.demo.entity.dto.ReviewDto;
import com.zyp.demo.entity.po.Review;
import com.zyp.demo.entity.vo.DeclareVo;
import com.zyp.demo.entity.vo.ReviewVo;
import com.zyp.demo.service.IReviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author author
 * @since 2024-08-15
 */
@Api(tags = "评审相关接口")
@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
    private final IReviewService reviewService;

    @ApiOperation("得到申报内容接口")
    @GetMapping("/{id}")
    public ReviewVo getReview(@PathVariable Long id) {
        return reviewService.get(id);
    }
    @ApiOperation("指导老师评审提交接口")
    @PostMapping("/tijiao/{id}")
    public void add(@PathVariable Long id,
            @RequestBody ReviewDto reviewDto) {
        reviewService.add(id,reviewDto);
    }
    @ApiOperation("打回接口")
    @PostMapping("/dahui/{id}")
    public void dahui(@PathVariable Long id){
        reviewService.dahui(id);
    }
    @ApiOperation("查询所有担当指导老师的项目")
    @GetMapping("/list")
    public List<DeclareVo> list(@RequestHeader("Authorization") String authorization){
        return  reviewService.getList(authorization);
    }

    @ApiOperation("查询所有担当评审老师的项目")
    @GetMapping("/list2")
    public List<DeclareVo> list2(@RequestHeader("Authorization") String authorization){
        return  reviewService.getList2(authorization);
    }

}

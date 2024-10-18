package com.zyp.demo.controller;


import com.zyp.demo.entity.dto.PingShenDto;
import com.zyp.demo.entity.vo.PingShenVo;
import com.zyp.demo.service.IPingshenService;
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
 * @since 2024-09-11
 */
@RestController
@RequestMapping("/pingshen")
@RequiredArgsConstructor

public class PingshenController {
    private final IPingshenService pingshenService;

    @ApiOperation("传数据")
    @PostMapping
    public void pingshen(@RequestBody PingShenDto pingShenDto) {
        pingshenService.add(pingShenDto);
    }

    @ApiOperation("前端默认获取后端数据")
    @GetMapping
    public List<PingShenVo> get(@RequestParam List<Long> ids) {
        return pingshenService.getList(ids);
    }

    @ApiOperation("删除评审老师")
    @DeleteMapping("/{id1}/{gxh}")
    public void delete(@PathVariable Long id1,@PathVariable String  gxh) {
        pingshenService.delete(id1,gxh);
    }
}

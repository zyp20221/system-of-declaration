package com.zyp.demo.controller;


import com.zyp.demo.entity.dto.DeclareDto;
import com.zyp.demo.entity.dto.JinduDto;
import com.zyp.demo.entity.vo.JinduVo;
import com.zyp.demo.service.IJinduService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author author
 * @since 2024-08-24
 */
@Api(tags = "进度条相关接口")
@Slf4j
@RestController
@RequestMapping("/jindu")
@RequiredArgsConstructor
public class JinduController {
    private final IJinduService jinduService;

    @ApiOperation("进度条提交保存接口")
    @PostMapping("/{id}")
    public void add(@PathVariable Long id,
            @RequestBody JinduDto jinduDto) {
        jinduService.add(id,jinduDto);
    }
    @ApiOperation("进度条提交保存接口")
    @PostMapping
    public void add2(@RequestHeader("Authorization") String authorization
            ,@RequestBody JinduDto jinduDto) {
        jinduService.add2(authorization,jinduDto);
    }
    @ApiOperation("进度条获取接口")
    @GetMapping("/{id}")
    public JinduVo get(@PathVariable Long id) {
        return  jinduService.get(id);
    }

    @ApiOperation("评审老师导致的进度条改变接口")
    @PostMapping("/gaibian2/{id}")
    public void update2(@PathVariable Long id,@RequestBody JinduDto jinduDto) {
        jinduService.update2(id,jinduDto);
    }

}

package com.zyp.demo.controller;



import cn.hutool.core.bean.BeanUtil;
import com.zyp.demo.entity.dto.PwdDto;
import com.zyp.demo.entity.dto.UserLoginDto;
import com.zyp.demo.entity.dto.UserRegisterDto;
import com.zyp.demo.entity.po.Users;
import com.zyp.demo.entity.vo.PersoalVo;
import com.zyp.demo.entity.vo.UserLoginVo;
import com.zyp.demo.service.IUsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
@Api(tags = "用户相关接口")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {
    private final IUsersService usersService;

    @ApiOperation("学生登录接口")
    @PostMapping("/login")
    public UserLoginVo login(@RequestBody UserLoginDto userLoginDto) {
        return usersService.login(userLoginDto);
    }
    @ApiOperation("老师登录接口")
    @PostMapping("/login2")
    public UserLoginVo login2(@RequestBody UserLoginDto userLoginDto) {
        return usersService.login2(userLoginDto);
    }
    @ApiOperation("管理员登录接口")
    @PostMapping("/login3")
    public UserLoginVo login3(@RequestBody UserLoginDto userLoginDto) {
        return usersService.login3(userLoginDto);
    }
    @ApiOperation("用户注册接口")
    @PostMapping("/register")
    public void register(@RequestBody UserRegisterDto userRegisterDto) {
        usersService.add(userRegisterDto);
    }
    @ApiOperation("用户获取个人中心信息接口")
    @GetMapping
    public PersoalVo getPersoal(@RequestHeader("Authorization") String authorization) {
        return usersService.get(authorization);
    }
    @ApiOperation("个人中心保存接口")
    @PostMapping("/save")
    public void login(@RequestHeader("Authorization") String authorization,
            @RequestBody PersoalVo persoalVo) {
        usersService.savePersonal(persoalVo,authorization);
    }
    @ApiOperation("个人中心密码保存接口")
    @PostMapping("/savepwd")
    public void savepwd(@RequestHeader("Authorization") String authorization,
                      @RequestBody PwdDto pwdDto) {
        usersService.savepwd(authorization,pwdDto);
    }
    @ApiOperation("管理员获取个人中心信息接口")
    @GetMapping("/{id}")
    public PersoalVo getPersoal2(@PathVariable Long id) {
        Users users = usersService.getById(id);
        return BeanUtil.copyProperties(users, PersoalVo.class);
    }
}

package com.zyp.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyp.demo.config.JwtProperties;
import com.zyp.demo.entity.dto.PwdDto;
import com.zyp.demo.entity.dto.UserLoginDto;
import com.zyp.demo.entity.dto.UserRegisterDto;
import com.zyp.demo.entity.po.Users;
import com.zyp.demo.entity.vo.PersoalVo;
import com.zyp.demo.entity.vo.UserLoginVo;
import com.zyp.demo.mapper.UsersMapper;
import com.zyp.demo.service.IUsersService;
import com.zyp.demo.utils.JwtTool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * @since 2024-08-15
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {
    private final UsersMapper usersMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTool jwtTool;
    private final JwtProperties jwtProperties;
    //学生登录
    @Override
    public UserLoginVo login(UserLoginDto userLoginDto) {
        return loginMethod(userLoginDto,3);
    }
    //老师登录
    @Override
    public UserLoginVo login2(UserLoginDto userLoginDto) {
        return loginMethod(userLoginDto,2);
    }
    //管理员登录
    @Override
    public UserLoginVo login3(UserLoginDto userLoginDto) {
        return loginMethod(userLoginDto,1);
    }
    @Override
    public void add(UserRegisterDto userRegisterDto) {
        //1.将dto转换为po
        Users users = BeanUtil.copyProperties(userRegisterDto, Users.class);
        users.setCreateTime(LocalDateTime.now());
        users.setUpdateTime(LocalDateTime.now());
        users.setCreaterId(1L);
        users.setUpdaterId(1L);
        //2.存入数据库
        save(users);
        Long id=users.getUid();
        users.setUpdaterId(id);
        users.setCreaterId(id);
        usersMapper.updateById(users);
    }

    @Override
    public PersoalVo get(String authorization) {
        //获取token
        String token = authorization.replace("Bearer ", "");
        Long id = jwtTool.parseToken(token);
        Users byId = getById(id);
        return BeanUtil.copyProperties(byId, PersoalVo.class);
    }

    @Override
    public void savePersonal(PersoalVo persoalVo, String authorization) {
        //获取token
        String token = authorization.replace("Bearer ", "");
        Long id = jwtTool.parseToken(token);
        Users users= getById(id);
        users.setGxh(persoalVo.getGxh());
        users.setEmail(persoalVo.getEmail());
        users.setPhoneNumber(persoalVo.getPhoneNumber());
        users.setUsername(persoalVo.getUsername());
        users.setUpdateTime(LocalDateTime.now());
        updateById(users);
    }

    @Override
    public void savepwd(String authorization, PwdDto password) {
        //获取token
        String token = authorization.replace("Bearer ", "");
        Long id = jwtTool.parseToken(token);
        Users users= getById(id);
        users.setPwd(password.getConfirmPassword());
        users.setUpdateTime(LocalDateTime.now());
        updateById(users);
    }

   public UserLoginVo loginMethod(UserLoginDto userLoginDto,Integer identify) {
       //1.数据校验
       String gxh = userLoginDto.getGxh();
       String pwd = userLoginDto.getPwd();
       //2.根据上面四个查询
       Users user = lambdaQuery().eq(Users::getGxh, gxh)
               .eq(Users::getIdentify,identify).one();
       Assert.notNull(user, "用户名或邮箱或电话号码错误");

       //3.校验密码
       if (!pwd.equals(userLoginDto.getPwd())){
           throw new RuntimeException("工学号或密码错误");
       }
       //4.生成token
       String token = jwtTool.createToken(user.getUid(), jwtProperties.getTokenTTL());
       //5.封装vo并返回
       UserLoginVo userLoginVo = new UserLoginVo();
       userLoginVo.setToken(token);
       userLoginVo.setUserId(user.getUid());
       return userLoginVo;
   }
}

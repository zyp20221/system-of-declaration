package com.zyp.demo.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zyp.demo.entity.dto.PwdDto;
import com.zyp.demo.entity.dto.UserLoginDto;
import com.zyp.demo.entity.dto.UserRegisterDto;
import com.zyp.demo.entity.po.Users;
import com.zyp.demo.entity.vo.PersoalVo;
import com.zyp.demo.entity.vo.UserLoginVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2024-08-15
 */
public interface IUsersService extends IService<Users> {

    UserLoginVo login(UserLoginDto userLoginDto);

    void add(UserRegisterDto userRegisterDto);

    PersoalVo get(String authorization);

    void savePersonal(PersoalVo persoalVo, String authorization);

    void savepwd(String authorization, PwdDto password);

    UserLoginVo login2(UserLoginDto userLoginDto);

    UserLoginVo login3(UserLoginDto userLoginDto);
}

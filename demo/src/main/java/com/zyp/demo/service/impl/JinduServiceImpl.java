package com.zyp.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyp.demo.entity.dto.JinduDto;
import com.zyp.demo.entity.po.Declaration;
import com.zyp.demo.entity.po.Jindu;
import com.zyp.demo.entity.vo.JinduVo;
import com.zyp.demo.mapper.DeclarationMapper;
import com.zyp.demo.mapper.JinduMapper;
import com.zyp.demo.service.IJinduService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyp.demo.utils.JwtTool;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * @since 2024-08-24
 */
@Service
@RequiredArgsConstructor
public class JinduServiceImpl extends ServiceImpl<JinduMapper, Jindu> implements IJinduService {
    private final JwtTool jwtTool;
    private final DeclarationMapper declarationMapper;
    @Override
    public void add(Long id, JinduDto jinduDto) {
        //1.获取传过来的pid
        Jindu jindu = BeanUtil.copyProperties(jinduDto, Jindu.class);
        jindu.setPId(id);
        LambdaQueryWrapper<Jindu> jinduLambdaQueryWrapper = new LambdaQueryWrapper<Jindu>().eq(Jindu::getPId, id);
        Jindu jindu1 = baseMapper.selectOne(jinduLambdaQueryWrapper);
        Long jid = jindu1.getJid();
        jindu.setJid(jid);
        updateById(jindu);

    }

    @Override
    public JinduVo get(Long id) {
        LambdaQueryWrapper<Jindu> jinduLambdaQueryWrapper = new LambdaQueryWrapper<Jindu>().eq(Jindu::getPId, id);
        Jindu jindu = baseMapper.selectOne(jinduLambdaQueryWrapper);
        return BeanUtil.copyProperties(jindu, JinduVo.class);
    }



    @Override
    public void update2(Long id, JinduDto jinduDto) {
        //1.根据项目id找到进度表
        Jindu jindu = BeanUtil.copyProperties(jinduDto, Jindu.class);
        LambdaQueryWrapper<Jindu> jinduLambdaQueryWrapper = new LambdaQueryWrapper<Jindu>().eq(Jindu::getPId, id);
        Jindu jindu2 = baseMapper.selectOne(jinduLambdaQueryWrapper);
        jindu.setJid(jindu2.getJid());
        updateById(jindu);
    }

    @Override
    public void add2(String authorization, JinduDto jinduDto) {
        //1.获取请求头中的token以此获取用户id
        String token = authorization.replace("Bearer ", "");
        Long id = jwtTool.parseToken(token);
        LambdaQueryWrapper<Declaration> declarationLambdaQueryWrapper = new LambdaQueryWrapper<Declaration>()
                .eq(Declaration::getDeclarantId, id)
                .eq(Declaration::getDeclarationName, jinduDto.getProjectName());
        Declaration declaration = declarationMapper.selectOne(declarationLambdaQueryWrapper);
        Long bid = declaration.getBid();
        Jindu jindu = BeanUtil.copyProperties(jinduDto, Jindu.class);
        jindu.setPId(bid);
        save(jindu);
    }

}

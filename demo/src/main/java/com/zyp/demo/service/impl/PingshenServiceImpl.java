package com.zyp.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyp.demo.entity.dto.PingShenDto;
import com.zyp.demo.entity.po.Declaration;
import com.zyp.demo.entity.po.Pingshen;
import com.zyp.demo.entity.vo.PingShenVo;
import com.zyp.demo.mapper.DeclarationMapper;
import com.zyp.demo.mapper.PingshenMapper;
import com.zyp.demo.mapper.ProjectMapper;
import com.zyp.demo.service.IPingshenService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * @since 2024-09-11
 */
@Service
@RequiredArgsConstructor
public class PingshenServiceImpl extends ServiceImpl<PingshenMapper, Pingshen> implements IPingshenService {
    private  final ProjectMapper projectMapper;
    private final DeclarationMapper declarationMapper;
    @Override
    public void add(PingShenDto pingShenDto) {
        Pingshen pingshen = BeanUtil.copyProperties(pingShenDto.getReviewer(), Pingshen.class);
        for (Long id : pingShenDto.getIds()) {
            pingshen.setDeclarationId(id);
            //1.获取申报书名称
            Declaration declaration = declarationMapper.selectById(id);
            //2.将项目名称加入其中
            String projectName = declaration.getDeclarationName();
            pingshen.setDeclarationName(projectName);
            save(pingshen);
        }
    }

    @Override
    public List<PingShenVo> getList(List<Long> ids) {
        List<PingShenVo> list=new ArrayList<>();
        for (Long id : ids) {
            LambdaQueryWrapper<Pingshen> eq = new LambdaQueryWrapper<Pingshen>().eq(Pingshen::getDeclarationId, id);
            List<Pingshen> pingshens = baseMapper.selectList(eq);
            for (Pingshen pingshen : pingshens) {
                PingShenVo pingShenVo = BeanUtil.copyProperties(pingshen, PingShenVo.class);
                list.add(pingShenVo);
            }
        }
        return list;
    }

    @Override
    public void delete(Long id1, String gxh) {
        LambdaQueryWrapper<Pingshen> pingshenLambdaQueryWrapper = new LambdaQueryWrapper<Pingshen>()
                .eq(Pingshen::getDeclarationId, id1)
                .eq(Pingshen::getGongHao, gxh);
        baseMapper.delete(pingshenLambdaQueryWrapper);
    }
}

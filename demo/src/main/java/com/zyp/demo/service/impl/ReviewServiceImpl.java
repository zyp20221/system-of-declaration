package com.zyp.demo.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyp.demo.config.JwtProperties;
import com.zyp.demo.entity.dto.ReviewDto;
import com.zyp.demo.entity.po.*;
import com.zyp.demo.entity.vo.DeclareVo;
import com.zyp.demo.entity.vo.ReviewVo;
import com.zyp.demo.mapper.*;
import com.zyp.demo.service.IReviewService;
import com.zyp.demo.utils.JwtTool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review> implements IReviewService {
    private final JwtTool jwtTool;
    private final JwtProperties jwtProperties;
    private final UsersMapper usersMapper;
    private final DeclarationMapper declarationMapper;
    private final PingshenMapper pingshenMapper;
    private final JinduMapper jinduMapper;
    @Override
    public ReviewVo get(Long id) {
        //根据传过来的id与申报书匹配
        LambdaQueryWrapper<Declaration> reviewVo = new LambdaQueryWrapper<Declaration>().eq(Declaration::getBid, id);
        Declaration declaration = declarationMapper.selectOne(reviewVo);
        return BeanUtil.copyProperties(declaration, ReviewVo.class);
    }

    @Override
    public void add(Long id, ReviewDto reviewDto) {
        String teachersAdvice = reviewDto.getTeachersAdvice();
        //1.根据id找到申请书表
        LambdaQueryWrapper<Declaration> reviewVo = new LambdaQueryWrapper<Declaration>().eq(Declaration::getBid, id);
        Declaration declaration = declarationMapper.selectOne(reviewVo);
        //2.将评审意见给申请书表
        declaration.setTeachersAdvice(teachersAdvice);
        declaration.setNodeName("评审老师审核");
        declaration.setModificationStatus(0);
        declaration.setCheckStatus(1);
        declaration.setPingShen(0);
        declaration.setUpdateTime(LocalDateTime.now());
        declarationMapper.updateById(declaration);
    }
    @Override
    public void add2(Long id, ReviewDto reviewDto) {
        String pingshenAdvice = reviewDto.getPingshenAdvice();
        //1.根据id找到申请书表
        LambdaQueryWrapper<Declaration> reviewVo = new LambdaQueryWrapper<Declaration>().eq(Declaration::getBid, id);
        Declaration declaration = declarationMapper.selectOne(reviewVo);
        //2.将评审意见给申请书表
        declaration.setTeachersAdvice(pingshenAdvice);
        declaration.setNodeName("评审老师审核");
        declaration.setModificationStatus(0);
        declaration.setCheckStatus(1);
        declaration.setPingShen(0);
        declaration.setUpdateTime(LocalDateTime.now());
        declarationMapper.updateById(declaration);
    }
    @Override
    public void dahui(Long id) {
        //1.根据id找到申请书
        LambdaQueryWrapper<Declaration> reviewVo = new LambdaQueryWrapper<Declaration>().eq(Declaration::getBid, id);
        Declaration declaration = declarationMapper.selectOne(reviewVo);
        declaration.setCheckStatus(0);
        declaration.setModificationStatus(1);
        declaration.setUpdateTime(LocalDateTime.now());
        declarationMapper.updateById(declaration);
    }

    @Override
    public List<DeclareVo> getList(String authorization) {
        //2.获取指导老师id
        String token = authorization.replace("Bearer ", "");
        Long id = jwtTool.parseToken(token);
        //3.以指导老师id为查询条件，查出所有申报书的数据
        LambdaQueryWrapper<Declaration> declaration = new LambdaQueryWrapper<Declaration>().eq(Declaration::getAdviserId, id);
        List<Declaration> declarations = declarationMapper.selectList(declaration);
        return vo(declarations);
    }

    @Override
    public List<DeclareVo> getList2(String authorization) {
        //1.获取评审老师的用户id
        String token = authorization.replace("Bearer ", "");
        Long id = jwtTool.parseToken(token);
        //2.获取评审老师对应的工号
        LambdaQueryWrapper<Users> usersLambdaQueryWrapper = new LambdaQueryWrapper<Users>().eq(Users::getUid, id);
        Users users = usersMapper.selectOne(usersLambdaQueryWrapper);
        String gxh = users.getGxh();
        //3.在评审老师表中获取评审老师对应的申报书id
        LambdaQueryWrapper<Pingshen> eq = new LambdaQueryWrapper<Pingshen>()
                .eq(Pingshen::getGongHao, gxh);
        List<Pingshen> pingshens = pingshenMapper.selectList(eq);
        List<DeclareVo> list = new ArrayList<>();
        for (Pingshen pingshen : pingshens) {
            //4.获取了申报书id
            Long projectId = pingshen.getDeclarationId();
            LambdaQueryWrapper<Declaration> declaration = new LambdaQueryWrapper<Declaration>().eq(Declaration::getBid, projectId);
            List<Declaration> declarations = declarationMapper.selectList(declaration);
            for (Declaration declaration1 : declarations) {
                Long bid = declaration1.getBid();
                Jindu jindu = jinduMapper.selectOne(new LambdaQueryWrapper<Jindu>().eq(Jindu::getPId, bid));
                Integer teacherJindu = jindu.getTeacherJindu();
                //5.查看指导老师是否已经指导过
                if (teacherJindu == 1) {
                    Long adviserId = declaration1.getAdviserId();
                    Users users2 = usersMapper.selectById(adviserId);
                    String adviser = users2.getUsername();
                    //8.根据declarantId查姓名
                    Long declarantId = declaration1.getDeclarantId();
                    String username = usersMapper.selectById(declarantId).getUsername();
                    DeclareVo declareVo = BeanUtil.copyProperties(declaration1, DeclareVo.class);
                    declareVo.setProjectLevel(1);
                    declareVo.setUsername(username);
                    declareVo.setAdviser(adviser);
                    list.add(declareVo);

                }
            }
        }
        return list;
    }

    public List<DeclareVo> vo(List<Declaration> declarations){
        List<DeclareVo> declareVos = new ArrayList<>();
        for (Declaration declaration1 : declarations) {
            //4.根据adviserId查得姓名
            Long adviserId = declaration1.getAdviserId();
            Users users = usersMapper.selectById(adviserId);
            String adviser = users.getUsername();
            //5.根据declarantId查姓名
            Long declarantId = declaration1.getDeclarantId();
            String username = usersMapper.selectById(declarantId).getUsername();
            DeclareVo declareVo = BeanUtil.copyProperties(declaration1, DeclareVo.class);
            declareVo.setUsername(username);
            declareVo.setAdviser(adviser);
            declareVos.add(declareVo);
        }
        return declareVos;
    }
}

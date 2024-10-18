package com.zyp.demo.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyp.demo.entity.dto.ProjectDto;
import com.zyp.demo.entity.po.Declaration;
import com.zyp.demo.entity.po.Project;
import com.zyp.demo.entity.po.Users;
import com.zyp.demo.entity.vo.DeclareVo;
import com.zyp.demo.entity.vo.ProjectCount;
import com.zyp.demo.entity.vo.ProjectVo;
import com.zyp.demo.entity.vo.UserVo;
import com.zyp.demo.mapper.DeclarationMapper;
import com.zyp.demo.mapper.ProjectMapper;
import com.zyp.demo.mapper.UsersMapper;
import com.zyp.demo.service.IProjectService;
import com.zyp.demo.utils.JwtTool;
import lombok.RequiredArgsConstructor;
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
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements IProjectService {
    private final JwtTool jwtTool;
    private final DeclarationMapper declarationMapper;
    private final UsersMapper usersMapper;

    @Override
    public void add(String authorization, ProjectDto projectDto) {
        //1.获取请求头中的token以此获取用户id
        String token = authorization.replace("Bearer ", "");
        Long id = jwtTool.parseToken(token);
        //2.转化为po
        Project project = BeanUtil.copyProperties(projectDto, Project.class);
        project.setLeaderId(id);
        project.setCreaterId(id);
        project.setUpdaterId(id);
        //3.检查数据是否已经存在该项目
        LambdaQueryWrapper<Project> projectLambdaQueryWrapper = new LambdaQueryWrapper<Project>().eq(Project::getProjectName, project.getProjectName());
        Project project1 = baseMapper.selectOne(projectLambdaQueryWrapper);
        if (project1 != null) {
            project.setPid(project1.getPid());
            project.setUpdateTime(LocalDateTime.now());
            updateById(project);
        }else {
            project.setCreateTime(LocalDateTime.now());
            save(project);
        }
    }

    @Override
    public List<ProjectVo> get(Integer status) {
        List<Project> projects;
        if (status == null) {
            projects = baseMapper.selectList(null);
        }else {
            LambdaQueryWrapper<Project> projectLambdaQueryWrapper = new LambdaQueryWrapper<Project>().eq(Project::getProjectStatus, status);
            projects = baseMapper.selectList(projectLambdaQueryWrapper);
        }
        //3.创建一个空数组
        List<ProjectVo> projectVos = new ArrayList<>();
        for (Project project : projects) {
            Integer selectCount = declarationMapper.selectCount(new LambdaQueryWrapper<Declaration>()
                    .eq(Declaration::getProjectYear, project.getProjectYear())
                    .eq(Declaration::getProjectType, project.getProjectType()));
            project.setProjectNumbers(selectCount);
            ProjectVo projectVo = BeanUtil.copyProperties(project, ProjectVo.class);
            projectVos.add(projectVo);
        }
        return projectVos;
    }

    @Override
    public void update2(ProjectDto projectDto) {
        String projectName = projectDto.getProjectName();
        Integer projectStatus = projectDto.getProjectStatus();
        LambdaQueryWrapper<Project> projectLambdaQueryWrapper = new LambdaQueryWrapper<Project>().eq(Project::getProjectName, projectName);
        Project project = baseMapper.selectOne(projectLambdaQueryWrapper);
        project.setProjectStatus(projectStatus);
        if (projectStatus==-1 || projectStatus==2) {
            project.setUpdateTime(LocalDateTime.now());
            project.setEndTime(LocalDateTime.now());
            LambdaQueryWrapper<Declaration> declarationLambdaQueryWrapper = new LambdaQueryWrapper<Declaration>().eq(Declaration::getProjectYear, projectDto.getProjectYear())
                    .eq(Declaration::getProjectType, projectDto.getProjectType());
            List<Declaration> declarations = declarationMapper.selectList(declarationLambdaQueryWrapper);
            for (Declaration declaration : declarations) {
                declaration.setModificationStatus(0);
                declaration.setPingShen(0);
                declaration.setUpdateTime(LocalDateTime.now());
                declarationMapper.updateById(declaration);
            }
        }else {
            project.setPublishTime(LocalDateTime.now());
            project.setUpdateTime(LocalDateTime.now());
            project.setProjectStatus(1);
        }
        baseMapper.updateById(project);

    }

    @Override
    public List<Project> getList(String authorization) {
        //1.获取请求头中的token以此获取用户id
        String token = authorization.replace("Bearer ", "");
        Long id = jwtTool.parseToken(token);
        Users users = usersMapper.selectById(id);
        Integer identify = users.getIdentify();
        LambdaQueryWrapper<Project> projectLambdaQueryWrapper = new LambdaQueryWrapper<Project>().eq(Project::getIdentifyRestriction, identify);
        List<Project> projects = new ArrayList<>();
        List<Project> projects1 = baseMapper.selectList(projectLambdaQueryWrapper);
        for (Project project : projects1) {
            if (project.getProjectStatus()==1) {
                projects.add(project);
            }
        }
        return projects;
    }

    @Override
    public Integer getCount() {
        LambdaQueryWrapper<Users> usersLambdaQueryWrapper = new LambdaQueryWrapper<Users>().eq(Users::getIdentify, 1);
        Users users = usersMapper.selectOne(usersLambdaQueryWrapper);
        Long uid = users.getUid();
        LambdaQueryWrapper<Project> projectLambdaQueryWrapper = new LambdaQueryWrapper<Project>().eq(Project::getLeaderId, uid);
        List<Project> projects = baseMapper.selectList(projectLambdaQueryWrapper);
        Project project = projects.getFirst();
        return project.getLastNotificationCount();
    }

    @Override
    public void update3(ProjectDto projectDto) {
        LambdaQueryWrapper<Users> usersLambdaQueryWrapper = new LambdaQueryWrapper<Users>().eq(Users::getIdentify, 1);
        Users users = usersMapper.selectOne(usersLambdaQueryWrapper);
        Long uid = users.getUid();
        LambdaQueryWrapper<Project> projectLambdaQueryWrapper = new LambdaQueryWrapper<Project>().eq(Project::getLeaderId, uid);
        List<Project> projects = baseMapper.selectList(projectLambdaQueryWrapper);
        for (Project project : projects) {
            project.setLastNotificationCount(projectDto.getLastNotificationCount());
            updateById(project);
        }
    }

    @Override
    public List<DeclareVo> getDeclarations(Long projectId) {
        LambdaQueryWrapper<Declaration> declarationLambdaQueryWrapper = new LambdaQueryWrapper<Declaration>().eq(Declaration::getProjectId, projectId);
        List<Declaration> declarations = declarationMapper.selectList(declarationLambdaQueryWrapper);
        return vo(declarations);
    }

    @Override
    public List<UserVo> getUsers(Long identify) {
        LambdaQueryWrapper<Users> usersLambdaQueryWrapper = new LambdaQueryWrapper<Users>().eq(Users::getIdentify, identify);
        List<Users> users = usersMapper.selectList(usersLambdaQueryWrapper);
        List<UserVo> userVos = new ArrayList<>();
        for (Users user : users) {
            UserVo userVo = BeanUtil.copyProperties(user, UserVo.class);
            userVos.add(userVo);
        }
        return userVos;
    }

    @Override
    public void delete(Long uid) {
        usersMapper.deleteById(uid);
    }

    @Override
    public ProjectVo getProjectVo(Long pid) {
        LambdaQueryWrapper<Project> projectLambdaQueryWrapper = new LambdaQueryWrapper<Project>().eq(Project::getPid, pid);
        Project project = baseMapper.selectOne(projectLambdaQueryWrapper);
        return BeanUtil.copyProperties(project, ProjectVo.class);
    }

    @Override
    public List<ProjectVo> getSpecial(String authorization) {
        //1.获取请求头中的token以此获取用户id
        String token = authorization.replace("Bearer ", "");
        Long id = jwtTool.parseToken(token);
        //2.获取用户身份
        Users users = usersMapper.selectById(id);
        Integer identify = users.getIdentify();
        //3.根据用户身份筛选项目
        LambdaQueryWrapper<Project> projectLambdaQueryWrapper = new LambdaQueryWrapper<Project>().eq(Project::getIdentifyRestriction, identify);
        List<Project> projects = baseMapper.selectList(projectLambdaQueryWrapper);
        //4.创建一个projectvo的空数组
        List<ProjectVo> projectVos = new ArrayList<>();
        for (Project project : projects) {
            Integer selectCount = declarationMapper.selectCount(new LambdaQueryWrapper<Declaration>()
                    .eq(Declaration::getProjectYear, project.getProjectYear())
                    .eq(Declaration::getProjectType, project.getProjectType()));
            project.setProjectNumbers(selectCount);
            ProjectVo projectVo = BeanUtil.copyProperties(project, ProjectVo.class);
            projectVos.add(projectVo);
        }
        return projectVos;
    }

    @Override
    public ProjectCount getProjectNumbers() {
        LambdaQueryWrapper<Project> projectLambdaQueryWrapper = new LambdaQueryWrapper<Project>().eq(Project::getProjectStatus, 1);
        //1.得到进行中的项目数量
        Integer onGoing = baseMapper.selectCount(projectLambdaQueryWrapper);
        //2.得到所有项目数量
        Integer allCount = baseMapper.selectCount(null);
        //3.得到停用的项目数量
        LambdaQueryWrapper<Project> projectLambdaQueryWrapper1 = new LambdaQueryWrapper<Project>().eq(Project::getProjectStatus, 2);
        Integer stopped = baseMapper.selectCount(projectLambdaQueryWrapper1);
        ProjectCount projectCount = new ProjectCount();
        projectCount.setOngoing(onGoing);
        projectCount.setPublished(allCount);
        projectCount.setStopped(stopped);
        return projectCount;
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
            declareVo.setProjectLevel(1);//项目等级
            declareVo.setUsername(username);
            declareVo.setAdviser(adviser);
            declareVos.add(declareVo);
        }
        return declareVos;
    }
}

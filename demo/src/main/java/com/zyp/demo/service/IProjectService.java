package com.zyp.demo.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zyp.demo.entity.dto.ProjectDto;
import com.zyp.demo.entity.po.Project;
import com.zyp.demo.entity.vo.DeclareVo;
import com.zyp.demo.entity.vo.ProjectCount;
import com.zyp.demo.entity.vo.ProjectVo;
import com.zyp.demo.entity.vo.UserVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2024-08-15
 */
public interface IProjectService extends IService<Project> {

    void add(String authorization, ProjectDto projectDto);

    List<ProjectVo> get(Integer status);

    void update2(ProjectDto projectDto);

    List<Project> getList(String authorization);

    Integer getCount();

    void update3(ProjectDto projectDto);

    List<DeclareVo> getDeclarations(Long projectId);

    List<UserVo> getUsers(Long identify);

    void delete(Long uid);

    ProjectVo getProjectVo(Long pid);

    List<ProjectVo> getSpecial(String authorization);

    ProjectCount getProjectNumbers();
}

package com.zyp.demo.controller;


import cn.hutool.core.bean.BeanUtil;
import com.zyp.demo.entity.dto.DeletesDto;
import com.zyp.demo.entity.dto.ProjectDto;
import com.zyp.demo.entity.po.Project;
import com.zyp.demo.entity.vo.DeclareVo;
import com.zyp.demo.entity.vo.ProjectCount;
import com.zyp.demo.entity.vo.ProjectVo;
import com.zyp.demo.entity.vo.UserVo;
import com.zyp.demo.mapper.ProjectMapper;
import com.zyp.demo.service.IProjectService;
import com.zyp.demo.timeThred.DataThread;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author author
 * @since 2024-08-15
 */
@Api(tags = "项目发布和管理相关接口")
@Slf4j
@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {
    private final IProjectService projectService;
    private final ProjectMapper projectMapper;
    @PostConstruct
    public void init() {
        DataThread dataThread = new DataThread(projectMapper);
        dataThread.start(); // 启动线程
    }
    @ApiOperation("发布的项目提交接口")
    @PostMapping
    public void save(@RequestHeader("Authorization") String authorization,
            @RequestBody ProjectDto projectDto) {
        projectService.add(authorization,projectDto);
    }
    @ApiOperation("项目管理接口")
    @GetMapping
    public List<ProjectVo> getProjects(Integer status) {
        return projectService.get(status);
    }
    @ApiOperation("项目管理中更改项目状态接口")
    @PostMapping("/update")
    public void update(@RequestBody ProjectDto projectDto) {
        projectService.update2(projectDto);
    }
    @ApiOperation("统计发布项目的数量的接口")
    @GetMapping("/length")
    public List<Project> getProjectList(@RequestHeader("Authorization") String authorization) {
        return projectService.getList(authorization);
    }
    @ApiOperation("统计发布项目的数量的接口")
    @GetMapping("/count")
    public Integer getProjectList2() {
        return projectService.getCount();
    }
    @ApiOperation("项目管理中更改最后通知数量的接口")
    @PostMapping("/update2")
    public void update2(@RequestBody ProjectDto projectDto) {
        projectService.update3(projectDto);
    }

    @ApiOperation("获取全部申报书")
    @GetMapping("/declaration")
    public List<DeclareVo> getDeclarationList3(Long projectId) {
        return  projectService.getDeclarations(projectId);
    }
    @ApiOperation("获取除管理员外所有用户")
    @GetMapping("/users/{identify}")
    public List<UserVo> getDeclarationList5(@PathVariable Long identify) {
        return  projectService.getUsers(identify);
    }

    @ApiOperation("用户管理中删除用户")
    @DeleteMapping("/delete/{uid}")
    public void delete(@PathVariable Long uid) {
        projectService.delete(uid);
    }
    @ApiOperation("获取项目表中的项目年份，项目名称和项目类型")
    @GetMapping("/{pid}")
    public ProjectVo getDeclarationList4(@PathVariable Long pid) {
        return  projectService.getProjectVo(pid);
    }
    @ApiOperation("根据用户身份返回不同项目")
    @GetMapping("/special")
    public List<ProjectVo> getSpecialProjectList(@RequestHeader("Authorization") String authorization) {
        return projectService.getSpecial(authorization);
    }
    @ApiOperation("根据项目状态不同，返回该种项目数量")
    @GetMapping("/numbers")
    public ProjectCount projectCount(){
        return projectService.getProjectNumbers();
    }
    @ApiOperation("项目管理中删除项目")
    @DeleteMapping("/delete2/{pid}")
    public void delete2(@PathVariable Long pid) {
        projectService.removeById(pid);
    }
    @ApiOperation("得到项目所有信息")
    @GetMapping("/getall/{pid}")
    public ProjectDto getAll(@PathVariable Long pid) {
        if (pid!=null){
            Project byId = projectService.getById(pid);
            return BeanUtil.copyProperties(byId,ProjectDto.class);
        }
        return null;
    }
    @ApiOperation("批量删除项目")
    @DeleteMapping("/delete")
    public void deleteAll(@RequestBody DeletesDto deletesDto) {
        projectService.removeByIds(deletesDto.getIds());
    }
}

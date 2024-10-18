package com.zyp.demo.timeThred;

import com.zyp.demo.entity.po.Project;
import com.zyp.demo.mapper.ProjectMapper;

import java.time.LocalDateTime;
import java.util.List;

public class DataThread extends Thread {
    private final ProjectMapper projectMapper;

    public DataThread(ProjectMapper projectMapper) {
        this.projectMapper = projectMapper;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // 执行检查操作
                check();
                // 暂停5秒
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                // 处理线程中断异常
                e.printStackTrace();
                // 退出循环
                break;
            }
        }
    }

    public void check() {
        // 1. 检索所有项目数据
        List<Project> projects = projectMapper.selectList(null);
        LocalDateTime now = LocalDateTime.now();

        for (Project project : projects) {
            // 2. 获取结束时间
            LocalDateTime endTime = project.getEndTime();
            // 3. 判断结束时间是否早于当前时间
            if (endTime != null && endTime.isBefore(now)) {
                // 4. 更新项目状态为已结束
                project.setProjectStatus(2);
            } else {
                project.setProjectStatus(1); // 项目仍在进行
            }
            // 5. 更新项目状态到数据库
            projectMapper.updateById(project);
        }
    }
}
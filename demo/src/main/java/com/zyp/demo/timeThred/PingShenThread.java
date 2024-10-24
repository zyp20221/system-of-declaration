package com.zyp.demo.timeThred;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyp.demo.entity.po.Declaration;
import com.zyp.demo.entity.po.Pingshen;
import com.zyp.demo.entity.po.Review;
import com.zyp.demo.mapper.DeclarationMapper;
import com.zyp.demo.mapper.PingshenMapper;
import com.zyp.demo.mapper.ReviewMapper;

import java.util.List;
import java.util.Objects;

public class PingShenThread extends Thread{
    private final DeclarationMapper declarationMapper;
    private final PingshenMapper pingshenMapper;
    private final ReviewMapper reviewMapper;
    public PingShenThread(DeclarationMapper declarationMapper, PingshenMapper pingshenMapper, ReviewMapper reviewMapper) {
        this.declarationMapper = declarationMapper;
        this.pingshenMapper = pingshenMapper;
        this.reviewMapper = reviewMapper;
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
    public void check(){
        //1.检索所有的申请书表
        List<Declaration> declarations = declarationMapper.selectList(null);
        //2.遍历
        for (Declaration declaration : declarations) {
            //3.得到所有的申请书id
            Long bid = declaration.getBid();
            //4.以得到的id算出评审老师数量
            LambdaQueryWrapper<Pingshen> pingshenLambdaQueryWrapper = new LambdaQueryWrapper<Pingshen>()
                    .eq(Pingshen::getDeclarationId, bid);
            Integer selectCount = pingshenMapper.selectCount(pingshenLambdaQueryWrapper);
            //5.算出已经被评审的数量
            LambdaQueryWrapper<Review> eq = new LambdaQueryWrapper<Review>()
                    .eq(Review::getDeclarationId, bid);
            Integer selectCount1 = reviewMapper.selectCount(eq);
            //6.比较两者
            if (Objects.equals(selectCount1, selectCount)){
                declaration.setNodeName("等待结果");
                declaration.setResult("通过");
                declarationMapper.updateById(declaration);
            }
        }
    }
}

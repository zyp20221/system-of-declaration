package com.zyp.demo.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyp.demo.entity.dto.DeclareDto;
import com.zyp.demo.entity.dto.SearchDto;
import com.zyp.demo.entity.dto.ZhiDingDto;
import com.zyp.demo.entity.po.Declaration;
import com.zyp.demo.entity.po.Pingshen;
import com.zyp.demo.entity.po.Users;
import com.zyp.demo.entity.vo.DeclareVo;
import com.zyp.demo.entity.vo.FileVo;
import com.zyp.demo.mapper.DeclarationMapper;
import com.zyp.demo.mapper.PingshenMapper;
import com.zyp.demo.mapper.ProjectMapper;
import com.zyp.demo.mapper.UsersMapper;
import com.zyp.demo.service.IDeclarationService;
import com.zyp.demo.utils.JwtTool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
@Slf4j
@Service
@RequiredArgsConstructor
public class DeclarationServiceImpl extends ServiceImpl<DeclarationMapper, Declaration> implements IDeclarationService {
    private final JwtTool jwtTool;
    private final UsersMapper usersMapper;
    private final ProjectMapper projectMapper;
    private final PingshenMapper pingshenMapper;
    //保存
    @Override
    public void add(DeclareDto declareDto, String authorization) {
        //1.获取请求头中的token以此获取用户id
        String token = authorization.replace("Bearer ", "");
        Long id = jwtTool.parseToken(token);
        //2.存申报书
        Declaration declaration = BeanUtil.copyProperties(declareDto, Declaration.class);
        declaration.setDeclarantId(id);
        //3.获取用户信息
        LambdaQueryWrapper<Declaration> declarationLambdaQueryWrapper = new LambdaQueryWrapper<Declaration>()
                .eq(Declaration::getDeclarantId, id)
                .eq(Declaration::getDeclarationName, declareDto.getDeclarationName());
        Declaration declaration2 = baseMapper.selectOne(declarationLambdaQueryWrapper);
        if (declaration2 == null) {
            declaration.setCreateTime(LocalDateTime.now());
            declaration.setUpdateTime(LocalDateTime.now());
            declaration.setNodeName("等待提交");
            declaration.setProcessStatus("");
            save(declaration);
        }else {
            declaration.setUpdateTime(LocalDateTime.now());
            declaration.setBid(declaration2.getBid());
            declaration.setProcessStatus("审核中");
            declaration.setNodeName("指导老师审核");
            updateById(declaration);
        }

    }
    //提交
    @Override
    public void add2(DeclareDto declareDto, String authorization) {
        //1.获取请求头中的token以此获取用户id
        String token = authorization.replace("Bearer ", "");
        Long id = jwtTool.parseToken(token);
        //2.存申报书
        Declaration declaration1 = BeanUtil.copyProperties(declareDto, Declaration.class);
        declaration1.setDeclarantId(id);
        //3.获取用户信息
        LambdaQueryWrapper<Users> usersLambdaQueryWrapper= new LambdaQueryWrapper<Users>().eq(Users::getUid, id);
        Users users = usersMapper.selectOne(usersLambdaQueryWrapper);
        LambdaQueryWrapper<Declaration> declarationLambdaQueryWrapper = new LambdaQueryWrapper<Declaration>()
                .eq(Declaration::getDeclarationName, declareDto.getDeclarationName());
        Declaration declaration = baseMapper.selectOne(declarationLambdaQueryWrapper);
        if (declaration == null) {
            declaration1.setDeclarantEmail(users.getEmail());
            declaration1.setCheckStatus(1);
            declaration1.setModificationStatus(0);
            declaration1.setProcessStatus("审核中");
            declaration1.setNodeName("指导老师审核");
            declaration1.setCreateTime(LocalDateTime.now());
            declaration1.setUpdateTime(LocalDateTime.now());
            save(declaration1);
        }else {
            declaration1.setUpdateTime(LocalDateTime.now());
            declaration1.setBid(declaration.getBid());
            declaration1.setProcessStatus("审核中");
            declaration1.setNodeName("指导老师审核");
            declaration1.setCheckStatus(1);
            declaration1.setModificationStatus(0);
            updateById(declaration1);
        }

    }

    @Override
    public String getName(Long id) {
        LambdaQueryWrapper<Users> usersLambdaQueryWrapper = new LambdaQueryWrapper<Users>().eq(Users::getUid, id);
        Users users = usersMapper.selectOne(usersLambdaQueryWrapper);
        return users.getUsername();
    }

    @Override
    public void delete(Long id) {
        baseMapper.deleteById(id);
    }

    @Override
    public FileVo upload(MultipartFile file) {
        final String uploadDir = "uploads"; // 文件上传目录
        try {
            // 创建上传目录
            Path directoryPath = Paths.get(uploadDir);
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }
            // 保存文件
            Path filePath = directoryPath.resolve(file.getOriginalFilename());
            file.transferTo(filePath);

            // 保存文件信息到数据库
            String originalFilename = file.getOriginalFilename();
            String path = filePath.toString();
            FileVo fileVo = new FileVo();
            fileVo.setFileName(originalFilename);
            fileVo.setFilePath(path);
            return fileVo;

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

   /* @Override
    public ResponseEntity<FileSystemResource> download(Long bid) {
        final String uploadDir = "uploads";//存储文件所在目录
        Declaration declaration = baseMapper.selectById(bid);
        File file = new File(uploadDir + "/"+ declaration.getFilePath());
        // 检查文件是否存在
        if (!file.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        // 设置响应头
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());

        // 返回文件资源
        return ResponseEntity.ok()
                .headers(headers)
                .body(new FileSystemResource(file));
    }*/

    @Override
    public ResponseEntity<Resource> xiafa(Long bid) throws MalformedURLException {
        Declaration declaration = baseMapper.selectById(bid);
        String filePath = declaration.getFilePath();
        Path path = Paths.get(filePath);
        Resource resource = new UrlResource(path.toUri());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @Override
    public FileVo getFileName(Long bid) {
        Declaration declaration = baseMapper.selectById(bid);
        return BeanUtil.copyProperties(declaration, FileVo.class);
    }

    @Override
    public List<DeclareVo> getAll() {
        List<Declaration> declarations = baseMapper.selectList(null);
        return vo(declarations);
    }

    @Override
    public void zhiding(ZhiDingDto zhiDingDto) {
        //1.获取所有所指定的申报书id
        List<Long> existingIds = zhiDingDto.getExistingIds();
        //2.遍历所有申报书id
        for (Long existingId : existingIds) {
            Pingshen pingshen = new Pingshen();
            Declaration declaration = baseMapper.selectById(existingId);
            Long bid = declaration.getBid();
            String declarationName = declaration.getDeclarationName();
            //3.遍历所有所选定的用户id
            for (Long id : zhiDingDto.getIds()) {
                Users users = usersMapper.selectById(id);
                String username = users.getUsername();
                String gxh = users.getGxh();
                pingshen.setUsername(username);
                pingshen.setGongHao(gxh);
                pingshen.setDeclarationName(declarationName);
                pingshen.setDeclarationId(bid);
                LambdaUpdateWrapper<Pingshen> eq = new LambdaUpdateWrapper<Pingshen>().eq(Pingshen::getDeclarationId, bid).eq(Pingshen::getGongHao, gxh);
                List<Pingshen> pingshens = pingshenMapper.selectList(eq);
                if (pingshens.size()>1) {
                    return;
                }else {
                    pingshenMapper.insert(pingshen);
                }
            }
        }
    }

    @Override
    public Long get(String adviserName) {
        LambdaQueryWrapper<Users> user = new LambdaQueryWrapper<Users>().eq(Users::getUsername, adviserName)
                .eq(Users::getIdentify, 2);
        Users users = usersMapper.selectOne(user);
        return users.getUid();
    }

    @Override
    public List<DeclareVo> getList(String authorization) {
        //1.创建空白的数组
        List<DeclareVo> declareVos = new ArrayList<>();
        //2.获取申报人id
        String token = authorization.replace("Bearer ", "");
        Long id = jwtTool.parseToken(token);
        //3.以申报人id为查询条件，查出所有申报书的数据
        LambdaQueryWrapper<Declaration> declaration = new LambdaQueryWrapper<Declaration>().eq(Declaration::getDeclarantId, id);
        List<Declaration> declarations = baseMapper.selectList(declaration);
        return vo(declarations);

    }
    @Override
    public List<DeclareVo> search(String authorization, SearchDto searchDto) {
        //1.获取请求头中的token以此获取用户id
        String token = authorization.replace("Bearer ", "");
        Long id = jwtTool.parseToken(token);
        if (searchDto!=null){
            if (searchDto.getProjectCode()!=null ){
                LambdaQueryWrapper<Declaration> eq = new LambdaQueryWrapper<Declaration>().eq(Declaration::getProjectId, searchDto.getProjectCode());
                List<Declaration> declarations = baseMapper.selectList(eq);
                return vo(declarations);
            }
            if (searchDto.getProjectName()!=null){
                LambdaQueryWrapper<Declaration> eq = new LambdaQueryWrapper<Declaration>().eq(Declaration::getProjectName, searchDto.getProjectName());
                List<Declaration> declarations = baseMapper.selectList(eq);
                return vo(declarations);
            }
            if (searchDto.getSelectedBatch()!=null){
                LambdaQueryWrapper<Declaration> eq = new LambdaQueryWrapper<Declaration>()
                        .eq(Declaration::getProjectYear,searchDto.getSelectedBatch());
                List<Declaration> declarations = baseMapper.selectList(eq);
                return vo(declarations);
            }
            if (searchDto.getSelectedType()!=null){
                LambdaQueryWrapper<Declaration> eq = new LambdaQueryWrapper<Declaration>().eq(Declaration::getProjectType, searchDto.getSelectedType());
                List<Declaration> declarations = baseMapper.selectList(eq);
                return vo(declarations);
            }
        }
        LambdaQueryWrapper<Declaration> declarationLambdaQueryWrapper = new LambdaQueryWrapper<Declaration>().eq(Declaration::getDeclarantId, id);
        List<Declaration> declarations = baseMapper.selectList(declarationLambdaQueryWrapper);
        return vo(declarations);

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
            declareVo.setProjectLevel(1);
            declareVo.setUsername(username);
            declareVo.setAdviser(adviser);
            declareVos.add(declareVo);
        }
        return declareVos;
    }
}

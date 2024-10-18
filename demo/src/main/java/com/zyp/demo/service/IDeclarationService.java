package com.zyp.demo.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zyp.demo.entity.dto.DeclareDto;
import com.zyp.demo.entity.dto.SearchDto;
import com.zyp.demo.entity.dto.ZhiDingDto;
import com.zyp.demo.entity.po.Declaration;
import com.zyp.demo.entity.vo.DeclareVo;
import com.zyp.demo.entity.vo.FileVo;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2024-08-15
 */
public interface IDeclarationService extends IService<Declaration> {

    void add(DeclareDto declareDto, String authorization);

    Long get(String adviserName);

    List<DeclareVo> getList(String authorization);

    List<DeclareVo> search(String authorization, SearchDto searchDto);

    void add2(DeclareDto declareDto, String authorization);

    String getName(Long id);

    void delete(Long id);

    FileVo upload(MultipartFile file);


/*
    ResponseEntity<FileSystemResource> download(Long bid);
*/

    ResponseEntity<Resource> xiafa(Long bid) throws MalformedURLException;

    FileVo getFileName(Long bid);

    List<DeclareVo> getAll();

    void zhiding(ZhiDingDto zhiDingDto);
}

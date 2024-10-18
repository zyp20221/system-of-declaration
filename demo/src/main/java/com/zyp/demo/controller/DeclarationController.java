package com.zyp.demo.controller;


import cn.hutool.core.bean.BeanUtil;
import com.zyp.demo.entity.dto.DeclareDto;
import com.zyp.demo.entity.dto.SearchDto;
import com.zyp.demo.entity.dto.ZhiDingDto;
import com.zyp.demo.entity.po.Declaration;
import com.zyp.demo.entity.vo.DeclareVo;
import com.zyp.demo.entity.vo.FileVo;
import com.zyp.demo.service.IDeclarationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author author
 * @since 2024-08-15
 */
@Api(tags = "申报相关接口")
@Slf4j
@RestController
@RequestMapping("/declaration")
@RequiredArgsConstructor
public class DeclarationController {
    private final IDeclarationService declarationService;

    @ApiOperation("用户申报保存接口")
    @PostMapping
    public void add(@RequestHeader("Authorization") String authorization,
                    @RequestBody DeclareDto declareDto) {
        declarationService.add(declareDto,authorization);
    }
    @ApiOperation("获取所有的申报书")
    @GetMapping("/all")
    public List<DeclareVo> getAllDeclaration(){
        return declarationService.getAll();
    }
    @ApiOperation("用户申报提交接口")
    @PostMapping("/tijiao")
    public void add2(@RequestHeader("Authorization") String authorization,
                    @RequestBody DeclareDto declareDto) {
        declarationService.add2(declareDto,authorization);
    }
    @ApiOperation("根据老师姓名查老师")
    @GetMapping
    public Long get(String adviserName) {
        return declarationService.get(adviserName);
    }
    @ApiOperation("查询申报书数据库中登录人id的数据")
    @GetMapping("/list")
    public List<DeclareVo> list(@RequestHeader("Authorization") String authorization){
        return  declarationService.getList(authorization);
    }
    @ApiOperation("根据搜索条件搜索数据")
    @GetMapping("/search")
    public List<DeclareVo> search(@RequestHeader("Authorization") String authorization,SearchDto searchDto){
        return declarationService.search(authorization,searchDto);
    }
    @ApiOperation("根据申报书id查数据")
    @GetMapping("/{id}")
    public DeclareDto getById(@PathVariable Long id){
        Declaration byId = declarationService.getById(id);
        return  BeanUtil.copyProperties(byId, DeclareDto.class);
    }
    @ApiOperation("根据老师id查数据")
    @GetMapping("/adviser/{id}")
    public String getName(@PathVariable Long id){
        return  declarationService.getName(id);
    }
    @ApiOperation("删除申报书")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        declarationService.delete(id);
    }
    @ApiOperation("申报书文件上传")
    @PostMapping("/upload")
    public FileVo upload(@RequestParam("file") MultipartFile file){
        return declarationService.upload(file);
    }
    @ApiOperation("根据申报书id把文件给前端")
    @GetMapping("/download/{bid}")
    public ResponseEntity<Resource> xiafa(@PathVariable Long bid) throws MalformedURLException {
        return declarationService.xiafa(bid);
    }
    @ApiOperation("获取文件名")
    @GetMapping("/getFileName/{bid}")
    public FileVo getFileName(@PathVariable Long bid){
        return declarationService.getFileName(bid);
    }
    @ApiOperation("指定申报书的评审老师")
    @PostMapping("/zhiding")
    public void zhiding(@RequestBody ZhiDingDto zhiDingDto){
        declarationService.zhiding(zhiDingDto);
    }
    /*@ApiOperation("根据文件路径把文件给前端")
    @GetMapping("/download/{bid}")
    public ResponseEntity<FileSystemResource> downloadAttachment(@PathVariable Long bid) {
        return declarationService.download(bid);
    }*/
}

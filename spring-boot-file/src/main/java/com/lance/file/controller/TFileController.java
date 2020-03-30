package com.lance.file.controller;

import com.lance.file.entity.TFile;
import com.lance.file.service.TFileService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * (TFile)表控制层
 *
 * @author makejava
 * @since 2020-03-19 13:20:49
 */
@RestController
@RequestMapping("tFile")
public class TFileController {
    /**
     * 服务对象
     */
    @Resource
    private TFileService tFileService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public TFile selectOne(Integer id) {
        return this.tFileService.queryById(id);
    }


    @PostMapping("upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file) {
        return this.tFileService.getUploadFilePath(file);
    }

}

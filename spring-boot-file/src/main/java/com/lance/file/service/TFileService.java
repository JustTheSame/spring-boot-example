package com.lance.file.service;

import com.lance.file.entity.TFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Description:
 * @author: zhaotian
 * @date: 2020/3/30
 */
public interface TFileService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TFile queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<TFile> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param tFile 实例对象
     * @return 实例对象
     */
    TFile insert(TFile tFile);

    /**
     * 修改数据
     *
     * @param tFile 实例对象
     * @return 实例对象
     */
    TFile update(TFile tFile);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    String getUploadFilePath(MultipartFile file);
}

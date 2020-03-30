package com.lance.file.dao;

import com.lance.file.entity.TFile;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * (TFile)表数据库访问层
 *
 * @author makejava
 * @since 2020-03-19 13:20:44
 */
public interface TFileDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TFile queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<TFile> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param tFile 实例对象
     * @return 对象列表
     */
    List<TFile> queryAll(TFile tFile);

    /**
     * 新增数据
     *
     * @param tFile 实例对象
     * @return 影响行数
     */
    int insert(TFile tFile);

    /**
     * 修改数据
     *
     * @param tFile 实例对象
     * @return 影响行数
     */
    int update(TFile tFile);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}
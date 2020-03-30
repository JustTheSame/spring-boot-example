package com.lance.file.service.impl;

import com.lance.file.entity.TFile;
import com.lance.file.dao.TFileDao;
import com.lance.file.service.TFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

/**
 * (TFile)表服务实现类
 *
 * @author makejava
 * @since 2020-03-19 13:20:48
 */
@Service("tFileService")
public class TFileServiceImpl implements TFileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TFileServiceImpl.class);

    @Value("${file.rootPath}")
    private String ROOT_PATH;

    @Value("${file.sonPath}")
    private String SON_PATH;

    @Value("${server.port}")
    private String PORT;

    @Resource
    private TFileDao tFileDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TFile queryById(Integer id) {
        return this.tFileDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<TFile> queryAllByLimit(int offset, int limit) {
        return this.tFileDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param tFile 实例对象
     * @return 实例对象
     */
    @Override
    public TFile insert(TFile tFile) {
        this.tFileDao.insert(tFile);
        return tFile;
    }

    /**
     * 修改数据
     *
     * @param tFile 实例对象
     * @return 实例对象
     */
    @Override
    public TFile update(TFile tFile) {
        this.tFileDao.update(tFile);
        return this.queryById(tFile.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.tFileDao.deleteById(id) > 0;
    }

    @Override
    public String getUploadFilePath(MultipartFile file) {

        if (file.isEmpty()) {
            throw new NullPointerException("文件为空");
        }
        String filePath = ROOT_PATH + SON_PATH;
        LOGGER.info(filePath);
        String suffix = file.getOriginalFilename();
        String prefix = suffix.substring(suffix.lastIndexOf(".") + 1);
        Random random = new Random();
        Integer randomFileName = random.nextInt(1000);
        String filename = randomFileName + "." + prefix;
        File dest = new File(filePath + filename);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
            String filePathNew = SON_PATH + filename;
            String profilePhoto = saveUploadFile(filePathNew);
            System.out.println(profilePhoto);
            return profilePhoto;
        } catch (Exception e) {
            return dest.toString();
        }
    }

    private String saveUploadFile(String filePathNew) {
        String host = null;
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        TFile file = new TFile();
        file.setCreateTime(Date.from(Instant.now()));
        file.setUpdateTime(Date.from(Instant.now()));
        file.setProfilePhoto(host + ":" + PORT + filePathNew);
        Integer result = tFileDao.insert(file);
        LOGGER.info("insert " + result + " data");
        LOGGER.info("url: " + file.getProfilePhoto());
        return file.getProfilePhoto();
    }
}

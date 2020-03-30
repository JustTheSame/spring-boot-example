package com.lance.file.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * (TFile)实体类
 *
 * @author makejava
 * @since 2020-03-19 13:20:43
 */
public class TFile implements Serializable {
    private static final long serialVersionUID = -11905705226587136L;
    
    private Integer id;
    
    private String profilePhoto;
    
    private Date createTime;
    
    private Date updateTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}
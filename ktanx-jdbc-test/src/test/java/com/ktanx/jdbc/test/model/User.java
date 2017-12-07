package com.ktanx.jdbc.test.model;

import com.ktanx.common.model.Pageable;
import com.ktanx.jdbc.annotation.Transient;

import java.util.Date;

/**
 * 用户
 * <p>
 * User: liyd
 * Date: Wed Dec 24 16:46:48 CST 2014
 */
public class User extends Pageable {

    private static final long serialVersionUID = 8166785520231287816L;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 登录名
     */
    private String loginName;

    /**
     * 密码
     */
    private String password;

    /**
     * 年龄
     */
    private Integer userAge;

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModify;

    /**
     * 数据库无
     */
    private Date gmtBeginTime;

    @Transient
    public Date getGmtBeginTime() {
        return gmtBeginTime;
    }

    public void setGmtBeginTime(Date gmtBeginTime) {
        this.gmtBeginTime = gmtBeginTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //    @Column("`USER_AGE`")
    public Integer getUserAge() {
        return userAge;
    }

    public void setUserAge(Integer userAge) {
        this.userAge = userAge;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(Date gmtModify) {
        this.gmtModify = gmtModify;
    }

}

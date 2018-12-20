/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.front.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.entity.BaseEntity;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 前台用户扩展信息表Entity
 * @author xuyuxiang
 * @version 2018-12-18
 */
@Table(name="${_prefix}front_user", alias="a", columns={
        @Column(includeEntity=DataEntity.class),
        @Column(name="up_code", attrName="upCode", label="UP主编码", isPK=true),
        @Column(name="up_name", attrName="upName", label="UP主姓名", queryType=QueryType.LIKE),
        @Column(name="up_city", attrName="upCity", label="城市"),
        @Column(name="up_kiss", attrName="upKiss", label="飞吻数"),
        @Column(name="up_auth", attrName="upAuth", label="认证信息"),
        @Column(name="up_vlevel", attrName="upVlevel", label="VIP等级"),
        @Column(name="up_sign_date", attrName="upSignDate", label="上次签到日期"),
        @Column(name="up_sign_count", attrName="upSignCount", label="连续签到天数"),
}, orderBy="a.update_date DESC"
)
public class Front extends DataEntity<Front> {

    private static final long serialVersionUID = 1L;
    private String upCode;		// UP主编码
    private String upName;		// UP主姓名
    private String upCity;		// UP主城市
    private Long upKiss;		// UP主飞吻数
    private String upAuth;		// UP主认证信息
    private Long upVlevel;		// UP主VIP等级
    private Date upSignDate;		// 上次签到日期
    private Long upSignCount;		// 连续签到天数

    public Front() {
        this(null);
    }

    public Front(String id){
        super(id);
    }

    public String getUpCode() {
        return upCode;
    }

    public void setUpCode(String upCode) {
        this.upCode = upCode;
    }

    @NotBlank(message="姓名不能为空")
    @Length(min=0, max=100, message="姓名长度不能超过 100 个字符")
    public String getUpName() {
        return upName;
    }

    public void setUpName(String upName) {
        this.upName = upName;
    }

    @Length(min=0, max=100, message="城市长度不能超过 100 个字符")
    public String getUpCity() {
        return upCity;
    }

    public void setUpCity(String upCity) {
        this.upCity = upCity;
    }

    public Long getUpKiss() {
        return upKiss;
    }

    public void setUpKiss(Long upKiss) {
        this.upKiss = upKiss;
    }

    @Length(min=0, max=100, message="认证信息长度不能超过 100 个字符")
    public String getUpAuth() {
        return upAuth;
    }

    public void setUpAuth(String upAuth) {
        this.upAuth = upAuth;
    }

    public Long getUpVlevel() {
        return upVlevel;
    }

    public void setUpVlevel(Long upVlevel) {
        this.upVlevel = upVlevel;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getUpSignDate() {
        return upSignDate;
    }

    public void setUpSignDate(Date upSignDate) {
        this.upSignDate = upSignDate;
    }

    public Long getUpSignCount() {
        return upSignCount;
    }

    public void setUpSignCount(Long upSignCount) {
        this.upSignCount = upSignCount;
    }

}
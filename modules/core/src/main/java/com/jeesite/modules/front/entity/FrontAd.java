/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.front.entity;

import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import org.springframework.data.annotation.Transient;

/**
 * 广告表Entity
 * @author xuyuxiang
 * @version 2019-01-09
 */
@Table(name="${_prefix}front_ad", alias="a", columns={
		@Column(name="id", attrName="id", label="编号", isPK=true),
		@Column(name="link", attrName="link", label="链接地址"),
		@Column(name="ad_type", attrName="adType", label="赞助商类型"),
		@Column(name="ad_title", attrName="adTitle", label="广告文字", queryType=QueryType.LIKE),
		@Column(name="ad_back_color", attrName="adBackColor", label="背景色"),
		@Column(includeEntity=DataEntity.class),
	}, orderBy="a.create_date DESC"
)
public class FrontAd extends DataEntity<FrontAd> {
	
	private static final long serialVersionUID = 1L;
	private String link;		// 链接地址
	private String adType;		// 赞助商类型
	private String adTitle;		// 广告文字
	@Transient
	private String picPath;
	@Transient
	private String delPicId;
	private String adBackColor;//背景色
	
	public FrontAd() {
		this(null);
	}

	public FrontAd(String id){
		super(id);
	}
	
	@NotBlank(message="链接地址不能为空")
	@Length(min=0, max=255, message="链接地址长度不能超过 255 个字符")
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
	@NotBlank(message="赞助商类型不能为空")
	@Length(min=0, max=1, message="赞助商类型长度不能超过 1 个字符")
	public String getAdType() {
		return adType;
	}

	public void setAdType(String adType) {
		this.adType = adType;
	}
	
	@Length(min=0, max=255, message="广告文字长度不能超过 255 个字符")
	public String getAdTitle() {
		return adTitle;
	}

	public void setAdTitle(String adTitle) {
		this.adTitle = adTitle;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public String getDelPicId() {
		return delPicId;
	}

	public void setDelPicId(String delPicId) {
		this.delPicId = delPicId;
	}

	public String getAdBackColor() {
		return adBackColor;
	}

	public void setAdBackColor(String adBackColor) {
		this.adBackColor = adBackColor;
	}
}
/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.front.entity;

import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.jeesite.modules.sys.entity.User;
import org.hibernate.validator.constraints.Length;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 收藏表Entity
 * @author xuyuxiang
 * @version 2018-12-29
 */
@Table(name="${_prefix}front_collect", alias="a", columns={
		@Column(name="id", attrName="id", label="编号", isPK=true),
		@Column(name="user_code", attrName="userCode", label="用户编码"),
		@Column(name="post_id", attrName="collectPost.id", label="帖子编码"),
		@Column(includeEntity=DataEntity.class),
	}, joinTable= {
		@JoinTable(type = Type.LEFT_JOIN, entity = FrontPost.class, alias = "p",
				on = "p.id = a.post_id", attrName = "collectPost", columns = {
				@Column(includeEntity= FrontPost.class)})
		},orderBy="a.user_code DESC"
)
public class FrontCollect extends DataEntity<FrontCollect> {
	
	private static final long serialVersionUID = 1L;
	// 用户编码
	private String userCode;
	// 帖子编码
	private FrontPost collectPost;
	
	public FrontCollect() {
		this(null);
	}

	public FrontCollect(String id){
		super(id);
	}
	
	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public FrontPost getCollectPost() {
		return collectPost;
	}

	public void setCollectPost(FrontPost collectPost) {
		this.collectPost = collectPost;
	}
}
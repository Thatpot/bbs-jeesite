/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.front.entity;

import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.modules.sys.entity.User;

/**
 * 前台用户表Entity
 * @author xuyuxiang
 * @version 2018-12-18
 */
@Table(name="${_prefix}sys_user", alias="a", columns={
		@Column(includeEntity= User.class),
	},joinTable= {
		@JoinTable(type = Type.JOIN, entity = Front.class, alias = "f",
				on = "f.up_code=a.ref_code AND a.user_type=#{USER_TYPE_FRONT}",
				attrName = "front", columns = {
				@Column(includeEntity = Front.class)
		})
	}, extWhereKeys="extkeys"
)
public class FrontUser extends User {

	private static final long serialVersionUID = 1L;
	public static final String USER_TYPE_FRONT = "front";

	public FrontUser() {
		this(null);
	}

	public FrontUser(String id){
		super(id);
	}

	public Front getFront(){
		Front front = (Front)super.getRefObj();
		if (front == null){
			front = new Front();
			super.setRefObj(front);
		}
		return front;
	}

	public void setFront(Front front){
		super.setRefObj(front);
	}
	
}
/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.front.entity;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 帖子表Entity
 * @author xuyuxiang
 * @version 2018-12-21
 */
@Table(name="${_prefix}front_comment", alias="a", columns={
		@Column(name="id", attrName="id", label="编号", isPK=true),
		@Column(name="post_id", attrName="postId.id", label="编号"),
		@Column(includeEntity=DataEntity.class),
		@Column(name="comment_up", attrName="commentUp.userCode", label="评论者"),
		@Column(name="comment_content", attrName="commentContent", label="评论内容"),
		@Column(name="comment_isaccept", attrName="commentIsaccept", label="是否被采纳"),
	}, orderBy="a.comment_isaccept DESC,a.create_date DESC"
)
public class FrontComment extends DataEntity<FrontComment> {
	
	private static final long serialVersionUID = 1L;
	private FrontPost postId;		// 编号 父类
	private FrontUser commentUp;		// 评论者
	private String commentContent;		// 评论内容
	private String commentIsaccept;		// 是否被采纳

	public FrontComment() {
		this(null);
	}


	public FrontComment(FrontPost postId){
		this.postId = postId;
	}
	
	@Length(min=0, max=64, message="编号长度不能超过 64 个字符")
	public FrontPost getPostId() {
		return postId;
	}

	public void setPostId(FrontPost postId) {
		this.postId = postId;
	}
	
	@NotBlank(message="评论内容不能为空")
	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}
	
	@Length(min=0, max=1, message="是否被采纳长度不能超过 1 个字符")
	public String getCommentIsaccept() {
		return commentIsaccept;
	}

	public void setCommentIsaccept(String commentIsaccept) {
		this.commentIsaccept = commentIsaccept;
	}

	public FrontUser getCommentUp() {
		return commentUp;
	}

	public void setCommentUp(FrontUser commentUp) {
		this.commentUp = commentUp;
	}
}
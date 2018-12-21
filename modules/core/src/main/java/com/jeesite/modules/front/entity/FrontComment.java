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
		@Column(name="comment_username", attrName="commentUsername", label="评论者"),
		@Column(name="comment_avatar", attrName="commentAvatar", label="评论者头像"),
		@Column(name="comment_vlevel", attrName="commentVlevel", label="评论者VIP等级"),
		@Column(name="comment_content", attrName="commentContent", label="评论内容"),
		@Column(name="comment_isaccept", attrName="commentIsaccept", label="是否被采纳"),
	}, orderBy="a.create_date ASC"
)
public class FrontComment extends DataEntity<FrontComment> {
	
	private static final long serialVersionUID = 1L;
	private FrontPost postId;		// 编号 父类
	private String commentUsername;		// 评论者
	private String commentAvatar;		// 评论者头像
	private Long commentVlevel;		// 评论者VIP等级
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
	
	@Length(min=0, max=100, message="评论者长度不能超过 100 个字符")
	public String getCommentUsername() {
		return commentUsername;
	}

	public void setCommentUsername(String commentUsername) {
		this.commentUsername = commentUsername;
	}
	
	@Length(min=0, max=255, message="评论者头像长度不能超过 255 个字符")
	public String getCommentAvatar() {
		return commentAvatar;
	}

	public void setCommentAvatar(String commentAvatar) {
		this.commentAvatar = commentAvatar;
	}
	
	public Long getCommentVlevel() {
		return commentVlevel;
	}

	public void setCommentVlevel(Long commentVlevel) {
		this.commentVlevel = commentVlevel;
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
	
}
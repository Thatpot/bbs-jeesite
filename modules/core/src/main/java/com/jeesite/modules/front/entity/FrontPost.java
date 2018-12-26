/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.front.entity;

import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import java.util.List;
import com.jeesite.common.collect.ListUtils;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 帖子表Entity
 * @author xuyuxiang
 * @version 2018-12-21
 */
@Table(name="${_prefix}front_post", alias="a", columns={
		@Column(name="id", attrName="id", label="编号", isPK=true),
		@Column(name="post_title", attrName="postTitle", label="帖子标题", queryType=QueryType.LIKE),
		@Column(name="post_content", attrName="postContent", label="帖子内容"),
		@Column(name="post_category", attrName="postCategory", label="帖子分类"),
		@Column(name="post_status", attrName="postStatus", label="帖子结束状态"),
		@Column(name="post_kiss", attrName="postKiss", label="帖子悬赏飞吻数"),
		@Column(name="post_comment_count", attrName="postCommentCount", label="帖子评论数"),
		@Column(name="post_view_count", attrName="postViewCount", label="帖子查看人数"),
		@Column(name="post_istop", attrName="postIstop", label="是否置顶"),
		@Column(name="post_isgood", attrName="postIsgood", label="是否精贴"),
		@Column(includeEntity=DataEntity.class),
		@Column(name="post_auth", attrName="postAuth", label="帖子作者"),
		@Column(name="post_auth_avatar", attrName="postAuthAvatar", label="作者头像"),
		@Column(name="post_auth_vlevel", attrName="postAuthVlevel", label="作者VIP等级"),
		@Column(name="post_auth_info", attrName="postAuthInfo", label="作者认证信息"),
	}, orderBy="a.post_istop DESC,a.create_date DESC"
)
public class FrontPost extends DataEntity<FrontPost> {
	
	private static final long serialVersionUID = 1L;
	private String postTitle;		// 帖子标题
	private String postContent;		// 帖子内容
	private String postCategory;		// 帖子分类
	private String postStatus;		// 帖子结束状态
	private Long postKiss;		// 帖子悬赏飞吻数
	private Long postCommentCount;		// 帖子评论数
	private Long postViewCount;		// 帖子查看人数
	private String postIstop;		// 是否置顶
	private String postIsgood;		// 是否精贴
	private String postAuth;		// 帖子作者
	private String postAuthAvatar;		// 作者头像
	private Long postAuthVlevel;		// 作者VIP等级
	private String postAuthInfo;
	private List<FrontComment> frontCommentList = ListUtils.newArrayList();		// 子表列表
	
	public FrontPost() {
		this(null);
	}

	public FrontPost(String id){
		super(id);
	}
	
	@NotBlank(message="帖子标题不能为空")
	@Length(min=0, max=200, message="帖子标题长度不能超过 200 个字符")
	public String getPostTitle() {
		return postTitle;
	}

	public void setPostTitle(String postTitle) {
		this.postTitle = postTitle;
	}
	
	@NotBlank(message="帖子内容不能为空")
	public String getPostContent() {
		return postContent;
	}

	public void setPostContent(String postContent) {
		this.postContent = postContent;
	}
	
	@NotBlank(message="帖子分类不能为空")
	@Length(min=0, max=1, message="帖子分类长度不能超过 1 个字符")
	public String getPostCategory() {
		return postCategory;
	}

	public void setPostCategory(String postCategory) {
		this.postCategory = postCategory;
	}
	
	@Length(min=0, max=1, message="帖子结束状态长度不能超过 1 个字符")
	public String getPostStatus() {
		return postStatus;
	}

	public void setPostStatus(String postStatus) {
		this.postStatus = postStatus;
	}
	
	public Long getPostKiss() {
		return postKiss;
	}

	public void setPostKiss(Long postKiss) {
		this.postKiss = postKiss;
	}
	
	public Long getPostCommentCount() {
		return postCommentCount;
	}

	public void setPostCommentCount(Long postCommentCount) {
		this.postCommentCount = postCommentCount;
	}
	
	public Long getPostViewCount() {
		return postViewCount;
	}

	public void setPostViewCount(Long postViewCount) {
		this.postViewCount = postViewCount;
	}
	
	@Length(min=0, max=1, message="是否置顶长度不能超过 1 个字符")
	public String getPostIstop() {
		return postIstop;
	}

	public void setPostIstop(String postIstop) {
		this.postIstop = postIstop;
	}
	
	@Length(min=0, max=1, message="是否精贴长度不能超过 1 个字符")
	public String getPostIsgood() {
		return postIsgood;
	}

	public void setPostIsgood(String postIsgood) {
		this.postIsgood = postIsgood;
	}
	
	@Length(min=0, max=100, message="帖子作者长度不能超过 100 个字符")
	public String getPostAuth() {
		return postAuth;
	}

	public void setPostAuth(String postAuth) {
		this.postAuth = postAuth;
	}
	
	@Length(min=0, max=255, message="作者头像长度不能超过 255 个字符")
	public String getPostAuthAvatar() {
		return postAuthAvatar;
	}

	public void setPostAuthAvatar(String postAuthAvatar) {
		this.postAuthAvatar = postAuthAvatar;
	}
	
	public Long getPostAuthVlevel() {
		return postAuthVlevel;
	}

	public void setPostAuthVlevel(Long postAuthVlevel) {
		this.postAuthVlevel = postAuthVlevel;
	}

	public String getPostAuthInfo() {
		return postAuthInfo;
	}

	public void setPostAuthInfo(String postAuthInfo) {
		this.postAuthInfo = postAuthInfo;
	}

	public List<FrontComment> getFrontCommentList() {
		return frontCommentList;
	}

	public void setFrontCommentList(List<FrontComment> frontCommentList) {
		this.frontCommentList = frontCommentList;
	}

}
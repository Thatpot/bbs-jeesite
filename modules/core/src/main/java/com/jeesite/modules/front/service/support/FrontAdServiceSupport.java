/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.front.service.support;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.file.entity.FileUpload;
import com.jeesite.modules.file.utils.FileUploadUtils;
import com.jeesite.modules.front.dao.FrontAdDao;
import com.jeesite.modules.front.entity.FrontAd;
import com.jeesite.modules.front.service.FrontAdService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 广告表Service
 * @author xuyuxiang
 * @version 2019-01-09
 */
@Service
@Transactional(readOnly=true)
public class FrontAdServiceSupport extends CrudService<FrontAdDao, FrontAd> implements FrontAdService {
	
	/**
	 * 获取单条数据
	 * @param frontAd
	 * @return
	 */
	@Override
	public FrontAd get(FrontAd frontAd) {
		frontAd =  super.get(frontAd);
		List<FileUpload> list = FileUploadUtils.findFileUpload(frontAd.getId(),"frontAd_image");
		if(list.size()!=0){
			FileUpload fileUpload =  list.get(0);
			frontAd.setPicPath("/js"+fileUpload.getFileUrl());
			frontAd.setDelPicId(fileUpload.getId());
		}
		return frontAd;
	}
	
	/**
	 * 查询分页数据
	 * @param frontAd 查询条件
	 * @return
	 */
	@Override
	public Page<FrontAd> findPage(FrontAd frontAd) {
		Page<FrontAd> frontAdPage = super.findPage(frontAd);
		for (FrontAd item:frontAdPage.getList()) {
			List<FileUpload> list = FileUploadUtils.findFileUpload(item.getId(),"frontAd_image");
			if(list.size()!=0){
				FileUpload fileUpload =  list.get(0);
				item.setPicPath("/js"+fileUpload.getFileUrl());
				item.setDelPicId(fileUpload.getId());
			}else{
				item.setPicPath("");
				item.setDelPicId("");
			}
		}
		return frontAdPage;
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param frontAd
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(FrontAd frontAd) {
		super.save(frontAd);
		// 保存上传图片
		FileUploadUtils.saveFileUpload(frontAd.getId(), "frontAd_image");
	}
	
	/**
	 * 更新状态
	 * @param frontAd
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(FrontAd frontAd) {
		super.updateStatus(frontAd);
	}
	
	/**
	 * 删除数据
	 * @param frontAd
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(FrontAd frontAd) {
		super.delete(frontAd);
	}
	
}
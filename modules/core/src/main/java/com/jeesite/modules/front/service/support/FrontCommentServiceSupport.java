package com.jeesite.modules.front.service.support;

import com.jeesite.common.service.CrudService;
import com.jeesite.modules.front.dao.FrontCommentDao;
import com.jeesite.modules.front.dao.FrontUserDao;
import com.jeesite.modules.front.entity.FrontCollect;
import com.jeesite.modules.front.entity.FrontComment;
import com.jeesite.modules.front.entity.FrontPost;
import com.jeesite.modules.front.entity.FrontUser;
import com.jeesite.modules.front.service.FrontCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName FrontCommentServiceSupport
 * @Description 评论service实现类
 * @Author xuyux
 * @Date 2019/1/2 11:12
 **/
@Service
@Transactional(readOnly=true)
public class FrontCommentServiceSupport extends CrudService<FrontCommentDao, FrontComment> implements FrontCommentService {



}

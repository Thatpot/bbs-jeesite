package com.jeesite.modules.front.service.support;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.front.dao.FrontCollectDao;
import com.jeesite.modules.front.dao.FrontCommentDao;
import com.jeesite.modules.front.dao.FrontPostDao;
import com.jeesite.modules.front.dao.FrontUserDao;
import com.jeesite.modules.front.entity.FrontCollect;
import com.jeesite.modules.front.entity.FrontComment;
import com.jeesite.modules.front.entity.FrontPost;
import com.jeesite.modules.front.entity.FrontUser;
import com.jeesite.modules.front.service.FrontCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName FrontCommentServiceSupport
 * @Description 评论service实现类
 * @Author xuyux
 * @Date 2019/1/2 11:12
 **/
@Service
@Transactional(readOnly=true)
public class FrontCommentServiceSupport extends CrudService<FrontCommentDao, FrontComment> implements FrontCommentService {
    @Autowired
    private FrontCommentDao frontCommentDao;
    @Autowired
    private FrontUserDao frontUserDao;
    @Autowired
    private FrontPostDao frontPostDao;
    /**
     * 获取单条数据
     * @param frontComment
     * @return
     */
    @Override
    public FrontComment get(FrontComment frontComment) {
        frontComment = super.get(frontComment);
        FrontUser frontUser = frontUserDao.get(frontComment.getCommentUp());
        frontComment.setCommentUp(frontUser);
        return frontComment;
    }

    @Override
    public FrontComment getByEntity(FrontComment frontComment) {
        return frontCommentDao.getByEntity(frontComment);
    }

    /**
     * 查询分页数据
     * @param frontComment 查询条件
     * @return
     */
    @Override
    public Page<FrontComment> findPage(FrontComment frontComment) {
        return super.findPage(frontComment);
    }

    /**
     * @Author xuyuxiang
     * @Description 根据条件查询列表
     * @Date 15:32 2019/1/3
     * @Param [frontComment]
     * @return java.util.List<com.jeesite.modules.front.entity.FrontComment>
     **/
    @Override
    public List<FrontComment> findList(FrontComment frontComment){
        List<FrontComment> list = frontCommentDao.findList(frontComment);
        for (FrontComment item:list) {
            FrontUser frontUser = frontUserDao.get(item.getCommentUp());
            item.setCommentUp(frontUser);
            FrontPost frontPost = frontPostDao.get(item.getPostId());
            item.setPostId(frontPost);
        }
        return list;
    }

    /**
     * 保存数据（插入或更新）
     * @param frontComment
     */
    @Override
    @Transactional(readOnly=false)
    public void save(FrontComment frontComment) {
        super.save(frontComment);
    }

    /**
     * 删除数据
     * @param frontComment
     */
    @Override
    @Transactional(readOnly=false)
    public void delete(FrontComment frontComment) {
        super.delete(frontComment);
    }

}

package com.jeesite.modules.front.service.support;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.front.dao.FrontCommentDao;
import com.jeesite.modules.front.dao.FrontPostDao;
import com.jeesite.modules.front.entity.FrontComment;
import com.jeesite.modules.front.entity.FrontPost;
import com.jeesite.modules.front.service.FrontPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName FrontPostServiceSupport
 * @Description TODO
 * @Author xuyux
 * @Date 2018/12/29 14:58
 **/
@Service
@Transactional(readOnly=true)
public class FrontPostServiceSupport extends CrudService<FrontPostDao, FrontPost> implements FrontPostService {

    @Autowired
    private FrontCommentDao frontCommentDao;

    /**
     * 获取单条数据
     * @param frontPost
     * @return
     */
    @Override
    public FrontPost get(FrontPost frontPost) {
        FrontPost entity = super.get(frontPost);
        if (entity != null){
            FrontComment frontComment = new FrontComment(entity);
            frontComment.setStatus(FrontComment.STATUS_NORMAL);
            entity.setFrontCommentList(frontCommentDao.findList(frontComment));
        }
        return entity;
    }

    /**
     * 查询分页数据
     * @param frontPost 查询条件
     * @return
     */
    @Override
    public Page<FrontPost> findPage(FrontPost frontPost) {
        return super.findPage(frontPost);
    }

    /**
     * 保存数据（插入或更新）
     * @param frontPost
     */
    @Override
    @Transactional(readOnly=false)
    public void save(FrontPost frontPost) {
        super.save(frontPost);
        // 保存 FrontPost子表
        for (FrontComment frontComment : frontPost.getFrontCommentList()){
            if (!FrontComment.STATUS_DELETE.equals(frontComment.getStatus())){
                frontComment.setPostId(frontPost);
                if (frontComment.getIsNewRecord()){
                    frontComment.preInsert();
                    frontCommentDao.insert(frontComment);
                }else{
                    frontComment.preUpdate();
                    frontCommentDao.update(frontComment);
                }
            }else{
                frontCommentDao.delete(frontComment);
            }
        }
    }

    /**
     * 更新状态
     * @param frontPost
     */
    @Override
    @Transactional(readOnly=false)
    public void updateStatus(FrontPost frontPost) {
        super.updateStatus(frontPost);
    }

    /**
     * 删除数据
     * @param frontPost
     */
    @Override
    @Transactional(readOnly=false)
    public void delete(FrontPost frontPost) {
        super.delete(frontPost);
        FrontComment frontComment = new FrontComment();
        frontComment.setPostId(frontPost);
        frontCommentDao.delete(frontComment);
    }
}

package com.jeesite.modules.front.service.support;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.front.dao.FrontCommentDao;
import com.jeesite.modules.front.dao.FrontPostDao;
import com.jeesite.modules.front.dao.FrontUserDao;
import com.jeesite.modules.front.entity.FrontComment;
import com.jeesite.modules.front.entity.FrontPost;
import com.jeesite.modules.front.entity.FrontUser;
import com.jeesite.modules.front.service.FrontPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    @Autowired
    private FrontUserDao frontUserDao;

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
            List<FrontComment> list = frontCommentDao.findList(frontComment);
            for (FrontComment item : list) {
                FrontUser frontUser = frontUserDao.get(item.getCommentUp());
                item.setCommentUp(frontUser);
            }
            entity.setFrontCommentList(list);

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
    /**
     * @Author xuyuxiang
     * @Description 查询该帖子是否有最佳答案
     * @Date 16:14 2019/1/2
     * @Param [frontPost]
     * @return boolean
     **/
    @Override
    public boolean hasAccept(FrontPost frontPost) {
        boolean flag = false;
        for (FrontComment frontComment: frontPost.getFrontCommentList()) {
            if("1".equals(frontComment.getCommentIsaccept())){
                flag = true;
                continue;
            }
        }
        return flag;
    }
    /**
     * @Author xuyuxiang
     * @Description 根据分类查询本周热议
     * @Date 13:41 2019/1/4
     * @Param [frontPost]
     * @return java.util.List<com.jeesite.modules.front.entity.FrontPost>
     **/
    @Override
    public List<FrontPost> findHotPlostList(FrontPost frontPost,int pageSize) {
        Page<FrontPost> page = new Page<FrontPost>();
        page.setPageSize(pageSize);
        page.setOrderBy("a.post_comment_count DESC");
        frontPost.setPage(page);
        return this.findPage(frontPost).getList();
    }
}

package com.jeesite.modules.front.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.api.CrudServiceApi;
import com.jeesite.modules.front.entity.FrontCollect;
import com.jeesite.modules.front.entity.FrontComment;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author xuyuxiang
 * @Description 评论service层
 * @Date 11:12 2019/1/2
 * @Param
 * @return
 **/
public interface FrontCommentService extends CrudServiceApi<FrontComment> {
    /**
     * 获取单条数据
     * @param frontComment
     * @return
     */
    @Override
    public FrontComment get(FrontComment frontComment);

    /**
     * @Author xuyuxiang
     * @Description 根据实体获取数据
     * @Date 17:32 2018/12/29
     * @Param [frontComment]
     * @return FrontComment
     **/
    public FrontComment getByEntity(FrontComment frontComment);


    /**
     * 查询分页数据
     * @param frontComment 查询条件
     * @return
     */
    @Override
    public Page<FrontComment> findPage(FrontComment frontComment);

    /**
     * @Author xuyuxiang
     * @Description 根据条件查询列表
     * @Date 15:30 2019/1/3
     * @Param [frontComment]
     * @return java.util.List<com.jeesite.modules.front.entity.FrontComment>
     **/
    @Override
    public List<FrontComment> findList(FrontComment frontComment);

    /**
     * 保存数据（插入或更新）
     * @param frontComment
     */
    @Override
    @Transactional(readOnly=false)
    public void save(FrontComment frontComment);

    /**
     * 删除数据
     * @param frontComment
     */
    @Override
    @Transactional(readOnly=false)
    public void delete(FrontComment frontComment);
}

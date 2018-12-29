package com.jeesite.modules.front.service.support;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.front.dao.FrontCollectDao;
import com.jeesite.modules.front.entity.FrontCollect;
import com.jeesite.modules.front.service.FrontCollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName FrontCollectServiceSupport
 * @Description TODO
 * @Author xuyux
 * @Date 2018/12/29 15:33
 **/
@Service
@Transactional(readOnly=true)
public class FrontCollectServiceSupport extends CrudService<FrontCollectDao, FrontCollect> implements FrontCollectService {

    @Autowired
    private FrontCollectDao frontCollectDao;
    /**
     * 获取单条数据
     * @param frontCollect
     * @return
     */
    @Override
    public FrontCollect get(FrontCollect frontCollect) {
        return super.get(frontCollect);
    }

    @Override
    public FrontCollect getByEntity(FrontCollect frontCollect) {
        return frontCollectDao.getByEntity(frontCollect);
    }

    /**
     * 查询分页数据
     * @param frontCollect 查询条件
     * @return
     */
    @Override
    public Page<FrontCollect> findPage(FrontCollect frontCollect) {
        return super.findPage(frontCollect);
    }

    /**
     * 保存数据（插入或更新）
     * @param frontCollect
     */
    @Override
    @Transactional(readOnly=false)
    public void save(FrontCollect frontCollect) {
        super.save(frontCollect);
    }

    /**
     * 删除数据
     * @param frontCollect
     */
    @Override
    @Transactional(readOnly=false)
    public void delete(FrontCollect frontCollect) {
        super.delete(frontCollect);
    }
}

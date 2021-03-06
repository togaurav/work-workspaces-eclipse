package sshdemo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sshdemo.core.dao.BaseDao;
import sshdemo.core.dao.Pager;
import sshdemo.core.service.BaseServiceImpl;
import sshdemo.dao.FatherDao;
import sshdemo.entity.Father;

@Service("FatherService")
public class FatherService extends BaseServiceImpl<Father, Integer> {

    @Autowired
    @Qualifier("FatherDao")
    private FatherDao fatherDao;

    @Override
    protected BaseDao<Father, Integer> getDao() {
        return fatherDao;
    }

    @Transactional
    public Father getFather() {
        final Father f = fatherDao.findByName("tom");
        initialize(f.getChildren());
        return f;
    }

    @Transactional
    public List<Father> getFathers() {
        final List<Father> fathers = fatherDao.findByQuery("from Father");
        for (final Father f : fathers) {
            initialize(f.getChildren());
        }
        return fathers;
    }

    @Transactional
    public List<Father> getFathers(final Pager pager) {
        final List<Father> fathers = fatherDao.findByQuery("from Father", pager);
        for (final Father f : fathers) {
            initialize(f.getChildren());
        }
        return fathers;
    }
}

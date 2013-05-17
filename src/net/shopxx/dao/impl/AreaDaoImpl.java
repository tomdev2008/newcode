package net.shopxx.dao.impl;

import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.Query;

import net.shopxx.dao.AreaDao;
import net.shopxx.entity.Area;

import org.springframework.stereotype.Repository;

@Repository("areaDaoImpl")
public class AreaDaoImpl extends BaseDaoImpl<Area, Long>
  implements AreaDao
{
  public List<Area> findRoots(Integer count)
  {
    String str = "select area from Area area where area.parent is null order by area.order asc";
	Query localTypedQuery = this.entityManager.createQuery(str).setFlushMode(FlushModeType.COMMIT);
    if (count != null)
      localTypedQuery.setMaxResults(count.intValue());
    return localTypedQuery.getResultList();
  }
}

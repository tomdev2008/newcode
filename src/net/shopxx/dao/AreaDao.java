package net.shopxx.dao;

import java.util.List;
import net.shopxx.entity.Area;

public abstract interface AreaDao extends BaseDao<Area, Long>
{
  public abstract List<Area> findRoots(Integer paramInteger);
}


 * Qualified Name:     net.shopxx.dao.AreaDao


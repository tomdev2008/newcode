package net.shopxx.dao;

import net.shopxx.entity.Log;

public abstract interface LogDao extends BaseDao<Log, Long>
{
  public abstract void removeAll();
}


 * Qualified Name:     net.shopxx.dao.LogDao


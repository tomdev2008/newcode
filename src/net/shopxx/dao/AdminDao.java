package net.shopxx.dao;

import net.shopxx.entity.Admin;

public abstract interface AdminDao extends BaseDao<Admin, Long>
{
  public abstract boolean usernameExists(String paramString);

  public abstract Admin findByUsername(String paramString);
}


 * Qualified Name:     net.shopxx.dao.AdminDao


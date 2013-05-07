package net.shopxx.dao;

import net.shopxx.entity.Seo;
import net.shopxx.entity.Seo.Type;

public abstract interface SeoDao extends BaseDao<Seo, Long>
{
  public abstract Seo find(Seo.Type paramType);
}


 * Qualified Name:     net.shopxx.dao.SeoDao


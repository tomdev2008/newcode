package net.shopxx.dao;

import net.shopxx.entity.Shipping;

public abstract interface ShippingDao extends BaseDao<Shipping, Long>
{
  public abstract Shipping findBySn(String paramString);
}


 * Qualified Name:     net.shopxx.dao.ShippingDao


package net.shopxx.dao;

import net.shopxx.entity.DeliveryCenter;

public abstract interface DeliveryCenterDao extends BaseDao<DeliveryCenter, Long>
{
  public abstract DeliveryCenter findDefault();
}


 * Qualified Name:     net.shopxx.dao.DeliveryCenterDao


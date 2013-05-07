package net.shopxx.dao;

import net.shopxx.entity.DeliveryTemplate;

public abstract interface DeliveryTemplateDao extends BaseDao<DeliveryTemplate, Long>
{
  public abstract DeliveryTemplate findDefault();
}


 * Qualified Name:     net.shopxx.dao.DeliveryTemplateDao


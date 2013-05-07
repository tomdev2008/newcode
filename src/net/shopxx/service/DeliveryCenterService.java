package net.shopxx.service;

import net.shopxx.entity.DeliveryCenter;

public abstract interface DeliveryCenterService extends BaseService<DeliveryCenter, Long>
{
  public abstract DeliveryCenter findDefault();
}

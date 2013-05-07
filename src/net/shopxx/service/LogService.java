package net.shopxx.service;

import net.shopxx.entity.Log;

public abstract interface LogService extends BaseService<Log, Long>
{
  public abstract void clear();
}

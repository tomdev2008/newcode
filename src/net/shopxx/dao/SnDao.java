package net.shopxx.dao;

import net.shopxx.entity.Sn.SnType;

public abstract interface SnDao
{
  public abstract String generate(SnType paramType);
}

package net.shopxx.service;

public abstract interface CacheService
{
  public abstract String getDiskStorePath();

  public abstract int getCacheSize();

  public abstract void clear();
}


 * Qualified Name:     net.shopxx.service.CacheService


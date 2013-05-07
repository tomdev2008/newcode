package net.shopxx.dao;

import net.shopxx.entity.PluginConfig;

public abstract interface PluginConfigDao extends BaseDao<PluginConfig, Long>
{
  public abstract boolean pluginIdExists(String paramString);

  public abstract PluginConfig findByPluginId(String paramString);
}


 * Qualified Name:     net.shopxx.dao.PluginConfigDao


package net.shopxx.service;

import net.shopxx.entity.Seo;
import net.shopxx.entity.Seo.SeoType;

public abstract interface SeoService extends BaseService<Seo, Long>
{
  public abstract Seo find(SeoType paramType);

  public abstract Seo find(SeoType paramType, String paramString);
}

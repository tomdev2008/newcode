package net.shopxx.service;

import net.shopxx.entity.Seo;
import net.shopxx.entity.Seo.Type;

public abstract interface SeoService extends BaseService<Seo, Long>
{
  public abstract Seo find(Seo.Type paramType);

  public abstract Seo find(Seo.Type paramType, String paramString);
}


 * Qualified Name:     net.shopxx.service.SeoService


package net.shopxx.dao;

import net.shopxx.entity.Seo;
import net.shopxx.entity.Seo.SeoType;

public abstract interface SeoDao extends BaseDao<Seo, Long> {
	public abstract Seo find(SeoType paramType);
}

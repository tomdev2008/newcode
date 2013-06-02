package net.shopxx.dao.impl;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;

import net.shopxx.dao.SeoDao;
import net.shopxx.entity.Seo;
import net.shopxx.entity.Seo.SeoType;

import org.springframework.stereotype.Repository;

@Repository("seoDaoImpl")
public class SeoDaoImpl extends BaseDaoImpl<Seo, Long> implements SeoDao {
	public Seo find(SeoType type) {
		if (type == null)
			return null;
		try {
			String str = "select seo from Seo seo where seo.type = :type";
			return (Seo) this.entityManager.createQuery(str, Seo.class)
					.setFlushMode(FlushModeType.COMMIT)
					.setParameter("type", type).getSingleResult();
		} catch (NoResultException localNoResultException1) {
		}
		return null;
	}
}

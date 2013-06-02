package net.shopxx.dao.impl;

import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

import net.shopxx.dao.TagDao;
import net.shopxx.entity.Tag;
import net.shopxx.entity.Tag.TagType;

import org.springframework.stereotype.Repository;

@Repository("tagDaoImpl")
public class TagDaoImpl extends BaseDaoImpl<Tag, Long> implements TagDao {
	public List<Tag> findList(TagType type) {
		CriteriaBuilder localCriteriaBuilder = this.entityManager
				.getCriteriaBuilder();
		CriteriaQuery<Tag> localCriteriaQuery = localCriteriaBuilder
				.createQuery(Tag.class);
		Root<Tag> localRoot = localCriteriaQuery.from(Tag.class);
		localCriteriaQuery.select(localRoot);
		if (type != null)
			localCriteriaQuery.where(localCriteriaBuilder.equal(
					localRoot.get("type"), type));
		localCriteriaQuery.orderBy(new Order[] { localCriteriaBuilder
				.asc(localRoot.get("order")) });
		return this.entityManager.createQuery(localCriteriaQuery)
				.setFlushMode(FlushModeType.COMMIT).getResultList();
	}
}

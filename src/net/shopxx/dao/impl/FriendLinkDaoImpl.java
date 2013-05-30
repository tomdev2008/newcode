package net.shopxx.dao.impl;

import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

import net.shopxx.dao.FriendLinkDao;
import net.shopxx.entity.FriendLink;
import net.shopxx.entity.FriendLink.FriendLinkType;

import org.springframework.stereotype.Repository;

@Repository("friendLinkDaoImpl")
public class FriendLinkDaoImpl extends BaseDaoImpl<FriendLink, Long> implements
		FriendLinkDao {
	public List<FriendLink> findList(FriendLinkType type) {
		CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<FriendLink> cq = cb.createQuery(FriendLink.class);
		Root<FriendLink> root = cq.from(FriendLink.class);
		cq.select(root);
		if (type != null)
			cq.where(cb.equal(root.get("type"), type));
		cq.orderBy(new Order[] { cb.asc(root.get("order")) });
		return this.entityManager.createQuery(cq)
				.setFlushMode(FlushModeType.COMMIT).getResultList();
	}
}

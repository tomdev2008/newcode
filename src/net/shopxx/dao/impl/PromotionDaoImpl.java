package net.shopxx.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.shopxx.Filter;
import net.shopxx.Order;
import net.shopxx.dao.PromotionDao;
import net.shopxx.entity.Promotion;

import org.springframework.stereotype.Repository;

@Repository("promotionDaoImpl")
public class PromotionDaoImpl extends BaseDaoImpl<Promotion, Long> implements
		PromotionDao {
	public List<Promotion> findList(Boolean hasBegun, Boolean hasEnded,
			Integer count, List<Filter> filters, List<Order> orders) {
		CriteriaBuilder localCriteriaBuilder = this.entityManager
				.getCriteriaBuilder();
		CriteriaQuery<Promotion> cb = localCriteriaBuilder
				.createQuery(Promotion.class);
		Root<Promotion> root = cb.from(Promotion.class);
		cb.select(root);
		Predicate localPredicate = localCriteriaBuilder.conjunction();
		if (hasBegun != null)
			if (hasBegun.booleanValue())
				localPredicate = localCriteriaBuilder.and(localPredicate,
						localCriteriaBuilder.or(
								root.get("beginDate").isNull(),
								localCriteriaBuilder.lessThanOrEqualTo(
										root.get("beginDate"), new Date())));
			else
				localPredicate = localCriteriaBuilder.and(new Predicate[] {
						localPredicate,
						root.get("beginDate").isNotNull(),
						localCriteriaBuilder.greaterThan(root.get("beginDate"),
								new Date()) });
		if (hasEnded != null)
			if (hasEnded.booleanValue())
				localPredicate = localCriteriaBuilder.and(new Predicate[] {
						localPredicate,
						root.get("endDate").isNotNull(),
						localCriteriaBuilder.lessThan(root.get("endDate"),
								new Date()) });
			else
				localPredicate = localCriteriaBuilder.and(localPredicate,
						localCriteriaBuilder.or(
								root.get("endDate").isNull(),
								localCriteriaBuilder.greaterThanOrEqualTo(
										root.get("endDate"), new Date())));
		cb.where(localPredicate);
		return super.entityManager(cb, null, count, filters, orders);
	}
}

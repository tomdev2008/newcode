package net.shopxx.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import net.shopxx.Page;
import net.shopxx.Pageable;
import net.shopxx.dao.ProductNotifyDao;
import net.shopxx.entity.Member;
import net.shopxx.entity.Product;
import net.shopxx.entity.ProductNotify;
import org.springframework.stereotype.Repository;

@Repository("productNotifyDaoImpl")
public class ProductNotifyDaoImpl extends BaseDaoImpl<ProductNotify, Long>
		implements ProductNotifyDao {
	public boolean exists(Product product, String email) {
		String str = "select count(*) from ProductNotify productNotify where productNotify.product = :product and lower(productNotify.email) = lower(:email) and productNotify.hasSent = false";
		Long localLong = (Long) this.entityManager.createQuery(str, Long.class)
				.setFlushMode(FlushModeType.COMMIT)
				.setParameter("product", product).setParameter("email", email)
				.getSingleResult();
		return localLong.longValue() > 0L;
	}

	public Page<ProductNotify> findPage(Member member, Boolean isMarketable,
			Boolean isOutOfStock, Boolean hasSent, Pageable pageable) {
		CriteriaBuilder cb = this.entityManager
				.getCriteriaBuilder();
		CriteriaQuery<ProductNotify> cq = cb
				.createQuery(ProductNotify.class);
		Root<ProductNotify> root = cq.from(ProductNotify.class);
		cq.select(root);
		Predicate localPredicate = cb.conjunction();
		if (member != null)
			localPredicate = cb
					.and(localPredicate, cb.equal(
							root.get("member"), member));
		if (isMarketable != null)
			localPredicate = cb.and(localPredicate,
					cb.equal(
							root.get("product").get("isMarketable"),
							isMarketable));
		if (isOutOfStock != null) {
			Path localPath1 = root.get("product").get("stock");
			Path localPath2 = root.get("product").get("allocatedStock");
			if (isOutOfStock.booleanValue())
				localPredicate = cb.and(new Predicate[] {
						localPredicate,
						cb.isNotNull(localPath1),
						cb.lessThanOrEqualTo(localPath1,
								localPath2) });
			else
				localPredicate = cb.and(localPredicate,
						cb.or(cb
								.isNull(localPath1), cb
								.greaterThan(localPath1, localPath2)));
		}
		if (hasSent != null)
			localPredicate = cb.and(localPredicate,
					cb.equal(root.get("hasSent"),
							hasSent));
		cq.where(localPredicate);
		return super.entityManager(cq, pageable);
	}

	public Long count(Member member, Boolean isMarketable,
			Boolean isOutOfStock, Boolean hasSent) {
		CriteriaBuilder cb = this.entityManager
				.getCriteriaBuilder();
		CriteriaQuery<ProductNotify> cq = cb
				.createQuery(ProductNotify.class);
		Root<ProductNotify> root = cq.from(ProductNotify.class);
		cq.select(root);
		Predicate localPredicate = cb.conjunction();
		if (member != null)
			localPredicate = cb
					.and(localPredicate, cb.equal(
							root.get("member"), member));
		if (isMarketable != null)
			localPredicate = cb.and(localPredicate,
					cb.equal(
							root.get("product").get("isMarketable"),
							isMarketable));
		if (isOutOfStock != null) {
			Path localPath1 = root.get("product").get("stock");
			Path localPath2 = root.get("product").get("allocatedStock");
			if (isOutOfStock.booleanValue())
				localPredicate = cb.and(new Predicate[] {
						localPredicate,
						cb.isNotNull(localPath1),
						cb.lessThanOrEqualTo(localPath1,
								localPath2) });
			else
				localPredicate = cb.and(localPredicate,
						cb.or(cb
								.isNull(localPath1), cb
								.greaterThan(localPath1, localPath2)));
		}
		if (hasSent != null)
			localPredicate = cb.and(localPredicate,
					cb.equal(root.get("hasSent"),
							hasSent));
		cq.where(localPredicate);
		return super.entityManager(cq, null,null);
	}
}

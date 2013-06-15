package net.shopxx.dao.impl;

import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.shopxx.Filter;
import net.shopxx.Order;
import net.shopxx.Page;
import net.shopxx.Pageable;
import net.shopxx.dao.ReviewDao;
import net.shopxx.entity.Member;
import net.shopxx.entity.Product;
import net.shopxx.entity.Review;
import net.shopxx.entity.Review.ReviewType;

import org.springframework.stereotype.Repository;

@Repository("reviewDaoImpl")
public class ReviewDaoImpl extends BaseDaoImpl<Review, Long> implements
		ReviewDao {
	public List<Review> findList(Member member, Product product,
			ReviewType type, Boolean isShow, Integer count,
			List<Filter> filters, List<Order> orders) {
		CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Review> cq = cb.createQuery(Review.class);
		Root<Review> root = cq.from(Review.class);
		cq.select(root);
		Predicate localPredicate = cb.conjunction();
		if (member != null)
			localPredicate = cb.and(localPredicate,cb.equal(root.get("member"), member));
		if (product != null)
			localPredicate = cb.and(localPredicate,
					cb.equal(root.get("product"), product));
		if (type == ReviewType.positive)
			localPredicate = cb.and(localPredicate,cb.ge(root.get("score"), Integer.valueOf(4)));
		else if (type == ReviewType.moderate)
			localPredicate = cb.and(localPredicate,
					cb.equal(root.get("score"), Integer.valueOf(3)));
		else if (type == ReviewType.negative)
			localPredicate = cb.and(localPredicate,cb.le(root.get("score"), Integer.valueOf(2)));
		if (isShow != null)
			localPredicate = cb.and(localPredicate,
					cb.equal(root.get("isShow"), isShow));
		cq.where(localPredicate);
		return super.entityManager(cq, null, count, filters, orders);
	}

	public Page<Review> findPage(Member member, Product product,
			ReviewType type, Boolean isShow, Pageable pageable) {
		CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Review> cq = cb.createQuery(Review.class);
		Root<Review> root = cq.from(Review.class);
		cq.select(root);
		Predicate localPredicate = cb.conjunction();
		if (member != null)
			localPredicate = cb.and(localPredicate,
					cb.equal(root.get("member"), member));
		if (product != null)
			localPredicate = cb.and(localPredicate,
					cb.equal(root.get("product"), product));
		if (type == ReviewType.positive)
			localPredicate = cb.and(localPredicate,
					cb.ge(root.get("score"), Integer.valueOf(4)));
		else if (type == ReviewType.moderate)
			localPredicate = cb.and(localPredicate,
					cb.equal(root.get("score"), Integer.valueOf(3)));
		else if (type == ReviewType.negative)
			localPredicate = cb.and(localPredicate,
					cb.le(root.get("score"), Integer.valueOf(2)));
		if (isShow != null)
			localPredicate = cb.and(localPredicate,
					cb.equal(root.get("isShow"), isShow));
		cq.where(localPredicate);
		return super.entityManager(cq, pageable);
	}

	public Long count(Member member, Product product, ReviewType type,
			Boolean isShow) {
		CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Review> cq = cb.createQuery(Review.class);
		Root<Review> root = cq.from(Review.class);
		cq.select(root);
		Predicate localPredicate = cb.conjunction();
		if (member != null)
			localPredicate = cb.and(localPredicate,
					cb.equal(root.get("member"), member));
		if (product != null)
			localPredicate = cb.and(localPredicate,
					cb.equal(root.get("product"), product));
		if (type == ReviewType.positive)
			localPredicate = cb.and(localPredicate,
					cb.ge(root.get("score"), Integer.valueOf(4)));
		else if (type == ReviewType.moderate)
			localPredicate = cb.and(localPredicate,
					cb.equal(root.get("score"), Integer.valueOf(3)));
		else if (type == ReviewType.negative)
			localPredicate = cb.and(localPredicate,
					cb.le(root.get("score"), Integer.valueOf(2)));
		if (isShow != null)
			localPredicate = cb.and(localPredicate,
					cb.equal(root.get("isShow"), isShow));
		cq.where(localPredicate);
		return super.entityManager(cq, null);
	}

	public boolean isReviewed(Member member, Product product) {
		if ((member == null) || (product == null))
			return false;
		String str = "select count(*) from Review review where review.member = :member and review.product = :product";
		Long localLong = (Long) this.entityManager.createQuery(str, Long.class)
				.setFlushMode(FlushModeType.COMMIT)
				.setParameter("member", member)
				.setParameter("product", product).getSingleResult();
		return localLong.longValue() > 0L;
	}

	public long calculateTotalScore(Product product) {
		if (product == null)
			return 0L;
		String str = "select sum(review.score) from Review review where review.product = :product and review.isShow = :isShow";
		Long localLong = (Long) this.entityManager.createQuery(str, Long.class)
				.setFlushMode(FlushModeType.COMMIT)
				.setParameter("product", product)
				.setParameter("isShow", Boolean.valueOf(true))
				.getSingleResult();
		return localLong != null ? localLong.longValue() : 0L;
	}

	public long calculateScoreCount(Product product) {
		if (product == null)
			return 0L;
		String str = "select count(*) from Review review where review.product = :product and review.isShow = :isShow";
		return ((Long) this.entityManager.createQuery(str, Long.class)
				.setFlushMode(FlushModeType.COMMIT)
				.setParameter("product", product)
				.setParameter("isShow", Boolean.valueOf(true))
				.getSingleResult()).longValue();
	}
}

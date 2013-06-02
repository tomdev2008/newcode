package net.shopxx.dao.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import net.shopxx.Page;
import net.shopxx.Pageable;
import net.shopxx.dao.MemberDao;
import net.shopxx.entity.Member;
import net.shopxx.entity.Order.OrderPaymentStatus;
import net.shopxx.entity.Order.OrderStatus;

import org.springframework.stereotype.Repository;

@Repository("memberDaoImpl")
public class MemberDaoImpl extends BaseDaoImpl<Member, Long> implements
		MemberDao {
	public boolean usernameExists(String username) {
		if (username == null)
			return false;
		String str = "select count(*) from Member members where lower(members.username) = lower(:username)";
		Long localLong = (Long) this.entityManager.createQuery(str, Long.class)
				.setFlushMode(FlushModeType.COMMIT)
				.setParameter("username", username).getSingleResult();
		return localLong.longValue() > 0L;
	}

	public boolean emailExists(String email) {
		if (email == null)
			return false;
		String str = "select count(*) from Member members where lower(members.email) = lower(:email)";
		Long localLong = (Long) this.entityManager.createQuery(str, Long.class)
				.setFlushMode(FlushModeType.COMMIT)
				.setParameter("email", email).getSingleResult();
		return localLong.longValue() > 0L;
	}

	public Member findByUsername(String username) {
		if (username == null)
			return null;
		try {
			String str = "select members from Member members where lower(members.username) = lower(:username)";
			return (Member) this.entityManager.createQuery(str, Member.class)
					.setFlushMode(FlushModeType.COMMIT)
					.setParameter("username", username).getSingleResult();
		} catch (NoResultException localNoResultException1) {
		}
		return null;
	}

	public List<Member> findListByEmail(String email) {
		if (email == null)
			return Collections.emptyList();
		String str = "select members from Member members where lower(members.email) = lower(:email)";
		return this.entityManager.createQuery(str, Member.class)
				.setFlushMode(FlushModeType.COMMIT)
				.setParameter("email", email).getResultList();
	}

	public Page<Member> findPurchasePage(Date beginDate, Date endDate,
			Pageable pageable) {
		CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Member> cq = cb.createQuery(Member.class);
		Root<Member> root = cq.from(Member.class);
		Join join = root.join("orders");
		cq.multiselect(new Selection[] { root, cb.sum(join.get("amountPaid")) });
		Predicate predicate = cb.conjunction();
		if (beginDate != null)
			predicate = cb.and(predicate,
					cb.greaterThanOrEqualTo(join.get("createDate"), beginDate));
		if (endDate != null)
			predicate = cb.and(predicate,
					cb.lessThanOrEqualTo(join.get("createDate"), endDate));
		predicate = cb.and(predicate,
				cb.equal(join.get("orderStatus"), OrderStatus.completed));
		predicate = cb.and(predicate,
				cb.equal(join.get("paymentStatus"), OrderPaymentStatus.paid));
		cq.where(predicate);
		cq.groupBy(new Expression[] { root.get("id") });
		CriteriaQuery localCriteriaQuery2 = cb.createQuery(Long.class);
		Root localRoot2 = localCriteriaQuery2.from(Member.class);
		Join localJoin2 = localRoot2.join("orders");
		localCriteriaQuery2.select(cb.countDistinct(localRoot2));
		Predicate localPredicate2 = cb.conjunction();
		if (beginDate != null)
			localPredicate2 = cb.and(localPredicate2, cb.greaterThanOrEqualTo(
					localJoin2.get("createDate"), beginDate));
		if (endDate != null)
			localPredicate2 = cb
					.and(localPredicate2, cb.lessThanOrEqualTo(
							localJoin2.get("createDate"), endDate));
		localPredicate2 = cb.and(localPredicate2,
				cb.equal(localJoin2.get("orderStatus"), OrderStatus.completed));
		localCriteriaQuery2.where(localPredicate2);
		Long localLong = (Long) this.entityManager
				.createQuery(localCriteriaQuery2)
				.setFlushMode(FlushModeType.COMMIT).getSingleResult();
		int i = (int) Math.ceil(localLong.longValue() / pageable.getPageSize());
		if (i < pageable.getPageNumber())
			pageable.setPageNumber(i);
		cq.orderBy(new Order[] { cb.desc(cb.sum(join.get("amountPaid"))) });
		TypedQuery localTypedQuery = this.entityManager.createQuery(cq)
				.setFlushMode(FlushModeType.COMMIT);
		localTypedQuery.setFirstResult((pageable.getPageNumber() - 1)
				* pageable.getPageSize());
		localTypedQuery.setMaxResults(pageable.getPageSize());
		return new Page(localTypedQuery.getResultList(), localLong.longValue(),
				pageable);
	}
}

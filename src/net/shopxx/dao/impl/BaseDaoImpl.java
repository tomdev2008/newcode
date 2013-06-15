package net.shopxx.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import net.shopxx.Filter;
import net.shopxx.Filter.FilterOperator;
import net.shopxx.Order.OrderDirection;
import net.shopxx.Page;
import net.shopxx.Pageable;
import net.shopxx.dao.BaseDao;
import net.shopxx.entity.Order;
import net.shopxx.entity.OrderEntity;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

public abstract class BaseDaoImpl<T, ID extends Serializable> implements
		BaseDao<T, ID> {
	private Class<T> entityClass;
	private static volatile long IIIlllII = 0L;

	@PersistenceContext
	protected EntityManager entityManager;

	/**
	 * 获取实际的泛型类
	 */
	@SuppressWarnings("unchecked")
	public BaseDaoImpl() {
		ParameterizedType parameterizedType =  (ParameterizedType) getClass().getGenericSuperclass();
        Type[] types = parameterizedType.getActualTypeArguments();
        this.entityClass = (Class<T>) types[0];
	}

	public T find(ID id) {
		if (id != null)
			return this.entityManager.find(this.entityClass, id);
		return null;
	}

	public List<T> findList(Integer first, Integer count, List<Filter> filters,
			List<net.shopxx.Order> orders) {
		CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb
				.createQuery(this.entityClass);
		cq.select(cq.from(this.entityClass));
		return this.entityManager(cq, first, count, filters,
				orders);
	}

	public Page<T> findPage(Pageable pageable) {
		CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(this.entityClass);
		cq.select(cq.from(this.entityClass));
		return entityManager(cq, pageable);
	}

	public long count(Filter[] filters) {
		CriteriaBuilder cb = this.entityManager
				.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb
				.createQuery(this.entityClass);
		cq.select(cq.from(this.entityClass));
		return entityManager(cq,
				filters != null ? Arrays.asList(filters) : null).longValue();
	}

	public void persist(T entity) {
		Assert.notNull(entity);
		this.entityManager.persist(entity);
	}

	public T merge(T entity) {
		Assert.notNull(entity);
		return this.entityManager.merge(entity);
	}

	public void remove(T entity) {
		if (entity != null)
			this.entityManager.remove(entity);
	}

	public void refresh(T entity) {
		Assert.notNull(entity);
		this.entityManager.refresh(entity);
	}

	public ID getIdentifier(T entity) {
		Assert.notNull(entity);
		return (Serializable) this.entityManager.getEntityManagerFactory()
				.getPersistenceUnitUtil().getIdentifier(entity);
	}

	public boolean isManaged(T entity) {
		return this.entityManager.contains(entity);
	}

	public void detach(T entity) {
		this.entityManager.detach(entity);
	}

	public void lock(T entity, LockModeType lockModeType) {
		if ((entity != null) && (lockModeType != null))
			this.entityManager.lock(entity, lockModeType);
	}

	public void clear() {
		this.entityManager.clear();
	}

	public void flush() {
		this.entityManager.flush();
	}

	protected List<T> entityManager(CriteriaQuery<T> paramCriteriaQuery,
			Integer paramInteger1, Integer paramInteger2,
			List<Filter> paramList, List<Order> paramList1) {
		Assert.notNull(paramCriteriaQuery);
		Assert.notNull(paramCriteriaQuery.getSelection());
		Assert.notEmpty(paramCriteriaQuery.getRoots());
		CriteriaBuilder cb = this.entityManager
				.getCriteriaBuilder();
		Root<T> root = entityManager(paramCriteriaQuery);
		entityClass(paramCriteriaQuery, paramList);
		IIIlllII(paramCriteriaQuery, paramList1);
		if (paramCriteriaQuery.getOrderList().isEmpty())
			if (OrderEntity.class.isAssignableFrom(this.entityClass))
				paramCriteriaQuery
						.orderBy(new javax.persistence.criteria.Order[] { cb
								.asc(root.get("order")) });
			else
				paramCriteriaQuery
						.orderBy(new javax.persistence.criteria.Order[] { cb
								.desc(root.get("createDate")) });
		TypedQuery<T> localTypedQuery = this.entityManager.createQuery(
				paramCriteriaQuery).setFlushMode(FlushModeType.COMMIT);
		if (paramInteger1 != null)
			localTypedQuery.setFirstResult(paramInteger1.intValue());
		if (paramInteger2 != null)
			localTypedQuery.setMaxResults(paramInteger2.intValue());
		return localTypedQuery.getResultList();
	}

	protected Page<T> entityManager(CriteriaQuery<T> paramCriteriaQuery,
			Pageable paramPageable) {
		Assert.notNull(paramCriteriaQuery);
		Assert.notNull(paramCriteriaQuery.getSelection());
		Assert.notEmpty(paramCriteriaQuery.getRoots());
		if (paramPageable == null)
			paramPageable = new Pageable();
		CriteriaBuilder cb = this.entityManager
				.getCriteriaBuilder();
		Root<T> root = entityManager(paramCriteriaQuery);
		entityClass(paramCriteriaQuery, paramPageable);
		IIIlllII(paramCriteriaQuery, paramPageable);
		if (paramCriteriaQuery.getOrderList().isEmpty())
			if (OrderEntity.class.isAssignableFrom(this.entityClass))
				paramCriteriaQuery
						.orderBy(new javax.persistence.criteria.Order[] { cb
								.asc(root.get("order")) });
			else
				paramCriteriaQuery
						.orderBy(new javax.persistence.criteria.Order[] { cb
								.desc(root.get("createDate")) });
		long l = entityManager(paramCriteriaQuery, null).longValue();
		int i = (int) Math.ceil(l / paramPageable.getPageSize());
		if (i < paramPageable.getPageNumber())
			paramPageable.setPageNumber(i);
		TypedQuery localTypedQuery = this.entityManager.createQuery(
				paramCriteriaQuery).setFlushMode(FlushModeType.COMMIT);
		localTypedQuery.setFirstResult((paramPageable.getPageNumber() - 1)
				* paramPageable.getPageSize());
		localTypedQuery.setMaxResults(paramPageable.getPageSize());
		return new Page(localTypedQuery.getResultList(), l, paramPageable);
	}

	protected Long entityManager(CriteriaQuery<T> paramCriteriaQuery,
			List<Filter> paramList) {
		Assert.notNull(paramCriteriaQuery);
		Assert.notNull(paramCriteriaQuery.getSelection());
		Assert.notEmpty(paramCriteriaQuery.getRoots());
		CriteriaBuilder cb = this.entityManager
				.getCriteriaBuilder();
		entityClass(paramCriteriaQuery, paramList);
		CriteriaQuery cq = cb
				.createQuery(Long.class);
		Iterator localIterator = paramCriteriaQuery.getRoots().iterator();
		while (localIterator.hasNext()) {
			root1 = (Root) localIterator.next();
			Root root2 = cq.from(root1.getJavaType());
			root2.alias(entityManager(root1));
			entityManager(root1, root2);
		}
		Root root1 = entityManager(cq,
				paramCriteriaQuery.getResultType());
		cq.select(cb.count(root1));
		if (paramCriteriaQuery.getGroupList() != null)
			cq.groupBy(paramCriteriaQuery.getGroupList());
		if (paramCriteriaQuery.getGroupRestriction() != null)
			cq.having(paramCriteriaQuery.getGroupRestriction());
		if (paramCriteriaQuery.getRestriction() != null)
			cq.where(paramCriteriaQuery.getRestriction());
		return (Long) this.entityManager.createQuery(cq)
				.setFlushMode(FlushModeType.COMMIT).getSingleResult();
	}

	private synchronized String entityManager(Selection<?> paramSelection) {
		if (paramSelection != null) {
			String str = paramSelection.getAlias();
			if (str == null) {
				if (IIIlllII >= 1000L)
					IIIlllII = 0L;
				str = "shopxxGeneratedAlias" + IIIlllII++;
				paramSelection.alias(str);
			}
			return str;
		}
		return null;
	}

	private Root<T> entityManager(CriteriaQuery<T> paramCriteriaQuery) {
		if (paramCriteriaQuery != null)
			return entityManager(paramCriteriaQuery,
					paramCriteriaQuery.getResultType());
		return null;
	}

	private Root<T> entityManager(CriteriaQuery<?> paramCriteriaQuery,
			Class<T> paramClass) {
		if ((paramCriteriaQuery != null)
				&& (paramCriteriaQuery.getRoots() != null)
				&& (paramClass != null)) {
			Iterator localIterator = paramCriteriaQuery.getRoots().iterator();
			while (localIterator.hasNext()) {
				Root root = (Root) localIterator.next();
				if (paramClass.equals(root.getJavaType()))
					return (Root) root.as(paramClass);
			}
		}
		return null;
	}

	private void entityManager(From<?, ?> paramFrom1, From<?, ?> paramFrom2) {
		Iterator localIterator = paramFrom1.getJoins().iterator();
		Object localObject1;
		Object localObject2;
		while (localIterator.hasNext()) {
			localObject1 = (Join) localIterator.next();
			localObject2 = paramFrom2.join(((Join) localObject1).getAttribute()
					.getName(), ((Join) localObject1).getJoinType());
			((Join) localObject2)
					.alias(entityManager((Selection) localObject1));
			entityManager((From) localObject1, (From) localObject2);
		}
		localIterator = paramFrom1.getFetches().iterator();
		while (localIterator.hasNext()) {
			localObject1 = (Fetch) localIterator.next();
			localObject2 = paramFrom2.fetch(((Fetch) localObject1)
					.getAttribute().getName());
			entityManager((Fetch) localObject1, (Fetch) localObject2);
		}
	}

	private void entityManager(Fetch<?, ?> paramFetch1, Fetch<?, ?> paramFetch2) {
		Iterator localIterator = paramFetch1.getFetches().iterator();
		while (localIterator.hasNext()) {
			Fetch localFetch1 = (Fetch) localIterator.next();
			Fetch localFetch2 = paramFetch2.fetch(localFetch1.getAttribute()
					.getName());
			entityManager(localFetch1, localFetch2);
		}
	}

	private void entityClass(CriteriaQuery<T> paramCriteriaQuery,
			List<Filter> paramList) {
		if ((paramCriteriaQuery == null) || (paramList == null)
				|| (paramList.isEmpty()))
			return;
		Root root = entityManager(paramCriteriaQuery);
		if (root == null)
			return;
		CriteriaBuilder cb = this.entityManager
				.getCriteriaBuilder();
		Predicate localPredicate = paramCriteriaQuery.getRestriction() != null ? paramCriteriaQuery
				.getRestriction() : cb.conjunction();
		Iterator localIterator = paramList.iterator();
		while (localIterator.hasNext()) {
			Filter localFilter = (Filter) localIterator.next();
			if ((localFilter == null)
					|| (StringUtils.isEmpty(localFilter.getProperty())))
				continue;
			if ((localFilter.getOperator() == FilterOperator.eq)
					&& (localFilter.getValue() != null)) {
				if ((localFilter.getIgnoreCase() != null)
						&& (localFilter.getIgnoreCase().booleanValue())
						&& ((localFilter.getValue() instanceof String)))
					localPredicate = cb.and(localPredicate,
							cb.equal(cb
									.lower(root.get(localFilter
											.getProperty())),
									((String) localFilter.getValue())
											.toLowerCase()));
				else
					localPredicate = cb.and(localPredicate,
							cb.equal(
									root.get(localFilter.getProperty()),
									localFilter.getValue()));
			} else if ((localFilter.getOperator() == FilterOperator.ne)
					&& (localFilter.getValue() != null)) {
				if ((localFilter.getIgnoreCase() != null)
						&& (localFilter.getIgnoreCase().booleanValue())
						&& ((localFilter.getValue() instanceof String)))
					localPredicate = cb.and(localPredicate,
							cb.notEqual(cb
									.lower(root.get(localFilter
											.getProperty())),
									((String) localFilter.getValue())
											.toLowerCase()));
				else
					localPredicate = cb.and(localPredicate,
							cb.notEqual(
									root.get(localFilter.getProperty()),
									localFilter.getValue()));
			} else if ((localFilter.getOperator() == FilterOperator.gt)
					&& (localFilter.getValue() != null)) {
				localPredicate = cb.and(localPredicate,
						cb.gt(
								root.get(localFilter.getProperty()),
								(Number) localFilter.getValue()));
			} else if ((localFilter.getOperator() == FilterOperator.lt)
					&& (localFilter.getValue() != null)) {
				localPredicate = cb.and(localPredicate,
						cb.lt(
								root.get(localFilter.getProperty()),
								(Number) localFilter.getValue()));
			} else if ((localFilter.getOperator() == FilterOperator.ge)
					&& (localFilter.getValue() != null)) {
				localPredicate = cb.and(localPredicate,
						cb.ge(
								root.get(localFilter.getProperty()),
								(Number) localFilter.getValue()));
			} else if ((localFilter.getOperator() == FilterOperator.le)
					&& (localFilter.getValue() != null)) {
				localPredicate = cb.and(localPredicate,
						cb.le(
								root.get(localFilter.getProperty()),
								(Number) localFilter.getValue()));
			} else if ((localFilter.getOperator() == FilterOperator.like)
					&& (localFilter.getValue() != null)
					&& ((localFilter.getValue() instanceof String))) {
				localPredicate = cb.and(localPredicate,
						cb.like(
								root.get(localFilter.getProperty()),
								(String) localFilter.getValue()));
			} else if ((localFilter.getOperator() == FilterOperator.in)
					&& (localFilter.getValue() != null)) {
				localPredicate = cb.and(
						localPredicate,
						root.get(localFilter.getProperty()).in(
								new Object[] { localFilter.getValue() }));
			} else if (localFilter.getOperator() == FilterOperator.isNull) {
				localPredicate = cb.and(localPredicate,
						root.get(localFilter.getProperty()).isNull());
			} else {
				if (localFilter.getOperator() != FilterOperator.isNotNull)
					continue;
				localPredicate = cb.and(localPredicate,
						root.get(localFilter.getProperty()).isNotNull());
			}
		}
		paramCriteriaQuery.where(localPredicate);
	}

	private void entityClass(CriteriaQuery<T> paramCriteriaQuery,
			Pageable paramPageable) {
		if ((paramCriteriaQuery == null) || (paramPageable == null))
			return;
		Root root = entityManager(paramCriteriaQuery);
		if (root == null)
			return;
		CriteriaBuilder cb = this.entityManager
				.getCriteriaBuilder();
		Predicate localPredicate = paramCriteriaQuery.getRestriction() != null ? paramCriteriaQuery
				.getRestriction() : cb.conjunction();
		if ((StringUtils.isNotEmpty(paramPageable.getSearchProperty()))
				&& (StringUtils.isNotEmpty(paramPageable.getSearchValue())))
			localPredicate = cb.and(localPredicate,
					cb.like(
							root.get(paramPageable.getSearchProperty()),
							"%" + paramPageable.getSearchValue() + "%"));
		if (paramPageable.getFilters() != null) {
			Iterator localIterator = paramPageable.getFilters().iterator();
			while (localIterator.hasNext()) {
				Filter localFilter = (Filter) localIterator.next();
				if ((localFilter == null)
						|| (StringUtils.isEmpty(localFilter.getProperty())))
					continue;
				if ((localFilter.getOperator() == FilterOperator.eq)
						&& (localFilter.getValue() != null)) {
					if ((localFilter.getIgnoreCase() != null)
							&& (localFilter.getIgnoreCase().booleanValue())
							&& ((localFilter.getValue() instanceof String)))
						localPredicate = cb.and(
								localPredicate,
								cb.equal(cb
										.lower(root.get(localFilter
												.getProperty())),
										((String) localFilter.getValue())
												.toLowerCase()));
					else
						localPredicate = cb.and(
								localPredicate,
								cb.equal(root
										.get(localFilter.getProperty()),
										localFilter.getValue()));
				} else if ((localFilter.getOperator() == FilterOperator.ne)
						&& (localFilter.getValue() != null)) {
					if ((localFilter.getIgnoreCase() != null)
							&& (localFilter.getIgnoreCase().booleanValue())
							&& ((localFilter.getValue() instanceof String)))
						localPredicate = cb
								.and(localPredicate,
										cb.notEqual(
												cb
														.lower(root
																.get(localFilter
																		.getProperty())),
												((String) localFilter
														.getValue())
														.toLowerCase()));
					else
						localPredicate = cb.and(
								localPredicate,
								cb.notEqual(root
										.get(localFilter.getProperty()),
										localFilter.getValue()));
				} else if ((localFilter.getOperator() == FilterOperator.gt)
						&& (localFilter.getValue() != null)) {
					localPredicate = cb.and(localPredicate,
							cb.gt(
									root.get(localFilter.getProperty()),
									(Number) localFilter.getValue()));
				} else if ((localFilter.getOperator() == FilterOperator.lt)
						&& (localFilter.getValue() != null)) {
					localPredicate = cb.and(localPredicate,
							cb.lt(
									root.get(localFilter.getProperty()),
									(Number) localFilter.getValue()));
				} else if ((localFilter.getOperator() == FilterOperator.ge)
						&& (localFilter.getValue() != null)) {
					localPredicate = cb.and(localPredicate,
							cb.ge(
									root.get(localFilter.getProperty()),
									(Number) localFilter.getValue()));
				} else if ((localFilter.getOperator() == FilterOperator.le)
						&& (localFilter.getValue() != null)) {
					localPredicate = cb.and(localPredicate,
							cb.le(
									root.get(localFilter.getProperty()),
									(Number) localFilter.getValue()));
				} else if ((localFilter.getOperator() == FilterOperator.like)
						&& (localFilter.getValue() != null)
						&& ((localFilter.getValue() instanceof String))) {
					localPredicate = cb.and(localPredicate,
							cb.like(
									root.get(localFilter.getProperty()),
									(String) localFilter.getValue()));
				} else if ((localFilter.getOperator() == FilterOperator.in)
						&& (localFilter.getValue() != null)) {
					localPredicate = cb.and(
							localPredicate,
							root.get(localFilter.getProperty()).in(
									new Object[] { localFilter.getValue() }));
				} else if (localFilter.getOperator() == FilterOperator.isNull) {
					localPredicate = cb.and(localPredicate,
							root.get(localFilter.getProperty()).isNull());
				} else {
					if (localFilter.getOperator() != FilterOperator.isNotNull)
						continue;
					localPredicate = cb.and(localPredicate,
							root.get(localFilter.getProperty())
									.isNotNull());
				}
			}
		}
		paramCriteriaQuery.where(localPredicate);
	}

	private void IIIlllII(CriteriaQuery<T> paramCriteriaQuery,
			List<net.shopxx.Order> paramList) {
		if ((paramCriteriaQuery == null) || (paramList == null)
				|| (paramList.isEmpty()))
			return;
		Root root = entityManager(paramCriteriaQuery);
		if (root == null)
			return;
		CriteriaBuilder cb = this.entityManager
				.getCriteriaBuilder();
		ArrayList localArrayList = new ArrayList();
		if (!paramCriteriaQuery.getOrderList().isEmpty())
			localArrayList.addAll(paramCriteriaQuery.getOrderList());
		Iterator localIterator = paramList.iterator();
		while (localIterator.hasNext()) {
			net.shopxx.Order localOrder = (net.shopxx.Order) localIterator
					.next();
			if (localOrder.getDirection() == OrderDirection.asc) {
				localArrayList.add(cb.asc(root
						.get(localOrder.getProperty())));
			} else {
				if (localOrder.getDirection() != OrderDirection.desc)
					continue;
				localArrayList.add(cb.desc(root
						.get(localOrder.getProperty())));
			}
		}
		paramCriteriaQuery.orderBy(localArrayList);
	}

	private void IIIlllII(CriteriaQuery<T> paramCriteriaQuery,
			Pageable paramPageable) {
		if ((paramCriteriaQuery == null) || (paramPageable == null))
			return;
		Root<T> root = entityManager(paramCriteriaQuery);
		if (root == null)
			return;
		CriteriaBuilder cb = this.entityManager
				.getCriteriaBuilder();
		ArrayList localArrayList = new ArrayList();
		if (!paramCriteriaQuery.getOrderList().isEmpty())
			localArrayList.addAll(paramCriteriaQuery.getOrderList());
		if ((StringUtils.isNotEmpty(paramPageable.getOrderProperty()))
				&& (paramPageable.getOrderDirection() != null))
			if (paramPageable.getOrderDirection() == OrderDirection.asc)
				localArrayList.add(cb.asc(root
						.get(paramPageable.getOrderProperty())));
			else if (paramPageable.getOrderDirection() == OrderDirection.desc)
				localArrayList.add(cb.desc(root
						.get(paramPageable.getOrderProperty())));
		if (paramPageable.getOrders() != null) {
			Iterator localIterator = paramPageable.getOrders().iterator();
			while (localIterator.hasNext()) {
				net.shopxx.Order localOrder = (net.shopxx.Order) localIterator
						.next();
				if (localOrder.getDirection() == OrderDirection.asc) {
					localArrayList.add(cb.asc(root
							.get(localOrder.getProperty())));
				} else {
					if (localOrder.getDirection() != OrderDirection.desc)
						continue;
					localArrayList.add(cb.desc(root
							.get(localOrder.getProperty())));
				}
			}
		}
		paramCriteriaQuery.orderBy(localArrayList);
	}
}

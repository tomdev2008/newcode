package net.shopxx.dao.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.shopxx.Filter;
import net.shopxx.Page;
import net.shopxx.Pageable;
import net.shopxx.dao.OrderDao;
import net.shopxx.entity.Member;
import net.shopxx.entity.Order;
import net.shopxx.entity.OrderItem;
import net.shopxx.entity.Product;

import org.springframework.stereotype.Repository;

@Repository("orderDaoImpl")
public class OrderDaoImpl extends BaseDaoImpl<net.shopxx.entity.Order, Long>
  implements OrderDao
{
  public net.shopxx.entity.Order findBySn(String sn)
  {
    if (sn == null)
      return null;
    String str = "select orders from Order orders where lower(orders.sn) = lower(:sn)";
    try
    {
      return (net.shopxx.entity.Order)this.entityManager.createQuery(str, net.shopxx.entity.Order.class).setFlushMode(FlushModeType.COMMIT).setParameter("sn", sn).getSingleResult();
    }
    catch (NoResultException localNoResultException)
    {
    }
    return null;
  }

  public List<Order> findList(Member member, Integer count, List<Filter> filters, List<Order> orders)
  {
    if (member == null)
      return Collections.emptyList();
    CriteriaBuilder localCriteriaBuilder = this.entityManager.getCriteriaBuilder();
    CriteriaQuery<Order> localCriteriaQuery = localCriteriaBuilder.createQuery(Order.class);
    Root<Order> localRoot = localCriteriaQuery.from(net.shopxx.entity.Order.class);
    localCriteriaQuery.select(localRoot);
    localCriteriaQuery.where(localCriteriaBuilder.equal(localRoot.get("member"), member));
    return super.entityManager(localCriteriaQuery, null, count, filters, orders);
  }

  public Page<net.shopxx.entity.Order> findPage(Member member, Pageable pageable)
  {
    if (member == null)
      return new Page(Collections.emptyList(), 0L, pageable);
    CriteriaBuilder localCriteriaBuilder = this.entityManager.getCriteriaBuilder();
    CriteriaQuery localCriteriaQuery = localCriteriaBuilder.createQuery(net.shopxx.entity.Order.class);
    Root localRoot = localCriteriaQuery.from(net.shopxx.entity.Order.class);
    localCriteriaQuery.select(localRoot);
    localCriteriaQuery.where(localCriteriaBuilder.equal(localRoot.get("member"), member));
    return super.entityManager(localCriteriaQuery, pageable);
  }

  public Page<net.shopxx.entity.Order> findPage(Order.OrderStatus orderStatus, Order.PaymentStatus paymentStatus, Order.ShippingStatus shippingStatus, Boolean hasExpired, Pageable pageable)
  {
    CriteriaBuilder localCriteriaBuilder = this.entityManager.getCriteriaBuilder();
    CriteriaQuery localCriteriaQuery = localCriteriaBuilder.createQuery(net.shopxx.entity.Order.class);
    Root localRoot = localCriteriaQuery.from(net.shopxx.entity.Order.class);
    localCriteriaQuery.select(localRoot);
    Predicate localPredicate = localCriteriaBuilder.conjunction();
    if (orderStatus != null)
      localPredicate = localCriteriaBuilder.and(localPredicate, localCriteriaBuilder.equal(localRoot.get("orderStatus"), orderStatus));
    if (paymentStatus != null)
      localPredicate = localCriteriaBuilder.and(localPredicate, localCriteriaBuilder.equal(localRoot.get("paymentStatus"), paymentStatus));
    if (shippingStatus != null)
      localPredicate = localCriteriaBuilder.and(localPredicate, localCriteriaBuilder.equal(localRoot.get("shippingStatus"), shippingStatus));
    if (hasExpired != null)
      if (hasExpired.booleanValue())
        localPredicate = localCriteriaBuilder.and(new Predicate[] { localPredicate, localRoot.get("expire").isNotNull(), localCriteriaBuilder.lessThan(localRoot.get("expire"), new Date()) });
      else
        localPredicate = localCriteriaBuilder.and(localPredicate, localCriteriaBuilder.or(localRoot.get("expire").isNull(), localCriteriaBuilder.greaterThanOrEqualTo(localRoot.get("expire"), new Date())));
    localCriteriaQuery.where(localPredicate);
    return super.entityManager(localCriteriaQuery, pageable);
  }

  public Long count(Order.OrderStatus orderStatus, Order.PaymentStatus paymentStatus, Order.ShippingStatus shippingStatus, Boolean hasExpired)
  {
    CriteriaBuilder localCriteriaBuilder = this.entityManager.getCriteriaBuilder();
    CriteriaQuery localCriteriaQuery = localCriteriaBuilder.createQuery(net.shopxx.entity.Order.class);
    Root localRoot = localCriteriaQuery.from(net.shopxx.entity.Order.class);
    localCriteriaQuery.select(localRoot);
    Predicate localPredicate = localCriteriaBuilder.conjunction();
    if (orderStatus != null)
      localPredicate = localCriteriaBuilder.and(localPredicate, localCriteriaBuilder.equal(localRoot.get("orderStatus"), orderStatus));
    if (paymentStatus != null)
      localPredicate = localCriteriaBuilder.and(localPredicate, localCriteriaBuilder.equal(localRoot.get("paymentStatus"), paymentStatus));
    if (shippingStatus != null)
      localPredicate = localCriteriaBuilder.and(localPredicate, localCriteriaBuilder.equal(localRoot.get("shippingStatus"), shippingStatus));
    if (hasExpired != null)
      if (hasExpired.booleanValue())
        localPredicate = localCriteriaBuilder.and(new Predicate[] { localPredicate, localRoot.get("expire").isNotNull(), localCriteriaBuilder.lessThan(localRoot.get("expire"), new Date()) });
      else
        localPredicate = localCriteriaBuilder.and(localPredicate, localCriteriaBuilder.or(localRoot.get("expire").isNull(), localCriteriaBuilder.greaterThanOrEqualTo(localRoot.get("expire"), new Date())));
    localCriteriaQuery.where(localPredicate);
    return super.entityManager(localCriteriaQuery, null);
  }

  public Long waitingPaymentCount(Member member)
  {
    CriteriaBuilder localCriteriaBuilder = this.entityManager.getCriteriaBuilder();
    CriteriaQuery localCriteriaQuery = localCriteriaBuilder.createQuery(net.shopxx.entity.Order.class);
    Root localRoot = localCriteriaQuery.from(net.shopxx.entity.Order.class);
    localCriteriaQuery.select(localRoot);
    Predicate localPredicate = localCriteriaBuilder.conjunction();
    localPredicate = localCriteriaBuilder.and(new Predicate[] { localPredicate, localCriteriaBuilder.notEqual(localRoot.get("orderStatus"), Order.OrderStatus.completed), localCriteriaBuilder.notEqual(localRoot.get("orderStatus"), Order.OrderStatus.cancelled) });
    localPredicate = localCriteriaBuilder.and(localPredicate, localCriteriaBuilder.or(localCriteriaBuilder.equal(localRoot.get("paymentStatus"), Order.PaymentStatus.unpaid), localCriteriaBuilder.equal(localRoot.get("paymentStatus"), Order.PaymentStatus.partialPayment)));
    localPredicate = localCriteriaBuilder.and(localPredicate, localCriteriaBuilder.or(localRoot.get("expire").isNull(), localCriteriaBuilder.greaterThanOrEqualTo(localRoot.get("expire"), new Date())));
    if (member != null)
      localPredicate = localCriteriaBuilder.and(localPredicate, localCriteriaBuilder.equal(localRoot.get("member"), member));
    localCriteriaQuery.where(localPredicate);
    return super.entityManager(localCriteriaQuery, null);
  }

  public Long waitingShippingCount(Member member)
  {
    CriteriaBuilder localCriteriaBuilder = this.entityManager.getCriteriaBuilder();
    CriteriaQuery localCriteriaQuery = localCriteriaBuilder.createQuery(net.shopxx.entity.Order.class);
    Root localRoot = localCriteriaQuery.from(net.shopxx.entity.Order.class);
    localCriteriaQuery.select(localRoot);
    Predicate localPredicate = localCriteriaBuilder.conjunction();
    localPredicate = localCriteriaBuilder.and(new Predicate[] { localPredicate, localCriteriaBuilder.notEqual(localRoot.get("orderStatus"), Order.OrderStatus.completed), localCriteriaBuilder.notEqual(localRoot.get("orderStatus"), Order.OrderStatus.cancelled), localCriteriaBuilder.equal(localRoot.get("paymentStatus"), Order.PaymentStatus.paid), localCriteriaBuilder.equal(localRoot.get("shippingStatus"), Order.ShippingStatus.unshipped) });
    localPredicate = localCriteriaBuilder.and(localPredicate, localCriteriaBuilder.or(localRoot.get("expire").isNull(), localCriteriaBuilder.greaterThanOrEqualTo(localRoot.get("expire"), new Date())));
    if (member != null)
      localPredicate = localCriteriaBuilder.and(localPredicate, localCriteriaBuilder.equal(localRoot.get("member"), member));
    localCriteriaQuery.where(localPredicate);
    return super.entityManager(localCriteriaQuery, null);
  }

  public BigDecimal getSalesAmount(Date beginDate, Date endDate)
  {
    CriteriaBuilder localCriteriaBuilder = this.entityManager.getCriteriaBuilder();
    CriteriaQuery localCriteriaQuery = localCriteriaBuilder.createQuery(BigDecimal.class);
    Root localRoot = localCriteriaQuery.from(net.shopxx.entity.Order.class);
    localCriteriaQuery.select(localCriteriaBuilder.sum(localRoot.get("amountPaid")));
    Predicate localPredicate = localCriteriaBuilder.conjunction();
    localPredicate = localCriteriaBuilder.and(localPredicate, localCriteriaBuilder.equal(localRoot.get("orderStatus"), Order.OrderStatus.completed));
    if (beginDate != null)
      localPredicate = localCriteriaBuilder.and(localPredicate, localCriteriaBuilder.greaterThanOrEqualTo(localRoot.get("createDate"), beginDate));
    if (endDate != null)
      localPredicate = localCriteriaBuilder.and(localPredicate, localCriteriaBuilder.lessThanOrEqualTo(localRoot.get("createDate"), endDate));
    localCriteriaQuery.where(localPredicate);
    return (BigDecimal)this.entityManager.createQuery(localCriteriaQuery).setFlushMode(FlushModeType.COMMIT).getSingleResult();
  }

  public Integer getSalesVolume(Date beginDate, Date endDate)
  {
    CriteriaBuilder localCriteriaBuilder = this.entityManager.getCriteriaBuilder();
    CriteriaQuery localCriteriaQuery = localCriteriaBuilder.createQuery(Integer.class);
    Root localRoot = localCriteriaQuery.from(net.shopxx.entity.Order.class);
    localCriteriaQuery.select(localCriteriaBuilder.sum(localRoot.join("orderItems").get("shippedQuantity")));
    Predicate localPredicate = localCriteriaBuilder.conjunction();
    localPredicate = localCriteriaBuilder.and(localPredicate, localCriteriaBuilder.equal(localRoot.get("orderStatus"), Order.OrderStatus.completed));
    if (beginDate != null)
      localPredicate = localCriteriaBuilder.and(localPredicate, localCriteriaBuilder.greaterThanOrEqualTo(localRoot.get("createDate"), beginDate));
    if (endDate != null)
      localPredicate = localCriteriaBuilder.and(localPredicate, localCriteriaBuilder.lessThanOrEqualTo(localRoot.get("createDate"), endDate));
    localCriteriaQuery.where(localPredicate);
    return (Integer)this.entityManager.createQuery(localCriteriaQuery).setFlushMode(FlushModeType.COMMIT).getSingleResult();
  }

  public void releaseStock()
  {
    String str = "select orders from Order orders where orders.isAllocatedStock = :isAllocatedStock and orders.expire is not null and orders.expire <= :now";
    List localList = this.entityManager.createQuery(str, net.shopxx.entity.Order.class).setParameter("isAllocatedStock", Boolean.valueOf(true)).setParameter("now", new Date()).getResultList();
    if (localList != null)
    {
      Iterator localIterator1 = localList.iterator();
      while (localIterator1.hasNext())
      {
        net.shopxx.entity.Order localOrder = (net.shopxx.entity.Order)localIterator1.next();
        if ((localOrder == null) || (localOrder.getOrderItems() == null))
          continue;
        Iterator localIterator2 = localOrder.getOrderItems().iterator();
        while (localIterator2.hasNext())
        {
          OrderItem localOrderItem = (OrderItem)localIterator2.next();
          if (localOrderItem == null)
            continue;
          Product localProduct = localOrderItem.getProduct();
          if (localProduct == null)
            continue;
          this.entityManager.lock(localProduct, LockModeType.PESSIMISTIC_WRITE);
          localProduct.setAllocatedStock(Integer.valueOf(localProduct.getAllocatedStock().intValue() - (localOrderItem.getQuantity().intValue() - localOrderItem.getShippedQuantity().intValue())));
        }
        localOrder.setIsAllocatedStock(Boolean.valueOf(false));
      }
    }
  }
}

package net.shopxx.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import net.shopxx.Filter;
import net.shopxx.Page;
import net.shopxx.Pageable;
import net.shopxx.entity.Member;
import net.shopxx.entity.Order.OrderOrderStatus;
import net.shopxx.entity.Order.OrderPaymentStatus;
import net.shopxx.entity.Order.OrderShippingStatus;

public abstract interface OrderDao extends BaseDao<net.shopxx.entity.Order, Long>
{
  public abstract net.shopxx.entity.Order findBySn(String paramString);

  public abstract List<net.shopxx.entity.Order> findList(Member paramMember, Integer paramInteger, List<Filter> paramList, List<net.shopxx.Order> paramList1);

  public abstract Page<net.shopxx.entity.Order> findPage(Member paramMember, Pageable paramPageable);

  public abstract Page<net.shopxx.entity.Order> findPage(OrderOrderStatus paramOrderStatus, OrderPaymentStatus paramPaymentStatus, OrderShippingStatus paramShippingStatus, Boolean paramBoolean, Pageable paramPageable);

  public abstract Long count(OrderOrderStatus paramOrderStatus, OrderPaymentStatus paramPaymentStatus, OrderShippingStatus paramShippingStatus, Boolean paramBoolean);

  public abstract Long waitingPaymentCount(Member paramMember);

  public abstract Long waitingShippingCount(Member paramMember);

  public abstract BigDecimal getSalesAmount(Date paramDate1, Date paramDate2);

  public abstract Integer getSalesVolume(Date paramDate1, Date paramDate2);

  public abstract void releaseStock();
}

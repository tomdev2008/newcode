package net.shopxx.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import net.shopxx.Filter;
import net.shopxx.Page;
import net.shopxx.Pageable;
import net.shopxx.entity.Member;
import net.shopxx.entity.Order;
import net.shopxx.entity.Order.OrderPaymentStatus;
import net.shopxx.entity.Order.OrderShippingStatus;
import net.shopxx.entity.Order.OrderStatus;

public abstract interface OrderDao extends BaseDao<Order, Long> {
	public abstract Order findBySn(String paramString);

	public abstract List<Order> findList(Member paramMember,
			Integer paramInteger, List<Filter> paramList, List<Order> paramList1);

	public abstract Page<Order> findPage(Member paramMember,
			Pageable paramPageable);

	public abstract Page<Order> findPage(OrderStatus paramOrderStatus,
			OrderPaymentStatus paramPaymentStatus,
			OrderShippingStatus paramShippingStatus, Boolean paramBoolean,
			Pageable paramPageable);

	public abstract Long count(OrderStatus paramOrderStatus,
			OrderPaymentStatus paramPaymentStatus,
			OrderShippingStatus paramShippingStatus, Boolean paramBoolean);

	public abstract Long waitingPaymentCount(Member paramMember);

	public abstract Long waitingShippingCount(Member paramMember);

	public abstract BigDecimal getSalesAmount(Date paramDate1, Date paramDate2);

	public abstract Integer getSalesVolume(Date paramDate1, Date paramDate2);

	public abstract void releaseStock();
}

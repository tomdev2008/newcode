package net.shopxx.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import net.shopxx.Filter;
import net.shopxx.Page;
import net.shopxx.Pageable;
import net.shopxx.entity.Admin;
import net.shopxx.entity.Cart;
import net.shopxx.entity.CouponCode;
import net.shopxx.entity.Member;
import net.shopxx.entity.Order;
import net.shopxx.entity.Order.OrderPaymentStatus;
import net.shopxx.entity.Order.OrderShippingStatus;
import net.shopxx.entity.Order.OrderStatus;
import net.shopxx.entity.Payment;
import net.shopxx.entity.PaymentMethod;
import net.shopxx.entity.Receiver;
import net.shopxx.entity.Refunds;
import net.shopxx.entity.Returns;
import net.shopxx.entity.Shipping;
import net.shopxx.entity.ShippingMethod;

public abstract interface OrderService extends BaseService<Order, Long> {
	public abstract Order findBySn(String paramString);

	public abstract List<Order> findList(Member paramMember,
			Integer paramInteger, List<Filter> paramList, List<Order> orders);

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

	public abstract Order build(Cart paramCart, Receiver paramReceiver,
			PaymentMethod paramPaymentMethod,
			ShippingMethod paramShippingMethod, CouponCode paramCouponCode,
			boolean paramBoolean1, String paramString1, boolean paramBoolean2,
			String paramString2);

	public abstract Order create(Cart paramCart, Receiver paramReceiver,
			PaymentMethod paramPaymentMethod,
			ShippingMethod paramShippingMethod, CouponCode paramCouponCode,
			boolean paramBoolean1, String paramString1, boolean paramBoolean2,
			String paramString2, Admin paramAdmin);

	public abstract void update(Order paramOrder, Admin paramAdmin);

	public abstract void confirm(Order paramOrder, Admin paramAdmin);

	public abstract void complete(Order paramOrder, Admin paramAdmin);

	public abstract void cancel(Order paramOrder, Admin paramAdmin);

	public abstract void payment(Order paramOrder, Payment paramPayment,
			Admin paramAdmin);

	public abstract void refunds(Order paramOrder, Refunds paramRefunds,
			Admin paramAdmin);

	public abstract void shipping(Order paramOrder, Shipping paramShipping,
			Admin paramAdmin);

	public abstract void returns(Order paramOrder, Returns paramReturns,
			Admin paramAdmin);
}

package net.shopxx.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.LockModeType;

import net.shopxx.Filter;
import net.shopxx.Page;
import net.shopxx.Pageable;
import net.shopxx.Setting;
import net.shopxx.dao.CartDao;
import net.shopxx.dao.CouponCodeDao;
import net.shopxx.dao.DepositDao;
import net.shopxx.dao.MemberDao;
import net.shopxx.dao.MemberRankDao;
import net.shopxx.dao.OrderDao;
import net.shopxx.dao.OrderItemDao;
import net.shopxx.dao.OrderLogDao;
import net.shopxx.dao.PaymentDao;
import net.shopxx.dao.ProductDao;
import net.shopxx.dao.RefundsDao;
import net.shopxx.dao.ReturnsDao;
import net.shopxx.dao.ShippingDao;
import net.shopxx.dao.SnDao;
import net.shopxx.entity.Admin;
import net.shopxx.entity.Cart;
import net.shopxx.entity.CartItem;
import net.shopxx.entity.Coupon;
import net.shopxx.entity.CouponCode;
import net.shopxx.entity.Deposit;
import net.shopxx.entity.GiftItem;
import net.shopxx.entity.Member;
import net.shopxx.entity.MemberRank;
import net.shopxx.entity.Order;
import net.shopxx.entity.Order.OrderPaymentStatus;
import net.shopxx.entity.Order.OrderShippingStatus;
import net.shopxx.entity.Order.OrderStatus;
import net.shopxx.entity.OrderItem;
import net.shopxx.entity.OrderLog;
import net.shopxx.entity.OrderLog.OrderLogType;
import net.shopxx.entity.Payment;
import net.shopxx.entity.Payment.PaymentType;
import net.shopxx.entity.PaymentMethod;
import net.shopxx.entity.PaymentMethod.PaymentMethodType;
import net.shopxx.entity.Product;
import net.shopxx.entity.Promotion;
import net.shopxx.entity.Receiver;
import net.shopxx.entity.Refunds;
import net.shopxx.entity.Refunds.RefundsType;
import net.shopxx.entity.Returns;
import net.shopxx.entity.ReturnsItem;
import net.shopxx.entity.Shipping;
import net.shopxx.entity.ShippingItem;
import net.shopxx.entity.ShippingMethod;
import net.shopxx.entity.Sn.SnType;
import net.shopxx.service.OrderService;
import net.shopxx.service.StaticService;
import net.shopxx.util.SettingUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service("orderServiceImpl")
public class OrderServiceImpl extends BaseServiceImpl<Order, Long> implements
		OrderService {

	@Resource(name = "orderDaoImpl")
	private OrderDao orderDao;

	@Resource(name = "orderItemDaoImpl")
	private OrderItemDao orderItemDao;

	@Resource(name = "orderLogDaoImpl")
	private OrderLogDao orderLogDao;

	@Resource(name = "cartDaoImpl")
	private CartDao cartDao;

	@Resource(name = "couponCodeDaoImpl")
	private CouponCodeDao couponCodeDao;

	@Resource(name = "snDaoImpl")
	private SnDao snDao;

	@Resource(name = "memberDaoImpl")
	private MemberDao memberDao;

	@Resource(name = "memberRankDaoImpl")
	private MemberRankDao memberRankDao;

	@Resource(name = "productDaoImpl")
	private ProductDao productDao;

	@Resource(name = "depositDaoImpl")
	private DepositDao depositDao;

	@Resource(name = "paymentDaoImpl")
	private PaymentDao paymentDao;

	@Resource(name = "refundsDaoImpl")
	private RefundsDao refundsDao;

	@Resource(name = "shippingDaoImpl")
	private ShippingDao shippingDao;

	@Resource(name = "returnsDaoImpl")
	private ReturnsDao returnsDao;

	@Resource(name = "staticServiceImpl")
	private StaticService staticService;

	@Resource(name = "orderDaoImpl")
	public void setBaseDao(OrderDao orderDao) {
		super.setBaseDao(orderDao);
	}

	@Transactional(readOnly = true)
	public Order findBySn(String sn) {
		return this.orderDao.findBySn(sn);
	}

	@Transactional(readOnly = true)
	public List<Order> findList(Member member, Integer count,
			List<Filter> filters, List<Order> orders) {
		return this.orderDao.findList(member, count, filters, orders);
	}

	@Transactional(readOnly = true)
	public Page<Order> findPage(Member member, Pageable pageable) {
		return this.orderDao.findPage(member, pageable);
	}

	@Transactional(readOnly = true)
	public Page<Order> findPage(OrderStatus orderStatus,
			OrderPaymentStatus paymentStatus,
			OrderShippingStatus shippingStatus, Boolean hasExpired,
			Pageable pageable) {
		return this.orderDao.findPage(orderStatus, paymentStatus,
				shippingStatus, hasExpired, pageable);
	}

	@Transactional(readOnly = true)
	public Long count(OrderStatus orderStatus,
			OrderPaymentStatus paymentStatus,
			OrderShippingStatus shippingStatus, Boolean hasExpired) {
		return this.orderDao.count(orderStatus, paymentStatus, shippingStatus,
				hasExpired);
	}

	@Transactional(readOnly = true)
	public Long waitingPaymentCount(Member member) {
		return this.orderDao.waitingPaymentCount(member);
	}

	@Transactional(readOnly = true)
	public Long waitingShippingCount(Member member) {
		return this.orderDao.waitingShippingCount(member);
	}

	@Transactional(readOnly = true)
	public BigDecimal getSalesAmount(Date beginDate, Date endDate) {
		return this.orderDao.getSalesAmount(beginDate, endDate);
	}

	@Transactional(readOnly = true)
	public Integer getSalesVolume(Date beginDate, Date endDate) {
		return this.orderDao.getSalesVolume(beginDate, endDate);
	}

	public void releaseStock() {
		this.orderDao.releaseStock();
	}

	@Transactional(readOnly = true)
	public Order build(Cart cart, Receiver receiver,
			PaymentMethod paymentMethod, ShippingMethod shippingMethod,
			CouponCode couponCode, boolean isInvoice, String invoiceTitle,
			boolean useBalance, String memo) {
		Assert.notNull(cart);
		Assert.notNull(cart.getMember());
		Assert.notEmpty(cart.getCartItems());
		Order order = new Order();
		order.setShippingStatus(OrderShippingStatus.unshipped);
		order.setFee(new BigDecimal(0));
		order.setDiscount(cart.getDiscount());
		order.setPoint(Integer.valueOf(cart.getPoint()));
		order.setMemo(memo);
		order.setMember(cart.getMember());
		if (receiver != null) {
			order.setConsignee(receiver.getConsignee());
			order.setAreaName(receiver.getAreaName());
			order.setAddress(receiver.getAddress());
			order.setZipCode(receiver.getZipCode());
			order.setPhone(receiver.getPhone());
			order.setArea(receiver.getArea());
		}
		if (!cart.getPromotions().isEmpty()) {
			StringBuffer str = new StringBuffer();
			Iterator<Promotion> iterator = cart.getPromotions().iterator();
			while (iterator.hasNext()) {
				Promotion promotion = iterator.next();
				if (promotion == null || promotion.getName() == null)
					continue;
				str.append(" " + promotion.getName());
			}
			if (str.length() > 0)
				str.deleteCharAt(0);
			order.setPromotion(str.toString());
		}
		order.setPaymentMethod(paymentMethod);
		if ((shippingMethod != null)
				&& (paymentMethod != null)
				&& (paymentMethod.getShippingMethods().contains(shippingMethod))) {
			localObject1 = shippingMethod.calculateFreight(Integer.valueOf(cart
					.getWeight()));
			localObject3 = cart.getPromotions().iterator();
			while (((Iterator) localObject3).hasNext()) {
				localObject2 = (Promotion) ((Iterator) localObject3).next();
				if (!((Promotion) localObject2).getIsFreeShipping()
						.booleanValue())
					continue;
				localObject1 = new BigDecimal(0);
				break;
			}
			order.setFreight((BigDecimal) localObject1);
			order.setShippingMethod(shippingMethod);
		} else {
			order.setFreight(new BigDecimal(0));
		}
		if ((couponCode != null) && (cart.isCouponAllowed())) {
			this.couponCodeDao.lock(couponCode, LockModeType.PESSIMISTIC_READ);
			if ((!couponCode.getIsUsed().booleanValue())
					&& (couponCode.getCoupon() != null)
					&& (cart.isValid(couponCode.getCoupon()))) {
				localObject1 = couponCode.getCoupon().calculatePrice(
						cart.getAmount());
				localObject2 = cart.getAmount().subtract(
						(BigDecimal) localObject1);
				if (((BigDecimal) localObject2).compareTo(new BigDecimal(0)) > 0)
					order.setDiscount(cart.getDiscount().add(
							(BigDecimal) localObject2));
				order.setCouponCode(couponCode);
			}
		}
		Object localObject1 = order.getOrderItems();
		Object localObject3 = cart.getCartItems().iterator();
		Product product;
		OrderItem orderItem;
		while (((Iterator) localObject3).hasNext()) {
			localObject2 = (CartItem) ((Iterator) localObject3).next();
			if ((localObject2 == null)
					|| (((CartItem) localObject2).getProduct() == null))
				continue;
			localProduct = ((CartItem) localObject2).getProduct();
			orderItem = new OrderItem();
			orderItem.setSn(localProduct.getSn());
			orderItem.setName(localProduct.getName());
			orderItem.setFullName(localProduct.getFullName());
			orderItem.setPrice(((CartItem) localObject2).getUnitPrice());
			orderItem.setWeight(localProduct.getWeight());
			orderItem.setThumbnail(localProduct.getThumbnail());
			orderItem.setIsGift(Boolean.valueOf(false));
			orderItem.setQuantity(((CartItem) localObject2).getQuantity());
			orderItem.setShippedQuantity(Integer.valueOf(0));
			orderItem.setReturnQuantity(Integer.valueOf(0));
			orderItem.setProduct(localProduct);
			orderItem.setOrder(order);
			((List) localObject1).add(orderItem);
		}
		localObject3 = cart.getGiftItems().iterator();
		while (((Iterator) localObject3).hasNext()) {
			localObject2 = (GiftItem) ((Iterator) localObject3).next();
			if ((localObject2 == null)
					|| (((GiftItem) localObject2).getGift() == null))
				continue;
			localProduct = ((GiftItem) localObject2).getGift();
			orderItem = new OrderItem();
			orderItem.setSn(localProduct.getSn());
			orderItem.setName(localProduct.getName());
			orderItem.setFullName(localProduct.getFullName());
			orderItem.setPrice(new BigDecimal(0));
			orderItem.setWeight(localProduct.getWeight());
			orderItem.setThumbnail(localProduct.getThumbnail());
			orderItem.setIsGift(Boolean.valueOf(true));
			orderItem.setQuantity(((GiftItem) localObject2).getQuantity());
			orderItem.setShippedQuantity(Integer.valueOf(0));
			orderItem.setReturnQuantity(Integer.valueOf(0));
			orderItem.setProduct(localProduct);
			orderItem.setOrder(order);
			((List) localObject1).add(orderItem);
		}
		Object localObject2 = SettingUtils.get();
		if ((((Setting) localObject2).getIsInvoiceEnabled().booleanValue())
				&& (isInvoice) && (StringUtils.isNotEmpty(invoiceTitle))) {
			order.setIsInvoice(Boolean.valueOf(true));
			order.setInvoiceTitle(invoiceTitle);
			order.setTax(order.calculateTax());
		} else {
			order.setIsInvoice(Boolean.valueOf(false));
			order.setTax(new BigDecimal(0));
		}
		if (useBalance) {
			localObject3 = cart.getMember();
			if (((Member) localObject3).getBalance().compareTo(
					order.getAmount()) >= 0)
				order.setAmountPaid(order.getAmount());
			else
				order.setAmountPaid(((Member) localObject3).getBalance());
		} else {
			order.setAmountPaid(new BigDecimal(0));
		}
		if (order.getAmountPayable().compareTo(new BigDecimal(0)) == 0) {
			order.setOrderStatus(Order.OrderStatus.confirmed);
			order.setPaymentStatus(Order.PaymentStatus.paid);
		} else if ((order.getAmountPayable().compareTo(new BigDecimal(0)) > 0)
				&& (order.getAmountPaid().compareTo(new BigDecimal(0)) > 0)) {
			order.setOrderStatus(Order.OrderStatus.confirmed);
			order.setPaymentStatus(Order.PaymentStatus.partialPayment);
		} else {
			order.setOrderStatus(Order.OrderStatus.unconfirmed);
			order.setPaymentStatus(Order.PaymentStatus.unpaid);
		}
		if ((paymentMethod != null) && (paymentMethod.getTimeout() != null)
				&& (order.getPaymentStatus() == Order.PaymentStatus.unpaid))
			order.setExpire(DateUtils.addMinutes(new Date(), paymentMethod
					.getTimeout().intValue()));
		return (Order) (Order) (Order) order;
	}

	public Order create(Cart cart, Receiver receiver,
			PaymentMethod paymentMethod, ShippingMethod shippingMethod,
			CouponCode couponCode, boolean isInvoice, String invoiceTitle,
			boolean useBalance, String memo, Admin operator) {
		Assert.notNull(cart);
		Assert.notNull(cart.getMember());
		Assert.notEmpty(cart.getCartItems());
		Assert.notNull(receiver);
		Assert.notNull(paymentMethod);
		Assert.notNull(shippingMethod);
		Order order = build(cart, receiver, paymentMethod, shippingMethod,
				couponCode, isInvoice, invoiceTitle, useBalance, memo);
		order.setSn(this.snDao.generate(SnType.order));
		if (paymentMethod.getType() == PaymentMethodType.online) {
			order.setLockExpire(DateUtils.addSeconds(new Date(), 10));
			order.setOperator(operator);
		}
		if (order.getCouponCode() != null) {
			couponCode.setIsUsed(Boolean.valueOf(true));
			couponCode.setUsedDate(new Date());
			this.couponCodeDao.merge(couponCode);
		}
		Object localObject2 = cart.getPromotions().iterator();
		Object localObject4;
		while (((Iterator) localObject2).hasNext()) {
			localObject1 = (Promotion) ((Iterator) localObject2).next();
			localObject4 = ((Promotion) localObject1).getCoupons().iterator();
			while (((Iterator) localObject4).hasNext()) {
				localObject3 = (Coupon) ((Iterator) localObject4).next();
				order.getCoupons().add(localObject3);
			}
		}
		Object localObject1 = SettingUtils.get();
		if ((((Setting) localObject1).getStockAllocationTime() == Setting.StockAllocationTime.order)
				|| ((((Setting) localObject1).getStockAllocationTime() == Setting.StockAllocationTime.payment) && ((order
						.getPaymentStatus() == Order.PaymentStatus.partialPayment) || (order
						.getPaymentStatus() == Order.PaymentStatus.paid))))
			order.setIsAllocatedStock(Boolean.valueOf(true));
		else
			order.setIsAllocatedStock(Boolean.valueOf(false));
		this.orderDao.persist(order);
		localObject2 = new OrderLog();
		((OrderLog) localObject2).setType(OrderLog.Type.create);
		((OrderLog) localObject2).setOperator(operator != null ? operator
				.getUsername() : null);
		((OrderLog) localObject2).setOrder(order);
		this.orderLogDao.persist(localObject2);
		Object localObject3 = cart.getMember();
		if (order.getAmountPaid().compareTo(new BigDecimal(0)) > 0) {
			this.memberDao.lock(localObject3, LockModeType.PESSIMISTIC_WRITE);
			((Member) localObject3).setBalance(((Member) localObject3)
					.getBalance().subtract(order.getAmountPaid()));
			this.memberDao.merge(localObject3);
			localObject4 = new Deposit();
			((Deposit) localObject4)
					.setType(operator != null ? Deposit.Type.adminPayment
							: Deposit.Type.memberPayment);
			((Deposit) localObject4).setCredit(new BigDecimal(0));
			((Deposit) localObject4).setDebit(order.getAmountPaid());
			((Deposit) localObject4).setBalance(((Member) localObject3)
					.getBalance());
			((Deposit) localObject4).setOperator(operator != null ? operator
					.getUsername() : null);
			((Deposit) localObject4).setMember((Member) localObject3);
			((Deposit) localObject4).setOrder(order);
			this.depositDao.persist(localObject4);
		}
		if ((((Setting) localObject1).getStockAllocationTime() == Setting.StockAllocationTime.order)
				|| ((((Setting) localObject1).getStockAllocationTime() == Setting.StockAllocationTime.payment) && ((order
						.getPaymentStatus() == Order.PaymentStatus.partialPayment) || (order
						.getPaymentStatus() == Order.PaymentStatus.paid)))) {
			Iterator localIterator = order.getOrderItems().iterator();
			while (localIterator.hasNext()) {
				localObject4 = (OrderItem) localIterator.next();
				if (localObject4 == null)
					continue;
				Product localProduct = ((OrderItem) localObject4).getProduct();
				this.productDao.lock(localProduct,
						LockModeType.PESSIMISTIC_WRITE);
				if ((localProduct == null) || (localProduct.getStock() == null))
					continue;
				localProduct
						.setAllocatedStock(Integer
								.valueOf(localProduct.getAllocatedStock()
										.intValue()
										+ (((OrderItem) localObject4)
												.getQuantity().intValue() - ((OrderItem) localObject4)
												.getShippedQuantity()
												.intValue())));
				this.productDao.merge(localProduct);
				this.orderDao.flush();
				this.staticService.build(localProduct);
			}
		}
		this.cartDao.remove(cart);
		return (Order) (Order) (Order) (Order) order;
	}

	public void update(Order order, Admin admin) {
		Assert.notNull(order);
		order = this.orderDao.find(order.getId());
		if (order.getIsAllocatedStock().booleanValue()) {
			Iterator<OrderItem> orderItems = order.getOrderItems().iterator();
			Product product;
			while (orderItems.hasNext()) {
				OrderItem orderItem = orderItems.next();
				if (orderItem == null)
					continue;
				product = orderItem.getProduct();
				this.productDao.lock(product, LockModeType.PESSIMISTIC_WRITE);
				if ((product == null) || (product.getStock() == null))
					continue;
				product.setAllocatedStock(Integer.valueOf(product
						.getAllocatedStock().intValue()
						- orderItem.getQuantity().intValue()
						- (orderItem.getShippedQuantity().intValue())));
				this.productDao.merge(product);
				this.orderDao.flush();
				this.staticService.build(product);
			}
			localIterator = order.getOrderItems().iterator();
			while (localIterator.hasNext()) {
				localObject = (OrderItem) localIterator.next();
				if (localObject == null)
					continue;
				localProduct = ((OrderItem) localObject).getProduct();
				this.productDao.lock(localProduct,
						LockModeType.PESSIMISTIC_WRITE);
				if ((localProduct == null) || (localProduct.getStock() == null))
					continue;
				localProduct.setAllocatedStock(Integer
						.valueOf(localProduct.getAllocatedStock().intValue()
								+ (((OrderItem) localObject).getQuantity()
										.intValue() - ((OrderItem) localObject)
										.getShippedQuantity().intValue())));
				this.productDao.merge(localProduct);
				this.productDao.flush();
				this.staticService.build(localProduct);
			}
		}
		this.orderDao.merge(order);
		Object localObject = new OrderLog();
		((OrderLog) localObject).setType(OrderLog.Type.modify);
		((OrderLog) localObject).setOperator(operator != null ? operator
				.getUsername() : null);
		((OrderLog) localObject).setOrder(order);
		this.orderLogDao.persist(localObject);
	}

	public void confirm(Order order, Admin operator) {
		Assert.notNull(order);
		order.setOrderStatus(Order.OrderStatus.confirmed);
		this.orderDao.merge(order);
		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLogType.confirm);
		orderLog.setOperator(operator != null ? operator.getUsername() : null);
		orderLog.setOrder(order);
		this.orderLogDao.persist(orderLog);
	}

	public void complete(Order order, Admin operator) {
		Assert.notNull(order);
		Member localMember = order.getMember();
		this.memberDao.lock(localMember, LockModeType.PESSIMISTIC_WRITE);
		if ((order.getShippingStatus() == OrderShippingStatus.partialShipment)
				|| (order.getShippingStatus() == OrderShippingStatus.shipped)) {
			localMember.setPoint(Long.valueOf(localMember.getPoint()
					.longValue() + order.getPoint().intValue()));
			Iterator<Coupon> coupons = order.getCoupons().iterator();
			while (coupons.hasNext()) {
				Coupon coupon = coupons.next();
				this.couponCodeDao.build(coupon, localMember);
			}
		}
		if ((order.getShippingStatus() == OrderShippingStatus.unshipped)
				|| (order.getShippingStatus() == OrderShippingStatus.returned)) {
			CouponCode couponCode = order.getCouponCode();
			if (couponCode != null) {
				couponCode.setIsUsed(Boolean.valueOf(false));
				couponCode.setUsedDate(null);
				this.couponCodeDao.merge(couponCode);
				order.setCouponCode(null);
				this.orderDao.merge(order);
			}
		}
		localMember.setAmount(localMember.getAmount()
				.add(order.getAmountPaid()));
		if (!localMember.getMemberRank().getIsSpecial().booleanValue()) {
			localObject = this.memberRankDao.findByAmount(localMember
					.getAmount());
			if ((localObject != null)
					&& (((MemberRank) localObject).getAmount().compareTo(
							localMember.getMemberRank().getAmount()) > 0))
				localMember.setMemberRank((MemberRank) localObject);
		}
		this.memberDao.merge(localMember);
		Product localProduct;
		if (order.getIsAllocatedStock().booleanValue()) {
			localIterator = order.getOrderItems().iterator();
			while (localIterator.hasNext()) {
				localObject = (OrderItem) localIterator.next();
				if (localObject == null)
					continue;
				localProduct = ((OrderItem) localObject).getProduct();
				this.productDao.lock(localProduct,
						LockModeType.PESSIMISTIC_WRITE);
				if ((localProduct == null) || (localProduct.getStock() == null))
					continue;
				localProduct.setAllocatedStock(Integer
						.valueOf(localProduct.getAllocatedStock().intValue()
								- (((OrderItem) localObject).getQuantity()
										.intValue() - ((OrderItem) localObject)
										.getShippedQuantity().intValue())));
				this.productDao.merge(localProduct);
				this.orderDao.flush();
				this.staticService.build(localProduct);
			}
			order.setIsAllocatedStock(Boolean.valueOf(false));
		}
		Iterator localIterator = order.getOrderItems().iterator();
		while (localIterator.hasNext()) {
			localObject = (OrderItem) localIterator.next();
			if (localObject == null)
				continue;
			localProduct = ((OrderItem) localObject).getProduct();
			this.productDao.lock(localProduct, LockModeType.PESSIMISTIC_WRITE);
			if (localProduct == null)
				continue;
			Integer localInteger = ((OrderItem) localObject).getQuantity();
			Calendar localCalendar1 = Calendar.getInstance();
			Calendar localCalendar2 = DateUtils.toCalendar(localProduct
					.getWeekSalesDate());
			Calendar localCalendar3 = DateUtils.toCalendar(localProduct
					.getMonthSalesDate());
			if ((localCalendar1.get(1) != localCalendar2.get(1))
					|| (localCalendar1.get(3) > localCalendar2.get(3)))
				localProduct
						.setWeekSales(Long.valueOf(localInteger.intValue()));
			else
				localProduct.setWeekSales(Long.valueOf(localProduct
						.getWeekSales().longValue() + localInteger.intValue()));
			if ((localCalendar1.get(1) != localCalendar3.get(1))
					|| (localCalendar1.get(2) > localCalendar3.get(2)))
				localProduct
						.setMonthSales(Long.valueOf(localInteger.intValue()));
			else
				localProduct
						.setMonthSales(Long.valueOf(localProduct
								.getMonthSales().longValue()
								+ localInteger.intValue()));
			localProduct.setSales(Long.valueOf(localProduct.getSales()
					.longValue() + localInteger.intValue()));
			localProduct.setWeekSalesDate(new Date());
			localProduct.setMonthSalesDate(new Date());
			this.productDao.merge(localProduct);
			this.orderDao.flush();
			this.staticService.build(localProduct);
		}
		order.setOrderStatus(Order.OrderStatus.completed);
		order.setExpire(null);
		this.orderDao.merge(order);
		Object localObject = new OrderLog();
		((OrderLog) localObject).setType(OrderLog.Type.complete);
		((OrderLog) localObject).setOperator(operator != null ? operator
				.getUsername() : null);
		((OrderLog) localObject).setOrder(order);
		this.orderLogDao.persist(localObject);
	}

	public void cancel(Order order, Admin operator) {
		Assert.notNull(order);
		CouponCode localCouponCode = order.getCouponCode();
		if (localCouponCode != null) {
			localCouponCode.setIsUsed(Boolean.valueOf(false));
			localCouponCode.setUsedDate(null);
			this.couponCodeDao.merge(localCouponCode);
			order.setCouponCode(null);
			this.orderDao.merge(order);
		}
		if (order.getIsAllocatedStock().booleanValue()) {
			Iterator localIterator = order.getOrderItems().iterator();
			while (localIterator.hasNext()) {
				localObject = (OrderItem) localIterator.next();
				if (localObject == null)
					continue;
				Product localProduct = ((OrderItem) localObject).getProduct();
				this.productDao.lock(localProduct,
						LockModeType.PESSIMISTIC_WRITE);
				if ((localProduct == null) || (localProduct.getStock() == null))
					continue;
				localProduct.setAllocatedStock(Integer
						.valueOf(localProduct.getAllocatedStock().intValue()
								- (((OrderItem) localObject).getQuantity()
										.intValue() - ((OrderItem) localObject)
										.getShippedQuantity().intValue())));
				this.productDao.merge(localProduct);
				this.orderDao.flush();
				this.staticService.build(localProduct);
			}
			order.setIsAllocatedStock(Boolean.valueOf(false));
		}
		order.setOrderStatus(Order.OrderStatus.cancelled);
		order.setExpire(null);
		this.orderDao.merge(order);
		Object localObject = new OrderLog();
		((OrderLog) localObject).setType(OrderLog.Type.cancel);
		((OrderLog) localObject).setOperator(operator != null ? operator
				.getUsername() : null);
		((OrderLog) localObject).setOrder(order);
		this.orderLogDao.persist(localObject);
	}

	public void payment(Order order, Payment payment, Admin operator) {
		Assert.notNull(order);
		Assert.notNull(payment);
		this.orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);
		payment.setOrder(order);
		this.paymentDao.merge(payment);
		if (payment.getType() == PaymentType.deposit) {
			localObject1 = order.getMember();
			this.memberDao.lock(localObject1, LockModeType.PESSIMISTIC_WRITE);
			((Member) localObject1).setBalance(((Member) localObject1)
					.getBalance().subtract(payment.getAmount()));
			this.memberDao.merge(localObject1);
			localObject2 = new Deposit();
			((Deposit) localObject2)
					.setType(operator != null ? Deposit.Type.adminPayment
							: Deposit.Type.memberPayment);
			((Deposit) localObject2).setCredit(new BigDecimal(0));
			((Deposit) localObject2).setDebit(payment.getAmount());
			((Deposit) localObject2).setBalance(((Member) localObject1)
					.getBalance());
			((Deposit) localObject2).setOperator(operator != null ? operator
					.getUsername() : null);
			((Deposit) localObject2).setMember((Member) localObject1);
			((Deposit) localObject2).setOrder(order);
			this.depositDao.persist(localObject2);
		}
		Object localObject1 = SettingUtils.get();
		if ((!order.getIsAllocatedStock().booleanValue())
				&& (((Setting) localObject1).getStockAllocationTime() == Setting.StockAllocationTime.payment)) {
			Iterator localIterator = order.getOrderItems().iterator();
			while (localIterator.hasNext()) {
				localObject2 = (OrderItem) localIterator.next();
				if (localObject2 == null)
					continue;
				Product localProduct = ((OrderItem) localObject2).getProduct();
				this.productDao.lock(localProduct,
						LockModeType.PESSIMISTIC_WRITE);
				if ((localProduct == null) || (localProduct.getStock() == null))
					continue;
				localProduct
						.setAllocatedStock(Integer
								.valueOf(localProduct.getAllocatedStock()
										.intValue()
										+ (((OrderItem) localObject2)
												.getQuantity().intValue() - ((OrderItem) localObject2)
												.getShippedQuantity()
												.intValue())));
				this.productDao.merge(localProduct);
				this.orderDao.flush();
				this.staticService.build(localProduct);
			}
			order.setIsAllocatedStock(Boolean.valueOf(true));
		}
		order.setAmountPaid(order.getAmountPaid().add(payment.getAmount()));
		order.setFee(payment.getFee());
		order.setExpire(null);
		if (order.getAmountPaid().compareTo(order.getAmount()) >= 0) {
			order.setOrderStatus(Order.OrderStatus.confirmed);
			order.setPaymentStatus(Order.PaymentStatus.paid);
		} else if (order.getAmountPaid().compareTo(new BigDecimal(0)) > 0) {
			order.setOrderStatus(Order.OrderStatus.confirmed);
			order.setPaymentStatus(Order.PaymentStatus.partialPayment);
		}
		this.orderDao.merge(order);
		Object localObject2 = new OrderLog();
		((OrderLog) localObject2).setType(OrderLog.Type.payment);
		((OrderLog) localObject2).setOperator(operator != null ? operator
				.getUsername() : null);
		((OrderLog) localObject2).setOrder(order);
		this.orderLogDao.persist(localObject2);
	}

	public void refunds(Order order, Refunds refunds, Admin operator) {
		Assert.notNull(order);
		Assert.notNull(refunds);
		this.orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);
		refunds.setOrder(order);
		this.refundsDao.persist(refunds);
		if (refunds.getType() == RefundsType.deposit) {
			localObject = order.getMember();
			this.memberDao.lock(localObject, LockModeType.PESSIMISTIC_WRITE);
			((Member) localObject).setBalance(((Member) localObject)
					.getBalance().add(refunds.getAmount()));
			this.memberDao.merge(localObject);
			Deposit localDeposit = new Deposit();
			localDeposit.setType(Deposit.Type.adminRefunds);
			localDeposit.setCredit(refunds.getAmount());
			localDeposit.setDebit(new BigDecimal(0));
			localDeposit.setBalance(((Member) localObject).getBalance());
			localDeposit.setOperator(operator != null ? operator.getUsername()
					: null);
			localDeposit.setMember((Member) localObject);
			localDeposit.setOrder(order);
			this.depositDao.persist(localDeposit);
		}
		order.setAmountPaid(order.getAmountPaid().subtract(refunds.getAmount()));
		order.setExpire(null);
		if (order.getAmountPaid().compareTo(new BigDecimal(0)) == 0)
			order.setPaymentStatus(Order.PaymentStatus.refunded);
		else if (order.getAmountPaid().compareTo(new BigDecimal(0)) > 0)
			order.setPaymentStatus(Order.PaymentStatus.partialRefunds);
		this.orderDao.merge(order);
		Object localObject = new OrderLog();
		((OrderLog) localObject).setType(OrderLog.Type.refunds);
		((OrderLog) localObject).setOperator(operator != null ? operator
				.getUsername() : null);
		((OrderLog) localObject).setOrder(order);
		this.orderLogDao.persist(localObject);
	}

	public void shipping(Order order, Shipping shipping, Admin operator) {
		Assert.notNull(order);
		Assert.notNull(shipping);
		Assert.notEmpty(shipping.getShippingItems());
		this.orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);
		Setting localSetting = SettingUtils.get();
		Object localObject2;
		if ((!order.getIsAllocatedStock().booleanValue())
				&& (localSetting.getStockAllocationTime() == Setting.StockAllocationTime.ship)) {
			localIterator = order.getOrderItems().iterator();
			while (localIterator.hasNext()) {
				localObject1 = (OrderItem) localIterator.next();
				if (localObject1 == null)
					continue;
				localObject2 = ((OrderItem) localObject1).getProduct();
				this.productDao.lock(localObject2,
						LockModeType.PESSIMISTIC_WRITE);
				if ((localObject2 == null)
						|| (((Product) localObject2).getStock() == null))
					continue;
				((Product) localObject2)
						.setAllocatedStock(Integer
								.valueOf(((Product) localObject2)
										.getAllocatedStock().intValue()
										+ (((OrderItem) localObject1)
												.getQuantity().intValue() - ((OrderItem) localObject1)
												.getShippedQuantity()
												.intValue())));
				this.productDao.merge(localObject2);
				this.orderDao.flush();
				this.staticService.build((Product) localObject2);
			}
			order.setIsAllocatedStock(Boolean.valueOf(true));
		}
		shipping.setOrder(order);
		this.shippingDao.persist(shipping);
		Iterator localIterator = shipping.getShippingItems().iterator();
		while (localIterator.hasNext()) {
			localObject1 = (ShippingItem) localIterator.next();
			localObject2 = order.getOrderItem(((ShippingItem) localObject1)
					.getSn());
			if (localObject2 == null)
				continue;
			Product localProduct = ((OrderItem) localObject2).getProduct();
			this.productDao.lock(localProduct, LockModeType.PESSIMISTIC_WRITE);
			if (localProduct != null) {
				if (localProduct.getStock() != null) {
					localProduct.setStock(Integer.valueOf(localProduct
							.getStock().intValue()
							- ((ShippingItem) localObject1).getQuantity()
									.intValue()));
					if (order.getIsAllocatedStock().booleanValue())
						localProduct.setAllocatedStock(Integer
								.valueOf(localProduct.getAllocatedStock()
										.intValue()
										- ((ShippingItem) localObject1)
												.getQuantity().intValue()));
				}
				this.productDao.merge(localProduct);
				this.orderDao.flush();
				this.staticService.build(localProduct);
			}
			this.orderItemDao
					.lock(localObject2, LockModeType.PESSIMISTIC_WRITE);
			((OrderItem) localObject2).setShippedQuantity(Integer
					.valueOf(((OrderItem) localObject2).getShippedQuantity()
							.intValue()
							+ ((ShippingItem) localObject1).getQuantity()
									.intValue()));
		}
		if (order.getShippedQuantity() >= order.getQuantity()) {
			order.setShippingStatus(Order.ShippingStatus.shipped);
			order.setIsAllocatedStock(Boolean.valueOf(false));
		} else if (order.getShippedQuantity() > 0) {
			order.setShippingStatus(Order.ShippingStatus.partialShipment);
		}
		order.setExpire(null);
		this.orderDao.merge(order);
		Object localObject1 = new OrderLog();
		((OrderLog) localObject1).setType(OrderLog.Type.shipping);
		((OrderLog) localObject1).setOperator(operator != null ? operator
				.getUsername() : null);
		((OrderLog) localObject1).setOrder(order);
		this.orderLogDao.persist(localObject1);
	}

	public void returns(Order order, Returns returns, Admin operator) {
		Assert.notNull(order);
		Assert.notNull(returns);
		Assert.notEmpty(returns.getReturnsItems());
		this.orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);
		returns.setOrder(order);
		this.returnsDao.persist(returns);
		Iterator localIterator = returns.getReturnsItems().iterator();
		while (localIterator.hasNext()) {
			localObject = (ReturnsItem) localIterator.next();
			OrderItem orderItem = order
					.getOrderItem(((ReturnsItem) localObject).getSn());
			if (orderItem == null)
				continue;
			this.orderItemDao.lock(orderItem, LockModeType.PESSIMISTIC_WRITE);
			orderItem.setReturnQuantity(Integer.valueOf(orderItem
					.getReturnQuantity().intValue()
					+ ((ReturnsItem) localObject).getQuantity().intValue()));
		}
		if (order.getReturnQuantity() >= order.getShippedQuantity())
			order.setShippingStatus(Order.ShippingStatus.returned);
		else if (order.getReturnQuantity() > 0)
			order.setShippingStatus(Order.ShippingStatus.partialReturns);
		order.setExpire(null);
		this.orderDao.merge(order);
		Object localObject = new OrderLog();
		((OrderLog) localObject).setType(OrderLog.Type.returns);
		((OrderLog) localObject).setOperator(operator != null ? operator
				.getUsername() : null);
		((OrderLog) localObject).setOrder(order);
		this.orderLogDao.persist(localObject);
	}

	public void delete(Order order) {
		if (order.getIsAllocatedStock().booleanValue()) {
			Iterator localIterator = order.getOrderItems().iterator();
			while (localIterator.hasNext()) {
				OrderItem orderItem = (OrderItem) localIterator.next();
				if (orderItem == null)
					continue;
				Product localProduct = orderItem.getProduct();
				this.productDao.lock(localProduct,
						LockModeType.PESSIMISTIC_WRITE);
				if ((localProduct == null) || (localProduct.getStock() == null))
					continue;
				localProduct.setAllocatedStock(Integer.valueOf(localProduct
						.getAllocatedStock().intValue()
						- (orderItem.getQuantity().intValue() - orderItem
								.getShippedQuantity().intValue())));
				this.productDao.merge(localProduct);
				this.orderDao.flush();
				this.staticService.build(localProduct);
			}
		}
		super.delete(order);
	}
}

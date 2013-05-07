package net.shopxx.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import net.shopxx.Setting;
import net.shopxx.util.SettingUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "xx_order")
public class Order extends BaseEntity {
	public enum OrderOrderStatus {
		unconfirmed, confirmed, completed, cancelled;
	}

	public enum OrderPaymentStatus {
		unpaid, partialPayment, paid, partialRefunds, refunded;
	}

	public enum OrderShippingStatus {
		unshipped, partialShipment, shipped, partialReturns, returned;
	}

	private static final long serialVersionUID = 8370942500343156156L;
	private static final String IIIllIlI = " ";
	private String sn;
	private OrderOrderStatus orderStatus;
	private OrderPaymentStatus paymentStatus;
	private OrderShippingStatus shippingStatus;
	private BigDecimal fee;
	private BigDecimal freight;
	private BigDecimal discount;
	private BigDecimal amountPaid;
	private Integer point;
	private String consignee;
	private String areaName;
	private String address;
	private String zipCode;
	private String phone;
	private Boolean isInvoice;
	private String invoiceTitle;
	private BigDecimal tax;
	private String memo;
	private String promotion;
	private Date expire;
	private Date lockExpire;
	private Boolean isAllocatedStock;
	private String paymentMethodName;
	private String shippingMethodName;
	private Area area;
	private PaymentMethod paymentMethod;
	private ShippingMethod shippingMethod;
	private Admin operator;
	private Member member;
	private CouponCode couponCode;
	private List<Coupon> coupons = new ArrayList();
	private List<OrderItem> orderItems = new ArrayList();
	private Set<OrderLog> orderLogs = new HashSet();
	private Set<Deposit> deposits = new HashSet();
	private Set<Payment> payments = new HashSet();
	private Set<Refunds> refunds = new HashSet();
	private Set<Shipping> shippings = new HashSet();
	private Set<Returns> returns = new HashSet();

	@Column(nullable = false, updatable = false, unique = true)
	public String getSn() {
		return this.sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	@Column(nullable = false)
	public OrderOrderStatus getOrderStatus() {
		return this.orderStatus;
	}

	public void setOrderStatus(OrderOrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	@Column(nullable = false)
	public OrderPaymentStatus getPaymentStatus() {
		return this.paymentStatus;
	}

	public void setPaymentStatus(OrderPaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	@Column(nullable = false)
	public OrderShippingStatus getShippingStatus() {
		return this.shippingStatus;
	}

	public void setShippingStatus(OrderShippingStatus shippingStatus) {
		this.shippingStatus = shippingStatus;
	}

	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getFee() {
		return this.fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	@NotNull
	@Min(0L)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getFreight() {
		return this.freight;
	}

	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}

	@NotNull
	@Min(0L)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getDiscount() {
		return this.discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getAmountPaid() {
		return this.amountPaid;
	}

	public void setAmountPaid(BigDecimal amountPaid) {
		this.amountPaid = amountPaid;
	}

	@NotNull
	@Min(0L)
	@Column(nullable = false)
	public Integer getPoint() {
		return this.point;
	}

	public void setPoint(Integer point) {
		this.point = point;
	}

	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getConsignee() {
		return this.consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	@Column(nullable = false)
	public String getAreaName() {
		return this.areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getZipCode() {
		return this.zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@NotNull
	@Column(nullable = false)
	public Boolean getIsInvoice() {
		return this.isInvoice;
	}

	public void setIsInvoice(Boolean isInvoice) {
		this.isInvoice = isInvoice;
	}

	@Length(max = 200)
	public String getInvoiceTitle() {
		return this.invoiceTitle;
	}

	public void setInvoiceTitle(String invoiceTitle) {
		this.invoiceTitle = invoiceTitle;
	}

	@Min(0L)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getTax() {
		return this.tax;
	}

	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	@Length(max = 200)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(updatable = false)
	public String getPromotion() {
		return this.promotion;
	}

	public void setPromotion(String promotion) {
		this.promotion = promotion;
	}

	public Date getExpire() {
		return this.expire;
	}

	public void setExpire(Date expire) {
		this.expire = expire;
	}

	public Date getLockExpire() {
		return this.lockExpire;
	}

	public void setLockExpire(Date lockExpire) {
		this.lockExpire = lockExpire;
	}

	@Column(nullable = false)
	public Boolean getIsAllocatedStock() {
		return this.isAllocatedStock;
	}

	public void setIsAllocatedStock(Boolean isAllocatedStock) {
		this.isAllocatedStock = isAllocatedStock;
	}

	@Column(nullable = false)
	public String getPaymentMethodName() {
		return this.paymentMethodName;
	}

	public void setPaymentMethodName(String paymentMethodName) {
		this.paymentMethodName = paymentMethodName;
	}

	@Column(nullable = false)
	public String getShippingMethodName() {
		return this.shippingMethodName;
	}

	public void setShippingMethodName(String shippingMethodName) {
		this.shippingMethodName = shippingMethodName;
	}

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	public Area getArea() {
		return this.area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	public PaymentMethod getPaymentMethod() {
		return this.paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	public ShippingMethod getShippingMethod() {
		return this.shippingMethod;
	}

	public void setShippingMethod(ShippingMethod shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Admin getOperator() {
		return this.operator;
	}

	public void setOperator(Admin operator) {
		this.operator = operator;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	public Member getMember() {
		return this.member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	@OneToOne(fetch = FetchType.LAZY)
	public CouponCode getCouponCode() {
		return this.couponCode;
	}

	public void setCouponCode(CouponCode couponCode) {
		this.couponCode = couponCode;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "xx_order_coupon")
	public List<Coupon> getCoupons() {
		return this.coupons;
	}

	public void setCoupons(List<Coupon> coupons) {
		this.coupons = coupons;
	}

	@Valid
	@NotEmpty
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = { javax.persistence.CascadeType.ALL }, orphanRemoval = true)
	@OrderBy("isGift asc")
	public List<OrderItem> getOrderItems() {
		return this.orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = { javax.persistence.CascadeType.REMOVE })
	@OrderBy("createDate asc")
	public Set<OrderLog> getOrderLogs() {
		return this.orderLogs;
	}

	public void setOrderLogs(Set<OrderLog> orderLogs) {
		this.orderLogs = orderLogs;
	}

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
	public Set<Deposit> getDeposits() {
		return this.deposits;
	}

	public void setDeposits(Set<Deposit> deposits) {
		this.deposits = deposits;
	}

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = { javax.persistence.CascadeType.REMOVE })
	@OrderBy("createDate asc")
	public Set<Payment> getPayments() {
		return this.payments;
	}

	public void setPayments(Set<Payment> payments) {
		this.payments = payments;
	}

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = { javax.persistence.CascadeType.REMOVE })
	@OrderBy("createDate asc")
	public Set<Refunds> getRefunds() {
		return this.refunds;
	}

	public void setRefunds(Set<Refunds> refunds) {
		this.refunds = refunds;
	}

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = { javax.persistence.CascadeType.REMOVE })
	@OrderBy("createDate asc")
	public Set<Shipping> getShippings() {
		return this.shippings;
	}

	public void setShippings(Set<Shipping> shippings) {
		this.shippings = shippings;
	}

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = { javax.persistence.CascadeType.REMOVE })
	@OrderBy("createDate asc")
	public Set<Returns> getReturns() {
		return this.returns;
	}

	public void setReturns(Set<Returns> returns) {
		this.returns = returns;
	}

	@Transient
	public String getProductName() {
		StringBuffer localStringBuffer = new StringBuffer();
		if (getOrderItems() != null) {
			Iterator localIterator = getOrderItems().iterator();
			while (localIterator.hasNext()) {
				OrderItem localOrderItem = (OrderItem) localIterator.next();
				if ((localOrderItem == null)
						|| (localOrderItem.getFullName() == null))
					continue;
				localStringBuffer.append(" ").append(
						localOrderItem.getFullName());
			}
			if (localStringBuffer.length() > 0)
				localStringBuffer.deleteCharAt(0);
		}
		return localStringBuffer.toString();
	}

	@Transient
	public int getWeight() {
		int i = 0;
		if (getOrderItems() != null) {
			Iterator localIterator = getOrderItems().iterator();
			while (localIterator.hasNext()) {
				OrderItem localOrderItem = (OrderItem) localIterator.next();
				if (localOrderItem == null)
					continue;
				i += localOrderItem.getTotalWeight();
			}
		}
		return i;
	}

	@Transient
	public int getQuantity() {
		int i = 0;
		if (getOrderItems() != null) {
			Iterator localIterator = getOrderItems().iterator();
			while (localIterator.hasNext()) {
				OrderItem localOrderItem = (OrderItem) localIterator.next();
				if ((localOrderItem == null)
						|| (localOrderItem.getQuantity() == null))
					continue;
				i += localOrderItem.getQuantity().intValue();
			}
		}
		return i;
	}

	@Transient
	public int getShippedQuantity() {
		int i = 0;
		if (getOrderItems() != null) {
			Iterator localIterator = getOrderItems().iterator();
			while (localIterator.hasNext()) {
				OrderItem localOrderItem = (OrderItem) localIterator.next();
				if ((localOrderItem == null)
						|| (localOrderItem.getShippedQuantity() == null))
					continue;
				i += localOrderItem.getShippedQuantity().intValue();
			}
		}
		return i;
	}

	@Transient
	public int getReturnQuantity() {
		int i = 0;
		if (getOrderItems() != null) {
			Iterator localIterator = getOrderItems().iterator();
			while (localIterator.hasNext()) {
				OrderItem localOrderItem = (OrderItem) localIterator.next();
				if ((localOrderItem == null)
						|| (localOrderItem.getReturnQuantity() == null))
					continue;
				i += localOrderItem.getReturnQuantity().intValue();
			}
		}
		return i;
	}

	@Transient
	public BigDecimal getPrice() {
		BigDecimal localBigDecimal = new BigDecimal(0);
		if (getOrderItems() != null) {
			Iterator localIterator = getOrderItems().iterator();
			while (localIterator.hasNext()) {
				OrderItem localOrderItem = (OrderItem) localIterator.next();
				if ((localOrderItem == null)
						|| (localOrderItem.getSubtotal() == null))
					continue;
				localBigDecimal = localBigDecimal.add(localOrderItem
						.getSubtotal());
			}
		}
		return localBigDecimal;
	}

	@Transient
	public BigDecimal getAmount() {
		BigDecimal localBigDecimal = getPrice()
				.subtract(
						getDiscount() != null ? getDiscount() : new BigDecimal(
								0))
				.add(getFreight() != null ? getFreight() : new BigDecimal(0))
				.add(getFee() != null ? getFee() : new BigDecimal(0))
				.add(getTax() != null ? getTax() : new BigDecimal(0));
		return localBigDecimal.compareTo(new BigDecimal(0)) > 0 ? localBigDecimal
				: new BigDecimal(0);
	}

	@Transient
	public BigDecimal getAmountPayable() {
		BigDecimal localBigDecimal = getAmount().subtract(getAmountPaid());
		return localBigDecimal.compareTo(new BigDecimal(0)) > 0 ? localBigDecimal
				: new BigDecimal(0);
	}

	@Transient
	public boolean isExpired() {
		return (getExpire() != null) && (new Date().after(getExpire()));
	}

	@Transient
	public OrderItem getOrderItem(String sn) {
		if ((sn != null) && (getOrderItems() != null)) {
			Iterator localIterator = getOrderItems().iterator();
			while (localIterator.hasNext()) {
				OrderItem localOrderItem = (OrderItem) localIterator.next();
				if ((localOrderItem != null)
						&& (sn.equalsIgnoreCase(localOrderItem.getSn())))
					return localOrderItem;
			}
		}
		return null;
	}

	@Transient
	public boolean isLocked(Admin operator) {
		return (getLockExpire() != null)
				&& (new Date().before(getLockExpire()))
				&& (getOperator() != operator);
	}

	@Transient
	public BigDecimal calculateTax() {
		Setting localSetting = SettingUtils.get();
		BigDecimal localBigDecimal;
		if (localSetting.getIsTaxPriceEnabled().booleanValue())
			localBigDecimal = getPrice().subtract(getDiscount()).multiply(
					new BigDecimal(localSetting.getTaxRate().toString()));
		else
			localBigDecimal = new BigDecimal(0);
		return localSetting.setScale(localBigDecimal);
	}

	@PrePersist
	public void prePersist() {
		if (getArea() != null)
			setAreaName(getArea().getFullName());
		if (getPaymentMethod() != null)
			setPaymentMethodName(getPaymentMethod().getName());
		if (getShippingMethod() != null)
			setShippingMethodName(getShippingMethod().getName());
	}

	@PreUpdate
	public void preUpdate() {
		if (getArea() != null)
			setAreaName(getArea().getFullName());
		if (getPaymentMethod() != null)
			setPaymentMethodName(getPaymentMethod().getName());
		if (getShippingMethod() != null)
			setShippingMethodName(getShippingMethod().getName());
	}

	@PreRemove
	public void preRemove() {
		Set localSet = getDeposits();
		if (localSet != null) {
			Iterator localIterator = localSet.iterator();
			while (localIterator.hasNext()) {
				Deposit localDeposit = (Deposit) localIterator.next();
				localDeposit.setOrder(null);
			}
		}
	}
}

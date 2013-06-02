package net.shopxx.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PreRemove;
import javax.persistence.Table;

@Entity
@Table(name = "xx_coupon_code")
public class CouponCode extends BaseEntity {
	private static final long serialVersionUID = -1812874037224306719L;
	private String code;
	private Boolean isUsed;
	private Date usedDate;
	private Coupon coupon;
	private Member member;
	private Order order;

	@Column(nullable = false, updatable = false, unique = true)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(nullable = false)
	public Boolean getIsUsed() {
		return this.isUsed;
	}

	public void setIsUsed(Boolean isUsed) {
		this.isUsed = isUsed;
	}

	public Date getUsedDate() {
		return this.usedDate;
	}

	public void setUsedDate(Date usedDate) {
		this.usedDate = usedDate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	public Coupon getCoupon() {
		return this.coupon;
	}

	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Member getMember() {
		return this.member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	@OneToOne(mappedBy = "couponCode", fetch = FetchType.LAZY)
	@JoinColumn(name = "orders")
	public Order getOrder() {
		return this.order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	@PreRemove
	public void preRemove() {
		if (getOrder() != null)
			getOrder().setCouponCode(null);
	}
}

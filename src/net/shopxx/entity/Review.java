package net.shopxx.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "xx_review")
public class Review extends BaseEntity {
	private static final long serialVersionUID = 8795901519290584100L;

	public enum ReviewType {
		positive, moderate, negative;
	}

	private static final String IIIllIlI = "/review/content";
	private static final String IIIllIll = ".jhtml";
	private Integer score;
	private String content;
	private Boolean isShow;
	private String ip;
	private Member member;
	private Product product;

	@NotNull
	@Min(1L)
	@Max(5L)
	@Column(nullable = false, updatable = false)
	public Integer getScore() {
		return this.score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false, updatable = false)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(nullable = false)
	public Boolean getIsShow() {
		return this.isShow;
	}

	public void setIsShow(Boolean isShow) {
		this.isShow = isShow;
	}

	@Column(nullable = false, updatable = false)
	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false)
	public Member getMember() {
		return this.member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	public Product getProduct() {
		return this.product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Transient
	public String getPath() {
		if ((getProduct() != null) && (getProduct().getId() != null))
			return "/review/content/" + getProduct().getId() + ".jhtml";
		return null;
	}
}

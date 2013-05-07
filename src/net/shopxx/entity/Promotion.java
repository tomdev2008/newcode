package net.shopxx.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="xx_promotion")
public class Promotion extends OrderEntity
{
  private static final long serialVersionUID = 3536993535267962279L;
  public enum PromotionOperator
  {
    add, subtract, multiply, divide;
  }
  private static final String IIIllIlI = "/promotion/content";
  private static final String IIIllIll = ".jhtml";
  private String name;
  private String title;
  private Date beginDate;
  private Date endDate;
  private BigDecimal startPrice;
  private BigDecimal endPrice;
  private PromotionOperator priceOperator;
  private BigDecimal priceValue;
  private PromotionOperator pointOperator;
  private BigDecimal pointValue;
  private Boolean isFreeShipping;
  private Boolean isCouponAllowed;
  private String introduction;
  private Set<MemberRank> memberRanks = new HashSet();
  private Set<ProductCategory> productCategories = new HashSet();
  private Set<Product> products = new HashSet();
  private Set<Brand> brands = new HashSet();
  private Set<Coupon> coupons = new HashSet();
  private List<GiftItem> giftItems = new ArrayList();

  @JsonProperty
  @NotEmpty
  @Length(max=200)
  @Column(nullable=false)
  public String getName()
  {
    return this.name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  @JsonProperty
  @NotEmpty
  @Length(max=200)
  @Column(nullable=false)
  public String getTitle()
  {
    return this.title;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }

  @JsonProperty
  public Date getBeginDate()
  {
    return this.beginDate;
  }

  public void setBeginDate(Date beginDate)
  {
    this.beginDate = beginDate;
  }

  @JsonProperty
  public Date getEndDate()
  {
    return this.endDate;
  }

  public void setEndDate(Date endDate)
  {
    this.endDate = endDate;
  }

  @JsonProperty
  @Min(0L)
  @Digits(integer=12, fraction=3)
  @Column(precision=21, scale=6)
  public BigDecimal getStartPrice()
  {
    return this.startPrice;
  }

  public void setStartPrice(BigDecimal startPrice)
  {
    this.startPrice = startPrice;
  }

  @JsonProperty
  @Min(0L)
  @Digits(integer=12, fraction=3)
  @Column(precision=21, scale=6)
  public BigDecimal getEndPrice()
  {
    return this.endPrice;
  }

  public void setEndPrice(BigDecimal endPrice)
  {
    this.endPrice = endPrice;
  }

  @NotNull
  @Column(nullable=false)
  public PromotionOperator getPriceOperator()
  {
    return this.priceOperator;
  }

  public void setPriceOperator(PromotionOperator priceOperator)
  {
    this.priceOperator = priceOperator;
  }

  @Digits(integer=12, fraction=3)
  @Column(precision=21, scale=6)
  public BigDecimal getPriceValue()
  {
    return this.priceValue;
  }

  public void setPriceValue(BigDecimal priceValue)
  {
    this.priceValue = priceValue;
  }

  @NotNull
  @Column(nullable=false)
  public PromotionOperator getPointOperator()
  {
    return this.pointOperator;
  }

  public void setPointOperator(PromotionOperator pointOperator)
  {
    this.pointOperator = pointOperator;
  }

  public BigDecimal getPointValue()
  {
    return this.pointValue;
  }

  public void setPointValue(BigDecimal pointValue)
  {
    this.pointValue = pointValue;
  }

  @NotNull
  @Column(nullable=false)
  public Boolean getIsFreeShipping()
  {
    return this.isFreeShipping;
  }

  public void setIsFreeShipping(Boolean isFreeShipping)
  {
    this.isFreeShipping = isFreeShipping;
  }

  @JsonProperty
  @NotNull
  @Column(nullable=false)
  public Boolean getIsCouponAllowed()
  {
    return this.isCouponAllowed;
  }

  public void setIsCouponAllowed(Boolean isCouponAllowed)
  {
    this.isCouponAllowed = isCouponAllowed;
  }

  @Lob
  public String getIntroduction()
  {
    return this.introduction;
  }

  public void setIntroduction(String introduction)
  {
    this.introduction = introduction;
  }

  @ManyToMany(fetch=FetchType.LAZY)
  @JoinTable(name="xx_promotion_member_rank")
  public Set<MemberRank> getMemberRanks()
  {
    return this.memberRanks;
  }

  public void setMemberRanks(Set<MemberRank> memberRanks)
  {
    this.memberRanks = memberRanks;
  }

  @ManyToMany(fetch=FetchType.LAZY)
  @JoinTable(name="xx_promotion_product_category")
  public Set<ProductCategory> getProductCategories()
  {
    return this.productCategories;
  }

  public void setProductCategories(Set<ProductCategory> productCategories)
  {
    this.productCategories = productCategories;
  }

  @ManyToMany(fetch=FetchType.LAZY)
  @JoinTable(name="xx_promotion_product")
  public Set<Product> getProducts()
  {
    return this.products;
  }

  public void setProducts(Set<Product> products)
  {
    this.products = products;
  }

  @ManyToMany(fetch=FetchType.LAZY)
  @JoinTable(name="xx_promotion_brand")
  public Set<Brand> getBrands()
  {
    return this.brands;
  }

  public void setBrands(Set<Brand> brands)
  {
    this.brands = brands;
  }

  @ManyToMany(fetch=FetchType.LAZY)
  @JoinTable(name="xx_promotion_coupon")
  public Set<Coupon> getCoupons()
  {
    return this.coupons;
  }

  public void setCoupons(Set<Coupon> coupons)
  {
    this.coupons = coupons;
  }

  @Valid
  @OneToMany(mappedBy="promotion", fetch=FetchType.LAZY, cascade={javax.persistence.CascadeType.ALL}, orphanRemoval=true)
  public List<GiftItem> getGiftItems()
  {
    return this.giftItems;
  }

  public void setGiftItems(List<GiftItem> giftItems)
  {
    this.giftItems = giftItems;
  }

  @Transient
  public boolean hasBegun()
  {
    return (getBeginDate() == null) || (new Date().after(getBeginDate()));
  }

  @Transient
  public boolean hasEnded()
  {
    return (getEndDate() != null) && (new Date().after(getEndDate()));
  }

  @Transient
  public String getPath()
  {
    if (getId() != null)
      return "/promotion/content/" + getId() + ".jhtml";
    return null;
  }

  @Transient
  public BigDecimal calculatePrice(BigDecimal price)
  {
    if ((price != null) && (getPriceOperator() != null) && (getPriceValue() != null))
    {
      BigDecimal localBigDecimal;
      if (getPriceOperator() == PromotionOperator.add)
        localBigDecimal = price.add(getPriceValue());
      else if (getPriceOperator() == PromotionOperator.subtract)
        localBigDecimal = price.subtract(getPriceValue());
      else if (getPriceOperator() == PromotionOperator.multiply)
        localBigDecimal = price.multiply(getPriceValue());
      else
        localBigDecimal = price.divide(getPriceValue());
      return localBigDecimal.compareTo(new BigDecimal(0)) > 0 ? localBigDecimal : new BigDecimal(0);
    }
    return price;
  }

  @Transient
  public Integer calculatePoint(Integer point)
  {
    if ((point != null) && (getPointOperator() != null) && (getPointValue() != null))
    {
      BigDecimal localBigDecimal;
      if (getPointOperator() == PromotionOperator.add)
        localBigDecimal = new BigDecimal(point.intValue()).add(getPointValue());
      else if (getPointOperator() == PromotionOperator.subtract)
        localBigDecimal = new BigDecimal(point.intValue()).subtract(getPointValue());
      else if (getPointOperator() == PromotionOperator.multiply)
        localBigDecimal = new BigDecimal(point.intValue()).multiply(getPointValue());
      else
        localBigDecimal = new BigDecimal(point.intValue()).divide(getPointValue());
      return Integer.valueOf(localBigDecimal.compareTo(new BigDecimal(0)) > 0 ? localBigDecimal.intValue() : 0);
    }
    return point;
  }
}

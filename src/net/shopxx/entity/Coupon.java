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
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import net.shopxx.Setting;
import net.shopxx.util.SettingUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="xx_coupon")
public class Coupon extends BaseEntity
{
  private static final long serialVersionUID = -7907808728349149722L;
  public enum CouponOperator
  {
    add, subtract, multiply, divide;
  }
  private String name;
  private String prefix;
  private Date beginDate;
  private Date endDate;
  private BigDecimal startPrice;
  private BigDecimal endPrice;
  private Boolean isEnabled;
  private Boolean isExchange;
  private Integer point;
  private CouponOperator priceOperator;
  private BigDecimal priceValue;
  private String introduction;
  private Set<CouponCode> couponCodes = new HashSet();
  private Set<Promotion> promotions = new HashSet();
  private List<Order> orders = new ArrayList();

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

  @NotEmpty
  @Length(max=200)
  @Column(nullable=false)
  public String getPrefix()
  {
    return this.prefix;
  }

  public void setPrefix(String prefix)
  {
    this.prefix = prefix;
  }

  public Date getBeginDate()
  {
    return this.beginDate;
  }

  public void setBeginDate(Date beginDate)
  {
    this.beginDate = beginDate;
  }

  public Date getEndDate()
  {
    return this.endDate;
  }

  public void setEndDate(Date endDate)
  {
    this.endDate = endDate;
  }

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
  public Boolean getIsEnabled()
  {
    return this.isEnabled;
  }

  public void setIsEnabled(Boolean isEnabled)
  {
    this.isEnabled = isEnabled;
  }

  @NotNull
  @Column(nullable=false)
  public Boolean getIsExchange()
  {
    return this.isExchange;
  }

  public void setIsExchange(Boolean isExchange)
  {
    this.isExchange = isExchange;
  }

  @Min(0L)
  public Integer getPoint()
  {
    return this.point;
  }

  public void setPoint(Integer point)
  {
    this.point = point;
  }

  @NotNull
  @Column(nullable=false)
  public CouponOperator getPriceOperator()
  {
    return this.priceOperator;
  }

  public void setPriceOperator(CouponOperator priceOperator)
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

  @Lob
  public String getIntroduction()
  {
    return this.introduction;
  }

  public void setIntroduction(String introduction)
  {
    this.introduction = introduction;
  }

  @OneToMany(mappedBy="coupon", fetch=FetchType.LAZY, cascade={javax.persistence.CascadeType.REMOVE})
  public Set<CouponCode> getCouponCodes()
  {
    return this.couponCodes;
  }

  public void setCouponCodes(Set<CouponCode> couponCodes)
  {
    this.couponCodes = couponCodes;
  }

  @ManyToMany(mappedBy="coupons", fetch=FetchType.LAZY)
  public Set<Promotion> getPromotions()
  {
    return this.promotions;
  }

  public void setPromotions(Set<Promotion> promotions)
  {
    this.promotions = promotions;
  }

  @ManyToMany(mappedBy="coupons", fetch=FetchType.LAZY)
  public List<Order> getOrders()
  {
    return this.orders;
  }

  public void setOrders(List<Order> orders)
  {
    this.orders = orders;
  }

  @Transient
  public boolean hasBegun()
  {
    return (getBeginDate() == null) || (new Date().after(getBeginDate()));
  }

  @Transient
  public boolean hasExpired()
  {
    return (getEndDate() != null) && (new Date().after(getEndDate()));
  }

  @Transient
  public BigDecimal calculatePrice(BigDecimal price)
  {
	 BigDecimal localBigDecimal = null;
    if ((price != null) && (getPriceOperator() != null) && (getPriceValue() != null))
    {
      Setting localSetting = SettingUtils.get();
      if (getPriceOperator() == CouponOperator.add)
        localBigDecimal = price.add(getPriceValue());
      else if (getPriceOperator() == CouponOperator.subtract)
        localBigDecimal = price.subtract(getPriceValue());
      else if (getPriceOperator() == CouponOperator.multiply)
        localBigDecimal = price.multiply(getPriceValue());
      else
        localBigDecimal = price.divide(getPriceValue());
      localBigDecimal = localSetting.setScale(localBigDecimal);
      return localBigDecimal.compareTo(new BigDecimal(0)) > 0 ? localBigDecimal : new BigDecimal(0);
    }
    return price;
  }

  @PreRemove
  public void preRemove()
  {
    Set localSet = getPromotions();
    Object localObject1;
    Object localObject2;
    if (localSet != null)
    {
      localObject2 = localSet.iterator();
      while (((Iterator)localObject2).hasNext())
      {
        localObject1 = (Promotion)((Iterator)localObject2).next();
        ((Promotion)localObject1).getCoupons().remove(this);
      }
    }
    localObject1 = getOrders();
    if (localObject1 != null)
    {
      Iterator localIterator = ((List)localObject1).iterator();
      while (localIterator.hasNext())
      {
        localObject2 = (Order)localIterator.next();
        ((Order)localObject2).getCoupons().remove(this);
      }
    }
  }
}

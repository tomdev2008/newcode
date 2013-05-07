package net.shopxx.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="xx_gift_item", uniqueConstraints={@javax.persistence.UniqueConstraint(columnNames={"gift", "promotion"})})
public class GiftItem extends BaseEntity
{
  private static final long serialVersionUID = 6593657730952481829L;
  private Integer quantity;
  private Product gift;
  private Promotion promotion;

  @JsonProperty
  @NotNull
  @Min(1L)
  @Column(nullable=false)
  public Integer getQuantity()
  {
    return this.quantity;
  }

  public void setQuantity(Integer quantity)
  {
    this.quantity = quantity;
  }

  @JsonProperty
  @NotNull
  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(nullable=false, updatable=false)
  public Product getGift()
  {
    return this.gift;
  }

  public void setGift(Product gift)
  {
    this.gift = gift;
  }

  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(nullable=false, updatable=false)
  public Promotion getPromotion()
  {
    return this.promotion;
  }

  public void setPromotion(Promotion promotion)
  {
    this.promotion = promotion;
  }
}

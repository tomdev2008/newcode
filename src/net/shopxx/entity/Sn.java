package net.shopxx.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="xx_sn")
public class Sn extends BaseEntity
{
  private static final long serialVersionUID = -2330598144835706164L;
  public enum SnType
  {
    product, order, payment, refunds, shipping, returns;
  }
  private SnType type;
  private Long lastValue;

  @Column(nullable=false, updatable=false, unique=true)
  public SnType getType()
  {
    return this.type;
  }

  public void setType(SnType type)
  {
    this.type = type;
  }

  @Column(nullable=false)
  public Long getLastValue()
  {
    return this.lastValue;
  }

  public void setLastValue(Long lastValue)
  {
    this.lastValue = lastValue;
  }
}

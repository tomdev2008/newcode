package net.shopxx.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="xx_sn")
public class Sn extends BaseEntity
{
  private static final long serialVersionUID = -2330598144835706164L;
  private Sn.Type IIIllIlI;
  private Long IIIllIll;

  @Column(nullable=false, updatable=false, unique=true)
  public Sn.Type getType()
  {
    return this.IIIllIlI;
  }

  public void setType(Sn.Type type)
  {
    this.IIIllIlI = type;
  }

  @Column(nullable=false)
  public Long getLastValue()
  {
    return this.IIIllIll;
  }

  public void setLastValue(Long lastValue)
  {
    this.IIIllIll = lastValue;
  }
}


 * Qualified Name:     net.shopxx.entity.Sn


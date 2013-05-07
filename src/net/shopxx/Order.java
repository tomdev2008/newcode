package net.shopxx;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Order
  implements Serializable
{
  private static final long serialVersionUID = -3078342809727773232L;
  public enum OrderDirection
  {
    asc, desc;
  }
  private static final OrderDirection IIIllIlI = OrderDirection.desc;
  private String property;
  private OrderDirection direction = IIIllIlI;

  public Order()
  {
  }

  public Order(String property, OrderDirection direction)
  {
    this.property = property;
    this.direction = direction;
  }

  public static Order asc(String property)
  {
    return new Order(property, OrderDirection.asc);
  }

  public static Order desc(String property)
  {
    return new Order(property, OrderDirection.desc);
  }

  public String getProperty()
  {
    return this.property;
  }

  public void setProperty(String property)
  {
    this.property = property;
  }

  public OrderDirection getDirection()
  {
    return this.direction;
  }

  public void setDirection(OrderDirection direction)
  {
    this.direction = direction;
  }

  public boolean equals(Object obj)
  {
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    if (this == obj)
      return true;
    Order localOrder = (Order)obj;
    return new EqualsBuilder().append(getProperty(), localOrder.getProperty()).append(getDirection(), localOrder.getDirection()).isEquals();
  }

  public int hashCode()
  {
    return new HashCodeBuilder(17, 37).append(getProperty()).append(getDirection()).toHashCode();
  }
}

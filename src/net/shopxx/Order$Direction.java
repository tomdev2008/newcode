package net.shopxx;

public enum Order$Direction
{
  asc, desc;

  public static Direction fromString(String value)
  {
    return valueOf(value.toLowerCase());
  }
}


 * Qualified Name:     net.shopxx.Order.Direction


package net.shopxx;

public enum Filter$Operator
{
  eq, ne, gt, lt, ge, le, like, in, isNull, isNotNull;

  public static Operator fromString(String value)
  {
    return valueOf(value.toLowerCase());
  }
}


 * Qualified Name:     net.shopxx.Filter.Operator


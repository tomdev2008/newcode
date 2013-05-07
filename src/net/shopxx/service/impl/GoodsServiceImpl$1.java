package net.shopxx.service.impl;

import net.shopxx.entity.Product;
import org.apache.commons.collections.Predicate;

class GoodsServiceImpl$1
  implements Predicate
{
  public boolean evaluate(Object object)
  {
    Product localProduct = (Product)object;
    return (localProduct != null) && (localProduct.getId() != null);
  }
}

package net.shopxx.service;

import java.util.List;
import net.shopxx.Filter;
import net.shopxx.Order;
import net.shopxx.entity.Brand;

public abstract interface BrandService extends BaseService<Brand, Long>
{
  public abstract List<Brand> findList(Integer paramInteger, List<Filter> paramList, List<Order> paramList1, String paramString);
}


 * Qualified Name:     net.shopxx.service.BrandService


package net.shopxx.dao;

import java.util.List;
import net.shopxx.Filter;
import net.shopxx.Order;
import net.shopxx.entity.Promotion;

public abstract interface PromotionDao extends BaseDao<Promotion, Long>
{
  public abstract List<Promotion> findList(Boolean paramBoolean1, Boolean paramBoolean2, Integer paramInteger, List<Filter> paramList, List<Order> paramList1);
}

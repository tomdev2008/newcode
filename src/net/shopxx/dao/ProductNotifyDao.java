package net.shopxx.dao;

import net.shopxx.Page;
import net.shopxx.Pageable;
import net.shopxx.entity.Member;
import net.shopxx.entity.Product;
import net.shopxx.entity.ProductNotify;

public abstract interface ProductNotifyDao extends BaseDao<ProductNotify, Long>
{
  public abstract boolean exists(Product paramProduct, String paramString);

  public abstract Page<ProductNotify> findPage(Member paramMember, Boolean paramBoolean1, Boolean paramBoolean2, Boolean paramBoolean3, Pageable paramPageable);

  public abstract Long count(Member paramMember, Boolean paramBoolean1, Boolean paramBoolean2, Boolean paramBoolean3);
}


 * Qualified Name:     net.shopxx.dao.ProductNotifyDao


package net.shopxx.dao;

import net.shopxx.entity.Cart;

public abstract interface CartDao extends BaseDao<Cart, Long>
{
  public abstract void evictExpired();
}


 * Qualified Name:     net.shopxx.dao.CartDao


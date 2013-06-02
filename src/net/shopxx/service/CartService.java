package net.shopxx.service;

import net.shopxx.entity.Cart;
import net.shopxx.entity.Member;

public abstract interface CartService extends BaseService<Cart, Long> {
	public abstract Cart getCurrent();

	public abstract void merge(Member paramMember, Cart paramCart);

	public abstract void evictExpired();
}

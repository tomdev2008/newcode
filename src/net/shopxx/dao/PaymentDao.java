package net.shopxx.dao;

import net.shopxx.entity.Payment;

public abstract interface PaymentDao extends BaseDao<Payment, Long> {
	public abstract Payment findBySn(String paramString);
}

package net.shopxx.service;

import net.shopxx.entity.Payment;

public abstract interface PaymentService extends BaseService<Payment, Long> {
	public abstract Payment findBySn(String paramString);
}

package net.shopxx.dao.impl;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;

import net.shopxx.dao.PaymentDao;
import net.shopxx.entity.Payment;

import org.springframework.stereotype.Repository;

@Repository("paymentDaoImpl")
public class PaymentDaoImpl extends BaseDaoImpl<Payment, Long> implements
		PaymentDao {
	public Payment findBySn(String sn) {
		if (sn == null)
			return null;
		String str = "select payment from Payment payment where lower(payment.sn) = lower(:sn)";
		try {
			return (Payment) this.entityManager.createQuery(str, Payment.class)
					.setFlushMode(FlushModeType.COMMIT).setParameter("sn", sn)
					.getSingleResult();
		} catch (NoResultException localNoResultException) {
		}
		return null;
	}
}

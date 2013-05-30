package net.shopxx.service.impl;

import javax.annotation.Resource;
import net.shopxx.dao.PaymentDao;
import net.shopxx.entity.Payment;
import net.shopxx.service.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("paymentServiceImpl")
public class PaymentServiceImpl extends BaseServiceImpl<Payment, Long>
		implements PaymentService {

	@Resource(name = "paymentDaoImpl")
	private PaymentDao paymentDao;

	@Resource(name = "paymentDaoImpl")
	public void setBaseDao(PaymentDao paymentDao) {
		super.setBaseDao(paymentDao);
	}

	@Transactional(readOnly = true)
	public Payment findBySn(String sn) {
		return this.paymentDao.findBySn(sn);
	}
}

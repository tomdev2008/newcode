package net.shopxx.dao.impl;

import net.shopxx.dao.PaymentMethodDao;
import net.shopxx.entity.PaymentMethod;
import org.springframework.stereotype.Repository;

@Repository("paymentMethodDaoImpl")
public class PaymentMethodDaoImpl extends BaseDaoImpl<PaymentMethod, Long>
  implements PaymentMethodDao
{
}

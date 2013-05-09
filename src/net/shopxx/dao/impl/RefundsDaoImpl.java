package net.shopxx.dao.impl;

import net.shopxx.dao.RefundsDao;
import net.shopxx.entity.Refunds;
import org.springframework.stereotype.Repository;

@Repository("refundsDaoImpl")
public class RefundsDaoImpl extends BaseDaoImpl<Refunds, Long>
  implements RefundsDao
{
}

package net.shopxx.dao.impl;

import net.shopxx.dao.OrderLogDao;
import net.shopxx.entity.OrderLog;
import org.springframework.stereotype.Repository;

@Repository("orderLogDaoImpl")
public class OrderLogDaoImpl extends BaseDaoImpl<OrderLog, Long> implements
		OrderLogDao {
}

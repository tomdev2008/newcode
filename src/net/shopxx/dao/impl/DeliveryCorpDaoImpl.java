package net.shopxx.dao.impl;

import net.shopxx.dao.DeliveryCorpDao;
import net.shopxx.entity.DeliveryCorp;
import org.springframework.stereotype.Repository;

@Repository("deliveryCorpDaoImpl")
public class DeliveryCorpDaoImpl extends BaseDaoImpl<DeliveryCorp, Long>
		implements DeliveryCorpDao {
}

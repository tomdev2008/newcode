package net.shopxx.dao.impl;

import net.shopxx.dao.ShippingMethodDao;
import net.shopxx.entity.ShippingMethod;
import org.springframework.stereotype.Repository;

@Repository("shippingMethodDaoImpl")
public class ShippingMethodDaoImpl extends BaseDaoImpl<ShippingMethod, Long>
		implements ShippingMethodDao {
}

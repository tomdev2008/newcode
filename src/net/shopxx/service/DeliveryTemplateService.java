package net.shopxx.service;

import net.shopxx.entity.DeliveryTemplate;

public abstract interface DeliveryTemplateService extends
		BaseService<DeliveryTemplate, Long> {
	public abstract DeliveryTemplate findDefault();
}

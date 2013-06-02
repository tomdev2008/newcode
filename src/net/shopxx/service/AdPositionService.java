package net.shopxx.service;

import net.shopxx.entity.AdPosition;

public abstract interface AdPositionService extends
		BaseService<AdPosition, Long> {
	public abstract AdPosition find(Long paramLong, String paramString);
}

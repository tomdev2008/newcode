package net.shopxx.service;

import net.shopxx.entity.Sn.SnType;

public abstract interface SnService {
	public abstract String generate(SnType paramType);
}

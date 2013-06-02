package net.shopxx.service;

import net.shopxx.Page;
import net.shopxx.Pageable;
import net.shopxx.entity.Member;
import net.shopxx.entity.Receiver;

public abstract interface ReceiverService extends BaseService<Receiver, Long> {
	public abstract Receiver findDefault(Member paramMember);

	public abstract Page<Receiver> findPage(Member paramMember,
			Pageable paramPageable);
}

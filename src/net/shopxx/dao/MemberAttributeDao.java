package net.shopxx.dao;

import java.util.List;
import net.shopxx.entity.MemberAttribute;

public abstract interface MemberAttributeDao extends
		BaseDao<MemberAttribute, Long> {
	public abstract Integer findUnusedPropertyIndex();

	public abstract List<MemberAttribute> findList();
}

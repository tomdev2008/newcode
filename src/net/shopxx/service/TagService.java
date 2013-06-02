package net.shopxx.service;

import java.util.List;

import net.shopxx.Filter;
import net.shopxx.Order;
import net.shopxx.entity.Tag;
import net.shopxx.entity.Tag.TagType;

public abstract interface TagService extends BaseService<Tag, Long> {
	public abstract List<Tag> findList(TagType paramType);

	public abstract List<Tag> findList(Integer paramInteger,
			List<Filter> paramList, List<Order> paramList1, String paramString);
}

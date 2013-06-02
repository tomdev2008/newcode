package net.shopxx.dao;

import java.util.List;

import net.shopxx.entity.Tag;
import net.shopxx.entity.Tag.TagType;

public abstract interface TagDao extends BaseDao<Tag, Long> {
	public abstract List<Tag> findList(TagType paramType);
}

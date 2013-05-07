package net.shopxx.dao;

import java.util.List;
import net.shopxx.entity.Tag;
import net.shopxx.entity.Tag.Type;

public abstract interface TagDao extends BaseDao<Tag, Long>
{
  public abstract List<Tag> findList(Tag.Type paramType);
}


 * Qualified Name:     net.shopxx.dao.TagDao


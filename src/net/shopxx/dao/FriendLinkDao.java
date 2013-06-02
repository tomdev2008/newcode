package net.shopxx.dao;

import java.util.List;

import net.shopxx.entity.FriendLink;
import net.shopxx.entity.FriendLink.FriendLinkType;

public abstract interface FriendLinkDao extends BaseDao<FriendLink, Long> {
	public abstract List<FriendLink> findList(FriendLinkType paramType);
}

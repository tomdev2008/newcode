package net.shopxx.service;

import java.util.List;

import net.shopxx.Filter;
import net.shopxx.Order;
import net.shopxx.entity.FriendLink;
import net.shopxx.entity.FriendLink.FriendLinkType;

public abstract interface FriendLinkService extends BaseService<FriendLink, Long>
{
  public abstract List<FriendLink> findList(FriendLinkType paramType);

  public abstract List<FriendLink> findList(Integer paramInteger, List<Filter> paramList, List<Order> paramList1, String paramString);
}

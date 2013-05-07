package net.shopxx.dao;

import net.shopxx.Page;
import net.shopxx.Pageable;
import net.shopxx.entity.Member;
import net.shopxx.entity.Receiver;

public abstract interface ReceiverDao extends BaseDao<Receiver, Long>
{
  public abstract Receiver findDefault(Member paramMember);

  public abstract Page<Receiver> findPage(Member paramMember, Pageable paramPageable);
}


 * Qualified Name:     net.shopxx.dao.ReceiverDao


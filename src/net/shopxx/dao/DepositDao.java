package net.shopxx.dao;

import net.shopxx.Page;
import net.shopxx.Pageable;
import net.shopxx.entity.Deposit;
import net.shopxx.entity.Member;

public abstract interface DepositDao extends BaseDao<Deposit, Long>
{
  public abstract Page<Deposit> findPage(Member paramMember, Pageable paramPageable);
}

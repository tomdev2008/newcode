package net.shopxx.dao;

import java.util.Date;
import java.util.List;
import net.shopxx.Page;
import net.shopxx.Pageable;
import net.shopxx.entity.Member;

public abstract interface MemberDao extends BaseDao<Member, Long>
{
  public abstract boolean usernameExists(String paramString);

  public abstract boolean emailExists(String paramString);

  public abstract Member findByUsername(String paramString);

  public abstract List<Member> findListByEmail(String paramString);

  public abstract Page<Member> findPurchasePage(Date paramDate1, Date paramDate2, Pageable paramPageable);
}

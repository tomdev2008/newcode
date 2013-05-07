package net.shopxx.dao;

import java.util.List;
import net.shopxx.entity.Navigation;
import net.shopxx.entity.Navigation.Position;

public abstract interface NavigationDao extends BaseDao<Navigation, Long>
{
  public abstract List<Navigation> findList(Navigation.Position paramPosition);
}


 * Qualified Name:     net.shopxx.dao.NavigationDao


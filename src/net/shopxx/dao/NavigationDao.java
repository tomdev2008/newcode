package net.shopxx.dao;

import java.util.List;

import net.shopxx.entity.Navigation;
import net.shopxx.entity.Navigation.NavigationPosition;

public abstract interface NavigationDao extends BaseDao<Navigation, Long>
{
  public abstract List<Navigation> findList(NavigationPosition paramPosition);
}

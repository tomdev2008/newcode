package net.shopxx.service;

import java.util.List;
import net.shopxx.entity.Area;

public abstract interface AreaService extends BaseService<Area, Long>
{
  public abstract List<Area> findRoots();

  public abstract List<Area> findRoots(Integer paramInteger);
}

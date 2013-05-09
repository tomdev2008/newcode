package net.shopxx.dao.impl;

import net.shopxx.dao.AdDao;
import net.shopxx.entity.Ad;
import org.springframework.stereotype.Repository;

@Repository("adDaoImpl")
public class AdDaoImpl extends BaseDaoImpl<Ad, Long>
  implements AdDao
{
}

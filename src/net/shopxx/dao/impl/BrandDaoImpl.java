package net.shopxx.dao.impl;

import net.shopxx.dao.BrandDao;
import net.shopxx.entity.Brand;
import org.springframework.stereotype.Repository;

@Repository("brandDaoImpl")
public class BrandDaoImpl extends BaseDaoImpl<Brand, Long>
  implements BrandDao
{
}

package net.shopxx.dao.impl;

import net.shopxx.dao.SpecificationValueDao;
import net.shopxx.entity.SpecificationValue;
import org.springframework.stereotype.Repository;

@Repository("specificationValueDaoImpl")
public class SpecificationValueDaoImpl extends
		BaseDaoImpl<SpecificationValue, Long> implements SpecificationValueDao {
}

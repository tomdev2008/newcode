package net.shopxx.service.impl;

import javax.annotation.Resource;

import net.shopxx.dao.SnDao;
import net.shopxx.entity.Sn.SnType;
import net.shopxx.service.SnService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("snServiceImpl")
public class SnServiceImpl implements SnService {

	@Resource(name = "snDaoImpl")
	private SnDao snDao;

	@Transactional
	public String generate(SnType type) {
		return this.snDao.generate(type);
	}
}

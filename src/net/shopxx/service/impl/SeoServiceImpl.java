package net.shopxx.service.impl;

import javax.annotation.Resource;

import net.shopxx.dao.SeoDao;
import net.shopxx.entity.Seo;
import net.shopxx.entity.Seo.SeoType;
import net.shopxx.service.SeoService;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("seoServiceImpl")
public class SeoServiceImpl extends BaseServiceImpl<Seo, Long> implements
		SeoService {

	@Resource(name = "seoDaoImpl")
	private SeoDao seoDao;

	@Resource(name = "seoDaoImpl")
	public void setBaseDao(SeoDao seoDao) {
		super.setBaseDao(seoDao);
	}

	@Transactional(readOnly = true)
	public Seo find(SeoType type) {
		return this.seoDao.find(type);
	}

	@Transactional(readOnly = true)
	@Cacheable({ "seo" })
	public Seo find(SeoType type, String cacheRegion) {
		return this.seoDao.find(type);
	}

	@Transactional
	@CacheEvict(value = { "seo" }, allEntries = true)
	public void save(Seo seo) {
		super.save(seo);
	}

	@Transactional
	@CacheEvict(value = { "seo" }, allEntries = true)
	public Seo update(Seo seo) {
		return (Seo) super.update(seo);
	}

	@Transactional
	@CacheEvict(value = { "seo" }, allEntries = true)
	public Seo update(Seo seo, String[] ignoreProperties) {
		return (Seo) super.update(seo, ignoreProperties);
	}

	@Transactional
	@CacheEvict(value = { "seo" }, allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Transactional
	@CacheEvict(value = { "seo" }, allEntries = true)
	public void delete(Long[] ids) {
		super.delete(ids);
	}

	@Transactional
	@CacheEvict(value = { "seo" }, allEntries = true)
	public void delete(Seo seo) {
		super.delete(seo);
	}
}

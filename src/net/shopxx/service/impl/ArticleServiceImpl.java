package net.shopxx.service.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.shopxx.Filter;
import net.shopxx.Order;
import net.shopxx.Page;
import net.shopxx.Pageable;
import net.shopxx.dao.ArticleDao;
import net.shopxx.entity.Article;
import net.shopxx.entity.ArticleCategory;
import net.shopxx.entity.Tag;
import net.shopxx.service.ArticleService;
import net.shopxx.service.StaticService;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service("articleServiceImpl")
public class ArticleServiceImpl extends BaseServiceImpl<Article, Long>
		implements ArticleService, DisposableBean {
	private long IIIllIlI = System.currentTimeMillis();

	@Resource(name = "ehCacheManager")
	private CacheManager cacheManager;

	@Resource(name = "articleDaoImpl")
	private ArticleDao articleDao;

	@Resource(name = "staticServiceImpl")
	private StaticService staticService;

	@Resource(name = "articleDaoImpl")
	public void setBaseDao(ArticleDao articleDao) {
		super.setBaseDao(articleDao);
	}

	@Transactional(readOnly = true)
	public List<Article> findList(ArticleCategory articleCategory,
			List<Tag> tags, Integer count, List<Filter> filters,
			List<Order> orders) {
		return this.articleDao.findList(articleCategory, tags, count, filters,
				orders);
	}

	@Transactional(readOnly = true)
	@Cacheable({ "article" })
	public List<Article> findList(ArticleCategory articleCategory,
			List<Tag> tags, Integer count, List<Filter> filters,
			List<Order> orders, String cacheRegion) {
		return this.articleDao.findList(articleCategory, tags, count, filters,
				orders);
	}

	@Transactional(readOnly = true)
	public List<Article> findList(ArticleCategory articleCategory,
			Date beginDate, Date endDate, Integer first, Integer count) {
		return this.articleDao.findList(articleCategory, beginDate, endDate,
				first, count);
	}

	@Transactional(readOnly = true)
	public Page<Article> findPage(ArticleCategory articleCategory,
			List<Tag> tags, Pageable pageable) {
		return this.articleDao.findPage(articleCategory, tags, pageable);
	}

	public long viewHits(Long id) {
		Ehcache localEhcache = this.cacheManager.getEhcache("articleHits");
		Element localElement = localEhcache.get(id);
		if (localElement != null) {
			localLong = (Long) localElement.getObjectValue();
		} else {
			Article localArticle = (Article) this.articleDao.find(id);
			if (localArticle == null)
				return 0L;
			localLong = localArticle.getHits();
		}
		Long localLong = Long.valueOf(localLong.longValue() + 1L);
		localEhcache.put(new Element(id, localLong));
		long l = System.currentTimeMillis();
		if (l > this.IIIllIlI + 600000L) {
			this.IIIllIlI = l;
			IIIllIlI();
			localEhcache.removeAll();
		}
		return localLong.longValue();
	}

	public void destroy() {
		IIIllIlI();
	}

	private void IIIllIlI() {
		Ehcache localEhcache = this.cacheManager.getEhcache("articleHits");
		List localList = localEhcache.getKeys();
		Iterator localIterator = localList.iterator();
		while (localIterator.hasNext()) {
			Long localLong = (Long) localIterator.next();
			Article localArticle = (Article) this.articleDao.find(localLong);
			if (localArticle == null)
				continue;
			Element localElement = localEhcache.get(localLong);
			long l = ((Long) localElement.getObjectValue()).longValue();
			localArticle.setHits(Long.valueOf(l));
			this.articleDao.merge(localArticle);
		}
	}

	@Transactional
	@CacheEvict(value = { "article", "articleCategory" }, allEntries = true)
	public void save(Article article) {
		Assert.notNull(article);
		super.save(article);
		this.articleDao.flush();
		this.staticService.build(article);
	}

	@Transactional
	@CacheEvict(value = { "article", "articleCategory" }, allEntries = true)
	public Article update(Article article) {
		Assert.notNull(article);
		Article localArticle = (Article) super.update(article);
		this.articleDao.flush();
		this.staticService.build(localArticle);
		return localArticle;
	}

	@Transactional
	@CacheEvict(value = { "article", "articleCategory" }, allEntries = true)
	public Article update(Article article, String[] ignoreProperties) {
		return (Article) super.update(article, ignoreProperties);
	}

	@Transactional
	@CacheEvict(value = { "article", "articleCategory" }, allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Transactional
	@CacheEvict(value = { "article", "articleCategory" }, allEntries = true)
	public void delete(Long[] ids) {
		super.delete(ids);
	}

	@Transactional
	@CacheEvict(value = { "article", "articleCategory" }, allEntries = true)
	public void delete(Article article) {
		if (article != null)
			this.staticService.delete(article);
		super.delete(article);
	}
}

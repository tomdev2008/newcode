package net.shopxx.service.impl;

import java.util.List;

import javax.annotation.Resource;

import net.shopxx.Filter;
import net.shopxx.Order;
import net.shopxx.Page;
import net.shopxx.Pageable;
import net.shopxx.dao.ProductDao;
import net.shopxx.dao.ReviewDao;
import net.shopxx.entity.Member;
import net.shopxx.entity.Product;
import net.shopxx.entity.Review;
import net.shopxx.entity.Review.ReviewType;
import net.shopxx.service.ReviewService;
import net.shopxx.service.StaticService;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("reviewServiceImpl")
public class ReviewServiceImpl extends BaseServiceImpl<Review, Long> implements
		ReviewService {

	@Resource(name = "reviewDaoImpl")
	private ReviewDao reviewDao;

	@Resource(name = "productDaoImpl")
	private ProductDao productDao;

	@Resource(name = "staticServiceImpl")
	private StaticService staticService;

	@Resource(name = "reviewDaoImpl")
	public void setBaseDao(ReviewDao reviewDao) {
		super.setBaseDao(reviewDao);
	}

	@Transactional(readOnly = true)
	public List<Review> findList(Member member, Product product,
			ReviewType type, Boolean isShow, Integer count,
			List<Filter> filters, List<Order> orders) {
		return this.reviewDao.findList(member, product, type, isShow, count,
				filters, orders);
	}

	@Transactional(readOnly = true)
	@Cacheable({ "review" })
	public List<Review> findList(Member member, Product product,
			ReviewType type, Boolean isShow, Integer count,
			List<Filter> filters, List<Order> orders, String cacheRegion) {
		return this.reviewDao.findList(member, product, type, isShow, count,
				filters, orders);
	}

	@Transactional(readOnly = true)
	public Page<Review> findPage(Member member, Product product,
			ReviewType type, Boolean isShow, Pageable pageable) {
		return this.reviewDao.findPage(member, product, type, isShow, pageable);
	}

	@Transactional(readOnly = true)
	public Long count(Member member, Product product, ReviewType type,
			Boolean isShow) {
		return this.reviewDao.count(member, product, type, isShow);
	}

	@Transactional(readOnly = true)
	public boolean isReviewed(Member member, Product product) {
		return this.reviewDao.isReviewed(member, product);
	}

	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review",
			"consultation" }, allEntries = true)
	public void save(Review review) {
		super.save(review);
		Product localProduct = review.getProduct();
		if (localProduct != null) {
			this.reviewDao.flush();
			long l1 = this.reviewDao.calculateTotalScore(localProduct);
			long l2 = this.reviewDao.calculateScoreCount(localProduct);
			localProduct.setTotalScore(Long.valueOf(l1));
			localProduct.setScoreCount(Long.valueOf(l2));
			this.productDao.merge(localProduct);
			this.reviewDao.flush();
			this.staticService.build(localProduct);
		}
	}

	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review",
			"consultation" }, allEntries = true)
	public Review update(Review review) {
		Review localReview = (Review) super.update(review);
		Product localProduct = localReview.getProduct();
		if (localProduct != null) {
			this.reviewDao.flush();
			long l1 = this.reviewDao.calculateTotalScore(localProduct);
			long l2 = this.reviewDao.calculateScoreCount(localProduct);
			localProduct.setTotalScore(Long.valueOf(l1));
			localProduct.setScoreCount(Long.valueOf(l2));
			this.productDao.merge(localProduct);
			this.reviewDao.flush();
			this.staticService.build(localProduct);
		}
		return localReview;
	}

	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review",
			"consultation" }, allEntries = true)
	public Review update(Review review, String[] ignoreProperties) {
		return (Review) super.update(review, ignoreProperties);
	}

	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review",
			"consultation" }, allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review",
			"consultation" }, allEntries = true)
	public void delete(Long[] ids) {
		super.delete(ids);
	}

	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review",
			"consultation" }, allEntries = true)
	public void delete(Review review) {
		if (review != null) {
			super.delete(review);
			Product localProduct = review.getProduct();
			if (localProduct != null) {
				this.reviewDao.flush();
				long l1 = this.reviewDao.calculateTotalScore(localProduct);
				long l2 = this.reviewDao.calculateScoreCount(localProduct);
				localProduct.setTotalScore(Long.valueOf(l1));
				localProduct.setScoreCount(Long.valueOf(l2));
				this.productDao.merge(localProduct);
				this.reviewDao.flush();
				this.staticService.build(localProduct);
			}
		}
	}
}

package net.shopxx.dao.impl;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import net.shopxx.Filter;
import net.shopxx.Page;
import net.shopxx.Pageable;
import net.shopxx.dao.ArticleDao;
import net.shopxx.entity.Article;
import net.shopxx.entity.ArticleCategory;
import net.shopxx.entity.Tag;
import org.springframework.stereotype.Repository;

@Repository("articleDaoImpl")
public class ArticleDaoImpl extends BaseDaoImpl<Article, Long> implements
		ArticleDao {
	public List<Article> findList(ArticleCategory articleCategory,
			List<Tag> tags, Integer count, List<Filter> filters,
			List<net.shopxx.Order> orders) {
		CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Article> cq = cb.createQuery(Article.class);
		Root<Article> root = cq.from(Article.class);
		cq.select(root);
		Predicate localPredicate = cb.conjunction();
		localPredicate = cb.and(localPredicate,
				cb.equal(root.get("isPublication"), Boolean.valueOf(true)));
		if (articleCategory != null)
			localPredicate = cb.and(localPredicate, cb.or(
					cb.equal(root.get("articleCategory"), articleCategory),
					cb.like(root.get("articleCategory").get("treePath"), "%,"
							+ articleCategory.getId() + "," + "%")));
		if ((tags != null) && (!tags.isEmpty())) {
			localPredicate = cb.and(localPredicate, root.join("tags").in(tags));
			cq.distinct(true);
		}
		cq.where(localPredicate);
		cq.orderBy(new javax.persistence.criteria.Order[] { cb.desc(root
				.get("isTop")) });
		return super.entityManager(cq, null, count, filters, orders);
	}

	public List<Article> findList(ArticleCategory articleCategory,
			Date beginDate, Date endDate, Integer first, Integer count) {
		CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Article> cq = cb.createQuery(Article.class);
		Root<Article> root = cq.from(Article.class);
		cq.select(root);
		Predicate localPredicate = cb.conjunction();
		localPredicate = cb.and(localPredicate,
				cb.equal(root.get("isPublication"), Boolean.valueOf(true)));
		if (articleCategory != null)
			localPredicate = cb.and(localPredicate, cb.or(
					cb.equal(root.get("articleCategory"), articleCategory),
					cb.like(root.get("articleCategory").get("treePath"), "%,"
							+ articleCategory.getId() + "," + "%")));
		if (beginDate != null)
			localPredicate = cb.and(localPredicate,
					cb.greaterThanOrEqualTo(root.get("createDate"), beginDate));
		if (endDate != null)
			localPredicate = cb.and(localPredicate,
					cb.lessThanOrEqualTo(root.get("createDate"), endDate));
		cq.where(localPredicate);
		cq.orderBy(new javax.persistence.criteria.Order[] { cb.desc(root
				.get("isTop")) });
		return super.entityManager(cq, first, count, null, null);
	}

	public Page<Article> findPage(ArticleCategory articleCategory,
			List<Tag> tags, Pageable pageable) {
		CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Article> cq = cb.createQuery(Article.class);
		Root<Article> root = cq.from(Article.class);
		cq.select(root);
		Predicate localPredicate = cb.conjunction();
		localPredicate = cb.and(localPredicate,
				cb.equal(root.get("isPublication"), Boolean.valueOf(true)));
		if (articleCategory != null)
			localPredicate = cb.and(localPredicate, cb.or(
					cb.equal(root.get("articleCategory"), articleCategory),
					cb.like(root.get("articleCategory").get("treePath"), "%,"
							+ articleCategory.getId() + "," + "%")));
		if ((tags != null) && (!tags.isEmpty())) {
			localPredicate = cb.and(localPredicate, root.join("tags").in(tags));
			cq.distinct(true);
		}
		cq.where(localPredicate);
		cq.orderBy(new javax.persistence.criteria.Order[] { cb.desc(root
				.get("isTop")) });
		return super.entityManager(cq, pageable);
	}
}

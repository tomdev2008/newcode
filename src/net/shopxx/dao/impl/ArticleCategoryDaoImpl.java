package net.shopxx.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;
import net.shopxx.dao.ArticleCategoryDao;
import net.shopxx.entity.ArticleCategory;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

@Repository("articleCategoryDaoImpl")
public class ArticleCategoryDaoImpl extends BaseDaoImpl<ArticleCategory, Long>
		implements ArticleCategoryDao {
	public List<ArticleCategory> findRoots(Integer count) {
		String str = "select articleCategory from ArticleCategory articleCategory where articleCategory.parent is null order by articleCategory.order asc";
		TypedQuery typedQuery = this.entityManager.createQuery(str,
				ArticleCategory.class).setFlushMode(FlushModeType.COMMIT);
		if (count != null)
			typedQuery.setMaxResults(count.intValue());
		return typedQuery.getResultList();
	}

	public List<ArticleCategory> findParents(ArticleCategory articleCategory,
			Integer count) {
		if ((articleCategory == null) || (articleCategory.getParent() == null))
			return Collections.emptyList();
		String str = "select articleCategory from ArticleCategory articleCategory where articleCategory.id in (:ids) order by articleCategory.grade asc";
		TypedQuery localTypedQuery = this.entityManager
				.createQuery(str, ArticleCategory.class)
				.setFlushMode(FlushModeType.COMMIT)
				.setParameter("ids", articleCategory.getTreePaths());
		if (count != null)
			localTypedQuery.setMaxResults(count.intValue());
		return localTypedQuery.getResultList();
	}

	public List<ArticleCategory> findChildren(ArticleCategory articleCategory,
			Integer count) {
		String str;
		TypedQuery localTypedQuery;
		if (articleCategory != null) {
			str = "select articleCategory from ArticleCategory articleCategory where articleCategory.treePath like :treePath order by articleCategory.order asc";
			localTypedQuery = this.entityManager
					.createQuery(str, ArticleCategory.class)
					.setFlushMode(FlushModeType.COMMIT)
					.setParameter("treePath",
							"%," + articleCategory.getId() + "," + "%");
		} else {
			str = "select articleCategory from ArticleCategory articleCategory order by articleCategory.order asc";
			localTypedQuery = this.entityManager.createQuery(str,
					ArticleCategory.class).setFlushMode(FlushModeType.COMMIT);
		}
		if (count != null)
			localTypedQuery.setMaxResults(count.intValue());
		return entityManager(localTypedQuery.getResultList(), articleCategory);
	}

	public void persist(ArticleCategory articleCategory) {
		Assert.notNull(articleCategory);
		entityManager(articleCategory);
		super.persist(articleCategory);
	}

	public ArticleCategory merge(ArticleCategory articleCategory) {
		Assert.notNull(articleCategory);
		entityManager(articleCategory);
		Iterator localIterator = findChildren(articleCategory, null).iterator();
		while (localIterator.hasNext()) {
			ArticleCategory localArticleCategory = (ArticleCategory) localIterator
					.next();
			entityManager(localArticleCategory);
		}
		return (ArticleCategory) super.merge(articleCategory);
	}

	private List<ArticleCategory> entityManager(
			List<ArticleCategory> paramList,
			ArticleCategory paramArticleCategory) {
		ArrayList localArrayList = new ArrayList();
		if (paramList != null) {
			Iterator localIterator = paramList.iterator();
			while (localIterator.hasNext()) {
				ArticleCategory localArticleCategory = (ArticleCategory) localIterator
						.next();
				if (localArticleCategory.getParent() != paramArticleCategory)
					continue;
				localArrayList.add(localArticleCategory);
				localArrayList.addAll(entityManager(paramList,
						localArticleCategory));
			}
		}
		return localArrayList;
	}

	private void entityManager(ArticleCategory paramArticleCategory) {
		if (paramArticleCategory == null)
			return;
		ArticleCategory localArticleCategory = paramArticleCategory.getParent();
		if (localArticleCategory != null)
			paramArticleCategory.setTreePath(localArticleCategory.getTreePath()
					+ localArticleCategory.getId() + ",");
		else
			paramArticleCategory.setTreePath(",");
		paramArticleCategory.setGrade(Integer.valueOf(paramArticleCategory
				.getTreePaths().size()));
	}
}

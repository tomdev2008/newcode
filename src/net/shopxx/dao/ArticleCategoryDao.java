package net.shopxx.dao;

import java.util.List;
import net.shopxx.entity.ArticleCategory;

public abstract interface ArticleCategoryDao extends BaseDao<ArticleCategory, Long>
{
  public abstract List<ArticleCategory> findRoots(Integer paramInteger);

  public abstract List<ArticleCategory> findParents(ArticleCategory paramArticleCategory, Integer paramInteger);

  public abstract List<ArticleCategory> findChildren(ArticleCategory paramArticleCategory, Integer paramInteger);
}


 * Qualified Name:     net.shopxx.dao.ArticleCategoryDao


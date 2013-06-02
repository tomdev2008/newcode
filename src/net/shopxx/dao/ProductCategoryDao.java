package net.shopxx.dao;

import java.util.List;
import net.shopxx.entity.ProductCategory;

public abstract interface ProductCategoryDao extends
		BaseDao<ProductCategory, Long> {
	public abstract List<ProductCategory> findRoots(Integer paramInteger);

	public abstract List<ProductCategory> findParents(
			ProductCategory paramProductCategory, Integer paramInteger);

	public abstract List<ProductCategory> findChildren(
			ProductCategory paramProductCategory, Integer paramInteger);
}

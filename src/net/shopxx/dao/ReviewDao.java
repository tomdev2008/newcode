package net.shopxx.dao;

import java.util.List;

import net.shopxx.Filter;
import net.shopxx.Order;
import net.shopxx.Page;
import net.shopxx.Pageable;
import net.shopxx.entity.Member;
import net.shopxx.entity.Product;
import net.shopxx.entity.Review;
import net.shopxx.entity.Review.ReviewType;

public abstract interface ReviewDao extends BaseDao<Review, Long> {
	public abstract List<Review> findList(Member paramMember,
			Product paramProduct, ReviewType paramType, Boolean paramBoolean,
			Integer paramInteger, List<Filter> paramList, List<Order> paramList1);

	public abstract Page<Review> findPage(Member paramMember,
			Product paramProduct, ReviewType paramType, Boolean paramBoolean,
			Pageable paramPageable);

	public abstract Long count(Member paramMember, Product paramProduct,
			ReviewType paramType, Boolean paramBoolean);

	public abstract boolean isReviewed(Member paramMember, Product paramProduct);

	public abstract long calculateTotalScore(Product paramProduct);

	public abstract long calculateScoreCount(Product paramProduct);
}

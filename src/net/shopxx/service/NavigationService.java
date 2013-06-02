package net.shopxx.service;

import java.util.List;

import net.shopxx.Filter;
import net.shopxx.Order;
import net.shopxx.entity.Navigation;
import net.shopxx.entity.Navigation.NavigationPosition;

public abstract interface NavigationService extends
		BaseService<Navigation, Long> {
	public abstract List<Navigation> findList(NavigationPosition paramPosition);

	public abstract List<Navigation> findList(Integer paramInteger,
			List<Filter> paramList, List<Order> paramList1, String paramString);
}

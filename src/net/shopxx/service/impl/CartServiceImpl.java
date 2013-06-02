package net.shopxx.service.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import net.shopxx.Principal;
import net.shopxx.dao.CartDao;
import net.shopxx.dao.CartItemDao;
import net.shopxx.dao.MemberDao;
import net.shopxx.entity.Cart;
import net.shopxx.entity.CartItem;
import net.shopxx.entity.Member;
import net.shopxx.entity.Product;
import net.shopxx.service.CartService;
import net.shopxx.util.CookieUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service("cartServiceImpl")
public class CartServiceImpl extends BaseServiceImpl<Cart, Long> implements
		CartService {

	@Resource(name = "cartDaoImpl")
	private CartDao cartDao;

	@Resource(name = "cartItemDaoImpl")
	private CartItemDao cartItemDao;

	@Resource(name = "memberDaoImpl")
	private MemberDao memberDao;

	@Resource(name = "cartDaoImpl")
	public void setBaseDao(CartDao cartDao) {
		super.setBaseDao(cartDao);
	}

	public Cart getCurrent() {
		RequestAttributes localRequestAttributes = RequestContextHolder
				.currentRequestAttributes();
		if (localRequestAttributes != null) {
			HttpServletRequest localHttpServletRequest = ((ServletRequestAttributes) localRequestAttributes)
					.getRequest();
			Principal localPrincipal = (Principal) localHttpServletRequest
					.getSession().getAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME);
			Object localObject1 = localPrincipal != null ? (Member) this.memberDao
					.find(localPrincipal.getId()) : null;
			Object localObject2;
			if (localObject1 != null) {
				localObject2 = localObject1.getCart();
				if (localObject2 != null) {
					if (!((Cart) localObject2).hasExpired()) {
						if (!DateUtils.isSameDay(
								((Cart) localObject2).getModifyDate(),
								new Date())) {
							((Cart) localObject2).setModifyDate(new Date());
							this.cartDao.merge(localObject2);
						}
						return localObject2;
					}
					this.cartDao.remove(localObject2);
				}
			} else {
				localObject2 = CookieUtils.getCookie(localHttpServletRequest,
						"cartId");
				String str = CookieUtils.getCookie(localHttpServletRequest,
						"cartKey");
				if ((StringUtils.isNotEmpty((String) localObject2))
						&& (StringUtils.isNumeric((String) localObject2))
						&& (StringUtils.isNotEmpty(str))) {
					Cart localCart = (Cart) this.cartDao.find(Long
							.valueOf((String) localObject2));
					if ((localCart != null) && (localCart.getMember() == null)
							&& (StringUtils.equals(localCart.getKey(), str))) {
						if (!localCart.hasExpired()) {
							if (!DateUtils.isSameDay(localCart.getModifyDate(),
									new Date())) {
								localCart.setModifyDate(new Date());
								this.cartDao.merge(localCart);
							}
							return localCart;
						}
						this.cartDao.remove(localCart);
					}
				}
			}
		}
		return (Cart) null;
	}

	public void merge(Member member, Cart cart) {
		if ((member != null) && (cart != null) && (cart.getMember() == null)) {
			Cart localCart = member.getCart();
			if (localCart != null) {
				Iterator localIterator = cart.getCartItems().iterator();
				while (localIterator.hasNext()) {
					CartItem localCartItem1 = (CartItem) localIterator.next();
					Product localProduct = localCartItem1.getProduct();
					if (localCart.contains(localProduct)) {
						if ((Cart.MAX_PRODUCT_COUNT != null)
								&& (localCart.getCartItems().size() > Cart.MAX_PRODUCT_COUNT
										.intValue()))
							continue;
						CartItem localCartItem2 = localCart
								.getCartItem(localProduct);
						localCartItem2.add(localCartItem1.getQuantity()
								.intValue());
						this.cartItemDao.merge(localCartItem2);
					} else {
						if ((Cart.MAX_PRODUCT_COUNT != null)
								&& (localCart.getCartItems().size() >= Cart.MAX_PRODUCT_COUNT
										.intValue()))
							continue;
						localIterator.remove();
						localCartItem1.setCart(localCart);
						localCart.getCartItems().add(localCartItem1);
						this.cartItemDao.merge(localCartItem1);
					}
				}
				this.cartDao.remove(cart);
			} else {
				member.setCart(cart);
				cart.setMember(member);
				this.cartDao.merge(cart);
			}
		}
	}

	public void evictExpired() {
		this.cartDao.evictExpired();
	}
}

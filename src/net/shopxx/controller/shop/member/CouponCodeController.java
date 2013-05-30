package net.shopxx.controller.shop.member;

import javax.annotation.Resource;
import net.shopxx.Message;
import net.shopxx.Pageable;
import net.shopxx.controller.shop.BaseController;
import net.shopxx.entity.Coupon;
import net.shopxx.entity.Member;
import net.shopxx.service.CouponCodeService;
import net.shopxx.service.CouponService;
import net.shopxx.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("shopMemberCouponCodeController")
@RequestMapping({ "/member/coupon_code" })
public class CouponCodeController extends BaseController {
	private static final int IIIlllIl = 10;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "couponServiceImpl")
	private CouponService couponService;

	@Resource(name = "couponCodeServiceImpl")
	private CouponCodeService couponCodeService;

	@RequestMapping(value = { "/exchange" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String exchange(Integer pageNumber, ModelMap model) {
		Pageable localPageable = new Pageable(pageNumber, Integer.valueOf(10));
		model.addAttribute("page", this.couponService.findPage(
				Boolean.valueOf(true), Boolean.valueOf(true),
				Boolean.valueOf(false), localPageable));
		return "shop/member/coupon_code/exchange";
	}

	@RequestMapping(value = { "/exchange" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Message exchange(Long id) {
		Coupon localCoupon = (Coupon) this.couponService.find(id);
		if ((localCoupon == null)
				|| (!localCoupon.getIsEnabled().booleanValue())
				|| (!localCoupon.getIsExchange().booleanValue())
				|| (localCoupon.hasExpired()))
			return IIIllIll;
		Member localMember = this.memberService.getCurrent();
		if (localMember.getPoint().longValue() < localCoupon.getPoint()
				.intValue())
			return Message.warn("shop.member.couponCode.point", new Object[0]);
		this.couponCodeService.exchange(localCoupon, localMember);
		return IIIlllII;
	}

	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String list(Integer pageNumber, ModelMap model) {
		Member localMember = this.memberService.getCurrent();
		Pageable localPageable = new Pageable(pageNumber, Integer.valueOf(10));
		model.addAttribute("page",
				this.couponCodeService.findPage(localMember, localPageable));
		return "shop/member/coupon_code/list";
	}
}

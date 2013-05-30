package net.shopxx.controller.shop.member;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.shopxx.Pageable;
import net.shopxx.Setting;
import net.shopxx.controller.shop.BaseController;
import net.shopxx.entity.Member;
import net.shopxx.entity.Payment;
import net.shopxx.entity.Payment.PaymentStatus;
import net.shopxx.entity.Payment.PaymentType;
import net.shopxx.entity.Sn.SnType;
import net.shopxx.plugin.PaymentPlugin;
import net.shopxx.service.DepositService;
import net.shopxx.service.MemberService;
import net.shopxx.service.PaymentService;
import net.shopxx.service.PluginService;
import net.shopxx.service.SnService;
import net.shopxx.util.SettingUtils;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("shopMemberDepositController")
@RequestMapping({ "/member/deposit" })
public class DepositController extends BaseController {
	private static final int IIIlllIl = 10;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "depositServiceImpl")
	private DepositService depositService;

	@Resource(name = "pluginServiceImpl")
	private PluginService pluginService;

	@Resource(name = "paymentServiceImpl")
	private PaymentService paymentService;

	@Resource(name = "snServiceImpl")
	private SnService snService;

	@RequestMapping(value = { "/recharge" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String recharge(ModelMap model) {
		List localList = this.pluginService.getPaymentPlugins(true);
		if (!localList.isEmpty()) {
			model.addAttribute("defaultPaymentPlugin", localList.get(0));
			model.addAttribute("paymentPlugins", localList);
		}
		return "shop/member/deposit/recharge";
	}

	@RequestMapping(value = { "/recharge" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	public String recharge(BigDecimal amount, String paymentPluginId,
			HttpServletRequest request, ModelMap model) {
		PaymentPlugin localPaymentPlugin = this.pluginService
				.getPaymentPlugin(paymentPluginId);
		if ((localPaymentPlugin == null)
				|| (!localPaymentPlugin.getIsEnabled()))
			return "/shop/common/error";
		Setting localSetting = SettingUtils.get();
		if ((amount == null) || (amount.compareTo(new BigDecimal(0)) <= 0)
				|| (amount.precision() > 15)
				|| (amount.scale() > localSetting.getPriceScale().intValue()))
			return "/shop/common/error";
		BigDecimal localBigDecimal = localPaymentPlugin.getFee(amount);
		amount = amount.add(localBigDecimal);
		Payment localPayment = new Payment();
		localPayment.setSn(this.snService.generate(SnType.payment));
		localPayment.setType(PaymentType.online);
		localPayment.setStatus(PaymentStatus.wait);
		localPayment.setPaymentMethod(localPaymentPlugin.getPaymentName());
		localPayment.setFee(localBigDecimal);
		localPayment.setAmount(amount);
		localPayment.setPaymentPluginId(paymentPluginId);
		localPayment
				.setExpire(localPaymentPlugin.getTimeout() != null ? DateUtils
						.addMinutes(new Date(), localPaymentPlugin.getTimeout()
								.intValue()) : null);
		localPayment.setMember(this.memberService.getCurrent());
		this.paymentService.save(localPayment);
		model.addAttribute("url", localPaymentPlugin.getUrl());
		model.addAttribute("method", localPaymentPlugin.getMethod());
		model.addAttribute("parameterMap", localPaymentPlugin.getParameterMap(
				localPayment.getSn(), amount,
				IIIllIlI("shop.member.deposit.recharge", new Object[0]),
				request));
		return "shop/payment/submit";
	}

	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String list(Integer pageNumber, ModelMap model) {
		Member localMember = this.memberService.getCurrent();
		Pageable localPageable = new Pageable(pageNumber, Integer.valueOf(10));
		model.addAttribute("page",
				this.depositService.findPage(localMember, localPageable));
		return "shop/member/deposit/list";
	}
}

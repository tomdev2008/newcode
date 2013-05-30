package net.shopxx.controller.shop.member;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.shopxx.Message;
import net.shopxx.Pageable;
import net.shopxx.Setting;
import net.shopxx.controller.shop.BaseController;
import net.shopxx.entity.Area;
import net.shopxx.entity.Cart;
import net.shopxx.entity.Coupon;
import net.shopxx.entity.CouponCode;
import net.shopxx.entity.Member;
import net.shopxx.entity.Order;
import net.shopxx.entity.Order.OrderPaymentStatus;
import net.shopxx.entity.PaymentMethod;
import net.shopxx.entity.PaymentMethod.PaymentMethodType;
import net.shopxx.entity.Receiver;
import net.shopxx.entity.Shipping;
import net.shopxx.entity.ShippingMethod;
import net.shopxx.plugin.PaymentPlugin;
import net.shopxx.service.AreaService;
import net.shopxx.service.CartService;
import net.shopxx.service.CouponCodeService;
import net.shopxx.service.MemberService;
import net.shopxx.service.OrderService;
import net.shopxx.service.PaymentMethodService;
import net.shopxx.service.PluginService;
import net.shopxx.service.ReceiverService;
import net.shopxx.service.ShippingMethodService;
import net.shopxx.service.ShippingService;
import net.shopxx.util.SettingUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("shopMemberOrderController")
@RequestMapping({ "/member/order" })
public class OrderController extends BaseController {
	private static final int IIIlllIl = 10;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "receiverServiceImpl")
	private ReceiverService receiverService;

	@Resource(name = "cartServiceImpl")
	private CartService cartService;

	@Resource(name = "paymentMethodServiceImpl")
	private PaymentMethodService paymentMethodService;

	@Resource(name = "shippingMethodServiceImpl")
	private ShippingMethodService shippingMethodService;

	@Resource(name = "couponCodeServiceImpl")
	private CouponCodeService couponCodeService;

	@Resource(name = "orderServiceImpl")
	private OrderService orderService;

	@Resource(name = "shippingServiceImpl")
	private ShippingService shippingService;

	@Resource(name = "pluginServiceImpl")
	private PluginService pluginService;

	@RequestMapping(value = { "/save_receiver" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Map<String, Object> saveReceiver(Receiver receiver, Long areaId) {
		HashMap localHashMap = new HashMap();
		receiver.setArea((Area) this.areaService.find(areaId));
		if (!IIIllIlI(receiver, new Class[0])) {
			localHashMap.put("message", IIIllIll);
			return localHashMap;
		}
		Member localMember = this.memberService.getCurrent();
		if ((Receiver.MAX_RECEIVER_COUNT != null)
				&& (localMember.getReceivers().size() >= Receiver.MAX_RECEIVER_COUNT
						.intValue())) {
			localHashMap.put("message", Message.error(
					"shop.order.addReceiverCountNotAllowed",
					new Object[] { Receiver.MAX_RECEIVER_COUNT }));
			return localHashMap;
		}
		receiver.setMember(localMember);
		this.receiverService.save(receiver);
		localHashMap.put("message", IIIlllII);
		localHashMap.put("receiver", receiver);
		return localHashMap;
	}

	@RequestMapping(value = { "/check_lock" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Message checkLock(String sn) {
		Order localOrder = this.orderService.findBySn(sn);
		if ((localOrder != null)
				&& (localOrder.getMember() == this.memberService.getCurrent())
				&& (!localOrder.isExpired())
				&& (localOrder.getPaymentMethod() != null)
				&& (localOrder.getPaymentMethod().getType() == PaymentMethodType.online)
				&& ((localOrder.getPaymentStatus() == OrderPaymentStatus.unpaid) || (localOrder
						.getPaymentStatus() == OrderPaymentStatus.partialPayment))) {
			if (localOrder.isLocked(null))
				return Message.warn("shop.order.locked", new Object[0]);
			localOrder.setLockExpire(DateUtils.addSeconds(new Date(), 60));
			localOrder.setOperator(null);
			this.orderService.update(localOrder);
			return IIIlllII;
		}
		return IIIllIll;
	}

	@RequestMapping(value = { "/check_payment" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public boolean checkPayment(String sn) {
		Order localOrder = this.orderService.findBySn(sn);
		return (localOrder != null)
				&& (localOrder.getMember() == this.memberService.getCurrent())
				&& (localOrder.getPaymentStatus() == OrderPaymentStatus.paid);
	}

	@RequestMapping(value = { "/coupon_info" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Map<String, Object> couponInfo(String code) {
		HashMap localHashMap = new HashMap();
		Cart localCart = this.cartService.getCurrent();
		if ((localCart == null) || (localCart.isEmpty())) {
			localHashMap.put("message",
					Message.warn("shop.order.cartNotEmpty", new Object[0]));
			return localHashMap;
		}
		if (!localCart.isCouponAllowed()) {
			localHashMap.put("message",
					Message.warn("shop.order.couponNotAllowed", new Object[0]));
			return localHashMap;
		}
		CouponCode localCouponCode = this.couponCodeService.findByCode(code);
		if ((localCouponCode != null) && (localCouponCode.getCoupon() != null)) {
			Coupon localCoupon = localCouponCode.getCoupon();
			if (!localCoupon.getIsEnabled().booleanValue()) {
				localHashMap.put("message", Message.warn(
						"shop.order.couponDisabled", new Object[0]));
				return localHashMap;
			}
			if (!localCoupon.hasBegun()) {
				localHashMap.put("message", Message.warn(
						"shop.order.couponNotBegin", new Object[0]));
				return localHashMap;
			}
			if (localCoupon.hasExpired()) {
				localHashMap.put("message", Message.warn(
						"shop.order.couponHasExpired", new Object[0]));
				return localHashMap;
			}
			if (!localCart.isValid(localCoupon)) {
				localHashMap
						.put("message", Message.warn(
								"shop.order.couponInvalid", new Object[0]));
				return localHashMap;
			}
			if (localCouponCode.getIsUsed().booleanValue()) {
				localHashMap.put("message", Message.warn(
						"shop.order.couponCodeUsed", new Object[0]));
				return localHashMap;
			}
			localHashMap.put("message", IIIlllII);
			localHashMap.put("couponName", localCoupon.getName());
			return localHashMap;
		}
		localHashMap.put("message",
				Message.warn("shop.order.couponCodeNotExist", new Object[0]));
		return localHashMap;
	}

	@RequestMapping(value = { "/info" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String info(ModelMap model) {
		Cart localCart = this.cartService.getCurrent();
		if ((localCart == null) || (localCart.isEmpty()))
			return "redirect:/cart/list.jhtml";
		if (!IIIllIlI(localCart, new Class[0]))
			return "/shop/common/error";
		Order localOrder = this.orderService.build(localCart, null, null, null,
				null, false, null, false, null);
		model.addAttribute("order", localOrder);
		model.addAttribute("cartToken", localCart.getToken());
		model.addAttribute("paymentMethods", this.paymentMethodService.findAll());
		model.addAttribute("shippingMethods", this.shippingMethodService.findAll());
		return "/shop/member/order/info";
	}

	@RequestMapping(value = { "/calculate" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Map<String, Object> calculate(Long paymentMethodId,
			Long shippingMethodId, String code,
			@RequestParam(defaultValue = "false") Boolean isInvoice,
			String invoiceTitle,
			@RequestParam(defaultValue = "false") Boolean useBalance,
			String memo) {
		HashMap localHashMap = new HashMap();
		Cart localCart = this.cartService.getCurrent();
		if ((localCart == null) || (localCart.isEmpty())) {
			localHashMap.put("message",
					Message.error("shop.order.cartNotEmpty", new Object[0]));
			return localHashMap;
		}
		PaymentMethod localPaymentMethod = (PaymentMethod) this.paymentMethodService
				.find(paymentMethodId);
		ShippingMethod localShippingMethod = (ShippingMethod) this.shippingMethodService
				.find(shippingMethodId);
		CouponCode localCouponCode = this.couponCodeService.findByCode(code);
		Order localOrder = this.orderService.build(localCart, null,
				localPaymentMethod, localShippingMethod, localCouponCode,
				isInvoice.booleanValue(), invoiceTitle,
				useBalance.booleanValue(), memo);
		localHashMap.put("message", IIIlllII);
		localHashMap.put("quantity", Integer.valueOf(localOrder.getQuantity()));
		localHashMap.put("price", localOrder.getPrice());
		localHashMap.put("freight", localOrder.getFreight());
		localHashMap.put("tax", localOrder.getTax());
		localHashMap.put("amountPayable", localOrder.getAmountPayable());
		return localHashMap;
	}

	@RequestMapping(value = { "/create" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Message create(String cartToken, Long receiverId,
			Long paymentMethodId, Long shippingMethodId, String code,
			@RequestParam(defaultValue = "false") Boolean isInvoice,
			String invoiceTitle,
			@RequestParam(defaultValue = "false") Boolean useBalance,
			String memo) {
		Cart localCart = this.cartService.getCurrent();
		if ((localCart == null) || (localCart.isEmpty()))
			return Message.warn("shop.order.cartNotEmpty", new Object[0]);
		if (!StringUtils.equals(localCart.getToken(), cartToken))
			return Message.warn("shop.order.cartHasChanged", new Object[0]);
		if (localCart.getIsLowStock())
			return Message.warn("shop.order.cartLowStock", new Object[0]);
		Receiver localReceiver = (Receiver) this.receiverService.find(receiverId);
		if (localReceiver == null)
			return Message.error("shop.order.receiverNotExsit", new Object[0]);
		PaymentMethod localPaymentMethod = (PaymentMethod) this.paymentMethodService
				.find(paymentMethodId);
		if (localPaymentMethod == null)
			return Message.error("shop.order.paymentMethodNotExsit",
					new Object[0]);
		ShippingMethod localShippingMethod = (ShippingMethod) this.shippingMethodService
				.find(shippingMethodId);
		if (localShippingMethod == null)
			return Message.error("shop.order.shippingMethodNotExsit",
					new Object[0]);
		if (!localPaymentMethod.getShippingMethods().contains(
				localShippingMethod))
			return Message.error("shop.order.deliveryUnsupported",
					new Object[0]);
		CouponCode localCouponCode = this.couponCodeService.findByCode(code);
		Order localOrder = this.orderService.create(localCart, localReceiver,
				localPaymentMethod, localShippingMethod, localCouponCode,
				isInvoice.booleanValue(), invoiceTitle,
				useBalance.booleanValue(), memo, null);
		return Message.success(localOrder.getSn(), new Object[0]);
	}

	@RequestMapping(value = { "/payment" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String payment(String sn, ModelMap model) {
		Order localOrder = this.orderService.findBySn(sn);
		if ((localOrder == null)
				|| (localOrder.getMember() != this.memberService.getCurrent())
				|| (localOrder.isExpired())
				|| (localOrder.getPaymentMethod() == null))
			return "/shop/common/error";
		if (localOrder.getPaymentMethod().getType() == PaymentMethodType.online) {
			List localList = this.pluginService.getPaymentPlugins(true);
			if (!localList.isEmpty()) {
				PaymentPlugin localPaymentPlugin = (PaymentPlugin) localList
						.get(0);
				localOrder.setFee(localPaymentPlugin.getFee(localOrder
						.getAmountPayable()));
				model.addAttribute("defaultPaymentPlugin", localPaymentPlugin);
				model.addAttribute("paymentPlugins", localList);
			}
		}
		model.addAttribute("order", localOrder);
		return "/shop/member/order/payment";
	}

	@RequestMapping(value = { "/payment_plugin_select" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Map<String, Object> paymentPluginSelect(String sn,
			String paymentPluginId) {
		HashMap localHashMap = new HashMap();
		Order localOrder = this.orderService.findBySn(sn);
		PaymentPlugin localPaymentPlugin = this.pluginService
				.getPaymentPlugin(paymentPluginId);
		if ((localOrder == null)
				|| (localOrder.getMember() != this.memberService.getCurrent())
				|| (localOrder.isExpired())
				|| (localOrder.isLocked(null))
				|| (localOrder.getPaymentMethod() == null)
				|| (localOrder.getPaymentMethod().getType() == PaymentMethodType.offline)
				|| (localPaymentPlugin == null)
				|| (!localPaymentPlugin.getIsEnabled())) {
			localHashMap.put("message", IIIllIll);
			return localHashMap;
		}
		localOrder.setFee(localPaymentPlugin.getFee(localOrder
				.getAmountPayable()));
		localHashMap.put("message", IIIlllII);
		localHashMap.put("fee", localOrder.getFee());
		localHashMap.put("amountPayable", localOrder.getAmountPayable());
		return localHashMap;
	}

	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String list(Integer pageNumber, ModelMap model) {
		Member localMember = this.memberService.getCurrent();
		Pageable localPageable = new Pageable(pageNumber, Integer.valueOf(10));
		model.addAttribute("page",
				this.orderService.findPage(localMember, localPageable));
		return "shop/member/order/list";
	}

	@RequestMapping(value = { "/view" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String view(String sn, ModelMap model) {
		Order localOrder = this.orderService.findBySn(sn);
		if (localOrder == null)
			return "/shop/common/error";
		Member localMember = this.memberService.getCurrent();
		if (!localMember.getOrders().contains(localOrder))
			return "/shop/common/error";
		model.addAttribute("order", localOrder);
		return "shop/member/order/view";
	}

	@RequestMapping(value = { "/cancel" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Message cancel(String sn) {
		Order localOrder = this.orderService.findBySn(sn);
		if ((localOrder != null)
				&& (localOrder.getMember() == this.memberService.getCurrent())
				&& (!localOrder.isExpired())
				&& (localOrder.getOrderStatus() == Order.OrderStatus.unconfirmed)
				&& (localOrder.getPaymentStatus() == OrderPaymentStatus.unpaid)) {
			if (localOrder.isLocked(null))
				return Message.warn("shop.member.order.locked", new Object[0]);
			this.orderService.cancel(localOrder, null);
			return IIIlllII;
		}
		return IIIllIll;
	}

	@RequestMapping(value = { "/delivery_query" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Map<String, Object> deliveryQuery(String sn) {
		Object localObject = new HashMap();
		Shipping localShipping = this.shippingService.findBySn(sn);
		Setting localSetting = SettingUtils.get();
		if ((localShipping != null)
				&& (localShipping.getOrder() != null)
				&& (localShipping.getOrder().getMember() == this.memberService
						.getCurrent())
				&& (StringUtils.isNotEmpty(localSetting.getKuaidi100Key()))
				&& (StringUtils.isNotEmpty(localShipping.getDeliveryCorpCode()))
				&& (StringUtils.isNotEmpty(localShipping.getTrackingNo())))
			localObject = this.shippingService.query(localShipping);
		return (Map<String, Object>) localObject;
	}
}

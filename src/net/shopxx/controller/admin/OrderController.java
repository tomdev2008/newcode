package net.shopxx.controller.admin;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;

import net.shopxx.Message;
import net.shopxx.Pageable;
import net.shopxx.entity.Admin;
import net.shopxx.entity.Area;
import net.shopxx.entity.DeliveryCorp;
import net.shopxx.entity.Member;
import net.shopxx.entity.Order;
import net.shopxx.entity.Order.OrderOrderStatus;
import net.shopxx.entity.Order.OrderPaymentStatus;
import net.shopxx.entity.Order.OrderShippingStatus;
import net.shopxx.entity.OrderItem;
import net.shopxx.entity.Payment;
import net.shopxx.entity.Payment.PaymentStatus;
import net.shopxx.entity.Payment.PaymentType;
import net.shopxx.entity.PaymentMethod;
import net.shopxx.entity.Product;
import net.shopxx.entity.Refunds;
import net.shopxx.entity.Refunds.RefundsType;
import net.shopxx.entity.Returns;
import net.shopxx.entity.ReturnsItem;
import net.shopxx.entity.Shipping;
import net.shopxx.entity.ShippingItem;
import net.shopxx.entity.ShippingMethod;
import net.shopxx.entity.Sn.SnType;
import net.shopxx.service.AdminService;
import net.shopxx.service.AreaService;
import net.shopxx.service.DeliveryCorpService;
import net.shopxx.service.OrderItemService;
import net.shopxx.service.OrderService;
import net.shopxx.service.PaymentMethodService;
import net.shopxx.service.ProductService;
import net.shopxx.service.ShippingMethodService;
import net.shopxx.service.SnService;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("adminOrderController")
@RequestMapping({ "/admin/order" })
public class OrderController extends BaseController {

	@Resource(name = "adminServiceImpl")
	private AdminService adminService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "productServiceImpl")
	private ProductService productService;

	@Resource(name = "orderServiceImpl")
	private OrderService orderService;

	@Resource(name = "orderItemServiceImpl")
	private OrderItemService orderItemService;

	@Resource(name = "shippingMethodServiceImpl")
	private ShippingMethodService shippingMethodService;

	@Resource(name = "deliveryCorpServiceImpl")
	private DeliveryCorpService deliveryCorpService;

	@Resource(name = "paymentMethodServiceImpl")
	private PaymentMethodService paymentMethodService;

	@Resource(name = "snServiceImpl")
	private SnService snService;

	@RequestMapping(value = { "/check_lock" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Message checkLock(Long id) {
		Order localOrder = (Order) this.orderService.find(id);
		if (localOrder == null)
			return Message.warn("admin.common.invalid", new Object[0]);
		Admin localAdmin = this.adminService.getCurrent();
		if (localOrder.isLocked(localAdmin)) {
			if (localOrder.getOperator() != null)
				return Message
						.warn("admin.order.adminLocked",
								new Object[] { localOrder.getOperator()
										.getUsername() });
			return Message.warn("admin.order.memberLocked", new Object[0]);
		}
		localOrder.setLockExpire(DateUtils.addSeconds(new Date(), 60));
		localOrder.setOperator(localAdmin);
		this.orderService.update(localOrder);
		return IIIlllII;
	}

	@RequestMapping(value = { "/view" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String view(Long id, ModelMap model) {
		model.addAttribute("types", PaymentType.values());
		model.addAttribute("refundsTypes", RefundsType.values());
		model.addAttribute("paymentMethods",
				this.paymentMethodService.findAll());
		model.addAttribute("shippingMethods",
				this.shippingMethodService.findAll());
		model.addAttribute("deliveryCorps", this.deliveryCorpService.findAll());
		model.addAttribute("order", this.orderService.find(id));
		return "/admin/order/view";
	}

	@RequestMapping(value = { "/confirm" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	public String confirm(Long id, RedirectAttributes redirectAttributes) {
		Order localOrder = (Order) this.orderService.find(id);
		Admin localAdmin = this.adminService.getCurrent();
		if ((localOrder != null)
				&& (!localOrder.isExpired())
				&& (localOrder.getOrderStatus() == OrderOrderStatus.unconfirmed)
				&& (!localOrder.isLocked(localAdmin))) {
			this.orderService.confirm(localOrder, localAdmin);
			IIIllIlI(redirectAttributes, IIIlllII);
		} else {
			IIIllIlI(redirectAttributes,
					Message.warn("admin.common.invalid", new Object[0]));
		}
		return "redirect:view.jhtml?id=" + id;
	}

	@RequestMapping(value = { "/complete" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	public String complete(Long id, RedirectAttributes redirectAttributes) {
		Order localOrder = (Order) this.orderService.find(id);
		Admin localAdmin = this.adminService.getCurrent();
		if ((localOrder != null) && (!localOrder.isExpired())
				&& (localOrder.getOrderStatus() == OrderOrderStatus.confirmed)
				&& (!localOrder.isLocked(localAdmin))) {
			this.orderService.complete(localOrder, localAdmin);
			IIIllIlI(redirectAttributes, IIIlllII);
		} else {
			IIIllIlI(redirectAttributes,
					Message.warn("admin.common.invalid", new Object[0]));
		}
		return "redirect:view.jhtml?id=" + id;
	}

	@RequestMapping(value = { "/cancel" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	public String cancel(Long id, RedirectAttributes redirectAttributes) {
		Order localOrder = (Order) this.orderService.find(id);
		Admin localAdmin = this.adminService.getCurrent();
		if ((localOrder != null)
				&& (!localOrder.isExpired())
				&& (localOrder.getOrderStatus() == OrderOrderStatus.unconfirmed)
				&& (!localOrder.isLocked(localAdmin))) {
			this.orderService.cancel(localOrder, localAdmin);
			IIIllIlI(redirectAttributes, IIIlllII);
		} else {
			IIIllIlI(redirectAttributes,
					Message.warn("admin.common.invalid", new Object[0]));
		}
		return "redirect:view.jhtml?id=" + id;
	}

	@RequestMapping(value = { "/payment" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	public String payment(Long orderId, Long paymentMethodId, Payment payment,
			RedirectAttributes redirectAttributes) {
		Order localOrder = (Order) this.orderService.find(orderId);
		payment.setOrder(localOrder);
		PaymentMethod localPaymentMethod = (PaymentMethod) this.paymentMethodService
				.find(paymentMethodId);
		payment.setPaymentMethod(localPaymentMethod != null ? localPaymentMethod
				.getName() : null);
		if (!IIIllIlI(payment, new Class[0]))
			return "/admin/common/error";
		if ((localOrder.isExpired())
				|| (localOrder.getOrderStatus() != OrderOrderStatus.confirmed))
			return "/admin/common/error";
		if ((localOrder.getPaymentStatus() != OrderPaymentStatus.unpaid)
				&& (localOrder.getPaymentStatus() != OrderPaymentStatus.partialPayment))
			return "/admin/common/error";
		if ((payment.getAmount().compareTo(new BigDecimal(0)) <= 0)
				|| (payment.getAmount()
						.compareTo(localOrder.getAmountPayable()) > 0))
			return "/admin/common/error";
		Member localMember = localOrder.getMember();
		if ((payment.getType() == PaymentType.deposit)
				&& (payment.getAmount().compareTo(localMember.getBalance()) > 0))
			return "/admin/common/error";
		Admin localAdmin = this.adminService.getCurrent();
		if (localOrder.isLocked(localAdmin))
			return "/admin/common/error";
		payment.setSn(this.snService.generate(SnType.payment));
		payment.setStatus(PaymentStatus.success);
		payment.setFee(new BigDecimal(0));
		payment.setOperator(localAdmin.getUsername());
		payment.setPaymentDate(new Date());
		payment.setPaymentPluginId(null);
		payment.setExpire(null);
		payment.setDeposit(null);
		payment.setMember(null);
		this.orderService.payment(localOrder, payment, localAdmin);
		IIIllIlI(redirectAttributes, IIIlllII);
		return "redirect:view.jhtml?id=" + orderId;
	}

	@RequestMapping(value = { "/refunds" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	public String refunds(Long orderId, Long paymentMethodId, Refunds refunds,
			RedirectAttributes redirectAttributes) {
		Order localOrder = (Order) this.orderService.find(orderId);
		refunds.setOrder(localOrder);
		PaymentMethod localPaymentMethod = (PaymentMethod) this.paymentMethodService
				.find(paymentMethodId);
		refunds.setPaymentMethod(localPaymentMethod != null ? localPaymentMethod
				.getName() : null);
		if (!IIIllIlI(refunds, new Class[0]))
			return "/admin/common/error";
		if ((localOrder.isExpired())
				|| (localOrder.getOrderStatus() != OrderOrderStatus.confirmed))
			return "/admin/common/error";
		if ((localOrder.getPaymentStatus() != OrderPaymentStatus.paid)
				&& (localOrder.getPaymentStatus() != OrderPaymentStatus.partialPayment)
				&& (localOrder.getPaymentStatus() != OrderPaymentStatus.partialRefunds))
			return "/admin/common/error";
		if ((refunds.getAmount().compareTo(new BigDecimal(0)) <= 0)
				|| (refunds.getAmount().compareTo(localOrder.getAmountPaid()) > 0))
			return "/admin/common/error";
		Admin localAdmin = this.adminService.getCurrent();
		if (localOrder.isLocked(localAdmin))
			return "/admin/common/error";
		refunds.setSn(this.snService.generate(SnType.refunds));
		refunds.setOperator(localAdmin.getUsername());
		this.orderService.refunds(localOrder, refunds, localAdmin);
		IIIllIlI(redirectAttributes, IIIlllII);
		return "redirect:view.jhtml?id=" + orderId;
	}

	@RequestMapping(value = { "/shipping" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	public String shipping(Long orderId, Long shippingMethodId,
			Long deliveryCorpId, Long areaId, Shipping shipping,
			RedirectAttributes redirectAttributes) {
		Object localObject2 = null;
		Object localObject3 = null;
		Order localOrder = (Order) this.orderService.find(orderId);
		if (localOrder == null)
			return "/admin/common/error";
		Object localObject1 = shipping.getShippingItems().iterator();
		while (((Iterator) localObject1).hasNext()) {
			localObject2 = (ShippingItem) ((Iterator) localObject1).next();
			if ((localObject2 == null)
					|| (StringUtils.isEmpty(((ShippingItem) localObject2)
							.getSn()))
					|| (((ShippingItem) localObject2).getQuantity() == null)
					|| (((ShippingItem) localObject2).getQuantity().intValue() <= 0)) {
				((Iterator) localObject1).remove();
			} else {
				localObject3 = localOrder
						.getOrderItem(((ShippingItem) localObject2).getSn());
				if ((localObject3 == null)
						|| (((ShippingItem) localObject2).getQuantity()
								.intValue() > ((OrderItem) localObject3)
								.getQuantity().intValue()
								- ((OrderItem) localObject3)
										.getShippedQuantity().intValue()))
					return "/admin/common/error";
				if ((((OrderItem) localObject3).getProduct() != null)
						&& (((OrderItem) localObject3).getProduct().getStock() != null)
						&& (((ShippingItem) localObject2).getQuantity()
								.intValue() > ((OrderItem) localObject3)
								.getProduct().getStock().intValue()))
					return "/admin/common/error";
				((ShippingItem) localObject2)
						.setName(((OrderItem) localObject3).getFullName());
				((ShippingItem) localObject2).setShipping(shipping);
			}
		}
		shipping.setOrder(localOrder);
		localObject1 = (ShippingMethod) this.shippingMethodService
				.find(shippingMethodId);
		shipping.setShippingMethod(localObject1 != null ? ((ShippingMethod) localObject1)
				.getName() : null);
		localObject2 = (DeliveryCorp) this.deliveryCorpService
				.find(deliveryCorpId);
		shipping.setDeliveryCorp(localObject2 != null ? ((DeliveryCorp) localObject2)
				.getName() : null);
		shipping.setDeliveryCorpUrl(localObject2 != null ? ((DeliveryCorp) localObject2)
				.getUrl() : null);
		shipping.setDeliveryCorpCode(localObject2 != null ? ((DeliveryCorp) localObject2)
				.getCode() : null);
		localObject3 = (Area) this.areaService.find(areaId);
		shipping.setArea(localObject3 != null ? ((Area) localObject3)
				.getFullName() : null);
		if (!IIIllIlI(shipping, new Class[0]))
			return "/admin/common/error";
		if ((localOrder.isExpired())
				|| (localOrder.getOrderStatus() != OrderOrderStatus.confirmed))
			return "/admin/common/error";
		if ((localOrder.getShippingStatus() != OrderShippingStatus.unshipped)
				&& (localOrder.getShippingStatus() != OrderShippingStatus.partialShipment))
			return "/admin/common/error";
		Admin localAdmin = this.adminService.getCurrent();
		if (localOrder.isLocked(localAdmin))
			return "/admin/common/error";
		shipping.setSn(this.snService.generate(SnType.shipping));
		shipping.setOperator(localAdmin.getUsername());
		this.orderService.shipping(localOrder, shipping, localAdmin);
		IIIllIlI(redirectAttributes, IIIlllII);
		return (String) (String) (String) ("redirect:view.jhtml?id=" + orderId);
	}

	@RequestMapping(value = { "/returns" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	public String returns(Long orderId, Long shippingMethodId,
			Long deliveryCorpId, Long areaId, Returns returns,
			RedirectAttributes redirectAttributes) {
		Object localObject2 = null;
		Object localObject3 = null;
		Order localOrder = (Order) this.orderService.find(orderId);
		if (localOrder == null)
			return "/admin/common/error";
		Object localObject1 = returns.getReturnsItems().iterator();
		while (((Iterator) localObject1).hasNext()) {
			localObject2 = (ReturnsItem) ((Iterator) localObject1).next();
			if ((localObject2 == null)
					|| (StringUtils.isEmpty(((ReturnsItem) localObject2)
							.getSn()))
					|| (((ReturnsItem) localObject2).getQuantity() == null)
					|| (((ReturnsItem) localObject2).getQuantity().intValue() <= 0)) {
				((Iterator) localObject1).remove();
			} else {
				localObject3 = localOrder
						.getOrderItem(((ReturnsItem) localObject2).getSn());
				if ((localObject3 == null)
						|| (((ReturnsItem) localObject2).getQuantity()
								.intValue() > ((OrderItem) localObject3)
								.getShippedQuantity().intValue()
								- ((OrderItem) localObject3)
										.getReturnQuantity().intValue()))
					return "/admin/common/error";
				((ReturnsItem) localObject2).setName(((OrderItem) localObject3)
						.getFullName());
				((ReturnsItem) localObject2).setReturns(returns);
			}
		}
		returns.setOrder(localOrder);
		localObject1 = (ShippingMethod) this.shippingMethodService
				.find(shippingMethodId);
		returns.setShippingMethod(localObject1 != null ? ((ShippingMethod) localObject1)
				.getName() : null);
		localObject2 = (DeliveryCorp) this.deliveryCorpService
				.find(deliveryCorpId);
		returns.setDeliveryCorp(localObject2 != null ? ((DeliveryCorp) localObject2)
				.getName() : null);
		localObject3 = (Area) this.areaService.find(areaId);
		returns.setArea(localObject3 != null ? ((Area) localObject3)
				.getFullName() : null);
		if (!IIIllIlI(returns, new Class[0]))
			return "/admin/common/error";
		if ((localOrder.isExpired())
				|| (localOrder.getOrderStatus() != OrderOrderStatus.confirmed))
			return "/admin/common/error";
		if ((localOrder.getShippingStatus() != OrderShippingStatus.shipped)
				&& (localOrder.getShippingStatus() != OrderShippingStatus.partialShipment)
				&& (localOrder.getShippingStatus() != OrderShippingStatus.partialReturns))
			return "/admin/common/error";
		Admin localAdmin = this.adminService.getCurrent();
		if (localOrder.isLocked(localAdmin))
			return "/admin/common/error";
		returns.setSn(this.snService.generate(SnType.returns));
		returns.setOperator(localAdmin.getUsername());
		this.orderService.returns(localOrder, returns, localAdmin);
		IIIllIlI(redirectAttributes, IIIlllII);
		return (String) (String) (String) ("redirect:view.jhtml?id=" + orderId);
	}

	@RequestMapping(value = { "/edit" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String edit(Long id, ModelMap model) {
		model.addAttribute("paymentMethods",
				this.paymentMethodService.findAll());
		model.addAttribute("shippingMethods",
				this.shippingMethodService.findAll());
		model.addAttribute("order", this.orderService.find(id));
		return "/admin/order/edit";
	}

	@RequestMapping(value = { "/order_item_add" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Map<String, Object> orderItemAdd(String productSn) {
		HashMap localHashMap = new HashMap();
		Product localProduct = this.productService.findBySn(productSn);
		if (localProduct == null) {
			localHashMap.put("message",
					Message.warn("admin.order.productNotExist", new Object[0]));
			return localHashMap;
		}
		if (!localProduct.getIsMarketable().booleanValue()) {
			localHashMap.put("message", Message.warn(
					"admin.order.productNotMarketable", new Object[0]));
			return localHashMap;
		}
		if (localProduct.getIsOutOfStock().booleanValue()) {
			localHashMap.put("message", Message.warn(
					"admin.order.productOutOfStock", new Object[0]));
			return localHashMap;
		}
		localHashMap.put("sn", localProduct.getSn());
		localHashMap.put("fullName", localProduct.getFullName());
		localHashMap.put("price", localProduct.getPrice());
		localHashMap.put("weight", localProduct.getWeight());
		localHashMap.put("isGift", localProduct.getIsGift());
		localHashMap.put("message", IIIlllII);
		return localHashMap;
	}

	@RequestMapping(value = { "/calculate" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Map<String, Object> calculate(Order order, Long areaId,
			Long paymentMethodId, Long shippingMethodId) {
		HashMap localHashMap = new HashMap();
		Object localObject1 = order.getOrderItems().iterator();
		Object localObject2 = null;
		Object localObject4 = null;
		while (((Iterator) localObject1).hasNext()) {
			localObject2 = (OrderItem) ((Iterator) localObject1).next();
			if ((localObject2 != null)
					&& (!StringUtils
							.isEmpty(((OrderItem) localObject2).getSn())))
				continue;
			((Iterator) localObject1).remove();
		}
		order.setArea((Area) this.areaService.find(areaId));
		order.setPaymentMethod((PaymentMethod) this.paymentMethodService
				.find(paymentMethodId));
		order.setShippingMethod((ShippingMethod) this.shippingMethodService
				.find(shippingMethodId));
		if (!IIIllIlI(order, new Class[0])) {
			localHashMap.put("message",
					Message.warn("admin.common.invalid", new Object[0]));
			return localHashMap;
		}
		localObject1 = (Order) this.orderService.find(order.getId());
		if (localObject1 == null) {
			localHashMap.put("message",
					Message.error("admin.common.invalid", new Object[0]));
			return localHashMap;
		}
		Object localObject3 = order.getOrderItems().iterator();
		while (((Iterator) localObject3).hasNext()) {
			localObject2 = (OrderItem) ((Iterator) localObject3).next();
			if (((OrderItem) localObject2).getId() != null) {
				localObject4 = (OrderItem) this.orderItemService
						.find(((OrderItem) localObject2).getId());
				if ((localObject4 == null)
						|| (!((Order) localObject1)
								.equals(((OrderItem) localObject4).getOrder()))) {
					localHashMap.put("message", Message.error(
							"admin.common.invalid", new Object[0]));
					return localHashMap;
				}
				Product localProduct = ((OrderItem) localObject4).getProduct();
				if ((localProduct == null) || (localProduct.getStock() == null))
					continue;
				if (((Order) localObject1).getIsAllocatedStock().booleanValue()) {
					if (((OrderItem) localObject2).getQuantity().intValue() <= localProduct
							.getAvailableStock().intValue()
							+ ((OrderItem) localObject4).getQuantity()
									.intValue())
						continue;
					localHashMap
							.put("message", Message.warn(
									"admin.order.lowStock", new Object[0]));
					return localHashMap;
				}
				if (((OrderItem) localObject2).getQuantity().intValue() <= localProduct
						.getAvailableStock().intValue())
					continue;
				localHashMap.put("message",
						Message.warn("admin.order.lowStock", new Object[0]));
				return localHashMap;
			}
			localObject4 = this.productService
					.findBySn(((OrderItem) localObject2).getSn());
			if (localObject4 == null) {
				localHashMap.put("message",
						Message.error("admin.common.invalid", new Object[0]));
				return localHashMap;
			}
			if ((((Product) localObject4).getStock() == null)
					|| (((OrderItem) localObject2).getQuantity().intValue() <= ((Product) localObject4)
							.getAvailableStock().intValue()))
				continue;
			localHashMap.put("message",
					Message.warn("admin.order.lowStock", new Object[0]));
			return localHashMap;
		}
		localObject2 = new HashMap();
		localObject4 = order.getOrderItems().iterator();
		while (((Iterator) localObject4).hasNext()) {
			localObject3 = (OrderItem) ((Iterator) localObject4).next();
			((Map) localObject2).put(((OrderItem) localObject3).getSn(),
					localObject3);
		}
		localHashMap.put("weight", Integer.valueOf(order.getWeight()));
		localHashMap.put("price", order.getPrice());
		localHashMap.put("quantity", Integer.valueOf(order.getQuantity()));
		localHashMap.put("amount", order.getAmount());
		localHashMap.put("orderItems", localObject2);
		localHashMap.put("message", IIIlllII);
		return (Map<String, Object>) (Map<String, Object>) (Map<String, Object>) (Map<String, Object>) localHashMap;
	}

	@RequestMapping(value = { "/update" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	public String update(Order order, Long areaId, Long paymentMethodId,
			Long shippingMethodId, RedirectAttributes redirectAttributes) {
		Object localObject2 = null;
		Object localObject1 = order.getOrderItems().iterator();
		while (((Iterator) localObject1).hasNext()) {
			localObject2 = (OrderItem) ((Iterator) localObject1).next();
			if ((localObject2 != null)
					&& (!StringUtils
							.isEmpty(((OrderItem) localObject2).getSn())))
				continue;
			((Iterator) localObject1).remove();
		}
		order.setArea((Area) this.areaService.find(areaId));
		order.setPaymentMethod((PaymentMethod) this.paymentMethodService
				.find(paymentMethodId));
		order.setShippingMethod((ShippingMethod) this.shippingMethodService
				.find(shippingMethodId));
		if (!IIIllIlI(order, new Class[0]))
			return "/admin/common/error";
		localObject1 = (Order) this.orderService.find(order.getId());
		if (localObject1 == null)
			return "/admin/common/error";
		if ((((Order) localObject1).isExpired())
				|| (((Order) localObject1).getOrderStatus() != OrderOrderStatus.unconfirmed))
			return "/admin/common/error";
		localObject2 = this.adminService.getCurrent();
		if (((Order) localObject1).isLocked((Admin) localObject2))
			return "/admin/common/error";
		if (!order.getIsInvoice().booleanValue()) {
			order.setInvoiceTitle(null);
			order.setTax(new BigDecimal(0));
		}
		Iterator localIterator = order.getOrderItems().iterator();
		while (localIterator.hasNext()) {
			OrderItem localOrderItem = (OrderItem) localIterator.next();
			Object localObject3;
			if (localOrderItem.getId() != null) {
				localObject3 = (OrderItem) this.orderItemService
						.find(localOrderItem.getId());
				if ((localObject3 == null)
						|| (!((Order) localObject1)
								.equals(((OrderItem) localObject3).getOrder())))
					return "/admin/common/error";
				Product localProduct = ((OrderItem) localObject3).getProduct();
				if ((localProduct != null) && (localProduct.getStock() != null))
					if (((Order) localObject1).getIsAllocatedStock()
							.booleanValue()) {
						if (localOrderItem.getQuantity().intValue() > localProduct
								.getAvailableStock().intValue()
								+ ((OrderItem) localObject3).getQuantity()
										.intValue())
							return "/admin/common/error";
					} else if (localOrderItem.getQuantity().intValue() > localProduct
							.getAvailableStock().intValue())
						return "/admin/common/error";
				BeanUtils.copyProperties(localObject3, localOrderItem,
						new String[] { "price", "quantity" });
				if (!((OrderItem) localObject3).getIsGift().booleanValue())
					continue;
				localOrderItem.setPrice(new BigDecimal(0));
			} else {
				localObject3 = this.productService.findBySn(localOrderItem
						.getSn());
				if (localObject3 == null)
					return "/admin/common/error";
				if ((((Product) localObject3).getStock() != null)
						&& (localOrderItem.getQuantity().intValue() > ((Product) localObject3)
								.getAvailableStock().intValue()))
					return "/admin/common/error";
				localOrderItem.setName(((Product) localObject3).getName());
				localOrderItem.setFullName(((Product) localObject3)
						.getFullName());
				if (((Product) localObject3).getIsGift().booleanValue())
					localOrderItem.setPrice(new BigDecimal(0));
				localOrderItem.setWeight(((Product) localObject3).getWeight());
				localOrderItem.setThumbnail(((Product) localObject3)
						.getThumbnail());
				localOrderItem.setIsGift(((Product) localObject3).getIsGift());
				localOrderItem.setShippedQuantity(Integer.valueOf(0));
				localOrderItem.setReturnQuantity(Integer.valueOf(0));
				localOrderItem.setProduct((Product) localObject3);
				localOrderItem.setOrder((Order) localObject1);
			}
		}
		order.setSn(((Order) localObject1).getSn());
		order.setOrderStatus(((Order) localObject1).getOrderStatus());
		order.setPaymentStatus(((Order) localObject1).getPaymentStatus());
		order.setShippingStatus(((Order) localObject1).getShippingStatus());
		order.setFee(((Order) localObject1).getFee());
		order.setAmountPaid(((Order) localObject1).getAmountPaid());
		order.setPromotion(((Order) localObject1).getPromotion());
		order.setExpire(((Order) localObject1).getExpire());
		order.setLockExpire(null);
		order.setIsAllocatedStock(((Order) localObject1).getIsAllocatedStock());
		order.setOperator(null);
		order.setMember(((Order) localObject1).getMember());
		order.setCouponCode(((Order) localObject1).getCouponCode());
		order.setCoupons(((Order) localObject1).getCoupons());
		order.setOrderLogs(((Order) localObject1).getOrderLogs());
		order.setDeposits(((Order) localObject1).getDeposits());
		order.setPayments(((Order) localObject1).getPayments());
		order.setRefunds(((Order) localObject1).getRefunds());
		order.setShippings(((Order) localObject1).getShippings());
		order.setReturns(((Order) localObject1).getReturns());
		this.orderService.update(order, (Admin) localObject2);
		IIIllIlI(redirectAttributes, IIIlllII);
		return (String) (String) (String) "redirect:list.jhtml";
	}

	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String list(OrderOrderStatus orderStatus,
			OrderPaymentStatus paymentStatus,
			OrderShippingStatus shippingStatus, Boolean hasExpired,
			Pageable pageable, ModelMap model) {
		model.addAttribute("orderStatus", orderStatus);
		model.addAttribute("paymentStatus", paymentStatus);
		model.addAttribute("shippingStatus", shippingStatus);
		model.addAttribute("hasExpired", hasExpired);
		model.addAttribute("page", this.orderService.findPage(orderStatus,
				paymentStatus, shippingStatus, hasExpired, pageable));
		return "/admin/order/list";
	}

	@RequestMapping(value = { "/delete" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Message delete(Long[] ids) {
		if (ids != null) {
			Admin localAdmin = this.adminService.getCurrent();
			for (Long localLong : ids) {
				Order localOrder = (Order) this.orderService.find(localLong);
				if ((localOrder != null) && (localOrder.isLocked(localAdmin)))
					return Message.error("admin.order.deleteLockedNotAllowed",
							new Object[] { localOrder.getSn() });
			}
			this.orderService.delete(ids);
		}
		return IIIlllII;
	}
}

package net.shopxx.controller.admin;

import java.util.Iterator;

import javax.annotation.Resource;

import net.shopxx.Message;
import net.shopxx.Pageable;
import net.shopxx.entity.Attribute;
import net.shopxx.entity.BaseEntity;
import net.shopxx.entity.ProductCategory;
import net.shopxx.service.AttributeService;
import net.shopxx.service.ProductCategoryService;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("adminAttributeController")
@RequestMapping({ "/admin/attribute" })
public class AttributeController extends BaseController {

	@Resource(name = "attributeServiceImpl")
	private AttributeService attributeService;

	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;

	@RequestMapping(value = { "/add" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String add(ModelMap model) {
		model.addAttribute("productCategoryTree",
				this.productCategoryService.findTree());
		model.addAttribute("attributeValuePropertyCount", Integer.valueOf(20));
		return "/admin/attribute/add";
	}

	@RequestMapping(value = { "/save" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	public String save(Attribute attribute, Long productCategoryId,
			RedirectAttributes redirectAttributes) {
		Iterator<String> localIterator = attribute.getOptions().iterator();
		while (localIterator.hasNext()) {
			String str = localIterator.next();
			if (!StringUtils.isEmpty(str))
				continue;
			localIterator.remove();
		}
		attribute
				.setProductCategory((ProductCategory) this.productCategoryService
						.find(productCategoryId));
		if (!IIIllIlI(attribute, new Class[] { BaseEntity.class }))
			return "/admin/common/error";
		if (attribute.getProductCategory().getAttributes().size() >= 20) {
			IIIllIlI(redirectAttributes, Message.error(
					"admin.attribute.addCountNotAllowed",
					new Object[] { Integer.valueOf(20) }));
		} else {
			attribute.setPropertyIndex(null);
			this.attributeService.save(attribute);
			IIIllIlI(redirectAttributes, IIIlllII);
		}
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = { "/edit" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String edit(Long id, ModelMap model) {
		model.addAttribute("productCategoryTree",
				this.productCategoryService.findTree());
		model.addAttribute("attributeValuePropertyCount", Integer.valueOf(20));
		model.addAttribute("attribute", this.attributeService.find(id));
		return "/admin/attribute/edit";
	}

	@RequestMapping(value = { "/update" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	public String update(Attribute attribute,
			RedirectAttributes redirectAttributes) {
		Iterator<String> localIterator = attribute.getOptions().iterator();
		while (localIterator.hasNext()) {
			String str = localIterator.next();
			if (!StringUtils.isEmpty(str))
				continue;
			localIterator.remove();
		}
		if (!IIIllIlI(attribute, new Class[0]))
			return "/admin/common/error";
		this.attributeService.update(attribute, new String[] { "propertyIndex",
				"productCategory" });
		IIIllIlI(redirectAttributes, IIIlllII);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", this.attributeService.findPage(pageable));
		return "/admin/attribute/list";
	}

	@RequestMapping(value = { "/delete" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Message delete(Long[] ids) {
		this.attributeService.delete(ids);
		return IIIlllII;
	}
}

package net.shopxx.controller.admin;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import net.shopxx.Message;
import net.shopxx.Pageable;
import net.shopxx.entity.BaseEntity;
import net.shopxx.entity.MemberAttribute;
import net.shopxx.entity.MemberAttribute.MemberAttributeType;
import net.shopxx.service.MemberAttributeService;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("adminMemberAttributeController")
@RequestMapping({ "/admin/member_attribute" })
public class MemberAttributeController extends BaseController {

	@Resource(name = "memberAttributeServiceImpl")
	private MemberAttributeService memberAttributeService;

	@RequestMapping(value = { "/add" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String add(ModelMap model, RedirectAttributes redirectAttributes) {
		if (this.memberAttributeService.count() - 8L >= 10L)
			IIIllIlI(redirectAttributes, Message.warn(
					"admin.memberAttribute.addCountNotAllowed",
					new Object[] { Integer.valueOf(10) }));
		return "/admin/member_attribute/add";
	}

	@RequestMapping(value = { "/save" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	public String save(MemberAttribute memberAttribute,
			RedirectAttributes redirectAttributes) {
		if (!IIIllIlI(memberAttribute, new Class[] { BaseEntity.class }))
			return "/admin/common/error";
		Object localObject = null;
		if ((memberAttribute.getType() == MemberAttributeType.select)
				|| (memberAttribute.getType() == MemberAttributeType.checkbox)) {
			localObject = memberAttribute.getOptions();
			if (localObject != null) {
				Iterator localIterator = ((List) localObject).iterator();
				while (localIterator.hasNext()) {
					String str = (String) localIterator.next();
					if (!StringUtils.isEmpty(str))
						continue;
					localIterator.remove();
				}
			}
			if ((localObject == null) || (((List) localObject).isEmpty()))
				return "/admin/common/error";
		} else if (memberAttribute.getType() == MemberAttributeType.text) {
			memberAttribute.setOptions(null);
		} else {
			return "/admin/common/error";
		}
		localObject = this.memberAttributeService.findUnusedPropertyIndex();
		if (localObject == null)
			return "/admin/common/error";
		memberAttribute.setPropertyIndex((Integer) localObject);
		this.memberAttributeService.save(memberAttribute);
		IIIllIlI(redirectAttributes, IIIlllII);
		return (String) "redirect:list.jhtml";
	}

	@RequestMapping(value = { "/edit" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String edit(Long id, ModelMap model) {
		model.addAttribute("memberAttribute",
				this.memberAttributeService.find(id));
		return "/admin/member_attribute/edit";
	}

	@RequestMapping(value = { "/update" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	public String update(MemberAttribute memberAttribute,
			RedirectAttributes redirectAttributes) {
		if (!IIIllIlI(memberAttribute, new Class[0]))
			return "/admin/common/error";
		MemberAttribute localMemberAttribute = (MemberAttribute) this.memberAttributeService
				.find(memberAttribute.getId());
		if (localMemberAttribute == null)
			return "/admin/common/error";
		if ((localMemberAttribute.getType() == MemberAttributeType.select)
				|| (localMemberAttribute.getType() == MemberAttributeType.checkbox)) {
			List localList = memberAttribute.getOptions();
			if (localList != null) {
				Iterator localIterator = localList.iterator();
				while (localIterator.hasNext()) {
					String str = (String) localIterator.next();
					if (!StringUtils.isEmpty(str))
						continue;
					localIterator.remove();
				}
			}
			if ((localList == null) || (localList.isEmpty()))
				return "/admin/common/error";
		} else {
			memberAttribute.setOptions(null);
		}
		this.memberAttributeService.update(memberAttribute, new String[] {
				"type", "propertyIndex" });
		IIIllIlI(redirectAttributes, IIIlllII);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page",
				this.memberAttributeService.findPage(pageable));
		return "/admin/member_attribute/list";
	}

	@RequestMapping(value = { "/delete" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Message delete(Long[] ids) {
		this.memberAttributeService.delete(ids);
		return IIIlllII;
	}
}

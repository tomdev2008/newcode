package net.shopxx.controller.admin;

import javax.annotation.Resource;

import net.shopxx.Message;
import net.shopxx.Pageable;
import net.shopxx.entity.BaseEntity;
import net.shopxx.entity.Tag;
import net.shopxx.entity.Tag.TagType;
import net.shopxx.service.TagService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("adminTagController")
@RequestMapping({ "/admin/tag" })
public class TagController extends BaseController {

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	@RequestMapping(value = { "/add" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String add(ModelMap model) {
		model.addAttribute("types", TagType.values());
		return "/admin/tag/add";
	}

	@RequestMapping(value = { "/save" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	public String save(Tag tag, RedirectAttributes redirectAttributes) {
		if (!IIIllIlI(tag, new Class[] { BaseEntity.class }))
			return "/admin/common/error";
		tag.setArticles(null);
		tag.setProducts(null);
		this.tagService.save(tag);
		IIIllIlI(redirectAttributes, IIIlllII);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = { "/edit" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String edit(Long id, ModelMap model) {
		model.addAttribute("types", TagType.values());
		model.addAttribute("tag", this.tagService.find(id));
		return "/admin/tag/edit";
	}

	@RequestMapping(value = { "/update" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	public String update(Tag tag, RedirectAttributes redirectAttributes) {
		if (!IIIllIlI(tag, new Class[0]))
			return "/admin/common/error";
		this.tagService.update(tag, new String[] { "type", "articles",
				"products" });
		IIIllIlI(redirectAttributes, IIIlllII);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", this.tagService.findPage(pageable));
		return "/admin/tag/list";
	}

	@RequestMapping(value = { "/delete" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Message delete(Long[] ids) {
		this.tagService.delete(ids);
		return IIIlllII;
	}
}

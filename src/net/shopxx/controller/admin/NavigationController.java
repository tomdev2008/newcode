package net.shopxx.controller.admin;

import javax.annotation.Resource;

import net.shopxx.Message;
import net.shopxx.Pageable;
import net.shopxx.entity.Navigation;
import net.shopxx.entity.Navigation.NavigationPosition;
import net.shopxx.service.ArticleCategoryService;
import net.shopxx.service.NavigationService;
import net.shopxx.service.ProductCategoryService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("adminNavigationController")
@RequestMapping({ "/admin/navigation" })
public class NavigationController extends BaseController {

	@Resource(name = "navigationServiceImpl")
	private NavigationService navigationService;

	@Resource(name = "articleCategoryServiceImpl")
	private ArticleCategoryService articleCategoryService;

	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;

	@RequestMapping(value = { "/add" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String add(ModelMap model) {
		model.addAttribute("positions", NavigationPosition.values());
		model.addAttribute("articleCategoryTree",
				this.articleCategoryService.findTree());
		model.addAttribute("productCategoryTree",
				this.productCategoryService.findTree());
		return "/admin/navigation/add";
	}

	@RequestMapping(value = { "/save" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	public String save(Navigation navigation,
			RedirectAttributes redirectAttributes) {
		if (!IIIllIlI(navigation, new Class[0]))
			return "/admin/common/error";
		this.navigationService.save(navigation);
		IIIllIlI(redirectAttributes, IIIlllII);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = { "/edit" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String edit(Long id, ModelMap model) {
		model.addAttribute("positions", NavigationPosition.values());
		model.addAttribute("articleCategoryTree",
				this.articleCategoryService.findTree());
		model.addAttribute("productCategoryTree",
				this.productCategoryService.findTree());
		model.addAttribute("navigation", this.navigationService.find(id));
		return "/admin/navigation/edit";
	}

	@RequestMapping(value = { "/update" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	public String update(Navigation navigation,
			RedirectAttributes redirectAttributes) {
		if (!IIIllIlI(navigation, new Class[0]))
			return "/admin/common/error";
		this.navigationService.update(navigation);
		IIIllIlI(redirectAttributes, IIIlllII);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("topNavigations",
				this.navigationService.findList(NavigationPosition.top));
		model.addAttribute("middleNavigations",
				this.navigationService.findList(NavigationPosition.middle));
		model.addAttribute("bottomNavigations",
				this.navigationService.findList(NavigationPosition.bottom));
		return "/admin/navigation/list";
	}

	@RequestMapping(value = { "/delete" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Message delete(Long[] ids) {
		this.navigationService.delete(ids);
		return IIIlllII;
	}
}

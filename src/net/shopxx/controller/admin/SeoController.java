package net.shopxx.controller.admin;

import javax.annotation.Resource;
import net.shopxx.Pageable;
import net.shopxx.entity.Seo;
import net.shopxx.service.SeoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("adminSeoController")
@RequestMapping({"/admin/seo"})
public class SeoController extends BaseController
{

  @Resource(name="seoServiceImpl")
  private SeoService seoService;

  @RequestMapping(value={"/edit"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public String edit(Long id, ModelMap model)
  {
    model.addAttribute("seo", this.seoService.find(id));
    return "/admin/seo/edit";
  }

  @RequestMapping(value={"/update"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public String update(Seo seo, RedirectAttributes redirectAttributes)
  {
    if (!IIIllIlI(seo, new Class[0]))
      return "/admin/common/error";
    this.seoService.update(seo, new String[] { "type" });
    IIIllIlI(redirectAttributes, IIIlllII);
    return "redirect:list.jhtml";
  }

  @RequestMapping(value={"/list"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public String list(Pageable pageable, ModelMap model)
  {
    model.addAttribute("page", this.seoService.findPage(pageable));
    return "/admin/seo/list";
  }
}

package net.shopxx.controller.admin;

import javax.annotation.Resource;
import net.shopxx.service.CacheService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("adminCacheController")
@RequestMapping({"/admin/cache"})
public class CacheController extends BaseController
{

  @Resource(name="cacheServiceImpl")
  private CacheService cacheService;

  @RequestMapping(value={"/clear"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public String clear(ModelMap model)
  {
    Long localLong1 = null;
    Long localLong2 = null;
    Long localLong3 = null;
    try
    {
      localLong1 = Long.valueOf(Runtime.getRuntime().totalMemory() / 1024L / 1024L);
      localLong2 = Long.valueOf(Runtime.getRuntime().maxMemory() / 1024L / 1024L);
      localLong3 = Long.valueOf(Runtime.getRuntime().freeMemory() / 1024L / 1024L);
    }
    catch (Exception localException)
    {
    }
    model.addAttribute("totalMemory", localLong1);
    model.addAttribute("maxMemory", localLong2);
    model.addAttribute("freeMemory", localLong3);
    model.addAttribute("cacheSize", Integer.valueOf(this.cacheService.getCacheSize()));
    model.addAttribute("diskStorePath", this.cacheService.getDiskStorePath());
    return "/admin/cache/clear";
  }

  @RequestMapping(value={"/clear"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public String clear(RedirectAttributes redirectAttributes)
  {
    this.cacheService.clear();
    IIIllIlI(redirectAttributes, IIIlllII);
    return "redirect:clear.jhtml";
  }
}

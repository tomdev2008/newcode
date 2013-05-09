package net.shopxx.controller.admin;

import javax.annotation.Resource;

import net.shopxx.Message;
import net.shopxx.Pageable;
import net.shopxx.entity.Ad;
import net.shopxx.entity.Ad.AdType;
import net.shopxx.entity.AdPosition;
import net.shopxx.service.AdPositionService;
import net.shopxx.service.AdService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("adminAdController")
@RequestMapping({"/admin/ad"})
public class AdController extends BaseController
{

  @Resource(name="adServiceImpl")
  private AdService adService;

  @Resource(name="adPositionServiceImpl")
  private AdPositionService adPositionService;

  @RequestMapping(value={"/add"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public String add(ModelMap model)
  {
    model.addAttribute("types", AdType.values());
    model.addAttribute("adPositions", this.adPositionService.findAll());
    return "/admin/ad/add";
  }

  @RequestMapping(value={"/save"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public String save(Ad ad, Long adPositionId, RedirectAttributes redirectAttributes)
  {
    ad.setAdPosition((AdPosition)this.adPositionService.find(adPositionId));
    if (!IIIllIlI(ad, new Class[0]))
      return "/admin/common/error";
    if ((ad.getBeginDate() != null) && (ad.getEndDate() != null) && (ad.getBeginDate().after(ad.getEndDate())))
      return "/admin/common/error";
    if (ad.getType() == AdType.text)
      ad.setPath(null);
    else
      ad.setContent(null);
    this.adService.save(ad);
    IIIllIlI(redirectAttributes, IIIlllII);
    return "redirect:list.jhtml";
  }

  @RequestMapping(value={"/edit"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public String edit(Long id, ModelMap model)
  {
    model.addAttribute("types", AdType.values());
    model.addAttribute("ad", this.adService.find(id));
    model.addAttribute("adPositions", this.adPositionService.findAll());
    return "/admin/ad/edit";
  }

  @RequestMapping(value={"/update"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public String update(Ad ad, Long adPositionId, RedirectAttributes redirectAttributes)
  {
    ad.setAdPosition((AdPosition)this.adPositionService.find(adPositionId));
    if (!IIIllIlI(ad, new Class[0]))
      return "/admin/common/error";
    if ((ad.getBeginDate() != null) && (ad.getEndDate() != null) && (ad.getBeginDate().after(ad.getEndDate())))
      return "/admin/common/error";
    if (ad.getType() == AdType.text)
      ad.setPath(null);
    else
      ad.setContent(null);
    this.adService.update(ad);
    IIIllIlI(redirectAttributes, IIIlllII);
    return "redirect:list.jhtml";
  }

  @RequestMapping(value={"/list"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public String list(Pageable pageable, ModelMap model)
  {
    model.addAttribute("page", this.adService.findPage(pageable));
    return "/admin/ad/list";
  }

  @RequestMapping(value={"/delete"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  @ResponseBody
  public Message delete(Long[] ids)
  {
    this.adService.delete(ids);
    return IIIlllII;
  }
}

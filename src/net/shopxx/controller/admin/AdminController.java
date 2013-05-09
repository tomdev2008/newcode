package net.shopxx.controller.admin;

import java.util.HashSet;
import javax.annotation.Resource;
import net.shopxx.Message;
import net.shopxx.Pageable;
import net.shopxx.entity.Admin;
import net.shopxx.entity.BaseEntity.Save;
import net.shopxx.service.AdminService;
import net.shopxx.service.RoleService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("adminAdminController")
@RequestMapping({"/admin/admin"})
public class AdminController extends BaseController
{

  @Resource(name="adminServiceImpl")
  private AdminService IIIlllIl;

  @Resource(name="roleServiceImpl")
  private RoleService IIIllllI;

  @RequestMapping(value={"/check_username"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  @ResponseBody
  public boolean checkUsername(String username)
  {
    if (StringUtils.isEmpty(username))
      return false;
    return !this.IIIlllIl.usernameExists(username);
  }

  @RequestMapping(value={"/add"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public String add(ModelMap model)
  {
    model.addAttribute("roles", this.IIIllllI.findAll());
    return "/admin/admin/add";
  }

  @RequestMapping(value={"/save"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public String save(Admin admin, Long[] roleIds, RedirectAttributes redirectAttributes)
  {
    admin.setRoles(new HashSet(this.IIIllllI.findList(roleIds)));
    if (!IIIllIlI(admin, new Class[] { BaseEntity.Save.class }))
      return "/admin/common/error";
    if (this.IIIlllIl.usernameExists(admin.getUsername()))
      return "/admin/common/error";
    admin.setPassword(DigestUtils.md5Hex(admin.getPassword()));
    admin.setIsLocked(Boolean.valueOf(false));
    admin.setLoginFailureCount(Integer.valueOf(0));
    admin.setLockedDate(null);
    admin.setLoginDate(null);
    admin.setLoginIp(null);
    admin.setOrders(null);
    this.IIIlllIl.save(admin);
    IIIllIlI(redirectAttributes, IIIlllII);
    return "redirect:list.jhtml";
  }

  @RequestMapping(value={"/edit"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public String edit(Long id, ModelMap model)
  {
    model.addAttribute("roles", this.IIIllllI.findAll());
    model.addAttribute("admin", this.IIIlllIl.find(id));
    return "/admin/admin/edit";
  }

  @RequestMapping(value={"/update"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public String update(Admin admin, Long[] roleIds, RedirectAttributes redirectAttributes)
  {
    admin.setRoles(new HashSet(this.IIIllllI.findList(roleIds)));
    if (!IIIllIlI(admin, new Class[0]))
      return "/admin/common/error";
    Admin localAdmin = (Admin)this.IIIlllIl.find(admin.getId());
    if (localAdmin == null)
      return "/admin/common/error";
    if (StringUtils.isNotEmpty(admin.getPassword()))
      admin.setPassword(DigestUtils.md5Hex(admin.getPassword()));
    else
      admin.setPassword(localAdmin.getPassword());
    if ((localAdmin.getIsLocked().booleanValue()) && (!admin.getIsLocked().booleanValue()))
    {
      admin.setLoginFailureCount(Integer.valueOf(0));
      admin.setLockedDate(null);
    }
    else
    {
      admin.setIsLocked(localAdmin.getIsLocked());
      admin.setLoginFailureCount(localAdmin.getLoginFailureCount());
      admin.setLockedDate(localAdmin.getLockedDate());
    }
    this.IIIlllIl.update(admin, new String[] { "username", "loginDate", "loginIp", "orders" });
    IIIllIlI(redirectAttributes, IIIlllII);
    return "redirect:list.jhtml";
  }

  @RequestMapping(value={"/list"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public String list(Pageable pageable, ModelMap model)
  {
    model.addAttribute("page", this.IIIlllIl.findPage(pageable));
    return "/admin/admin/list";
  }

  @RequestMapping(value={"/delete"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  @ResponseBody
  public Message delete(Long[] ids)
  {
    if (ids.length >= this.IIIlllIl.count())
      return Message.error("admin.common.deleteAllNotAllowed", new Object[0]);
    this.IIIlllIl.delete(ids);
    return IIIlllII;
  }
}

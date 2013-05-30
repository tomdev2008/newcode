package net.shopxx.controller.shop.member;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.shopxx.CommonAttributes;
import net.shopxx.Setting;
import net.shopxx.controller.shop.BaseController;
import net.shopxx.entity.Area;
import net.shopxx.entity.Member;
import net.shopxx.entity.Member.MemberGender;
import net.shopxx.entity.MemberAttribute;
import net.shopxx.entity.MemberAttribute.MemberAttributeType;
import net.shopxx.service.AreaService;
import net.shopxx.service.MemberAttributeService;
import net.shopxx.service.MemberService;
import net.shopxx.util.SettingUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("shopMemberProfileController")
@RequestMapping({"/member/profile"})
public class ProfileController extends BaseController
{

  @Resource(name="memberServiceImpl")
  private MemberService IIIlllIl;

  @Resource(name="memberAttributeServiceImpl")
  private MemberAttributeService IIIllllI;

  @Resource(name="areaServiceImpl")
  private AreaService IIIlllll;

  @RequestMapping(value={"/check_email"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  @ResponseBody
  public boolean checkEmail(String email)
  {
    if (StringUtils.isEmpty(email))
      return false;
    Member localMember = this.IIIlllIl.getCurrent();
    return this.IIIlllIl.emailUnique(localMember.getEmail(), email);
  }

  @RequestMapping(value={"/edit"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public String edit(ModelMap model)
  {
    model.addAttribute("genders", MemberGender.values());
    model.addAttribute("memberAttributes", this.IIIllllI.findList());
    return "shop/member/profile/edit";
  }

  @RequestMapping(value={"/update"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public String update(String email, HttpServletRequest request, RedirectAttributes redirectAttributes)
  {
    if (!IIIllIlI(Member.class, "email", email, new Class[0]))
      return "/shop/common/error";
    Setting localSetting = SettingUtils.get();
    Member localMember = this.IIIlllIl.getCurrent();
    if ((!localSetting.getIsDuplicateEmail().booleanValue()) && (!this.IIIlllIl.emailUnique(localMember.getEmail(), email)))
      return "/shop/common/error";
    localMember.setEmail(email);
    List localList = this.IIIllllI.findList();
    Iterator localIterator = localList.iterator();
    while (localIterator.hasNext())
    {
      MemberAttribute localMemberAttribute = (MemberAttribute)localIterator.next();
      String str = request.getParameter("memberAttribute_" + localMemberAttribute.getId());
      if ((localMemberAttribute.getType() == MemberAttributeType.name) || (localMemberAttribute.getType() == MemberAttributeType.address) || (localMemberAttribute.getType() == MemberAttributeType.zipCode) || (localMemberAttribute.getType() == MemberAttributeType.phone) || (localMemberAttribute.getType() == MemberAttributeType.mobile) || (localMemberAttribute.getType() == MemberAttributeType.text) || (localMemberAttribute.getType() == MemberAttributeType.select))
      {
        if ((localMemberAttribute.getIsRequired().booleanValue()) && (StringUtils.isEmpty(str)))
          return "/shop/common/error";
        localMember.setAttributeValue(localMemberAttribute, str);
      }
      else
      {
        MemberGender localGender;
        if (localMemberAttribute.getType() == MemberAttributeType.gender)
        {
          localGender = StringUtils.isNotEmpty(str) ? MemberGender.valueOf(str) : null;
          if ((localMemberAttribute.getIsRequired().booleanValue()) && (localGender == null))
            return "/shop/common/error";
          localMember.setGender(localGender);
        }
        else if (localMemberAttribute.getType() == MemberAttributeType.birth)
        {
          try
          {
            localGender = StringUtils.isNotEmpty(str) ? DateUtils.parseDate(str, CommonAttributes.DATE_PATTERNS) : null;
            if ((localMemberAttribute.getIsRequired().booleanValue()) && (localGender == null))
              return "/shop/common/error";
            localMember.setBirth(localGender);
          }
          catch (ParseException localParseException1)
          {
            return "/shop/common/error";
          }
        }
        else
        {
          Object localObject1;
          if (localMemberAttribute.getType() == MemberAttribute.Type.area)
          {
            localObject1 = StringUtils.isNotEmpty(str) ? (Area)this.IIIlllll.find(Long.valueOf(str)) : null;
            if (localObject1 != null)
              localMember.setArea((Area)localObject1);
            else if (localMemberAttribute.getIsRequired().booleanValue())
              return "/shop/common/error";
          }
          else
          {
            if (localMemberAttribute.getType() != MemberAttributeType.checkbox)
              continue;
            localObject1 = request.getParameterValues("memberAttribute_" + localMemberAttribute.getId());
            Object localObject2 = localObject1 != null ? Arrays.asList(localObject1) : null;
            if ((localMemberAttribute.getIsRequired().booleanValue()) && ((localObject2 == null) || (localObject2.isEmpty())))
              return "/shop/common/error";
            localMember.setAttributeValue(localMemberAttribute, localObject2);
          }
        }
      }
    }
    this.IIIlllIl.update(localMember);
    IIIllIlI(redirectAttributes, IIIlllII);
    return (String)"redirect:edit.jhtml";
  }
}

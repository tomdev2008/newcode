package net.shopxx.controller.admin;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Iterator;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.shopxx.CommonAttributes;
import net.shopxx.Message;
import net.shopxx.Pageable;
import net.shopxx.Setting;
import net.shopxx.entity.Area;
import net.shopxx.entity.BaseEntity;
import net.shopxx.entity.Member;
import net.shopxx.entity.Member.MemberGender;
import net.shopxx.entity.MemberAttribute;
import net.shopxx.entity.MemberAttribute.MemberAttributeType;
import net.shopxx.entity.MemberRank;
import net.shopxx.service.AdminService;
import net.shopxx.service.AreaService;
import net.shopxx.service.MemberAttributeService;
import net.shopxx.service.MemberRankService;
import net.shopxx.service.MemberService;
import net.shopxx.util.SettingUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("adminMemberController")
@RequestMapping({"/admin/member"})
public class MemberController extends BaseController
{

  @Resource(name="memberServiceImpl")
  private MemberService memberService;

  @Resource(name="memberRankServiceImpl")
  private MemberRankService memberRankService;

  @Resource(name="memberAttributeServiceImpl")
  private MemberAttributeService memberAttributeService;

  @Resource(name="areaServiceImpl")
  private AreaService IIlIIIII;

  @Resource(name="adminServiceImpl")
  private AdminService IIlIIIIl;

  @RequestMapping(value={"/check_username"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  @ResponseBody
  public boolean checkUsername(String username)
  {
    if (StringUtils.isEmpty(username))
      return false;
    return (!this.memberService.usernameDisabled(username)) && (!this.memberService.usernameExists(username));
  }

  @RequestMapping(value={"/check_email"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  @ResponseBody
  public boolean checkEmail(String previousEmail, String email)
  {
    if (StringUtils.isEmpty(email))
      return false;
    return this.memberService.emailUnique(previousEmail, email);
  }

  @RequestMapping(value={"/view"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public String view(Long id, ModelMap model)
  {
    model.addAttribute("genders", MemberGender.values());
    model.addAttribute("memberAttributes", this.memberAttributeService.findList());
    model.addAttribute("member", this.memberService.find(id));
    return "/admin/member/view";
  }

  @RequestMapping(value={"/add"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public String add(ModelMap model)
  {
    model.addAttribute("genders", MemberGender.values());
    model.addAttribute("memberRanks", this.memberRankService.findAll());
    model.addAttribute("memberAttributes", this.memberAttributeService.findList());
    return "/admin/member/add";
  }

  @RequestMapping(value={"/save"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public String save(Member member, Long memberRankId, HttpServletRequest request, RedirectAttributes redirectAttributes)
  {
    member.setMemberRank((MemberRank)this.memberRankService.find(memberRankId));
    if (!IIIllIlI(member, new Class[] { BaseEntity.class }))
      return "/admin/common/error";
    Setting localSetting = SettingUtils.get();
    if ((member.getUsername().length() < localSetting.getUsernameMinLength().intValue()) || (member.getUsername().length() > localSetting.getUsernameMaxLength().intValue()))
      return "/admin/common/error";
    if ((member.getPassword().length() < localSetting.getPasswordMinLength().intValue()) || (member.getPassword().length() > localSetting.getPasswordMaxLength().intValue()))
      return "/admin/common/error";
    if ((this.memberService.usernameDisabled(member.getUsername())) || (this.memberService.usernameExists(member.getUsername())))
      return "/admin/common/error";
    if ((!localSetting.getIsDuplicateEmail().booleanValue()) && (this.memberService.emailExists(member.getEmail())))
      return "/admin/common/error";
    member.removeAttributeValue();
    Iterator localIterator = this.memberAttributeService.findList().iterator();
    while (localIterator.hasNext())
    {
      MemberAttribute localMemberAttribute = (MemberAttribute)localIterator.next();
      String str = request.getParameter("memberAttribute_" + localMemberAttribute.getId());
      if ((localMemberAttribute.getType() == MemberAttributeType.name) || (localMemberAttribute.getType() == MemberAttributeType.address) || (localMemberAttribute.getType() == MemberAttributeType.zipCode) || (localMemberAttribute.getType() == MemberAttributeType.phone) || (localMemberAttribute.getType() == MemberAttributeType.mobile) || (localMemberAttribute.getType() == MemberAttributeType.text) || (localMemberAttribute.getType() == MemberAttributeType.select))
      {
        if ((localMemberAttribute.getIsRequired().booleanValue()) && (StringUtils.isEmpty(str)))
          return "/admin/common/error";
        member.setAttributeValue(localMemberAttribute, str);
      }
      else
      {
        MemberGender localGender;
        if (localMemberAttribute.getType() == MemberAttributeType.gender)
        {
          localGender = StringUtils.isNotEmpty(str) ? MemberGender.valueOf(str) : null;
          if ((localMemberAttribute.getIsRequired().booleanValue()) && (localGender == null))
            return "/admin/common/error";
          member.setGender(localGender);
        }
        else if (localMemberAttribute.getType() == MemberAttributeType.birth)
        {
          try
          {
            localGender = StringUtils.isNotEmpty(str) ? DateUtils.parseDate(str, CommonAttributes.DATE_PATTERNS) : null;
            if ((localMemberAttribute.getIsRequired().booleanValue()) && (localGender == null))
							member.setBirth(localGender);
            return "/admin/common/error";
          }
          catch (ParseException localParseException1)
          {
            return "/admin/common/error";
          }
        }
        else
        {
          Object localObject1;
          if (localMemberAttribute.getType() == MemberAttributeType.area)
          {
            localObject1 = StringUtils.isNotEmpty(str) ? (Area)this.IIlIIIII.find(Long.valueOf(str)) : null;
            if (localObject1 != null)
              member.setArea((Area)localObject1);
            else if (localMemberAttribute.getIsRequired().booleanValue())
              return "/admin/common/error";
          }
          else
          {
            if (localMemberAttribute.getType() != MemberAttributeType.checkbox)
              continue;
            localObject1 = request.getParameterValues("memberAttribute_" + localMemberAttribute.getId());
            Object localObject2 = localObject1 != null ? Arrays.asList(localObject1) : null;
            if ((localMemberAttribute.getIsRequired().booleanValue()) && ((localObject2 == null) || (localObject2.isEmpty())))
              return "/admin/common/error";
            member.setAttributeValue(localMemberAttribute, localObject2);
          }
        }
      }
    }
    member.setUsername(member.getUsername().toLowerCase());
    member.setPassword(DigestUtils.md5Hex(member.getPassword()));
    member.setAmount(new BigDecimal(0));
    member.setIsLocked(Boolean.valueOf(false));
    member.setLoginFailureCount(Integer.valueOf(0));
    member.setLockedDate(null);
    member.setRegisterIp(request.getRemoteAddr());
    member.setLoginIp(null);
    member.setLoginDate(null);
    member.setSafeKey(null);
    member.setCart(null);
    member.setOrders(null);
    member.setDeposits(null);
    member.setPayments(null);
    member.setCouponCodes(null);
    member.setReceivers(null);
    member.setReviews(null);
    member.setConsultations(null);
    member.setFavoriteProducts(null);
    member.setProductNotifies(null);
    member.setInMessages(null);
    member.setOutMessages(null);
    this.memberService.save(member, this.IIlIIIIl.getCurrent());
    IIIllIlI(redirectAttributes, IIIlllII);
    return (String)"redirect:list.jhtml";
  }

  @RequestMapping(value={"/edit"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public String edit(Long id, ModelMap model)
  {
    model.addAttribute("genders", MemberGender.values());
    model.addAttribute("memberRanks", this.memberRankService.findAll());
    model.addAttribute("memberAttributes", this.memberAttributeService.findList());
    model.addAttribute("member", this.memberService.find(id));
    return "/admin/member/edit";
  }

  @RequestMapping(value={"/update"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public String update(Member member, Long memberRankId, Integer modifyPoint, BigDecimal modifyBalance, String depositMemo, HttpServletRequest request, RedirectAttributes redirectAttributes)
  {
    member.setMemberRank((MemberRank)this.memberRankService.find(memberRankId));
    if (!IIIllIlI(member, new Class[0]))
      return "/admin/common/error";
    Setting localSetting = SettingUtils.get();
    if ((member.getPassword() != null) && ((member.getPassword().length() < localSetting.getPasswordMinLength().intValue()) || (member.getPassword().length() > localSetting.getPasswordMaxLength().intValue())))
      return "/admin/common/error";
    Member localMember = (Member)this.memberService.find(member.getId());
    if (localMember == null)
      return "/admin/common/error";
    if ((!localSetting.getIsDuplicateEmail().booleanValue()) && (!this.memberService.emailUnique(localMember.getEmail(), member.getEmail())))
      return "/admin/common/error";
    member.removeAttributeValue();
    Iterator localIterator = this.memberAttributeService.findList().iterator();
    while (localIterator.hasNext())
    {
      MemberAttribute localMemberAttribute = (MemberAttribute)localIterator.next();
      String str = request.getParameter("memberAttribute_" + localMemberAttribute.getId());
      if ((localMemberAttribute.getType() == MemberAttributeType.name) || (localMemberAttribute.getType() == MemberAttributeType.address) || (localMemberAttribute.getType() == MemberAttributeType.zipCode) || (localMemberAttribute.getType() == MemberAttributeType.phone) || (localMemberAttribute.getType() == MemberAttributeType.mobile) || (localMemberAttribute.getType() == MemberAttributeType.text) || (localMemberAttribute.getType() == MemberAttributeType.select))
      {
        if ((localMemberAttribute.getIsRequired().booleanValue()) && (StringUtils.isEmpty(str)))
          return "/admin/common/error";
        member.setAttributeValue(localMemberAttribute, str);
      }
      else
      {
        MemberGender localGender;
        if (localMemberAttribute.getType() == MemberAttributeType.gender)
        {
          localGender = StringUtils.isNotEmpty(str) ? MemberGender.valueOf(str) : null;
          if ((localMemberAttribute.getIsRequired().booleanValue()) && (localGender == null))
            return "/admin/common/error";
          member.setGender(localGender);
        }
        else if (localMemberAttribute.getType() == MemberAttributeType.birth)
        {
          try
          {
            localGender = StringUtils.isNotEmpty(str) ? DateUtils.parseDate(str, CommonAttributes.DATE_PATTERNS) : null;
            if ((localMemberAttribute.getIsRequired().booleanValue()) && (localGender == null))
              return "/admin/common/error";
            member.setBirth(localGender);
          }
          catch (ParseException localParseException1)
          {
            return "/admin/common/error";
          }
        }
        else
        {
          Object localObject1;
          if (localMemberAttribute.getType() == MemberAttributeType.area)
          {
            localObject1 = StringUtils.isNotEmpty(str) ? (Area)this.IIlIIIII.find(Long.valueOf(str)) : null;
            if (localObject1 != null)
              member.setArea((Area)localObject1);
            else if (localMemberAttribute.getIsRequired().booleanValue())
              return "/admin/common/error";
          }
          else
          {
            if (localMemberAttribute.getType() != MemberAttributeType.checkbox)
              continue;
            localObject1 = request.getParameterValues("memberAttribute_" + localMemberAttribute.getId());
            Object localObject2 = localObject1 != null ? Arrays.asList(localObject1) : null;
            if ((localMemberAttribute.getIsRequired().booleanValue()) && ((localObject2 == null) || (localObject2.isEmpty())))
              return "/admin/common/error";
            member.setAttributeValue(localMemberAttribute, localObject2);
          }
        }
      }
    }
    if (StringUtils.isEmpty(member.getPassword()))
      member.setPassword(localMember.getPassword());
    else
      member.setPassword(DigestUtils.md5Hex(member.getPassword()));
    if ((localMember.getIsLocked().booleanValue()) && (!member.getIsLocked().booleanValue()))
    {
      member.setLoginFailureCount(Integer.valueOf(0));
      member.setLockedDate(null);
    }
    else
    {
      member.setIsLocked(localMember.getIsLocked());
      member.setLoginFailureCount(localMember.getLoginFailureCount());
      member.setLockedDate(localMember.getLockedDate());
    }
    BeanUtils.copyProperties(member, localMember, new String[] { "username", "point", "amount", "balance", "registerIp", "loginIp", "loginDate", "safeKey", "cart", "orders", "deposits", "payments", "couponCodes", "receivers", "reviews", "consultations", "favoriteProducts", "productNotifies", "inMessages", "outMessages" });
    this.memberService.update(localMember, modifyPoint, modifyBalance, depositMemo, this.IIlIIIIl.getCurrent());
    IIIllIlI(redirectAttributes, IIIlllII);
    return (String)"redirect:list.jhtml";
  }

  @RequestMapping(value={"/list"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public String list(Pageable pageable, ModelMap model)
  {
    model.addAttribute("memberRanks", this.memberRankService.findAll());
    model.addAttribute("memberAttributes", this.memberAttributeService.findAll());
    model.addAttribute("page", this.memberService.findPage(pageable));
    return "/admin/member/list";
  }

  @RequestMapping(value={"/delete"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  @ResponseBody
  public Message delete(Long[] ids)
  {
    if (ids != null)
    {
      for (Long localLong : ids)
      {
        Member localMember = (Member)this.memberService.find(localLong);
        if ((localMember != null) && (localMember.getBalance().compareTo(new BigDecimal(0)) > 0))
          return Message.error("admin.member.deleteExistDepositNotAllowed", new Object[] { localMember.getUsername() });
      }
      this.memberService.delete(ids);
    }
    return IIIlllII;
  }
}

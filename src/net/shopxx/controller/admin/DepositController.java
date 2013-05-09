package net.shopxx.controller.admin;

import javax.annotation.Resource;
import net.shopxx.Pageable;
import net.shopxx.entity.Member;
import net.shopxx.service.DepositService;
import net.shopxx.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("adminDepositController")
@RequestMapping({"/admin/deposit"})
public class DepositController extends BaseController
{

  @Resource(name="depositServiceImpl")
  private DepositService depositService;

  @Resource(name="memberServiceImpl")
  private MemberService memberService;

  @RequestMapping(value={"/list"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public String list(Long memberId, Pageable pageable, ModelMap model)
  {
    Member localMember = (Member)this.memberService.find(memberId);
    if (localMember != null)
    {
      model.addAttribute("member", localMember);
      model.addAttribute("page", this.depositService.findPage(localMember, pageable));
    }
    else
    {
      model.addAttribute("page", this.depositService.findPage(pageable));
    }
    return "/admin/deposit/list";
  }
}

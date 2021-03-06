package net.shopxx.controller.shop.member;

import javax.annotation.Resource;
import net.shopxx.Pageable;
import net.shopxx.controller.shop.BaseController;
import net.shopxx.entity.Member;
import net.shopxx.service.ConsultationService;
import net.shopxx.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("shopMemberConsultationController")
@RequestMapping({ "/member/consultation" })
public class ConsultationController extends BaseController {
	private static final int IIIlllIl = 10;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "consultationServiceImpl")
	private ConsultationService consultationService;

	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String list(Integer pageNumber, ModelMap model) {
		Member localMember = this.memberService.getCurrent();
		Pageable localPageable = new Pageable(pageNumber, Integer.valueOf(10));
		model.addAttribute("page", this.consultationService.findPage(
				localMember, null, null, localPageable));
		return "shop/member/consultation/list";
	}
}

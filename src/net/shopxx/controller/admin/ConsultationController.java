package net.shopxx.controller.admin;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import net.shopxx.Message;
import net.shopxx.Pageable;
import net.shopxx.entity.Consultation;
import net.shopxx.service.ConsultationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("adminConsultationController")
@RequestMapping({ "/admin/consultation" })
public class ConsultationController extends BaseController {

	@Resource(name = "consultationServiceImpl")
	private ConsultationService consultationService;

	@RequestMapping(value = { "/reply" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String reply(Long id, ModelMap model) {
		model.addAttribute("consultation", this.consultationService.find(id));
		return "/admin/consultation/reply";
	}

	@RequestMapping(value = { "/reply" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	public String reply(Long id, String content, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		if (!IIIllIlI(Consultation.class, "content", content, new Class[0]))
			return "/admin/common/error";
		Consultation localConsultation1 = (Consultation) this.consultationService
				.find(id);
		if (localConsultation1 == null)
			return "/admin/common/error";
		Consultation localConsultation2 = new Consultation();
		localConsultation2.setContent(content);
		localConsultation2.setIp(request.getRemoteAddr());
		this.consultationService.reply(localConsultation1, localConsultation2);
		IIIllIlI(redirectAttributes, IIIlllII);
		return "redirect:reply.jhtml?id=" + id;
	}

	@RequestMapping(value = { "/edit" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String edit(Long id, ModelMap model) {
		model.addAttribute("consultation", this.consultationService.find(id));
		return "/admin/consultation/edit";
	}

	@RequestMapping(value = { "/update" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	public String update(Long id,
			@RequestParam(defaultValue = "false") Boolean isShow,
			RedirectAttributes redirectAttributes) {
		Consultation localConsultation = (Consultation) this.consultationService
				.find(id);
		if (localConsultation == null)
			return "/admin/common/error";
		if (isShow != localConsultation.getIsShow()) {
			localConsultation.setIsShow(isShow);
			this.consultationService.update(localConsultation);
		}
		IIIllIlI(redirectAttributes, IIIlllII);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page",
				this.consultationService.findPage(null, null, null, pageable));
		return "/admin/consultation/list";
	}

	@RequestMapping(value = { "/delete_reply" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Message deleteReply(Long id) {
		Consultation localConsultation = (Consultation) this.consultationService
				.find(id);
		if ((localConsultation == null)
				|| (localConsultation.getForConsultation() == null))
			return IIIllIll;
		this.consultationService.delete(localConsultation);
		return IIIlllII;
	}

	@RequestMapping(value = { "/delete" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Message delete(Long[] ids) {
		if (ids != null)
			this.consultationService.delete(ids);
		return IIIlllII;
	}
}

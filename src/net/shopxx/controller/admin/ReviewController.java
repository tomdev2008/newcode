package net.shopxx.controller.admin;

import javax.annotation.Resource;
import net.shopxx.Message;
import net.shopxx.Pageable;
import net.shopxx.entity.Review;
import net.shopxx.entity.Review.Type;
import net.shopxx.service.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("adminReviewController")
@RequestMapping({ "/admin/review" })
public class ReviewController extends BaseController {

	@Resource(name = "reviewServiceImpl")
	private ReviewService reviewService;

	@RequestMapping(value = { "/edit" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String edit(Long id, ModelMap model) {
		model.addAttribute("review", this.reviewService.find(id));
		return "/admin/review/edit";
	}

	@RequestMapping(value = { "/update" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	public String update(Long id,
			@RequestParam(defaultValue = "false") Boolean isShow,
			RedirectAttributes redirectAttributes) {
		Review localReview = (Review) this.reviewService.find(id);
		if (localReview == null)
			return "/admin/common/error";
		localReview.setIsShow(isShow);
		this.reviewService.update(localReview);
		IIIllIlI(redirectAttributes, IIIlllII);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String list(Review.Type type, Pageable pageable, ModelMap model) {
		model.addAttribute("type", type);
		model.addAttribute("types", Review.Type.values());
		model.addAttribute("page",
				this.reviewService.findPage(null, null, type, null, pageable));
		return "/admin/review/list";
	}

	@RequestMapping(value = { "/delete" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Message delete(Long[] ids) {
		this.reviewService.delete(ids);
		return IIIlllII;
	}
}

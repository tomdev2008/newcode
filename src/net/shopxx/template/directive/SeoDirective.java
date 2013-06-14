package net.shopxx.template.directive;

import java.util.Map;

import javax.annotation.Resource;

import net.shopxx.entity.Seo;
import net.shopxx.entity.Seo.SeoType;
import net.shopxx.service.SeoService;
import net.shopxx.util.FreemarkerUtils;

import org.springframework.stereotype.Component;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateModel;

@Component("seoDirective")
public class SeoDirective extends BaseDirective {
	private static final String IIIllIlI = "type";
	private static final String IIIllIll = "seo";

	@Resource(name = "seoServiceImpl")
	private SeoService seoService;

	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) {
		SeoType localType = (SeoType) FreemarkerUtils.getParameter("type",
				SeoType.class, params);
		boolean bool = IIIllIlI(env, params);
		String str = IIIllIll(env, params);
		Seo localSeo;
		if (bool)
			localSeo = this.seoService.find(localType, str);
		else
			localSeo = this.seoService.find(localType);
		IIIllIlI("seo", localSeo, env, body);
	}
}

package net.shopxx.template.directive;

import freemarker.core.Environment;
import freemarker.template.Template;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateModel;
import java.io.StringReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import net.shopxx.entity.AdPosition;
import net.shopxx.service.AdPositionService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

@Component("adPositionDirective")
public class AdPositionDirective extends BaseDirective {
	private static final String POSITION_VALUE_PARAMETER_NAME = "adPosition";

	@Resource(name = "freeMarkerConfigurer")
	private FreeMarkerConfigurer freeMarkerConfigurer;

	@Resource(name = "adPositionServiceImpl")
	private AdPositionService adPositionService;

	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) {
		Long localLong = IIIllIlI(params);
		boolean bool = IIIllIlI(env, params);
		String str = freeMarkerConfigurer(env, params);
		AdPosition localAdPosition;
		if (bool)
			localAdPosition = this.adPositionService.find(localLong, str);
		else
			localAdPosition = (AdPosition) this.adPositionService.find(localLong);
		if (body != null)
			IIIllIlI("adPosition", localAdPosition, env, body);
		else if ((localAdPosition != null)
				&& (localAdPosition.getTemplate() != null))
			try {
				HashMap localHashMap = new HashMap();
				localHashMap.put("adPosition", localAdPosition);
				Writer localWriter = env.getOut();
				new Template("adTemplate", new StringReader(
						localAdPosition.getTemplate()),
						this.freeMarkerConfigurer.getConfiguration()).process(localHashMap,
						localWriter);
			} catch (Exception localException1) {
				localException1.printStackTrace();
			}
	}
}

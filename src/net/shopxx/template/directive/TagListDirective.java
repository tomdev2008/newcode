package net.shopxx.template.directive;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateModel;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import net.shopxx.entity.Tag;
import net.shopxx.service.TagService;
import org.springframework.stereotype.Component;

@Component("tagListDirective")
public class TagListDirective extends BaseDirective {
	private static final String IIIllIlI = "tags";

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) {
		boolean bool = IIIllIlI(env, params);
		String str = tagService(env, params);
		Integer localInteger = tagService(params);
		List localList2 = IIIllIlI(params, Tag.class, new String[0]);
		List localList3 = IIIllIlI(params, new String[0]);
		List localList1;
		if (bool)
			localList1 = this.tagService.findList(localInteger, localList2,
					localList3, str);
		else
			localList1 = this.tagService.findList(localInteger, localList2,
					localList3);
		IIIllIlI("tags", localList1, env, body);
	}
}

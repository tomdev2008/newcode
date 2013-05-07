package net.shopxx.service;

import java.util.List;
import net.shopxx.Template;
import net.shopxx.Template.Type;

public abstract interface TemplateService
{
  public abstract List<Template> getAll();

  public abstract List<Template> getList(Template.Type paramType);

  public abstract Template get(String paramString);

  public abstract String read(String paramString);

  public abstract String read(Template paramTemplate);

  public abstract void write(String paramString1, String paramString2);

  public abstract void write(Template paramTemplate, String paramString);
}


 * Qualified Name:     net.shopxx.service.TemplateService


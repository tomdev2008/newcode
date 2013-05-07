package net.shopxx;

import java.io.Serializable;

public class Template
  implements Serializable
{
  private static final long serialVersionUID = -517540800437045215L;
  public enum TemplateType
  {
    page, mail, print;
  }
  private String id;
  private TemplateType type;
  private String name;
  private String templatePath;
  private String staticPath;
  private String description;

  public String getId()
  {
    return this.id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public TemplateType getType()
  {
    return this.type;
  }

  public void setType(TemplateType type)
  {
    this.type = type;
  }

  public String getName()
  {
    return this.name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getTemplatePath()
  {
    return this.templatePath;
  }

  public void setTemplatePath(String templatePath)
  {
    this.templatePath = templatePath;
  }

  public String getStaticPath()
  {
    return this.staticPath;
  }

  public void setStaticPath(String staticPath)
  {
    this.staticPath = staticPath;
  }

  public String getDescription()
  {
    return this.description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }
}

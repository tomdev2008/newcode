package net.shopxx.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.shopxx.CommonAttributes;
import net.shopxx.Setting;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.io.IOUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.core.io.ClassPathResource;

public final class SettingUtils
{
  private static final CacheManager CACHE_MANAGER = CacheManager.create();
  private static final BeanUtilsBean BEAN_UTILS_BEAN;

  static
  {
    SettingUtils.1 local1 = new SettingUtils.1();
    DateConverter localDateConverter = new DateConverter();
    localDateConverter.setPatterns(CommonAttributes.DATE_PATTERNS);
    local1.register(localDateConverter, Date.class);
    BEAN_UTILS_BEAN = new BeanUtilsBean(local1);
  }

  public static Setting get()
  {
    Ehcache ehcache = CACHE_MANAGER.getEhcache("setting");
    Element element = ehcache.get(Setting.CACHE_KEY);
    Setting setting;
    if (element != null)
    {
      setting = (Setting)element.getObjectValue();
    }
    else
    {
      setting = new Setting();
      try
      {
        File file = new ClassPathResource("/shopxx.xml").getFile();
        Document document = new SAXReader().read(file);
        List list = document.selectNodes("/shopxx/setting");
        Iterator localIterator = list.iterator();
        while (localIterator.hasNext())
        {
          org.dom4j.Element tempElement = (org.dom4j.Element)localIterator.next();
          String name = tempElement.attributeValue("name");
          String value = tempElement.attributeValue("value");
          try
          {
            BEAN_UTILS_BEAN.setProperty(setting, name, value);
          }
          catch (IllegalAccessException localIllegalAccessException)
          {
            localIllegalAccessException.printStackTrace();
          }
          catch (InvocationTargetException localInvocationTargetException)
          {
            localInvocationTargetException.printStackTrace();
          }
        }
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
      }
      ehcache.put(new net.sf.ehcache.Element(Setting.CACHE_KEY, setting));
    }
    return setting;
  }

  public static void set(Setting setting)
  {
    try
    {
      File localFile = new ClassPathResource("/shopxx.xml").getFile();
      Document document = new SAXReader().read(localFile);
      List<Node> list = document.selectNodes("/shopxx/setting");
      Iterator<Node> iterator = list.iterator();
      while (iterator.hasNext())
      {
    	  org.dom4j.Element element = (org.dom4j.Element)iterator.next();
        try
        {
          String str1 = element.attributeValue("name");
          String str2 = BEAN_UTILS_BEAN.getProperty(setting, str1);
          Attribute localAttribute = element.attribute("value");
          localAttribute.setValue(str2);
        }
        catch (IllegalAccessException localIllegalAccessException1)
        {
          localIllegalAccessException1.printStackTrace();
        }
        catch (InvocationTargetException localInvocationTargetException1)
        {
          localInvocationTargetException1.printStackTrace();
        }
        catch (NoSuchMethodException localNoSuchMethodException1)
        {
          localNoSuchMethodException1.printStackTrace();
        }
      }
      FileOutputStream fileOutputStream = null;
      XMLWriter xmlWriter = null;
      try
      {
        OutputFormat outputFormat = OutputFormat.createPrettyPrint();
        outputFormat.setEncoding("UTF-8");
        outputFormat.setIndent(true);
        outputFormat.setIndent("\t");
        outputFormat.setNewlines(true);
        fileOutputStream = new FileOutputStream(localFile);
        xmlWriter = new XMLWriter(fileOutputStream, outputFormat);
        xmlWriter.write(document);
      }
      catch (Exception localException3)
      {
        localException3.printStackTrace();
      }
      finally
      {
        if (fileOutputStream != null)
          try
          {
        	  xmlWriter.close();
          }
          catch (IOException localIOException4)
          {
          }
        IOUtils.closeQuietly(fileOutputStream);
      }
      CACHE_MANAGER.getEhcache("setting").put(new net.sf.ehcache.Element(Setting.CACHE_KEY, setting));
    }
    catch (Exception localException2)
    {
      localException2.printStackTrace();
    }
  }
}

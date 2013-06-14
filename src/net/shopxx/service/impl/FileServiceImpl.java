package net.shopxx.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import net.shopxx.FileInfo;
import net.shopxx.FileInfo.FileInfoFileType;
import net.shopxx.FileInfo.FileInfoOrderType;
import net.shopxx.Setting;
import net.shopxx.plugin.StoragePlugin;
import net.shopxx.service.FileService;
import net.shopxx.service.PluginService;
import net.shopxx.util.FreemarkerUtils;
import net.shopxx.util.SettingUtils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;

@Service("fileServiceImpl")
public class FileServiceImpl
  implements FileService, ServletContextAware
{
  private ServletContext servletContext;

  @Resource(name="taskExecutor")
  private TaskExecutor taskExecutor;

  @Resource(name="pluginServiceImpl")
  private PluginService pluginService;

  public void setServletContext(ServletContext servletContext)
  {
    this.servletContext = servletContext;
  }

  private void servletContext(StoragePlugin paramStoragePlugin, String paramString1, File paramFile, String paramString2)
  {
    this.taskExecutor.execute(new FileServiceImpl.1(this, paramFile, paramStoragePlugin, paramString1, paramString2));
  }

  public boolean isValid(FileInfoFileType fileType, MultipartFile multipartFile)
  {
    if (multipartFile == null)
      return false;
    Setting localSetting = SettingUtils.get();
    if ((localSetting.getUploadMaxSize() != null) && (localSetting.getUploadMaxSize().intValue() != 0) && (multipartFile.getSize() > localSetting.getUploadMaxSize().intValue() * 1024L * 1024L))
      return false;
    String[] arrayOfString;
    if (fileType == FileInfoFileType.flash)
      arrayOfString = localSetting.getUploadFlashExtensions();
    else if (fileType == FileInfoFileType.media)
      arrayOfString = localSetting.getUploadMediaExtensions();
    else if (fileType == FileInfoFileType.file)
      arrayOfString = localSetting.getUploadFileExtensions();
    else
      arrayOfString = localSetting.getUploadImageExtensions();
    if (ArrayUtils.isNotEmpty(arrayOfString))
      return FilenameUtils.isExtension(multipartFile.getOriginalFilename(), arrayOfString);
    return false;
  }

  public String upload(FileInfoFileType fileType, MultipartFile multipartFile, boolean async)
  {
    if (multipartFile == null)
      return null;
    Setting localSetting = SettingUtils.get();
    String str1;
    if (fileType == FileInfoFileType.flash)
      str1 = localSetting.getFlashUploadPath();
    else if (fileType == FileInfoFileType.media)
      str1 = localSetting.getMediaUploadPath();
    else if (fileType == FileInfoFileType.file)
      str1 = localSetting.getFileUploadPath();
    else
      str1 = localSetting.getImageUploadPath();
    try
    {
      HashMap localHashMap = new HashMap();
      localHashMap.put("uuid", UUID.randomUUID().toString());
      String str2 = FreemarkerUtils.process(str1, localHashMap);
      String str3 = str2 + UUID.randomUUID() + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());
      Iterator localIterator = this.pluginService.getStoragePlugins(true).iterator();
      if (localIterator.hasNext())
      {
        StoragePlugin localStoragePlugin = (StoragePlugin)localIterator.next();
        File localFile = new File(System.getProperty("java.io.tmpdir") + "/upload_" + UUID.randomUUID() + ".tmp");
        if (!localFile.getParentFile().exists())
          localFile.getParentFile().mkdirs();
        multipartFile.transferTo(localFile);
        if (async)
          servletContext(localStoragePlugin, str3, localFile, multipartFile.getContentType());
        else
          try
          {
            localStoragePlugin.upload(str3, localFile, multipartFile.getContentType());
          }
          finally
          {
            FileUtils.deleteQuietly(localFile);
          }
        return localStoragePlugin.getUrl(str3);
      }
    }
    catch (Exception localException1)
    {
      localException1.printStackTrace();
    }
    return null;
  }

  public String upload(FileInfoFileType fileType, MultipartFile multipartFile)
  {
    return upload(fileType, multipartFile, false);
  }

  public String uploadLocal(FileInfoFileType fileType, MultipartFile multipartFile)
  {
    if (multipartFile == null)
      return null;
    Setting localSetting = SettingUtils.get();
    String str1;
    if (fileType == FileInfoFileType.flash)
      str1 = localSetting.getFlashUploadPath();
    else if (fileType == FileInfoFileType.media)
      str1 = localSetting.getMediaUploadPath();
    else if (fileType == FileInfoFileType.file)
      str1 = localSetting.getFileUploadPath();
    else
      str1 = localSetting.getImageUploadPath();
    try
    {
      HashMap localHashMap = new HashMap();
      localHashMap.put("uuid", UUID.randomUUID().toString());
      String str2 = FreemarkerUtils.process(str1, localHashMap);
      String str3 = str2 + UUID.randomUUID() + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());
      File localFile = new File(this.servletContext.getRealPath(str3));
      if (!localFile.getParentFile().exists())
        localFile.getParentFile().mkdirs();
      multipartFile.transferTo(localFile);
      return str3;
    }
    catch (Exception localException1)
    {
      localException1.printStackTrace();
    }
    return null;
  }

  public List<FileInfo> browser(String path, FileInfoFileType fileType, FileInfoOrderType orderType)
  {
    if (path != null)
    {
      if (!path.startsWith("/"))
        path = "/" + path;
      if (!path.endsWith("/"))
        path = path + "/";
    }
    else
    {
      path = "/";
    }
    Setting localSetting = SettingUtils.get();
    String str1;
    if (fileType == FileInfoFileType.flash)
      str1 = localSetting.getFlashUploadPath();
    else if (fileType == FileInfoFileType.media)
      str1 = localSetting.getMediaUploadPath();
    else if (fileType == FileInfoFileType.file)
      str1 = localSetting.getFileUploadPath();
    else
      str1 = localSetting.getImageUploadPath();
    String str2 = StringUtils.substringBefore(str1, "${");
    str2 = StringUtils.substringBeforeLast(str2, "/") + path;
    Object localObject = new ArrayList();
    if (str2.indexOf("..") >= 0)
      return localObject;
    Iterator localIterator = this.pluginService.getStoragePlugins(true).iterator();
    if (localIterator.hasNext())
    {
      StoragePlugin localStoragePlugin = (StoragePlugin)localIterator.next();
      localObject = localStoragePlugin.browser(str2);
    }
    if (orderType == FileInfoOrderType.size)
      Collections.sort((List)localObject, new FileServiceImpl.SizeComparator(this, null));
    else if (orderType == FileInfoOrderType.type)
      Collections.sort((List)localObject, new FileServiceImpl.TypeComparator(this, null));
    else
      Collections.sort((List)localObject, new FileServiceImpl.NameComparator(this, null));
    return (List<FileInfo>)localObject;
  }
  private class NameComparator implements Comparator<FileInfo>{

	final FileServiceImpl fileServiceImpl;

	public int compare(FileInfo fileInfos1, FileInfo fileInfos2)
	{
		return (new CompareToBuilder()).append(!fileInfos1.getIsDirectory().booleanValue(), !fileInfos2.getIsDirectory().booleanValue()).append(fileInfos1.getName(), fileInfos2.getName()).toComparison();
	}

	public volatile int compare(Object obj, Object obj1)
	{
		return compare((FileInfo)obj, (FileInfo)obj1);
	}

	private NameComparator()
	{
		fileServiceImpl = FileServiceImpl.this;
		super();
	}

	NameComparator(NameComparator namecomparator)
	{
		this();
	}
}
}



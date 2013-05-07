package net.shopxx.service;

import java.util.List;
import net.shopxx.FileInfo;
import net.shopxx.FileInfo.FileType;
import net.shopxx.FileInfo.OrderType;
import org.springframework.web.multipart.MultipartFile;

public abstract interface FileService
{
  public abstract boolean isValid(FileInfo.FileType paramFileType, MultipartFile paramMultipartFile);

  public abstract String upload(FileInfo.FileType paramFileType, MultipartFile paramMultipartFile, boolean paramBoolean);

  public abstract String upload(FileInfo.FileType paramFileType, MultipartFile paramMultipartFile);

  public abstract String uploadLocal(FileInfo.FileType paramFileType, MultipartFile paramMultipartFile);

  public abstract List<FileInfo> browser(String paramString, FileInfo.FileType paramFileType, FileInfo.OrderType paramOrderType);
}


 * Qualified Name:     net.shopxx.service.FileService


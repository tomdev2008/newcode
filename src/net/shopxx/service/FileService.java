package net.shopxx.service;

import java.util.List;

import net.shopxx.FileInfo;
import net.shopxx.FileInfo.FileInfoFileType;
import net.shopxx.FileInfo.FileInfoOrderType;

import org.springframework.web.multipart.MultipartFile;

public abstract interface FileService
{
  public abstract boolean isValid(FileInfoFileType paramFileType, MultipartFile paramMultipartFile);

  public abstract String upload(FileInfoFileType paramFileType, MultipartFile paramMultipartFile, boolean paramBoolean);

  public abstract String upload(FileInfoFileType paramFileType, MultipartFile paramMultipartFile);

  public abstract String uploadLocal(FileInfoFileType paramFileType, MultipartFile paramMultipartFile);

  public abstract List<FileInfo> browser(String paramString, FileInfoFileType paramFileType, FileInfoOrderType paramOrderType);
}

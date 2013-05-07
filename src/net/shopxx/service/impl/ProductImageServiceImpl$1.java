package net.shopxx.service.impl;

import java.io.File;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletContext;
import net.shopxx.Setting;
import net.shopxx.plugin.StoragePlugin;
import net.shopxx.util.ImageUtils;
import net.shopxx.util.SettingUtils;
import org.apache.commons.io.FileUtils;

class ProductImageServiceImpl$1
  implements Runnable
{
  public void run()
  {
    Collections.sort(ProductImageServiceImpl.IIIllIlI(this.IIIllIlI));
    Iterator localIterator = ProductImageServiceImpl.IIIllIlI(this.IIIllIlI).iterator();
    while (localIterator.hasNext())
    {
      StoragePlugin localStoragePlugin = (StoragePlugin)localIterator.next();
      if (!localStoragePlugin.getIsEnabled())
        continue;
      Setting localSetting = SettingUtils.get();
      String str = System.getProperty("java.io.tmpdir");
      File localFile1 = new File(ProductImageServiceImpl.IIIllIll(this.IIIllIlI).getRealPath(localSetting.getWatermarkImage()));
      File localFile2 = new File(str + "/upload_" + UUID.randomUUID() + "." + "jpg");
      File localFile3 = new File(str + "/upload_" + UUID.randomUUID() + "." + "jpg");
      File localFile4 = new File(str + "/upload_" + UUID.randomUUID() + "." + "jpg");
      try
      {
        ImageUtils.zoom(this.IIIllIll, localFile2, localSetting.getLargeProductImageWidth().intValue(), localSetting.getLargeProductImageHeight().intValue());
        ImageUtils.addWatermark(localFile2, localFile2, localFile1, localSetting.getWatermarkPosition(), localSetting.getWatermarkAlpha().intValue());
        ImageUtils.zoom(this.IIIllIll, localFile3, localSetting.getMediumProductImageWidth().intValue(), localSetting.getMediumProductImageHeight().intValue());
        ImageUtils.addWatermark(localFile3, localFile3, localFile1, localSetting.getWatermarkPosition(), localSetting.getWatermarkAlpha().intValue());
        ImageUtils.zoom(this.IIIllIll, localFile4, localSetting.getThumbnailProductImageWidth().intValue(), localSetting.getThumbnailProductImageHeight().intValue());
        localStoragePlugin.upload(this.IIIlllII, this.IIIllIll, this.IIIlllIl);
        localStoragePlugin.upload(this.IIIllllI, localFile2, "image/jpeg");
        localStoragePlugin.upload(this.IIIlllll, localFile3, "image/jpeg");
        localStoragePlugin.upload(this.IIlIIIII, localFile4, "image/jpeg");
      }
      finally
      {
        FileUtils.deleteQuietly(this.IIIllIll);
        FileUtils.deleteQuietly(localFile2);
        FileUtils.deleteQuietly(localFile3);
        FileUtils.deleteQuietly(localFile4);
      }
      break;
    }
  }
}


 * Qualified Name:     net.shopxx.service.impl.ProductImageServiceImpl.1


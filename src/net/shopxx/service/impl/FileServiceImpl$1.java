package net.shopxx.service.impl;

import java.io.File;
import net.shopxx.plugin.StoragePlugin;
import org.apache.commons.io.FileUtils;

class FileServiceImpl$1 implements Runnable {
	public void run() {
		try {
			this.IIIlllII.upload(this.IIIlllIl, this.IIIllIll, this.IIIllllI);
		} finally {
			FileUtils.deleteQuietly(this.IIIllIll);
		}
	}
}

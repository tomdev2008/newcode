package net.shopxx.service.impl;

import java.util.Comparator;
import net.shopxx.FileInfo;
import org.apache.commons.lang.builder.CompareToBuilder;

class FileServiceImpl$SizeComparator implements Comparator<FileInfo> {
	private FileServiceImpl$SizeComparator(FileServiceImpl paramFileServiceImpl) {
	}

	public int compare(FileInfo fileInfos1, FileInfo fileInfos2) {
		return new CompareToBuilder()
				.append(!fileInfos1.getIsDirectory().booleanValue(),
						!fileInfos2.getIsDirectory().booleanValue())
				.append(fileInfos1.getSize(), fileInfos2.getSize())
				.toComparison();
	}
}

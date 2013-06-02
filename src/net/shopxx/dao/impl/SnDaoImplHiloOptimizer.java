package net.shopxx.dao.impl;

import net.shopxx.entity.Sn;
import net.shopxx.entity.Sn.SnType;
import net.shopxx.util.FreemarkerUtils;

class SnDaoImplHiloOptimizer {
	private SnType snType;
	private String IIIlllII;
	private int IIIlllIl;
	private int IIIllllI;
	private long IIIlllll;
	private long IIlIIIII;

	public SnDaoImplHiloOptimizer(SnDaoImpl paramSnDaoImpl, SnType type,
			String prefix, int maxLo) {
		this.snType = type;
		this.IIIlllII = (prefix != null ? prefix.replace("{", "${") : "");
		this.IIIlllIl = maxLo;
		this.IIIllllI = (maxLo + 1);
	}

	public synchronized String generate() {
		if (this.IIIllllI > this.IIIlllIl) {
			this.IIlIIIII = SnDaoImpl.IIIllIlI(this.IIIllIlI, this.snType);
			this.IIIllllI = (this.IIlIIIII == 0L ? 1 : 0);
			this.IIIlllll = (this.IIlIIIII * (this.IIIlllIl + 1));
		}
		return FreemarkerUtils.process(this.IIIlllII, null)
				+ (this.IIIlllll + this.IIIllllI++);
	}
}

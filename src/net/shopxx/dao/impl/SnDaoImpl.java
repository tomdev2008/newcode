package net.shopxx.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

import net.shopxx.dao.SnDao;
import net.shopxx.entity.Sn;
import net.shopxx.entity.Sn.SnType;
import net.shopxx.util.FreemarkerUtils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

@Repository("snDaoImpl")
public class SnDaoImpl implements SnDao, InitializingBean {
	private SnType snType;
	private String IIIlllII;
	private int maxLo;
	private int IIIllllI;
	private long IIIlllll;
	private long IIlIIIII;

	private SnDaoImpl IIIllIlI;
	private SnDaoImpl IIIllIll;
	private SnDaoImpl IIIlllII;
	private SnDaoImpl IIIlllIl;
	private SnDaoImpl IIIllllI;
	private SnDaoImpl IIIlllll;

	public SnDaoImpl(SnDaoImpl snDaoImpl, SnType type, String prefix, int maxLo) {
		this.snType = type;
		this.IIIlllII = (prefix != null ? prefix.replace("{", "${") : "");
		this.IIIlllIl = maxLo;
		this.IIIllllI = (maxLo + 1);
	}

	@PersistenceContext
	private EntityManager entityManager;

	@Value("${sn.product.prefix}")
	private String snProductPrefix;

	@Value("${sn.product.maxLo}")
	private int snProductMaxLo;

	@Value("${sn.order.prefix}")
	private String snOrderPerfix;

	@Value("${sn.order.maxLo}")
	private int snOrderMaxLo;

	@Value("${sn.payment.prefix}")
	private String snPaymentPrefix;

	@Value("${sn.payment.maxLo}")
	private int snPaymentMaxLo;

	@Value("${sn.refunds.prefix}")
	private String snRefundsPrefix;

	@Value("${sn.refunds.maxLo}")
	private int snRefundsMaxLo;

	@Value("${sn.shipping.prefix}")
	private String snShippingPrefix;

	@Value("${sn.shipping.maxLo}")
	private int snShippingMaxLo;

	@Value("${sn.returns.prefix}")
	private String snReturnsPrefix;

	@Value("${sn.returns.maxLo}")
	private int snReturnsMaxLo;

	public void afterPropertiesSet() {
		this.IIIllIlI = new SnDaoImpl(this, SnType.product,
				this.snProductPrefix, this.snProductMaxLo);
		this.IIIllIll = new SnDaoImpl(this, SnType.order, this.snOrderPerfix,
				this.snOrderMaxLo);
		this.IIIlllII = new SnDaoImpl(this, SnType.payment,
				this.snPaymentPrefix, this.snPaymentMaxLo);
		this.IIIlllIl = new SnDaoImpl(this, SnType.refunds,
				this.snRefundsPrefix, this.snRefundsMaxLo);
		this.IIIllllI = new SnDaoImpl(this, SnType.shipping,
				this.snShippingPrefix, this.snShippingMaxLo);
		this.IIIlllll = new SnDaoImpl(this, SnType.returns,
				this.snReturnsPrefix, this.snReturnsMaxLo);
	}

	public String generate(SnType type) {
		Assert.notNull(type);
		if (type == SnType.product)
			return this.IIIllIlI.generate();
		if (type == SnType.order)
			return this.IIIllIll.generate();
		if (type == SnType.payment)
			return this.IIIlllII.generate();
		if (type == SnType.refunds)
			return this.IIIlllIl.generate();
		if (type == SnType.shipping)
			return this.IIIllllI.generate();
		if (type == SnType.returns)
			return this.IIIlllll.generate();
		return null;
	}

	private long IIIllIlI(SnType paramType) {
		String str = "select sn from Sn sn where sn.type = :type";
		Sn localSn = (Sn) this.entityManager.createQuery(str, Sn.class)
				.setFlushMode(FlushModeType.COMMIT)
				.setParameter("type", paramType)
				.setLockMode(LockModeType.PESSIMISTIC_WRITE).getSingleResult();
		long l = localSn.getLastValue().longValue();
		localSn.setLastValue(Long.valueOf(l + 1L));
		this.entityManager.merge(localSn);
		return l;
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

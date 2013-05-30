package net.shopxx.service.impl;

import java.util.List;
import javax.annotation.Resource;
import javax.persistence.LockModeType;
import net.shopxx.Page;
import net.shopxx.Pageable;
import net.shopxx.dao.CouponCodeDao;
import net.shopxx.dao.MemberDao;
import net.shopxx.entity.Coupon;
import net.shopxx.entity.CouponCode;
import net.shopxx.entity.Member;
import net.shopxx.service.CouponCodeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service("couponCodeServiceImpl")
public class CouponCodeServiceImpl extends BaseServiceImpl<CouponCode, Long>
		implements CouponCodeService {

	@Resource(name = "couponCodeDaoImpl")
	private CouponCodeDao CouponCodeDao;

	@Resource(name = "memberDaoImpl")
	private MemberDao memberDao;

	@Resource(name = "couponCodeDaoImpl")
	public void setBaseDao(CouponCodeDao couponCodeDao) {
		super.setBaseDao(couponCodeDao);
	}

	@Transactional(readOnly = true)
	public boolean codeExists(String code) {
		return this.CouponCodeDao.codeExists(code);
	}

	@Transactional(readOnly = true)
	public CouponCode findByCode(String code) {
		return this.CouponCodeDao.findByCode(code);
	}

	public CouponCode build(Coupon coupon, Member member) {
		return this.CouponCodeDao.build(coupon, member);
	}

	public List<CouponCode> build(Coupon coupon, Member member, Integer count) {
		return this.CouponCodeDao.build(coupon, member, count);
	}

	public CouponCode exchange(Coupon coupon, Member member) {
		Assert.notNull(coupon);
		Assert.notNull(member);
		this.memberDao.lock(member, LockModeType.PESSIMISTIC_WRITE);
		member.setPoint(Long.valueOf(member.getPoint().longValue()
				- coupon.getPoint().intValue()));
		this.memberDao.merge(member);
		return this.CouponCodeDao.build(coupon, member);
	}

	@Transactional(readOnly = true)
	public Page<CouponCode> findPage(Member member, Pageable pageable) {
		return this.CouponCodeDao.findPage(member, pageable);
	}

	@Transactional(readOnly = true)
	public Long count(Coupon coupon, Member member, Boolean hasBegun,
			Boolean hasExpired, Boolean isUsed) {
		return this.CouponCodeDao.count(coupon, member, hasBegun, hasExpired,
				isUsed);
	}
}

package net.shopxx.dao.impl;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import net.shopxx.Page;
import net.shopxx.Pageable;
import net.shopxx.dao.DepositDao;
import net.shopxx.entity.Deposit;
import net.shopxx.entity.Member;

import org.springframework.stereotype.Repository;

@Repository("depositDaoImpl")
public class DepositDaoImpl extends BaseDaoImpl<Deposit, Long> implements
		DepositDao {
	public Page<Deposit> findPage(Member member, Pageable pageable) {
		if (member == null){
			return new Page<Deposit>(null, 0L, pageable);
		}	
		CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Deposit> cq = cb.createQuery(Deposit.class);
		Root<Deposit> root = cq.from(Deposit.class);
		cq.select(root);
		cq.where(cb.equal(root.get("member"), member));
		return super.entityManager(cq, pageable);
	}
}

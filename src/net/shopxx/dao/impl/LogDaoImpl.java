package net.shopxx.dao.impl;

import javax.persistence.FlushModeType;

import net.shopxx.dao.LogDao;
import net.shopxx.entity.Log;

import org.springframework.stereotype.Repository;

@Repository("logDaoImpl")
public class LogDaoImpl extends BaseDaoImpl<Log, Long> implements LogDao {
	public void removeAll() {
		String str = "delete from Log log";
		this.entityManager.createQuery(str).setFlushMode(FlushModeType.COMMIT)
				.executeUpdate();
	}
}

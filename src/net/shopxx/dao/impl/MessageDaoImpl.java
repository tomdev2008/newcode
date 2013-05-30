package net.shopxx.dao.impl;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.shopxx.Page;
import net.shopxx.Pageable;
import net.shopxx.dao.MessageDao;
import net.shopxx.entity.Member;
import net.shopxx.entity.Message;

import org.springframework.stereotype.Repository;

@Repository("messageDaoImpl")
public class MessageDaoImpl extends BaseDaoImpl<Message, Long> implements
		MessageDao {
	public Page<Message> findPage(Member member, Pageable pageable) {
		CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Message> cq = cb.createQuery(Message.class);
		Root<Message> localRoot = cq.from(Message.class);
		cq.select(localRoot);
		Predicate predicate = cb.conjunction();
		predicate = cb.and(new Predicate[] { predicate,
				cb.isNull(localRoot.get("forMessage")),
				cb.equal(localRoot.get("isDraft"), Boolean.valueOf(false)) });
		if (member != null)
			predicate = cb.and(
					predicate,
					cb.or(cb.and(
							cb.equal(localRoot.get("sender"), member),
							cb.equal(localRoot.get("senderDelete"),
									Boolean.valueOf(false))), cb.and(
							cb.equal(localRoot.get("receiver"), member),
							cb.equal(localRoot.get("receiverDelete"),
									Boolean.valueOf(false)))));
		else
			predicate = cb.and(
					predicate,
					cb.or(cb.and(
							cb.isNull(localRoot.get("sender")),
							cb.equal(localRoot.get("senderDelete"),
									Boolean.valueOf(false))), cb.and(
							cb.isNull(localRoot.get("receiver")),
							cb.equal(localRoot.get("receiverDelete"),
									Boolean.valueOf(false)))));
		cq.where(predicate);
		return super.entityManager(cq, pageable);
	}

	public Page<Message> findDraftPage(Member sender, Pageable pageable) {
		CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Message> cq = cb.createQuery(Message.class);
		Root<Message> localRoot = cq.from(Message.class);
		cq.select(localRoot);
		Predicate predicate = cb.conjunction();
		predicate = cb.and(new Predicate[] { predicate,
				cb.isNull(localRoot.get("forMessage")),
				cb.equal(localRoot.get("isDraft"), Boolean.valueOf(true)) });
		if (sender != null)
			predicate = cb.and(predicate,
					cb.equal(localRoot.get("sender"), sender));
		else
			predicate = cb.and(predicate, cb.isNull(localRoot.get("sender")));
		cq.where(predicate);
		return super.entityManager(cq, pageable);
	}

	public Long count(Member member, Boolean read) {
		CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Message> cq = cb.createQuery(Message.class);
		Root<Message> localRoot = cq.from(Message.class);
		cq.select(localRoot);
		Predicate predicate = cb.conjunction();
		predicate = cb.and(new Predicate[] {predicate,cb.isNull(localRoot.get("forMessage")),cb.equal(localRoot.get("isDraft"),	Boolean.valueOf(false)) });
		if (member != null) {
			if (read != null)
				predicate = cb.and(predicate,cb.or(cb.and(new Predicate[] {
												cb.equal(
														localRoot.get("sender"),
														member),
												cb.equal(
														localRoot
																.get("senderDelete"),
														Boolean.valueOf(false)),
												cb.equal(
														localRoot
																.get("senderRead"),
														read) }),
										cb.and(new Predicate[] {
												cb.equal(
														localRoot
																.get("receiver"),
														member),
												cb.equal(
														localRoot
																.get("receiverDelete"),
														Boolean.valueOf(false)),
												cb.equal(
														localRoot
																.get("receiverRead"),
														read) })));
			else
				predicate = cb.and(predicate,
						cb.or(cb.and(
								cb.equal(
										localRoot.get("sender"), member),
								cb.equal(
										localRoot.get("senderDelete"),
										Boolean.valueOf(false))),
								cb.and(cb
										.equal(localRoot.get("receiver"),
												member), cb
										.equal(localRoot.get("receiverDelete"),
												Boolean.valueOf(false)))));
		} else if (read != null) {
			predicate = cb
					.and(predicate,
							cb.or(
									cb.and(new Predicate[] {
											cb
													.isNull(localRoot
															.get("sender")),
											cb.equal(
													localRoot
															.get("senderDelete"),
													Boolean.valueOf(false)),
											cb.equal(
													localRoot.get("senderRead"),
													read) }),
									cb.and(new Predicate[] {
											cb
													.isNull(localRoot
															.get("receiver")),
											cb.equal(
													localRoot
															.get("receiverDelete"),
													Boolean.valueOf(false)),
											cb.equal(
													localRoot
															.get("receiverRead"),
													read) })));
		} else {
			predicate = cb.and(predicate,
					cb.or(
							cb.and(cb
									.isNull(localRoot.get("sender")),
									cb.equal(
											localRoot.get("senderDelete"),
											Boolean.valueOf(false))),
							cb.and(cb
									.isNull(localRoot.get("receiver")),
									cb.equal(
											localRoot.get("receiverDelete"),
											Boolean.valueOf(false)))));
			cq.where(predicate);
		}
		return null;
	}

	public void remove(Long id, Member member) {
		Message localMessage = (Message) super.find(id);
		if ((localMessage == null) || (localMessage.getForMessage() != null))
			return;
		if (member == localMessage.getReceiver()) {
			if (!localMessage.getIsDraft().booleanValue())
				if (localMessage.getSenderDelete().booleanValue()) {
					super.remove(localMessage);
				} else {
					localMessage.setReceiverDelete(Boolean.valueOf(true));
					super.merge(localMessage);
				}
		} else if (member == localMessage.getSender())
			if (localMessage.getIsDraft().booleanValue()) {
				super.remove(localMessage);
			} else if (localMessage.getReceiverDelete().booleanValue()) {
				super.remove(localMessage);
			} else {
				localMessage.setSenderDelete(Boolean.valueOf(true));
				super.merge(localMessage);
			}
	}
}

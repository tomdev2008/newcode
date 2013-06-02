package net.shopxx.entity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "xx_area")
public class Area extends OrderEntity {
	private static final long serialVersionUID = -2158109459123036967L;
	private static final String IIIllIlI = ",";
	private String name;
	private String fullName;
	private String treePath;
	private Area parent;
	private Set<Area> children = new HashSet<Area>();
	private Set<Member> members = new HashSet<Member>();
	private Set<Receiver> receivers = new HashSet<Receiver>();
	private Set<Order> orders = new HashSet<Order>();
	private Set<DeliveryCenter> deliveryCenters = new HashSet<DeliveryCenter>();

	@NotEmpty
	@Length(max = 100)
	@Column(nullable = false, length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(nullable = false, length = 500)
	public String getFullName() {
		return this.fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Column(nullable = false, updatable = false)
	public String getTreePath() {
		return this.treePath;
	}

	public void setTreePath(String treePath) {
		this.treePath = treePath;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Area getParent() {
		return this.parent;
	}

	public void setParent(Area parent) {
		this.parent = parent;
	}

	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = { javax.persistence.CascadeType.REMOVE })
	@OrderBy("order asc")
	public Set<Area> getChildren() {
		return this.children;
	}

	public void setChildren(Set<Area> children) {
		this.children = children;
	}

	@OneToMany(mappedBy = "area", fetch = FetchType.LAZY)
	public Set<Member> getMembers() {
		return this.members;
	}

	public void setMembers(Set<Member> members) {
		this.members = members;
	}

	@OneToMany(mappedBy = "area", fetch = FetchType.LAZY)
	public Set<Receiver> getReceivers() {
		return this.receivers;
	}

	public void setReceivers(Set<Receiver> receivers) {
		this.receivers = receivers;
	}

	@OneToMany(mappedBy = "area", fetch = FetchType.LAZY)
	public Set<Order> getOrders() {
		return this.orders;
	}

	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}

	@OneToMany(mappedBy = "area", fetch = FetchType.LAZY)
	public Set<DeliveryCenter> getDeliveryCenters() {
		return this.deliveryCenters;
	}

	public void setDeliveryCenters(Set<DeliveryCenter> deliveryCenters) {
		this.deliveryCenters = deliveryCenters;
	}

	@PrePersist
	public void prePersist() {
		Area localArea = getParent();
		if (localArea != null) {
			setFullName(localArea.getFullName() + getName());
			setTreePath(localArea.getTreePath() + localArea.getId() + ",");
		} else {
			setFullName(getName());
			setTreePath(",");
		}
	}

	@PreUpdate
	public void preUpdate() {
		Area localArea = getParent();
		if (localArea != null)
			setFullName(localArea.getFullName() + getName());
		else
			setFullName(getName());
	}

	@PreRemove
	public void preRemove() {
		Set<Member> localSet = getMembers();
		Member member;
		if (localSet != null) {
			Iterator<Member> memberIterator = localSet.iterator();
			while (memberIterator.hasNext()) {
				member = memberIterator.next();
				member.setArea(null);
			}
		}
		Set<Receiver> receiverSet = getReceivers();
		Receiver receiver;
		if (receiverSet != null) {
			Iterator<Receiver> receiverIterator = receiverSet.iterator();
			while (receiverIterator.hasNext()) {
				receiver = receiverIterator.next();
				receiver.setArea(null);
			}
		}
		Set<Order> orderSet = getOrders();
		Order order;
		if (orderSet != null) {
			Iterator<Order> orderIterator = orderSet.iterator();
			while (orderIterator.hasNext()) {
				order = orderIterator.next();
				order.setArea(null);
			}
		}
		Set<DeliveryCenter> deliveryCenterSet = getDeliveryCenters();
		DeliveryCenter deliveryCenter;
		if (deliveryCenterSet != null) {
			Iterator<DeliveryCenter> localIterator = deliveryCenterSet.iterator();
			while (localIterator.hasNext()) {
				deliveryCenter = localIterator.next();
				deliveryCenter.setArea(null);
			}
		}
	}

	public String toString() {
		return getFullName();
	}
}

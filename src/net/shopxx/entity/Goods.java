package net.shopxx.entity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "xx_goods")
public class Goods extends BaseEntity {
	private static final long serialVersionUID = -6977025562650112419L;
	private Set<Product> products = new HashSet<Product>();

	@OneToMany(mappedBy = "goods", fetch = FetchType.EAGER, cascade = { javax.persistence.CascadeType.ALL }, orphanRemoval = true)
	public Set<Product> getProducts() {
		return this.products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	@Transient
	public Set<SpecificationValue> getSpecificationValues() {
		Set<SpecificationValue> localHashSet = new HashSet<SpecificationValue>();
		Set<Product> productSet = getProducts();
		if (productSet != null) {
			Iterator<Product> localIterator = productSet.iterator();
			while (localIterator.hasNext()) {
				Product localProduct = localIterator.next();
				localHashSet.addAll(localProduct.getSpecificationValues());
			}
		}
		return localHashSet;
	}
}

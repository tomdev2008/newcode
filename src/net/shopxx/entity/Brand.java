package net.shopxx.entity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "xx_brand")
public class Brand extends OrderEntity {
	private static final long serialVersionUID = -6109590619136943215L;

	public enum BrandType {
		text, image;
	}

	private static final String IIIllIlI = "/brand/content";
	private static final String IIIllIll = ".jhtml";
	private String name;
	private BrandType type;
	private String logo;
	private String url;
	private String introduction;
	private Set<Product> products = new HashSet<Product>();
	private Set<ProductCategory> productCategories = new HashSet<ProductCategory>();
	private Set<Promotion> promotions = new HashSet<Promotion>();

	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotNull
	@Column(nullable = false)
	public BrandType getType() {
		return this.type;
	}

	public void setType(BrandType type) {
		this.type = type;
	}

	@Length(max = 200)
	public String getLogo() {
		return this.logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	@Length(max = 200)
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Lob
	public String getIntroduction() {
		return this.introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	@OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
	public Set<Product> getProducts() {
		return this.products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	@ManyToMany(mappedBy = "brands", fetch = FetchType.LAZY)
	@OrderBy("order asc")
	public Set<ProductCategory> getProductCategories() {
		return this.productCategories;
	}

	public void setProductCategories(Set<ProductCategory> productCategories) {
		this.productCategories = productCategories;
	}

	@ManyToMany(mappedBy = "brands", fetch = FetchType.LAZY)
	public Set<Promotion> getPromotions() {
		return this.promotions;
	}

	public void setPromotions(Set<Promotion> promotions) {
		this.promotions = promotions;
	}

	@Transient
	public String getPath() {
		if (getId() != null)
			return "/brand/content/" + getId() + ".jhtml";
		return null;
	}

	@PreRemove
	public void preRemove() {
		Set<Product> localSet = getProducts();
		Product product;
		if (localSet != null) {
			Iterator<Product> productIterator = localSet.iterator();
			while (productIterator.hasNext()) {
				product = productIterator.next();
				product.setBrand(null);
			}
		}
		Set<ProductCategory> productCategorySet = getProductCategories();
		ProductCategory productCategory;
		if (productCategorySet != null) {
			Iterator<ProductCategory> productCategoryIterator = productCategorySet
					.iterator();
			while (productCategoryIterator.hasNext()) {
				productCategory = productCategoryIterator.next();
				productCategory.getBrands().remove(this);
			}
		}
		Set<Promotion> promotionSet = getPromotions();
		Promotion promotion;
		if (promotionSet != null) {
			Iterator<Promotion> localIterator = promotionSet.iterator();
			while (localIterator.hasNext()) {
				promotion = localIterator.next();
				promotion.getBrands().remove(this);
			}
		}
	}
}

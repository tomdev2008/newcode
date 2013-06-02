package net.shopxx.entity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "xx_tag")
public class Tag extends OrderEntity {
	private static final long serialVersionUID = -2735037966597250149L;

	public enum TagType {
		article, product;
	}

	private String name;
	private TagType type;
	private String icon;
	private String memo;
	private Set<Article> articles = new HashSet<Article>();
	private Set<Product> products = new HashSet<Product>();

	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotNull(groups = { BaseEntity.class })
	@Column(nullable = false, updatable = false)
	public TagType getType() {
		return this.type;
	}

	public void setType(TagType type) {
		this.type = type;
	}

	@Length(max = 200)
	public String getIcon() {
		return this.icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Length(max = 200)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
	public Set<Article> getArticles() {
		return this.articles;
	}

	public void setArticles(Set<Article> articles) {
		this.articles = articles;
	}

	@ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
	public Set<Product> getProducts() {
		return this.products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	@PreRemove
	public void preRemove() {
		Set<Article> localSet = getArticles();
		Article article;
		if (localSet != null) {
			Iterator<Article> articleIterator = localSet.iterator();
			while (articleIterator.hasNext()) {
				article = articleIterator.next();
				article.getTags().remove(this);
			}
		}
		Set<Product> productSet = getProducts();
		Product product;
		if (productSet != null) {
			Iterator<Product> localIterator = productSet.iterator();
			while (localIterator.hasNext()) {
				product = localIterator.next();
				product.getTags().remove(this);
			}
		}
	}
}

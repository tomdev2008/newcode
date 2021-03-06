package net.shopxx.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "xx_seo")
public class Seo extends BaseEntity {
	private static final long serialVersionUID = -3503657242384822672L;

	public enum SeoType {
		index, productList, productSearch, productContent, articleList, articleSearch, articleContent, brandList, brandContent;
	}

	private SeoType type;
	private String title;
	private String keywords;
	private String description;

	@Column(nullable = false, updatable = false, unique = true)
	public SeoType getType() {
		return this.type;
	}

	public void setType(SeoType type) {
		this.type = type;
	}

	@Length(max = 200)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Length(max = 200)
	public String getKeywords() {
		return this.keywords;
	}

	public void setKeywords(String keywords) {
		if (keywords != null)
			keywords = keywords.replaceAll("[,\\s]*,[,\\s]*", ",").replaceAll(
					"^,|,$", "");
		this.keywords = keywords;
	}

	@Length(max = 200)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}

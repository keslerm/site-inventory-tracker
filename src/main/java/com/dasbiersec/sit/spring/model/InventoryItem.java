package com.dasbiersec.sit.spring.model;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "inventory_item")
public class InventoryItem implements Serializable
{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scraper_sequence")
	@SequenceGenerator(name = "scraper_sequence", sequenceName = "scraper_sequence", allocationSize = 1)
	private Long id;
	private String name;
	private String price;
	private String status;
	private String identifier;
	private String url;

	@Column(name = "in_stock")
	private Boolean inStock;

	@Column(name = "create_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;

	@Column(name = "update_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;

	@ManyToMany(mappedBy = "items")
	private List<Alert> alerts;

	@PrePersist
	public void beforeSave()
	{
		createDate = updateDate = new Date();
	}

	@PreUpdate
	public void beforeUpdate()
	{
		updateDate = new Date();
	}

    public void setId(Long id)
    {
        this.id = id;
    }

    public Boolean getInStock()
    {
        return inStock;
    }

    public Date getUpdateDate()
	{
		return updateDate;
	}

	public Date getCreateDate()
	{
		return createDate;
	}

	public Boolean isInStock()
	{
		return inStock;
	}

	public void setInStock(Boolean inStock)
	{
		this.inStock = inStock;
	}

	public Long getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPrice()
	{
		return price;
	}

	public void setPrice(String price)
	{
		this.price = price;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getIdentifier()
	{
		return identifier;
	}

	public void setIdentifier(String category)
	{
		this.identifier = category;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	@Override
	public String toString()
	{
		return "InventoryItem{" +
				"name='" + name + '\'' +
				", price=" + price +
				", status='" + status + '\'' +
				", category='" + identifier + '\'' +
				", url='" + url + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		InventoryItem that = (InventoryItem) o;

		if (name != null ? !name.equals(that.name) : that.name != null) return false;
		if (url != null ? !url.equals(that.url) : that.url != null) return false;

		return true;
	}

	@Override
	public int hashCode()
	{
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + (url != null ? url.hashCode() : 0);
		return result;
	}
}

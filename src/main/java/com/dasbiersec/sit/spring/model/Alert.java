package com.dasbiersec.sit.spring.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "alert")
public class Alert implements Serializable
{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scraper_sequence")
	@SequenceGenerator(name = "scraper_sequence", sequenceName = "scraper_sequence", allocationSize = 1)
	private Long id;

	private String email;

	@Column(name = "global_item_alert")
	private Boolean globalItemAlert;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name="alert_inventory_item",
			joinColumns = {@JoinColumn(name = "alert_id", referencedColumnName = "id")},
			inverseJoinColumns = {@JoinColumn(name = "item_id", referencedColumnName = "id")}
	)
	private List<InventoryItem> items;

	public Long getId()
	{
		return id;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public Boolean getGlobalItemAlert()
	{
		return globalItemAlert;
	}

	public void setGlobalItemAlert(Boolean globalItemAlert)
	{
		this.globalItemAlert = globalItemAlert;
	}

	public List<InventoryItem> getItems()
	{
		return items;
	}

	public void setItems(List<InventoryItem> items)
	{
		this.items = items;
	}
}

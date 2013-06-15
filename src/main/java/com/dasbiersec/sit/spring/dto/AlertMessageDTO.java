package com.dasbiersec.sit.spring.dto;

import com.dasbiersec.sit.spring.model.Alert;
import com.dasbiersec.sit.spring.model.InventoryItem;

import java.io.Serializable;
import java.util.List;

public class AlertMessageDTO implements Serializable
{
	private Alert alert;
	private List<InventoryItem> items;

	public Alert getAlert()
	{
		return alert;
	}

	public void setAlert(Alert alert)
	{
		this.alert = alert;
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

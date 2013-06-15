package com.dasbiersec.sit.spring.alerts;

import com.dasbiersec.sit.spring.model.InventoryItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AlertItemQueue
{
	private static HashMap<String, InventoryItem> items = new HashMap<String, InventoryItem>();

	public static void flush()
	{
		items.clear();
	}

	public static void add(InventoryItem item)
	{
		items.put(item.getUrl(), item);
	}

	public static int size()
	{
		return items.size();
	}

	public static List<InventoryItem> getItems()
	{
		return new ArrayList<InventoryItem>(items.values());
	}
}

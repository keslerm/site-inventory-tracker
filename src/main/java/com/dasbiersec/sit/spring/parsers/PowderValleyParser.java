package com.dasbiersec.sit.spring.parsers;

import com.dasbiersec.sit.spring.datasources.DataSource;
import com.dasbiersec.sit.spring.model.InventoryItem;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class PowderValleyParser implements Parser
{
	private DataSource dataSource;
	private String identifier;

	@Override
	public void setDataSource(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	@Override
	public void setIdentifier(String identifier)
	{
		this.identifier = identifier;
	}

	@Override
	public ArrayList<InventoryItem> process()
	{
		ArrayList<InventoryItem> inventoryItems = new ArrayList<InventoryItem>();

		Document doc = dataSource.getDocument();

		Elements elements = doc.select("form[name=itemsform] tr");

		boolean start = false;

		for (Element ele : elements)
		{
			// we have to skip a few rows, skip until an empty row comes up, which is where we start
			if (start == false && ele.html().equalsIgnoreCase(""))
			{
				start = true;
				continue;
			}

			// if we haven't come to the first blank row, keep going
			if (start == false)
				continue;

			// get the detail rows of the items
			Elements itemDetails = ele.select("td font");

			// if less than 4 elements, we probably hit a break in the table, restart the blank row check to skip empty rows
			if (itemDetails.size() < 4)
			{
				start = false;
				continue;
			}

			// setup inventory item
			InventoryItem inventoryItem = new InventoryItem();
			inventoryItem.setName(itemDetails.get(1).text());
			inventoryItem.setStatus(itemDetails.get(2).text());
			inventoryItem.setPrice(itemDetails.get(3).text().replace("$", ""));
			inventoryItem.setIdentifier(identifier);

			// no unique url for each product, make our own using the item no of each item.
			inventoryItem.setUrl(dataSource.getCurrentURL() + "?" + itemDetails.get(0).text().replace(" ", "").replace("*", ""));

			// skip 0.01 cost items, these are note items and not actual products
			if (inventoryItem.getPrice().equalsIgnoreCase("0.01"))
				continue;

			// check status, in stock items are a Yes
			if (inventoryItem.getStatus().equalsIgnoreCase("Yes"))
				inventoryItem.setInStock(true);
			else
				inventoryItem.setInStock(false);

			inventoryItems.add(inventoryItem);

		}

		return inventoryItems;
	}
}

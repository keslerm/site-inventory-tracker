package com.dasbiersec.sit.spring.parsers;

import com.dasbiersec.sit.spring.datasources.DataSource;
import com.dasbiersec.sit.spring.model.InventoryItem;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class MidwayUSAParser implements Parser
{
	private Logger log = Logger.getLogger(getClass());

	private DataSource dataSource;
	private String identifier;

	public MidwayUSAParser() {}

	public MidwayUSAParser(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}

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
		Document doc = dataSource.getDocument();

		// get total pages
		Element pageElement = doc.select(".pagination").first();
		int totalPages = getTotalPages(pageElement);

		// setup our array list
		ArrayList<InventoryItem> allItems = new ArrayList<InventoryItem>();

		// begin processing each page and building the inventory items
		for (int x = 1; x <= totalPages; x++)
		{
			// load our current page
			log.info("Processing page " + dataSource.currentPage() + " (" + dataSource.getCurrentURL() + ")");
			doc = dataSource.getDocument();

			Elements elements = doc.select(".prodlistitem");
			allItems.addAll(parseItems(elements));

			dataSource.nextPage();
		}

		return allItems;

	}

	public ArrayList<InventoryItem> parseItems(Elements elements)
	{
		ArrayList<InventoryItem> itemList = new ArrayList<InventoryItem>();

		// parse each Element which would be each individual item listed on the page and generate the InventoryItem for it.
		for (Element element : elements)
		{
			InventoryItem item = new InventoryItem();

			// name
			Element nameElement = element.select("span[itemprop=name]").first();
			item.setName(nameElement.text());

			// price
			Element priceElement = element.select("span.nowprice").first();
			String price = priceElement.text().trim().replace("$", "").replace(",", "");
			item.setPrice(price);

			// status
			Element statusElement = element.select("span[itemprop=availability]").first();
			item.setStatus(statusElement.text().trim());

			// set inStock flag
			if (item.getStatus().contains("Available") || item.getStatus().contains("Mixed Availability"))
				item.setInStock(true);
			else
				item.setInStock(false);

			// URL
			Element urlElement = element.select("a[itemprop=url]").first();
			item.setUrl(urlElement.attr("href"));

			// add identifier
			item.setIdentifier(identifier);

			itemList.add(item);
		}

		return itemList;
	}


	public int getTotalPages(Element element)
	{
		// this is to get the total number of pages for Midway, using the navigation at the bottom of the page

		Elements elements = element.select("a:not(.pageincrement)");

		String lastNumber = "0";

		for (Element page : elements)
		{
			lastNumber = page.text();
		}

		log.info("Got total pages: " + lastNumber);

		return Integer.parseInt(lastNumber);
	}

}

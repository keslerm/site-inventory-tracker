package com.dasbiersec.sit.spring.parsers;

import com.dasbiersec.sit.spring.datasources.DataSource;
import com.dasbiersec.sit.spring.model.InventoryItem;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class PSAParser implements Parser
{
	private Logger log = Logger.getLogger(getClass());

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
		Document doc = dataSource.getDocument();

		// get total number of pages
		Element element = doc.select("div.pages").first();
		int totalPages = getTotalPages(element);

		ArrayList<InventoryItem> itemList = new ArrayList<InventoryItem>();

		for (int x = 1; x <= totalPages; x++)
		{
			log.info("Processing page " + dataSource.currentPage() + " (" + dataSource.getCurrentURL() + ")");

			Elements items = doc.select("li.item");

			for (Element item : items)
			{
				InventoryItem currentItem = new InventoryItem();
				currentItem.setIdentifier(identifier);

				// name & URL
				Element nameElement = item.select("h2.product-name a").first();
				currentItem.setName(nameElement.text());
				currentItem.setUrl(nameElement.attr("href"));

				// status
				Element statusElement = item.select("p.availability").first();

				if (statusElement != null)
				{
					currentItem.setStatus(statusElement.text());
				}
				else
				{
					currentItem.setStatus("In Stock");
				}


				// set inStock flag
				if (currentItem.getStatus().contains("TEMPORARILY OUT OF STOCK"))
					currentItem.setInStock(false);
				else
					currentItem.setInStock(true);

				// price, can be an old price/special price or just a regular price

				// check for special price
				if (item.select("span.price").last() != null)
				{
					Element priceElement = item.select("span.price").last();
					currentItem.setPrice(priceElement.text().replace("$", ""));
				}

				itemList.add(currentItem);

			}

			dataSource.nextPage();
			doc = dataSource.getDocument();
		}

		return itemList;
	}

	public int getTotalPages(Element element)
	{
		String lastNumber = "1";

		if (element != null)
		{
			Elements elements = element.select("li a:not(.next)");

			for (Element page : elements)
			{
				lastNumber = page.text();
			}
		}

		log.info("Got total pages: " + lastNumber);

		return Integer.parseInt(lastNumber);
	}
}


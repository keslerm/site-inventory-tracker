package com.dasbiersec.sit.spring.parsers;

import com.dasbiersec.sit.spring.datasources.DataSource;
import com.dasbiersec.sit.spring.model.InventoryItem;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class VolusionProcessor implements Parser
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
		ArrayList<InventoryItem> itemList = new ArrayList<InventoryItem>();

		Document doc = dataSource.getDocument();
		Elements elements = doc.select("a.productnamecolor");

		for (Element element : elements)
		{
			InventoryItem item = new InventoryItem();
			item.setIdentifier(identifier);

			// get name
			item.setName(element.text());

			// get url
			item.setUrl(element.attr("href"));

			// get each product page, much easier than parsing the product list page which is a mess
			Document itemDocument = dataSource.getDocument(element.attr("href"));

			log.info("Getting product details for " + item.getName() + " (" + item.getUrl() + ")");

			// get price and status
			Element details = itemDocument.select("div[itemprop=offers]").first();

			item.setPrice(details.select("span[itemprop=price]").text().replace("$", ""));

			if (details.text().contains("Out of Stock"))
			{
				item.setStatus("Out of Stock");
				item.setInStock(false);
			}
			else
			{
				item.setStatus("Available");
				item.setInStock(true);
			}

			itemList.add(item);

		}

		return itemList;
	}

	public int getTotalPages(Element element)
	{
		// get total pages, only used for search on marks
		return 0;
	}
}

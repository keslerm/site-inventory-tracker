package com.dasbiersec.sit.spring.service.impl;

import com.dasbiersec.sit.spring.alerts.AlertItemQueue;
import com.dasbiersec.sit.spring.datasources.DataSource;
import com.dasbiersec.sit.spring.model.InventoryItem;
import com.dasbiersec.sit.spring.model.Scraper;
import com.dasbiersec.sit.spring.parsers.Parser;
import com.dasbiersec.sit.spring.repos.InventoryItemRepository;
import com.dasbiersec.sit.spring.repos.ScraperRepository;
import com.dasbiersec.sit.spring.service.ScraperService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScraperServiceImpl implements ScraperService
{
	private Logger log = Logger.getLogger(getClass());

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    @Autowired
    private ScraperRepository scraperRepository;

	@Override
	public void save(InventoryItem item)
	{

        InventoryItem existing = inventoryItemRepository.getInventoryItemByUrl(item.getUrl());

		if (existing != null)
		{
			Boolean isUpdated = false;

			if (existing.isInStock() == false && item.isInStock() == true)
				isUpdated = true;

			existing.setStatus(item.getStatus());
			existing.setPrice(item.getPrice());
			existing.setName(item.getName());
			existing.setIdentifier(item.getIdentifier());
			existing.setInStock(item.isInStock());
			existing.setUrl(item.getUrl());

			log.info("Updating item " + existing.getId());

            inventoryItemRepository.save(existing);

			if (isUpdated)
				AlertItemQueue.add(existing);

			return;
		}

		if (item.isInStock() == true)
			AlertItemQueue.add(item);

	}

	public void saveBulk(ArrayList<InventoryItem> items)
	{
		log.info("Got bulk save, saving items");

		for (InventoryItem item : items)
		{
			save(item);
		}
	}


	public List<Scraper> getAllScrapers()
	{
        Iterable<Scraper> scraperList = scraperRepository.findAll();

        List<Scraper> scrapers = new ArrayList<Scraper>();

        for (Scraper sc : scraperList)
            scrapers.add(sc);

		return scrapers;

	}

	public Scraper getScraper(Long id)
	{
        Scraper scraper = scraperRepository.findOne(id);
		return scraper;
	}

	public List<InventoryItem> getItemsByIdentifier(String identifier)
	{
        Iterable<InventoryItem> items = inventoryItemRepository.getInventoryItemByIdentifier(identifier);

        List<InventoryItem> itemList = new ArrayList<InventoryItem>();

        for (InventoryItem item : items)
            itemList.add(item);

		return itemList;
	}

	public List<InventoryItem> getAllItems()
	{
        Iterable<InventoryItem> items = inventoryItemRepository.findAll();

        List<InventoryItem> itemList = new ArrayList<InventoryItem>();

        for (InventoryItem item : items)
            itemList.add(item);

        return itemList;
	}

	@Override
	public List<String> getIdentifiers()
	{
		List<String> identifierList = new ArrayList<String>();

		List<Scraper> scraperList = getAllScrapers();

		if (scraperList.size() > 0)
		{
			for (Scraper scraper : scraperList)
			{
				if (!identifierList.contains(scraper.getIdentifier()))
					identifierList.add(scraper.getIdentifier());
			}

		}

		return identifierList;

	}

	public List<InventoryItem> runScraper(Scraper scraper)
	{
		try
		{
			// intialize scraper class
			Parser parser = (Parser) Class.forName("com.dasbiersec.sit.parsers." + scraper.getParser()).newInstance();
			parser.setDataSource(new DataSource(scraper.getUrl()));
			parser.setIdentifier(scraper.getIdentifier());

			ArrayList<InventoryItem> inventoryItems = parser.process();
			saveBulk(inventoryItems);

			return inventoryItems;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}


		return null;
	}

	@Override
	public List<InventoryItem> findItems(String name)
	{
		log.info("Got search request for - " + name);

        Iterable<InventoryItem> items = inventoryItemRepository.findItemsByName(name);

        List<InventoryItem> itemList = new ArrayList<InventoryItem>();

        for (InventoryItem item : items)
            itemList.add(item);

        return itemList;
	}
}

package com.dasbiersec.sit.spring.service.impl;

import com.dasbiersec.sit.spring.alerts.AlertItemQueue;
import com.dasbiersec.sit.spring.datasources.DataSource;
import com.dasbiersec.sit.spring.model.InventoryItem;
import com.dasbiersec.sit.spring.model.Scraper;
import com.dasbiersec.sit.spring.parsers.Parser;
import com.dasbiersec.sit.spring.service.ScraperService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScraperServiceImpl implements ScraperService
{
	private Logger log = Logger.getLogger(getClass());

	@PersistenceContext(unitName = "ScraperPU")
	private EntityManager em;

	@Override
	public void save(InventoryItem item)
	{
		Query query = em.createQuery("select i from InventoryItem i where i.url = :url");
		query.setParameter("url", item.getUrl());

		List<InventoryItem> temp = query.getResultList();

		if (temp != null && temp.size() > 0)
		{
			InventoryItem existing = temp.get(0);

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
			em.flush();

			if (isUpdated)
				AlertItemQueue.add(existing);

			return;
		}

		em.persist(item);

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
		Query query = em.createQuery("select s from Scraper s");

		List<Scraper> scraperList = query.getResultList();

		return scraperList;

	}

	public Scraper getScraper(Long id)
	{
		Scraper scraper = em.find(Scraper.class, id);
		return scraper;
	}

	public List<InventoryItem> getItemsByIdentifier(String identifier)
	{
		log.info("Fetching items for identifier - " + identifier);
		Query query = em.createQuery("select i from InventoryItem i where i.identifier = :identifier order by i.inStock desc, i.updateDate desc");
		query.setParameter("identifier", identifier);

		List<InventoryItem> items = query.getResultList();

		return items;
	}

	public List<InventoryItem> getAllItems()
	{
		Query query = em.createQuery("select i from InventoryItem i order by i.inStock desc, i.updateDate desc");

		List<InventoryItem> items = query.getResultList();

		return items;
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
		
		Query query = em.createQuery("SELECT i from InventoryItem i where upper(i.name) like :query");
		query.setParameter("query", "%" + name.toUpperCase() + "%");

		List<InventoryItem> items = query.getResultList();

		return items;
	}
}

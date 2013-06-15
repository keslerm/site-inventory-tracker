package com.dasbiersec.sit.spring.service;

import com.dasbiersec.sit.spring.model.InventoryItem;
import com.dasbiersec.sit.spring.model.Scraper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public interface ScraperService
{
	public abstract void save(InventoryItem item);
	public abstract void saveBulk(ArrayList<InventoryItem> items);
	public abstract List<Scraper> getAllScrapers();
	public abstract List<InventoryItem> runScraper(Scraper scraper);
	public abstract Scraper getScraper(Long id);
	public abstract List<InventoryItem> getItemsByIdentifier(String identifier);
	public abstract List<InventoryItem> getAllItems();
	public abstract List<String> getIdentifiers();
	public abstract List<InventoryItem> findItems(String name);
}

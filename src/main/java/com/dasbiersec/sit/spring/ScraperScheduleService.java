package com.dasbiersec.sit.spring;

import com.dasbiersec.sit.spring.alerts.AlertItemQueue;
import com.dasbiersec.sit.spring.dto.AlertMessageDTO;
import com.dasbiersec.sit.spring.model.Alert;
import com.dasbiersec.sit.spring.model.Scraper;
import com.dasbiersec.sit.spring.service.AlertServices;
import com.dasbiersec.sit.spring.service.ScraperService;
import org.apache.log4j.Logger;

import java.util.List;

public class ScraperScheduleService
{
    private AlertServices alertServices;

    private ScraperService scraperService;

	private Logger log = Logger.getLogger(getClass());

	private void process()
	{
		List<Scraper> scrapers = scraperService.getAllScrapers();

		log.info("Got " + scrapers.size() + " scrapers");

		for (Scraper scraper : scrapers)
		{
			if (scraper.getEnabled() == true)
			{
				log.info("Running scraper - " + scraper.getId());
				scraperService.runScraper(scraper);

			}
		}

		log.info("Items pending in alert: " + AlertItemQueue.size());

		List<Alert> alerts = alertServices.getAlerts();
		log.debug("Got " + alerts.size() + " alerts");

		List<AlertMessageDTO> messages = alertServices.processPendingAlerts(alerts);
		log.info("Got " + messages.size() + " messages");

		alertServices.sendMessages(messages);
	}
}

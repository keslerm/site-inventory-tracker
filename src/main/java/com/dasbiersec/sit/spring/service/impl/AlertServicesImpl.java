package com.dasbiersec.sit.spring.service.impl;

import com.dasbiersec.sit.spring.alerts.AlertItemQueue;
import com.dasbiersec.sit.spring.alerts.senders.EmailSender;
import com.dasbiersec.sit.spring.dto.AlertMessageDTO;
import com.dasbiersec.sit.spring.model.Alert;
import com.dasbiersec.sit.spring.model.InventoryItem;
import com.dasbiersec.sit.spring.service.AlertServices;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AlertServicesImpl implements AlertServices
{
	private final Logger log = Logger.getLogger(getClass());

    @Autowired
	EmailSender emailSender;

	@PersistenceContext(unitName = "ScraperPU")
	private EntityManager em;

	@Override
	public List<Alert> getAlerts()
	{
		Query query = em.createQuery("select a from Alert a");
		List<Alert> alert = query.getResultList();

		return alert;
	}

	@Override
	public List<AlertMessageDTO> processPendingAlerts(List<Alert> alerts)
	{
		List<AlertMessageDTO> alertMessages = new ArrayList<AlertMessageDTO>();

		// process all ouf our passed alerts
		for (Alert alert : alerts)
		{
			// parse out this alerts registered items
			HashMap<String, InventoryItem> itemList = new HashMap<String, InventoryItem>();

			for (InventoryItem item : alert.getItems())
			{
				log.info("Got Item ID " + item.getName() + " in alert queue");
				itemList.put(item.getUrl(), item);
			}

			// construct an alert message
			AlertMessageDTO alertMessage = new AlertMessageDTO();
			alertMessage.setAlert(alert);

			List<InventoryItem> itemAlerts = new ArrayList<InventoryItem>();

			// Check each item in the alert queue to see if this alert is assigned to it
			for (InventoryItem item : AlertItemQueue.getItems())
			{
				if (itemList.containsKey(item.getUrl()) || alert.getGlobalItemAlert() == true)
					itemAlerts.add(item);
			}

			// set the found items to the alert message
			alertMessage.setItems(itemAlerts);

			// we only want alert messages that actually have items
			if (itemAlerts.size() > 0)
				alertMessages.add(alertMessage);
		}

		return alertMessages;
	}

	@Override
	public void sendMessages(List<AlertMessageDTO> messages)
	{
		// obviously if we are sending out the messages we want to flush the item queue
		AlertItemQueue.flush();

		// for now since these are only emails, we will pass to the email sender.
		// TODO: Clean this up
		emailSender.send(messages);

	}
}

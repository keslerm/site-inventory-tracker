package com.dasbiersec.sit.spring.service;

import com.dasbiersec.sit.spring.dto.AlertMessageDTO;
import com.dasbiersec.sit.spring.model.Alert;
import org.springframework.stereotype.Service;

import java.util.List;

public interface AlertServices
{
	public abstract List<Alert> getAlerts();
	public abstract List<AlertMessageDTO> processPendingAlerts(List<Alert> alerts);
	public abstract void sendMessages(List<AlertMessageDTO> messages);
}

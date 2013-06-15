package com.dasbiersec.sit.spring.alerts.senders;


import com.dasbiersec.sit.spring.dto.AlertMessageDTO;

import java.util.List;

public interface Sender
{
	public void send(List<AlertMessageDTO> messages);
}

package com.dasbiersec.sit.spring.parsers;

import com.dasbiersec.sit.spring.datasources.DataSource;
import com.dasbiersec.sit.spring.model.InventoryItem;

import java.util.ArrayList;

public interface Parser
{
	public abstract void setDataSource(DataSource dataSource);
	public abstract void setIdentifier(String identifier);
	public abstract ArrayList<InventoryItem> process();
}

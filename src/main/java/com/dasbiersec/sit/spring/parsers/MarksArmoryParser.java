package com.dasbiersec.sit.spring.parsers;

import com.dasbiersec.sit.spring.datasources.DataSource;
import com.dasbiersec.sit.spring.model.InventoryItem;

import java.util.ArrayList;

public class MarksArmoryParser extends VolusionProcessor implements Parser
{
	@Override
	@Deprecated
	public ArrayList<InventoryItem> process()
	{
		return super.process();
	}
}

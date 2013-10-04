package com.dasbiersec.sit.spring.parsers;

import com.dasbiersec.sit.spring.datasources.DataSource;
import com.dasbiersec.sit.spring.model.InventoryItem;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class MidwayUSAParserTest
{
	@Before
	public void setUp()
	{

	}


	@Test
	public void simpleTest()
	{
		DataSource ds = new DataSource("");

		MidwayUSAParser parser = new MidwayUSAParser(ds);

		ArrayList<InventoryItem> items = parser.process();
	}
}

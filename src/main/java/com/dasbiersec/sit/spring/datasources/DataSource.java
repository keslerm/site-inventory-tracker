package com.dasbiersec.sit.spring.datasources;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class DataSource
{
	private String baseURL;
	private int currentPage = 1;

	public DataSource(String URL)
	{
		baseURL = URL;
	}

	public Document getDocument()
	{
		try
		{
			Document doc = Jsoup.connect(getCurrentURL()).get();
			return doc;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return null;

	}

	public Document getDocument(String URL)
	{
		try
		{
			Document doc = Jsoup.connect(URL).get();
			return doc;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public String getCurrentURL()
	{
		// get url for current page
		return baseURL.replace("%PAGENUMBER%", String.valueOf(currentPage));
	}

	public int nextPage()
	{
		return currentPage++;
	}

	public int prevPage()
	{
		if (currentPage == 1)
			return currentPage;

		return currentPage--;
	}

	public int currentPage()
	{
		return currentPage;
	}


}

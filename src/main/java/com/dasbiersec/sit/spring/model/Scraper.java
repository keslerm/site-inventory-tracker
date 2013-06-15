package com.dasbiersec.sit.spring.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "scraper")
public class Scraper implements Serializable
{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scraper_sequence")
	@SequenceGenerator(name = "scraper_sequence", sequenceName = "scraper_sequence", allocationSize = 1)
	private Long id;

	private String parser;
	private String identifier;
	private String url;
	private Boolean enabled;

	public Long getId()
	{
		return id;
	}

	public String getParser()
	{
		return parser;
	}

	public void setParser(String scraperName)
	{
		this.parser = scraperName;
	}

	public String getIdentifier()
	{
		return identifier;
	}

	public void setIdentifier(String category)
	{
		this.identifier = category;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public Boolean getEnabled()
	{
		return enabled;
	}

	public void setEnabled(Boolean enabled)
	{
		this.enabled = enabled;
	}
}


package com.dasbiersec.sit.spring.util;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

public class ConfigProperties
{
	private Logger log = Logger.getLogger(getClass());

	private static ConfigProperties instance;
	private Properties props;

	public ConfigProperties()
	{
		props = new Properties();

		try
		{
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			props.load(classLoader.getResourceAsStream("config.properties"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		log.debug("Loaded " + props.size() + " properties");

	}

	public static String getProperty(String propertyName)
	{
		if (instance == null)
			instance = new ConfigProperties();

		return instance.props.getProperty(propertyName);
	}
}

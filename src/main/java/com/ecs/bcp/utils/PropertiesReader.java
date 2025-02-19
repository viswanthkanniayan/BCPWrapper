package com.ecs.bcp.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {
	
	
	//Regular
	private static final String URL_PROPERTY_FILE = System.getProperty("jboss.home.dir") + File.separator + "BCP" 
			+ File.separator + "PropertyFiles" + File.separator + "BCP_props.properties";
			
	private static Properties urlProp = null;
	FileInputStream urlFile = null;

	public PropertiesReader() {
		try {
			System.out.println("URL_PROPERTY_FILE :" + URL_PROPERTY_FILE);
			urlFile = new FileInputStream(URL_PROPERTY_FILE);
			urlProp = new Properties();
			urlProp.load(urlFile);
			urlFile.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (urlFile != null) {
				try {
					urlFile.close();
				} catch (IOException e) {

				}
			}
		}
	}
	
	public static String getUrlProperty(String propertyName) {
		return urlProp.getProperty(propertyName.trim());
	}
	
}
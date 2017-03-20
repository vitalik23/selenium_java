package com.mycompany.app;

import java.io.IOException;
import java.io.InputStream;
import java.lang.RuntimeException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Formatter;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Common utilities class for Selenium Webdriver Elementor Tool
 * 
 * @author Serguei Kouzmine (kouzmine_serguei@yahoo.com)
 */

public class Utils {

	public String getScriptContent(String resourceFileName) {
		try {
			System.err
					.println("Script contents: " + getResourceURI(resourceFileName));
			final InputStream stream = this.getClass().getClassLoader()
					.getResourceAsStream(resourceFileName);
			final byte[] bytes = new byte[stream.available()];
			stream.read(bytes);
			return new String(bytes, "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public String getPageContent(String resourceFileName) {
		try {
			URI uri = this.getClass().getClassLoader().getResource(resourceFileName)
					.toURI();
			System.err.println("Page contents: " + uri.toString());
			return uri.toString();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	// NOTE: getResourceURI does not work well with standalone app.
	public String getResourceURI(String resourceFileName) {
		try {
			URI uri = this.getClass().getClassLoader().getResource(resourceFileName)
					.toURI();
			return uri.toString();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	public String getResourcePath(String resourceFileName) {
		return String.format("%s/src/main/resources/%s",
				System.getProperty("user.dir"), resourceFileName);
	}

	public String readData(Optional<HashMap<String, String>> parameters) {
		return readData(null, parameters);
	}

	public String readData(String payload,
			Optional<HashMap<String, String>> parameters) {

		Boolean collectResults = parameters.isPresent();
		HashMap<String, String> collector = (collectResults) ? parameters.get()
				: new HashMap<String, String>();

		String data = (payload == null)
				? "{ \"Url\": \"http://www.google.com\", \"ElementCodeName\": \"Name of the element\", \"CommandId\": \"d5be4ea9-c51f-4e61-aefc-e5c83ba00be8\", \"ElementCssSelector\": \"html div.home-logo_custom > img\", \"ElementId\": \"\", \"ElementXPath\": \"/html//img[1]\" }"
				: payload;
		try {
			JSONObject elementObj = new JSONObject(data);
			@SuppressWarnings("unchecked")
			Iterator<String> propIterator = elementObj.keys();
			while (propIterator.hasNext()) {
				String propertyKey = propIterator.next();

				String propertyVal = elementObj.getString(propertyKey);
				System.err.println(propertyKey + ": " + propertyVal);
				collector.put(propertyKey, propertyVal);
			}
		} catch (JSONException e) {
			System.err.println("Ignored exception: " + e.toString());
			return null;
		}
		return collector.get("ElementCodeName");
	}
}
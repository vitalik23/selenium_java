package com.mycompany.app;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ByAngularButtonText extends ByAngular.BaseBy {

	public ByAngularButtonText(String buttonText) {
		super();
		this.searchText = buttonText;
	}

	private String searchText;

	protected Object getObject(SearchContext context, JavascriptExecutor javascriptExecutor) {
		return javascriptExecutor.executeScript(
				"var using = arguments[0] || document;\n" +
						"var searchText = '" + searchText + "';\n" +
						"\n" +
						ByAngular.functions.get("findByButtonText")
				, context);
	}

	@Override
	public String toString() {
		return "searchText(" + searchText + ')';
	}
}
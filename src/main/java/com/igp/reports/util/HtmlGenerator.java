package com.igp.reports.util;

import com.igp.reports.models.Row;

import java.util.List;

public class HtmlGenerator
{

	public static String BREAK = "<br>";

	public static String toHtmlTable(List<Row> rowList){
		StringBuilder builder = new StringBuilder();
		if(!rowList.isEmpty()){
			builder.append("<table border=\"1\">");
			int i = 0;
			for(Row row : rowList){
				if(i++ == 0)
					builder.append(getTableRow(row, true));
				else
					builder.append(getTableRow(row));
			}
			builder.append("</table>").append(BREAK);
		}
		return builder.toString();
	}

	private static String getTableRow(Row row, boolean isHeading){
		//return one row
		List<String> columns = row.getColList();
		String colTag = (isHeading) ? "th" : "td" ;
		StringBuilder builder = new StringBuilder();
		if(!columns.isEmpty()){
			builder.append("<tr>");
			for(String column : columns){
				builder.append("<").append(colTag).append(">").append(column).append("</").append(colTag).append(">");
			}
			builder.append("</tr>");
		}
		return builder.toString();

	}

	private static String getTableRow(Row row){
		 return getTableRow(row,false);
	}


	public static String toHtmlHeading(String text, int headingType){
		StringBuilder builder = new StringBuilder();
		String headingTag;
		switch (headingType){
			case 1: headingTag = "h1>";break;
			case 2: headingTag = "h2>";break;
			case 3: headingTag = "h3>";break;
			case 4: headingTag = "h4>";break;
			case 5: headingTag = "h5>";break;
			default: headingTag = "h6>";break;
		}
		if(!text.isEmpty()){
			builder.append("<").append(headingTag).append(text);

			builder.append("</").append(headingTag);
		}
		return builder.toString();
	}

}

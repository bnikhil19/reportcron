package com.igp.reports.domestic;

import com.igp.reports.models.Row;
import com.igp.reports.util.HtmlGenerator;
import com.igp.reports.util.Report;

import java.util.List;

import static com.igp.reports.util.HtmlGenerator.toHtmlHeading;

public class DomesticMain implements Report
{

	public String start(boolean isReport, int interval) throws Exception
	{
		System.out.println("inside DomesticMain");
		List<List<Row>> rowList = DomesticUtil.getData(interval);
		StringBuilder finalTable = new StringBuilder();

		finalTable.append(toHtmlHeading("Orders by origin/destination : ", 4));

		if(!rowList.isEmpty()){
			finalTable.append(HtmlGenerator.toHtmlHeading("Ordered From :", 5));
			finalTable.append(HtmlGenerator.toHtmlTable(rowList.get(0)));

			finalTable.append(HtmlGenerator.toHtmlHeading("Delivered To :", 5));
			finalTable.append(HtmlGenerator.toHtmlTable(rowList.get(1)));
		}
		return  finalTable.toString();
	}
}

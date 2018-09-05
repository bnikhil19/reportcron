package com.igp.reports.hourlyOrders;

import com.igp.reports.models.Row;
import com.igp.reports.util.HtmlGenerator;
import com.igp.reports.util.Report;

import java.util.List;

import static com.igp.reports.util.HtmlGenerator.toHtmlHeading;

public class HourlyOrdersMain implements Report
{
	public String start(boolean isReport, int interval) throws Exception
	{
		List<Row> rowList = HourlyOrdersUtil.getData(interval);
		String finalTable;
		finalTable = toHtmlHeading("Hourly orders/revenue : ", 4);
		if(!rowList.isEmpty()){
			finalTable += HtmlGenerator.toHtmlTable(rowList);

		}else{
			finalTable += "No data hourly orders/revenue.";
		}
		return finalTable;
	}
}

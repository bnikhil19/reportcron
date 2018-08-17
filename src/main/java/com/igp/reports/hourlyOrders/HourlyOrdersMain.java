package com.igp.reports.hourlyOrders;

import com.igp.reports.models.Row;
import com.igp.reports.util.HtmlGenerator;
import com.igp.reports.util.Report;

import java.util.List;

public class HourlyOrdersMain implements Report
{
	public String start(boolean isReport, int interval) throws Exception
	{
		List<Row> rowList = HourlyOrdersUtil.getData(interval);
		String finalTable;
		if(!rowList.isEmpty()){
			finalTable = HtmlGenerator.toHtmlTable(rowList);

		}else{
			finalTable = "No data hourly orders/revenue.";
		}
		//System.out.println(finalTable);
		if (!isReport)
		{
			//MailUtil.sendGenericMail("" , "Test Mail", finalTable, "nikhil.bonte@indiangiftsportal.com", true);
		}
		return finalTable;
	}
}

package com.igp.reports.ops.OrderNotShipWhWise;

import com.igp.reports.models.Row;
import com.igp.reports.util.HtmlGenerator;
import com.igp.reports.util.Report;

import java.util.List;

import static com.igp.reports.util.HtmlGenerator.toHtmlHeading;

//nsww
public class OrderNotShipWhWiseMain implements Report

{
	public String start(boolean isReport, int interval) throws Exception
	{
		String finalTable="";
		finalTable += toHtmlHeading("Not Shipped Warehouse wise : ",4);
		List<Row> rowList = OrderNotShipWhWiseUtil.getData(interval);

		if(!rowList.isEmpty()){
			finalTable += HtmlGenerator.toHtmlTable(rowList);
		}else{
			finalTable += "No data.";
		}
		return finalTable;
	}



}

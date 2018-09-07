package com.igp.reports.ops.OrderdispVDelIndia;

import com.igp.reports.main.Main;
import com.igp.reports.models.Row;
import com.igp.reports.util.HtmlGenerator;
import com.igp.reports.util.Report;

import java.util.List;

import static com.igp.reports.util.HtmlGenerator.toHtmlHeading;

public class OrderDispVDelIndMain implements Report
{
	public String start(boolean isReport, int interval) throws Exception
	{
		System.out.printf("started OrderDispVDelIndMain {%s}\n",Main.tnxId);
		String finalTable="";
		finalTable += toHtmlHeading("Shipped vs Delivered : ",4);
		List<Row> rowList = OrderDispVDelIndUtil.getData(interval);
		if(!rowList.isEmpty()){
			finalTable += HtmlGenerator.toHtmlTable(rowList);

		}else{
			finalTable += "No data.";
		}
		return finalTable;
	}
}

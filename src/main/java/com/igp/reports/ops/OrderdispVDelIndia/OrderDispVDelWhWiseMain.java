package com.igp.reports.ops.OrderdispVDelIndia;

import com.igp.reports.main.Main;
import com.igp.reports.models.Row;
import com.igp.reports.util.HtmlGenerator;
import com.igp.reports.util.Report;

import java.util.List;

import static com.igp.reports.util.HtmlGenerator.toHtmlHeading;

public class OrderDispVDelWhWiseMain implements Report
{

	public String start(boolean isReport, int interval) throws Exception
	{
		System.out.printf("started OrderDispVDelWhWiseMain {%s}\n",Main.tnxId);
		String finalTable="";
		finalTable += toHtmlHeading("Shipped vs Delivered Warehouse wise : ",4);
		List<List<Row>> rowListList = OrderDispVDelWhWiseUtil.getData(interval);
		if(!rowListList.isEmpty()){
			for (List<Row> rowList : rowListList){
				finalTable += HtmlGenerator.BREAK;
				finalTable += HtmlGenerator.toHtmlTable(rowList);
			}
		}else{
			finalTable += "No data.";
		}
		return finalTable;
	}
}

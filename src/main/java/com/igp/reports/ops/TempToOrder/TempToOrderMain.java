package com.igp.reports.ops.TempToOrder;

import com.igp.reports.models.Row;
import com.igp.reports.util.HtmlGenerator;
import com.igp.reports.util.Report;

import java.util.List;

import static com.igp.reports.util.HtmlGenerator.toHtmlHeading;
//tto
public class TempToOrderMain implements Report
{
	public String start(boolean isReport, int interval) throws Exception
	{
		String finalTable="";
		finalTable += toHtmlHeading("Temp order to Order conversion with payment option : ",4);
		List<Row> rowList = TempToOrderUtil.getData(interval);

		if(!rowList.isEmpty()){
			finalTable += HtmlGenerator.toHtmlTable(rowList);

		}else{
			finalTable += "No data.";
		}
		return finalTable;
	}


}

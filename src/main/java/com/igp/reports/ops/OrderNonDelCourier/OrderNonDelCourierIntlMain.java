package com.igp.reports.ops.OrderNonDelCourier;

import com.igp.reports.main.Main;
import com.igp.reports.models.Row;
import com.igp.reports.util.HtmlGenerator;
import com.igp.reports.util.Report;

import java.util.List;

import static com.igp.reports.util.HtmlGenerator.toHtmlHeading;

//ndcintl
public class OrderNonDelCourierIntlMain implements Report
{
	public String start(boolean isReport, int interval) throws Exception
	{
		System.out.printf("started OrderNonDelCourierIntlMain {%s}\n",Main.tnxId);
		String finalTable="";
		finalTable += toHtmlHeading("Not Delivered Courier wise (International) : ",4);
		List<Row> rowList = OrderNonDelCourierIntlUtil.getData(interval);

		if(!rowList.isEmpty()){
			finalTable += HtmlGenerator.toHtmlTable(rowList);

		}else{
			finalTable += "No data.";
		}
		return finalTable;
	}
}

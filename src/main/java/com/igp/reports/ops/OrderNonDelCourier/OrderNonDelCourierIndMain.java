package com.igp.reports.ops.OrderNonDelCourier;

import com.igp.reports.main.Main;
import com.igp.reports.models.Row;
import com.igp.reports.util.HtmlGenerator;
import com.igp.reports.util.Report;

import java.util.List;

import static com.igp.reports.util.HtmlGenerator.toHtmlHeading;

//ndcind
public class OrderNonDelCourierIndMain implements Report
{
	public String start(boolean isReport, int interval) throws Exception
	{
		System.out.printf("started OrderNonDelCourierIndMain  {%s}\n",Main.tnxId);
		String finalTable="";
		finalTable += toHtmlHeading("Not Delivered Courier wise (India) : ",4);
		List<Row> rowList = OrderNonDelCourierIndUtil.getData(interval);

		if(!rowList.isEmpty()){
			finalTable += HtmlGenerator.toHtmlTable(rowList);

		}else{
			finalTable += "No data.";
		}
		return finalTable;
	}
}

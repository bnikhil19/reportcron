package com.igp.reports.orderDailyAOV;

import com.igp.reports.main.Main;
import com.igp.reports.models.Row;
import com.igp.reports.util.HtmlGenerator;
import com.igp.reports.util.Report;

import java.util.List;

public class DailyAOVMain implements Report
{
	public String start(boolean isReport, int interval) throws Exception{
		System.out.printf("started DailyAOVMain {%s}\n",Main.tnxId);
		List<Row> rowList = DailyAOVUtil.getData(interval);
		String finalTable;

		finalTable = HtmlGenerator.toHtmlHeading("Orders/AOV : ", 4);

		if(!rowList.isEmpty()){
			finalTable += HtmlGenerator.toHtmlTable(rowList);

		}else{
			finalTable += "No data for daily AOV.";
		}
		System.out.printf("ended DailyAOVMain {%s}\n",Main.tnxId);
		return finalTable;
	}

}

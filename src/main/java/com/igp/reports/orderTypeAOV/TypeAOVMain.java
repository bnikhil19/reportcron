package com.igp.reports.orderTypeAOV;

import com.igp.reports.main.Main;
import com.igp.reports.models.Row;
import com.igp.reports.util.HtmlGenerator;
import com.igp.reports.util.Report;

import java.util.List;

import static com.igp.reports.util.HtmlGenerator.toHtmlHeading;

public class TypeAOVMain implements Report
{
	public String start(boolean isReport, int interval) throws Exception{
		System.out.printf("started TypeAOVMain {%s}\n",Main.tnxId);
		List<Row> rowList = TypeAOVUtil.getData(interval);
		String finalTable;

		finalTable = toHtmlHeading("Orders/AOV by type : ", 4);

		if(!rowList.isEmpty()){
			finalTable += HtmlGenerator.toHtmlTable(rowList);

		}else{
			finalTable += "No data for daily AOV.";
		}
		return finalTable;
	}

}

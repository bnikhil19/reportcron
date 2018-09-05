package com.igp.reports.newRepeat;

import com.igp.reports.main.Main;
import com.igp.reports.models.Row;
import com.igp.reports.util.HtmlGenerator;
import com.igp.reports.util.Report;

import java.util.List;

import static com.igp.reports.util.HtmlGenerator.toHtmlHeading;

public class NewRepeatMain implements Report
{
	public String start(boolean isReport, int interval) throws Exception
	{
		System.out.printf("started NewRepeatNonIndiaMain {%s}\n",Main.tnxId);
		List<Row> rowList = NewRepeatUtil.getData(interval);
		String finalTable;

		finalTable = toHtmlHeading("New/Repeat orders (India) : ", 4);

		if(!rowList.isEmpty()){
			finalTable += HtmlGenerator.toHtmlTable(rowList);

		}else{
			finalTable += "No data.";
		}

		return finalTable;
	}
}

package com.igp.reports.newRepeat;

import com.igp.reports.main.Main;
import com.igp.reports.models.Row;
import com.igp.reports.util.HtmlGenerator;
import com.igp.reports.util.Report;

import java.util.List;

public class NewRepeatMain implements Report
{
	public String start(boolean isReport, int interval) throws Exception
	{
		System.out.printf("started NewRepeatNonIndiaMain {%s}\n",Main.tnxId);
		List<Row> rowList = NewRepeatUtil.getData(interval);
		String finalTable;
		if(!rowList.isEmpty()){
			finalTable = HtmlGenerator.toHtmlTable(rowList);

		}else{
			finalTable = "No data.";
		}
		//System.out.println(finalTable);
		if(!isReport)
		{
			//MailUtil.sendGenericMail("" , "Test Mail", finalTable, "nikhil.bonte@indiangiftsportal.com", true);
		}
		System.out.printf("ended NewRepeatNonIndiaMain {%s}\n",Main.tnxId);
		return finalTable;
	}
}

package com.igp.reports.orderTypeAOV;

import com.igp.reports.main.Main;
import com.igp.reports.models.Row;
import com.igp.reports.util.HtmlGenerator;
import com.igp.reports.util.Report;

import java.util.List;

public class TypeAOVMain implements Report
{
	public String start(boolean isReport, int interval) throws Exception{
		System.out.printf("started TypeAOVMain {%s}\n",Main.tnxId);
		List<Row> rowList = TypeAOVUtil.getData(interval);
		String finalTable;
		if(!rowList.isEmpty()){
			finalTable = HtmlGenerator.toHtmlTable(rowList);

		}else{
			finalTable = "No data for daily AOV.";
		}
		//System.out.println(finalTable);
		if(!isReport){
			//MailUtil.sendGenericMail("" , "Test Mail", finalTable, "nikhil.bonte@indiangiftsportal.com", true);
		}
		System.out.printf("ended TypeAOVMain {%s}\n",Main.tnxId);
		return finalTable;
	}
}

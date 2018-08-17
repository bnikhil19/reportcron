package com.igp.reports.domestic;

import com.igp.reports.models.Row;
import com.igp.reports.util.HtmlGenerator;
import com.igp.reports.util.Report;

import java.util.List;

public class DomesticMain implements Report
{

	public String startReport(boolean isReport, int interval) throws Exception
	{
		System.out.println("inside DomesticMain");
		List<List<Row>> rowList = DomesticUtil.getData(interval);
		StringBuilder finalTable = new StringBuilder();
		if(!rowList.isEmpty()){
			finalTable.append(HtmlGenerator.toHtmlHeading("Ordered From :", 5));
			finalTable.append(HtmlGenerator.toHtmlTable(rowList.get(0)));

			finalTable.append(HtmlGenerator.toHtmlHeading("Delivered To :", 5));
			finalTable.append(HtmlGenerator.toHtmlTable(rowList.get(1)));
			//System.out.println(finalTable);

			if(!isReport){
				//MailUtil.sendGenericMail("" , "Test Mail", finalTable, "nikhil.bonte@indiangiftsportal.com", true);
			}
		}
		return  finalTable.toString();
	}

	public String start(boolean isReport, int interval) throws Exception
	{
		System.out.println("inside DomesticMain");
		List<List<Row>> rowList = DomesticUtil.getData(interval);
		StringBuilder finalTable = new StringBuilder();
		if(!rowList.isEmpty()){
			finalTable.append(HtmlGenerator.toHtmlHeading("Ordered From :", 5));
			finalTable.append(HtmlGenerator.toHtmlTable(rowList.get(0)));

			finalTable.append(HtmlGenerator.toHtmlHeading("Delivered To :", 5));
			finalTable.append(HtmlGenerator.toHtmlTable(rowList.get(1)));
			//System.out.println(finalTable);

			if(!isReport){
				//MailUtil.sendGenericMail("" , "Test Mail", finalTable, "nikhil.bonte@indiangiftsportal.com", true);
			}
		}
		return  finalTable.toString();
	}
}

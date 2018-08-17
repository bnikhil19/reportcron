package com.igp.reports.discount;

import com.igp.reports.main.Main;
import com.igp.reports.models.Row;
import com.igp.reports.util.HtmlGenerator;
import com.igp.reports.util.Report;

import java.util.List;

public class DiscountMain implements Report
{
	public String startReport(boolean isReport, int interval) throws Exception
	{
		List<Row> rowList = DiscountUtil.getData(interval);
		String finalTable;
		if(!rowList.isEmpty()){
			finalTable = HtmlGenerator.toHtmlTable(rowList);
		}else{
			finalTable = "No data for Discount Percentage.";
		}

		return finalTable;
	}

	public String start(boolean isReport, int interval) throws Exception
	{
		System.out.printf("started DiscountMain {%s}\n",Main.tnxId);

		List<Row> rowList = DiscountUtil.getData(interval);
		String finalTable;
		if(!rowList.isEmpty()){
			finalTable = HtmlGenerator.toHtmlTable(rowList);

		}else{
			finalTable = "No data for Discount Percentage.";
		}
		//System.out.println(finalTable);
		if(!isReport){
			//MailUtil.sendGenericMail("" , "Test Mail", finalTable, "nikhil.bonte@indiangiftsportal.com", true);
		}

		System.out.printf("ended CouponMain {%s}\n",Main.tnxId);
		return finalTable;
	}

	public String startThreshold(boolean isReport, int interval) throws Exception
	{
		List<Row> rowList = DiscountUtil.getData(interval);
		rowList = DiscountUtil.filterThreshold(rowList);
		String finalTable;
		if(!rowList.isEmpty()){
			finalTable = HtmlGenerator.toHtmlTable(rowList);
		}else{
			finalTable = "No data for Discount Percentage.";
		}

		return finalTable;
	}
}

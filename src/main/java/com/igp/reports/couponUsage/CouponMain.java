package com.igp.reports.couponUsage;

import com.igp.reports.main.Main;
import com.igp.reports.models.Row;
import com.igp.reports.util.HtmlGenerator;
import com.igp.reports.util.Report;

import java.util.List;

public class CouponMain implements Report
{


	public String startReport(boolean isReport, int interval) throws Exception{
		System.out.printf("started CouponMain {%s}\n",Main.tnxId);
		List<Row> rowList = CouponUtil.getData(interval);
		String finalTable;

		if(!rowList.isEmpty()){
			finalTable = HtmlGenerator.toHtmlTable(rowList);

		}else{
			finalTable = HtmlGenerator.BREAK + "No data for Coupon applied.";
		}

		System.out.printf("ended CouponMain {%s}\n",Main.tnxId);
		return finalTable;
	}

	public String start(boolean isReport, int interval) throws Exception{
		List<Row> rowList = CouponUtil.getData(interval);
		String finalTable;

		if(!rowList.isEmpty()){
			finalTable = HtmlGenerator.toHtmlTable(rowList);

		}else{
			finalTable = HtmlGenerator.BREAK + "No data for Coupon applied.";
		}
		if(!isReport){
			//MailUtil.sendGenericMail("" , "Test Mail", finalTable, "nikhil.bonte@indiangiftsportal.com", true);
		}
		return finalTable;
	}

	public String startThreshold(boolean isReport, int interval) throws Exception{
		List<Row> rowList = CouponUtil.getData(interval);
		String finalTable;

		if(!rowList.isEmpty()){
			finalTable = HtmlGenerator.toHtmlTable(rowList);

		}else{
			finalTable = HtmlGenerator.BREAK + "No data for Coupon applied.";
		}
		if(!isReport){
			//MailUtil.sendGenericMail("" , "Test Mail", finalTable, "nikhil.bonte@indiangiftsportal.com", true);
		}
		return finalTable;
	}


}

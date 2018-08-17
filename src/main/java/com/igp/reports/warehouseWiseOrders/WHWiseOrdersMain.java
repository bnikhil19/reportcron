package com.igp.reports.warehouseWiseOrders;

import com.igp.reports.models.Row;
import com.igp.reports.util.HtmlGenerator;
import com.igp.reports.util.Report;

import java.util.List;

public class WHWiseOrdersMain implements Report
{

	public String start(boolean isReport, int interval) throws Exception{
		List<Row> rowList = WHWiseOrdersUtil.getData(interval);
		String finalTable;

		if(!rowList.isEmpty()){
			finalTable = HtmlGenerator.toHtmlTable(rowList);

		}else{
			finalTable = HtmlGenerator.BREAK + "No data.";
		}
		if(!isReport){
			//MailUtil.sendGenericMail("" , "Test Mail", finalTable, "nikhil.bonte@indiangiftsportal.com", true);
		}
		return finalTable;
	}

}

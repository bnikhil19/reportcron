package com.igp.reports.discount;

import com.igp.reports.main.Main;
import com.igp.reports.models.Row;
import com.igp.reports.util.HtmlGenerator;
import com.igp.reports.util.Report;

import java.util.List;

public class DiscountMain implements Report
{

	public String start(boolean isReport, int interval) throws Exception
	{
		System.out.printf("started DiscountMain {%s}\n",Main.tnxId);

		String finalTable;
		finalTable = HtmlGenerator.toHtmlHeading("Discount Percentage : ", 4);
		List<Row> rowList = DiscountUtil.getData(interval);

		if(!rowList.isEmpty()){
			finalTable += HtmlGenerator.toHtmlTable(rowList);

		}else{
			finalTable += "No data for Discount Percentage.";
		}

		return finalTable;
	}

}

package com.igp.reports.productMappings;

import com.igp.reports.main.Main;
import com.igp.reports.models.Row;
import com.igp.reports.util.HtmlGenerator;
import com.igp.reports.util.Report;

import java.util.List;

public class ProductMappinngMain implements Report
{
	public String start(boolean isReport, int interval) throws Exception{
		System.out.printf("started ProductMappinngMain {%s}\n",Main.tnxId);
		List<Row> rowList = ProductMappinngUtil.getData(interval);
		String finalTable;

		finalTable = HtmlGenerator.toHtmlHeading("Rakhi freeshipping product mapping : ", 4);
		if(!rowList.isEmpty()){
			finalTable += HtmlGenerator.toHtmlTable(rowList);

		}else{
			finalTable += "No data for freeshipping product mapping.";
		}

		//System.out.printf("ended ProductMappinngMain {%s}\n",Main.tnxId);
		return finalTable;
	}
}

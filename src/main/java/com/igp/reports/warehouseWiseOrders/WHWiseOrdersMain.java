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

		finalTable = HtmlGenerator.toHtmlHeading("Warehouse wise orders : ", 4);
		if(!rowList.isEmpty()){
			finalTable += HtmlGenerator.toHtmlTable(rowList);

		}else{
			finalTable += HtmlGenerator.BREAK + "No data.";
		}
		return finalTable;
	}

}

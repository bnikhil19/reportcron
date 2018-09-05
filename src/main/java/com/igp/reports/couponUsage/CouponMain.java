package com.igp.reports.couponUsage;

import com.igp.reports.models.Row;
import com.igp.reports.util.HtmlGenerator;
import com.igp.reports.util.Report;

import java.util.List;

public class CouponMain implements Report
{

	public String start(boolean isReport, int interval) throws Exception{
		List<Row> rowList = CouponUtil.getData(interval);
		String finalTable;

		finalTable = HtmlGenerator.toHtmlHeading("Top coupons used : ", 4);
		if(!rowList.isEmpty()){
			finalTable += HtmlGenerator.toHtmlTable(rowList);

		}else{
			finalTable += HtmlGenerator.BREAK + "No data for Coupon applied.";
		}
		return finalTable;
	}

}

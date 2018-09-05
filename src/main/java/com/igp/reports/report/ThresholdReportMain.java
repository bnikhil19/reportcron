package com.igp.reports.report;

import com.igp.reports.discount.DiscountMain;
import com.igp.reports.main.Main;
import com.igp.reports.newRepeat.NewRepeatMain;
import com.igp.reports.newRepeatNonInd.NewRepeatNonIndiaMain;
import com.igp.reports.orderDailyAOV.DailyAOVMain;
import com.igp.reports.orderTypeAOV.TypeAOVMain;
import com.igp.reports.util.Mail.MailUtil;
import com.igp.reports.util.Report;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.igp.reports.util.HtmlGenerator.toHtmlHeading;

public class ThresholdReportMain implements Report
{


	public String start(boolean isReport, int interval) throws Exception{
		System.out.println("started ThresholdReportMain {"+Main.tnxId+"} - " + new Date());

		List<String> tableList = new ArrayList<>();
		Report report ;

		tableList.add("<html><body>");

		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		String strDate = formatter.format(date);
		//adding date
		tableList.add(toHtmlHeading( strDate, 4));

		System.out.println("1");
		report = new DailyAOVMain();
		tableList.add(((DailyAOVMain)report).startThreshold(interval));
		System.out.println("2");
		report = new TypeAOVMain();
		tableList.add(((TypeAOVMain)report).startThreshold(interval));
		System.out.println("3");
		report = new NewRepeatMain();
		tableList.add(((NewRepeatMain) report).startThreshold(interval));
		System.out.println("4");
		report = new NewRepeatNonIndiaMain();
		tableList.add(((NewRepeatNonIndiaMain) report).startThreshold(interval));
		System.out.println("5");

		report = new DiscountMain();
		tableList.add(((DiscountMain) report).startThreshold(interval));

		/*tableList.add(toHtmlHeading("Orders by origin/destination : ", 4));
		report = new DomesticMain();
		tableList.add(report.start(true, interval));

		tableList.add(toHtmlHeading("Top coupons used : ", 4));
		report = new CouponMain();
		tableList.add(report.start(true, interval));*/

		/*tableList.add(toHtmlHeading("Hourly orders/revenue : ", 4));
		report = new HourlyOrdersMain();
		tableList.add(((HourlyOrdersMain) report).startThreshold( interval));*/

		/*tableList.add(toHtmlHeading("Rakhi freeshipping product mapping : ", 4));
		report = new ProductMappinngMain();
		tableList.add(report.start(true, interval));*/

		tableList.add("</body></html>");

		StringBuilder finalHtmlStr = new StringBuilder();
		for (String item : tableList){
			finalHtmlStr.append(item);
		}

		MailUtil.sendGenericMail("" , "Test Mail(Threshold report)", finalHtmlStr.toString(), "nikhil.bonte@indiangiftsportal.com", true);
		//MailUtil.sendGenericMail("" , "Orders Report", finalHtmlStr.toString(), "priyesh.neema@igp.com,suditi.choudhary@indiangiftsportal.com,nikhil.bonte@indiangiftsportal.com", true);
		System.out.println();
		System.out.println(finalHtmlStr);
		System.out.println("ended ThresholdReportMain {"+Main.tnxId+"}\n");
		return "";
	}
}

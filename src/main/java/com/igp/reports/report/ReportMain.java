package com.igp.reports.report;

import com.igp.reports.couponUsage.CouponMain;
import com.igp.reports.discount.DiscountMain;
import com.igp.reports.domestic.DomesticMain;
import com.igp.reports.hourlyOrders.HourlyOrdersMain;
import com.igp.reports.main.Main;
import com.igp.reports.newRepeat.NewRepeatMain;
import com.igp.reports.newRepeatNonInd.NewRepeatNonIndiaMain;
import com.igp.reports.orderDailyAOV.DailyAOVMain;
import com.igp.reports.orderTypeAOV.TypeAOVMain;
import com.igp.reports.util.Mail.MailUtil;
import com.igp.reports.util.Report;
import com.igp.reports.warehouseWiseOrders.WHWiseOrdersMain;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.igp.reports.util.HtmlGenerator.toHtmlHeading;

public class ReportMain implements Report
{
	//this will gather all data & push one email

	//this will called from cron

	public String start(boolean isReport, int interval) throws Exception{
		System.out.println("started ReportMain {"+Main.tnxId+"} - " + new Date());

		List<String> tableList = new ArrayList<>();
		Report report ;

		tableList.add("<html><body>");

		Date date = new Date();
		if(interval > 0){
			//date = new Date(date.getTime() - interval * 24 * 3600 * 1000 ); //Subtract n days
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -interval);
			date = calendar.getTime();
		}

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		String strDate = formatter.format(date);

		//adding date
		tableList.add(toHtmlHeading( strDate, 4));


		//tableList.add(toHtmlHeading("Orders/AOV : ", 4));
		report = new DailyAOVMain();
		tableList.add(report.start(true, interval));

		//tableList.add(toHtmlHeading("Orders/AOV by type : ", 4));
		report = new TypeAOVMain();
		tableList.add(report.start(true, interval));

		//tableList.add(toHtmlHeading("New/Repeat orders (India) : ", 4));
		report = new NewRepeatMain();
		tableList.add(report.start(true, interval));

		//tableList.add(toHtmlHeading("New/Repeat orders (International) : ", 4));
		report = new NewRepeatNonIndiaMain();
		tableList.add(report.start(true, interval));

		//tableList.add(toHtmlHeading("Discount Percentage : ", 4));
		report = new DiscountMain();
		tableList.add(report.start(true, interval));

		//tableList.add(toHtmlHeading("Orders by origin/destination : ", 4));
		report = new DomesticMain();
		tableList.add(report.start(true, interval));

		//tableList.add(toHtmlHeading("Top coupons used : ", 4));
		report = new CouponMain();
		tableList.add(report.start(true, interval));

		//tableList.add(toHtmlHeading("Warehouse wise orders : ", 4));
		report = new WHWiseOrdersMain();
		tableList.add(report.start(true, interval));

		//tableList.add(toHtmlHeading("Hourly orders/revenue : ", 4));
		report = new HourlyOrdersMain();
		tableList.add(report.start(true, interval));

		/*tableList.add(toHtmlHeading("Rakhi freeshipping product mapping : ", 4));
		report = new ProductMappinngMain();
		tableList.add(report.start(true, interval));*/

		tableList.add("</body></html>");

		StringBuilder finalHtmlStr = new StringBuilder();
		for (String item : tableList){
			finalHtmlStr.append(item);
		}

		MailUtil.sendGenericMail("" , "Test Mail(daily Report)", finalHtmlStr.toString(), "nikhil.bonte@indiangiftsportal.com", true);
		//MailUtil.sendGenericMail("" , "Orders Report", finalHtmlStr.toString(), "priyesh.neema@igp.com,suditi.choudhary@indiangiftsportal.com,nikhil.bonte@indiangiftsportal.com,", true);
		System.out.println();
		System.out.println(finalHtmlStr);
		System.out.println("ended ReportMain {"+Main.tnxId+"}\n");
		return "";
	}
}

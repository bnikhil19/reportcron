package com.igp.reports.main;

import com.igp.reports.couponUsage.CouponMain;
import com.igp.reports.discount.DiscountMain;
import com.igp.reports.domestic.DomesticMain;
import com.igp.reports.hourlyOrders.HourlyOrdersMain;
import com.igp.reports.newRepeat.NewRepeatMain;
import com.igp.reports.newRepeatNonInd.NewRepeatNonIndiaMain;
import com.igp.reports.ops.OrderNonDelCourier.OrderNonDelCourierIndMain;
import com.igp.reports.ops.OrderNonDelCourier.OrderNonDelCourierIntlMain;
import com.igp.reports.ops.OrderNotShipWhWise.OrderNotShipWhWiseMain;
import com.igp.reports.ops.OrderdispVDelIndia.OrderDispVDelIndMain;
import com.igp.reports.ops.TempToOrder.TempToOrderMain;
import com.igp.reports.orderDailyAOV.DailyAOVMain;
import com.igp.reports.orderTypeAOV.TypeAOVMain;
import com.igp.reports.productMappings.ProductMappinngMain;
import com.igp.reports.util.Mail.MailUtil;
import com.igp.reports.util.Report;
import com.igp.reports.util.database.ServerProperties;
import com.igp.reports.warehouseWiseOrders.WHWiseOrdersMain;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.igp.reports.util.HtmlGenerator.toHtmlHeading;

public class Main
{
	public final static String tnxId = UUID.randomUUID().toString();
	public static void main(String[] args)
	{
		System.out.println("Starting Main-["+tnxId+"]- "+args);
		ServerProperties serverProperties=new ServerProperties();
		try
		{
			System.out.println("Main ["+tnxId+"]");
			serverProperties.initiate();
			Report report;

			Properties properties = new Properties();
			properties.load(new FileInputStream("./conf.properties"));

			int argsCount = args.length;
			String argument = (argsCount > 0) ? args[0] : "";
			int interval = (argsCount > 1) ? Integer.parseInt(args[1]) : 0;


			List<String> tableList = new ArrayList<>();

			tableList.add("<html><body>");

			Date date = new Date();
			if(interval > 0){
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.DATE, -interval);
				date = calendar.getTime();
			}

			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			String strDate = formatter.format(date);
			tableList.add(toHtmlHeading( strDate, 4));


			String[] values = new String[0];
			String valuesRaw = properties.getProperty(argument);
			if(valuesRaw != null && !valuesRaw.isEmpty()){
				values = valuesRaw.split(",");
			}else{
				throw new Exception("cannot find configuration for key "+ argument);
			}

			for(String s : values){
				switch (s.toLowerCase()){

					case "cp":
						report = new CouponMain(); break;
					case "disc":
						report = new DiscountMain(); break;
					case "dom":
						report = new DomesticMain(); break;
					case "hour":
						report = new HourlyOrdersMain(); break;
					case "new_rpt_ind":
						report = new NewRepeatMain(); break;
					case "new_rpt_intl":
						report = new NewRepeatNonIndiaMain(); break;
					case "aov_daily" :
						report = new DailyAOVMain(); break;
					case "aov_type" :
						report = new TypeAOVMain(); break;
					case "prod_mapping" :
						report = new ProductMappinngMain(); break;
					case "dis_v_del_ind":
						report = new OrderDispVDelIndMain(); break;
					case "non_del_cr_ind":
						report = new OrderNonDelCourierIndMain(); break;
					case "non_del_cr_intl":
						report = new OrderNonDelCourierIntlMain(); break;
					case "not_ship_wh":
						report = new OrderNotShipWhWiseMain(); break;
					case "temp_to_order":
						report = new TempToOrderMain(); break;
					case "wh_orders":
						report = new WHWiseOrdersMain(); break;
					default:
						System.out.println("No valid argument found.");
						return;

				}
				tableList.add(report.start(false, interval));
			}

			tableList.add("</body></html>");

			StringBuilder finalHtmlStr = new StringBuilder();
			for (String item : tableList){
				finalHtmlStr.append(item);
			}

			MailUtil.sendGenericMail("" , "Test Mail(daily Report)", finalHtmlStr.toString(), "nikhil.bonte@indiangiftsportal.com", true);
			//MailUtil.sendGenericMail("" , "Orders Report", finalHtmlStr.toString(), "priyesh.neema@igp.com,suditi.choudhary@indiangiftsportal.com,nikhil.bonte@indiangiftsportal.com,", true);
		}
		catch (Exception e)
		{
			System.out.println("Exception in Main-["+tnxId+"]- "+e.getMessage());
		}
	}
}

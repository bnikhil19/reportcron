package com.igp.reports.main;

import com.igp.reports.couponUsage.CouponMain;
import com.igp.reports.discount.DiscountMain;
import com.igp.reports.domestic.DomesticMain;
import com.igp.reports.hourlyOrders.HourlyOrdersMain;
import com.igp.reports.newRepeat.NewRepeatMain;
import com.igp.reports.report.ReportMain;
import com.igp.reports.util.Report;
import com.igp.reports.util.database.ServerProperties;

import java.util.UUID;

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

			int argsCount = args.length;
			String argument = (argsCount > 0) ? args[0] : "";
			int interval = (argsCount > 1) ? Integer.parseInt(args[1]) : 0;
			switch (argument){
				case "a":
				case "A"://couponUsage
					report = new CouponMain(); break;
				case "b":
				case "B"://discount
					report = new DiscountMain(); break;
				case "c":
				case "C"://domestic vs international
					report = new DomesticMain(); break;
				case "d":
				case "D"://domestic vs international
					report = new HourlyOrdersMain(); break;
				case "e":
				case "E"://domestic vs international
					report = new NewRepeatMain(); break;
				case "r":
				case "R"://domestic vs international
					report = new ReportMain(); break;
				default:
					System.out.println("No valid argument found.");
					return;

			}
			report.start(false, interval);
		}
		catch (Exception e)
		{
			System.out.println("Exception in CouponUsage Main-["+tnxId+"]- "+e.getMessage());
		}
	}
}

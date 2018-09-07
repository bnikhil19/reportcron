package com.igp.reports.ops.OrderdispVDelIndia;

import com.igp.reports.models.Row;
import com.igp.reports.util.database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;

public class OrderDispVDelWhWiseUtil
{

	public static List<List<Row>> getData(int interval) throws Exception{
		Connection connection = null;
		PreparedStatement preparedStatement14 = null;
		PreparedStatement preparedStatement5  = null;
		ResultSet resultSet14 = null;
		ResultSet resultSet5 = null;
		List<Row> rowList = new ArrayList<>();
		List<List<Row>> rowListList = new ArrayList<>();
		Map<String, Map<String, WhModel>> masterMap = new HashMap<>();
		Map<String, WhModel> masterMap14 = new HashMap<>();

		try {
			connection = Database.INSTANCE.getReadOnlyConnection();
			String statement5Days = "select op.fk_associate_id, date_format(o.date_purchased, '%Y-%m-%d') as day, count(distinct(o.orders_id)) as total_orders, sum(case when tk.orderStatus = 'Dispatched' then 1 else 0 end) as dispatched, sum(case when (tk.deliveredDate <> '0000-00-00 00:00:00' OR tk.deliveryDate <>'0000-00-00 00:00:00') then 1 else 0 end) as delivered from orders o left join trackorders tk ON o.orders_id = tk.orders_id left join orders_products op on o.orders_id = op.orders_id where date_format(o.date_purchased, '%Y-%m-%d') < date_format(now(), '%Y-%m-%d') AND date_format(o.date_purchased, '%Y-%m-%d') >= date_format(now() - INTERVAL 5 DAY, '%Y-%m-%d') AND o.fk_associate_id = 5 AND op.fk_associate_id in (4,318,354,383) AND orders_status <> 'Cancelled' group by op.fk_associate_id, day order by op.fk_associate_id,day";

			String statement14Days = "select op.fk_associate_id, count(distinct(o.orders_id)) as total_orders, sum(case when tk.orderStatus = 'Dispatched' then 1 else 0 end) as dispatched, sum(case when (tk.deliveredDate <> '0000-00-00 00:00:00' OR tk.deliveryDate <>'0000-00-00 00:00:00') then 1 else 0 end) as delivered from orders o left join trackorders tk ON o.orders_id = tk.orders_id left join orders_products op on o.orders_id = op.orders_id where date_format(o.date_purchased, '%Y-%m-%d') < date_format(now()- INTERVAL 5 DAY, '%Y-%m-%d') AND date_format(o.date_purchased, '%Y-%m-%d') >= date_format(now() - INTERVAL 20 DAY, '%Y-%m-%d') AND o.fk_associate_id = 5 AND op.fk_associate_id in (4,318,354,383) AND orders_status <> 'Cancelled' group by op.fk_associate_id order by op.fk_associate_id";


			preparedStatement5 = connection.prepareStatement(statement5Days,ResultSet.TYPE_SCROLL_SENSITIVE);
			resultSet5 = preparedStatement5.executeQuery();

			preparedStatement14 = connection.prepareStatement(statement14Days,ResultSet.TYPE_SCROLL_SENSITIVE);
			resultSet14 = preparedStatement14.executeQuery();

			List<String> daysList = getPrevDaysList(1,"yyyy-MM-dd",5);

			while (resultSet14.next()){
				masterMap14.put(resultSet14.getString("fk_associate_id"), new WhModel( resultSet14.getString("total_orders"), resultSet14.getString("dispatched"), resultSet14.getString("delivered")));
			}

			while(resultSet5.next()) {
				String currentWarehouse = resultSet5.getString("fk_associate_id");
				if(masterMap.containsKey(currentWarehouse)){
					Map<String, WhModel> subMap = masterMap.get(currentWarehouse);
					subMap.put(resultSet5.getString("day"),new WhModel( resultSet5.getString("total_orders"), resultSet5.getString("dispatched"), resultSet5.getString("delivered")));
				}else{
					Map<String, WhModel> subMap = new HashMap<>();
					subMap.put(resultSet5.getString("day"), new WhModel( resultSet5.getString("total_orders"), resultSet5.getString("dispatched"), resultSet5.getString("delivered")));
					masterMap.put(currentWarehouse , subMap);
				}
			}

			for(Map.Entry<String, Map<String, WhModel>> entry : masterMap.entrySet()){
				rowList = new ArrayList<>();
				Map<String, WhModel> subMap = entry.getValue();

				Row row1 = new Row();
				row1.setColList(entry.getKey());
				rowList.add(0,row1);

				for(String day : daysList){
					Row row = new Row();
					List<String> dataList = new ArrayList<>();
					WhModel value = subMap.get(day);
					dataList.add(day);
					if(subMap.containsKey(day)){
						dataList.add(value.totalOrders);
						dataList.add(value.dispatched);
						dataList.add(value.delivered);
					}else{
						dataList.add("0");
						dataList.add("0");
						dataList.add("0");
					}
					row.setColList(dataList);
					rowList.add(row);
				}

				Row row = new Row();
				List<String> dataList = new ArrayList<>();
				dataList.add( "Last 14 days");
				if(masterMap14.containsKey(entry.getKey())){
					WhModel value2 = masterMap14.get(entry.getKey());
					dataList.add(value2.totalOrders);
					dataList.add(value2.dispatched);
					dataList.add(value2.delivered);
					masterMap14.remove(entry.getKey());
				}else{
					dataList.add("0");
					dataList.add("0");
					dataList.add("0");
				}
				row.setColList(dataList);
				rowList.add(row);
				rowListList.add(rowList);
			}


/////72
			statement5Days = "select date_format(o.date_purchased, '%Y-%m-%d') as day, p.fk_associate_id, count(distinct(o.orders_id)) as total_orders, sum(case when tk.orderStatus = 'Dispatched' then 1 else 0 end) as dispatched, sum(case when (tk.deliveredDate <> '0000-00-00 00:00:00' OR tk.deliveryDate <>'0000-00-00 00:00:00') then 1 else 0 end) as delivered from orders o left join trackorders tk ON o.orders_id = tk.orders_id left join orders_products op on o.orders_id = op.orders_id left join products p on op.products_id = p.products_id where date_format(o.date_purchased, '%Y-%m-%d') < date_format(now(), '%Y-%m-%d') AND date_format(o.date_purchased, '%Y-%m-%d') >= date_format(now() - INTERVAL 5 DAY, '%Y-%m-%d') AND o.fk_associate_id = 5 AND orders_status <> 'Cancelled' AND p.fk_associate_id = 72 group by day order by day;";

			statement14Days = "select p.fk_associate_id,count(distinct(o.orders_id)) as total_orders, sum(case when tk.orderStatus = 'Dispatched' then 1 else 0 end) as dispatched, sum(case when (tk.deliveredDate <> '0000-00-00 00:00:00' OR tk.deliveryDate <>'0000-00-00 00:00:00') then 1 else 0 end) as delivered from orders o left join trackorders tk ON o.orders_id = tk.orders_id left join orders_products op on o.orders_id = op.orders_id left join products p on op.products_id = p.products_id where date_format(o.date_purchased, '%Y-%m-%d') < date_format(now() - INTERVAL 5 DAY, '%Y-%m-%d') AND date_format(o.date_purchased, '%Y-%m-%d') >= date_format(now() - INTERVAL 20 DAY, '%Y-%m-%d') AND o.fk_associate_id = 5 AND orders_status <> 'Cancelled' AND p.fk_associate_id = 72 ;";

			masterMap = new HashMap<>();
			masterMap14 = new HashMap<>();

			preparedStatement5 = connection.prepareStatement(statement5Days,ResultSet.TYPE_SCROLL_SENSITIVE);
			resultSet5 = preparedStatement5.executeQuery();

			preparedStatement14 = connection.prepareStatement(statement14Days,ResultSet.TYPE_SCROLL_SENSITIVE);
			resultSet14 = preparedStatement14.executeQuery();

			while (resultSet14.next()){
				masterMap14.put(resultSet14.getString("fk_associate_id"), new WhModel( resultSet14.getString("total_orders"), resultSet14.getString("dispatched"), resultSet14.getString("delivered")));
			}

			System.out.println("masterMap14 " + masterMap14);

			while(resultSet5.next()) {
				String currentWarehouse = resultSet5.getString("fk_associate_id");
				if(masterMap.containsKey(currentWarehouse)){
					Map<String, WhModel> subMap = masterMap.get(currentWarehouse);
					subMap.put(resultSet5.getString("day"),new WhModel( resultSet5.getString("total_orders"), resultSet5.getString("dispatched"), resultSet5.getString("delivered")));
				}else{
					Map<String, WhModel> subMap = new HashMap<>();
					subMap.put(resultSet5.getString("day"), new WhModel( resultSet5.getString("total_orders"), resultSet5.getString("dispatched"), resultSet5.getString("delivered")));
					masterMap.put(currentWarehouse , subMap);
				}
			}

			System.out.println("masterMap " + masterMap);

			for(Map.Entry<String, Map<String, WhModel>> entry : masterMap.entrySet()){
				rowList = new ArrayList<>();
				Map<String, WhModel> subMap = entry.getValue();

				Row row1 = new Row();
				row1.setColList(entry.getKey());
				rowList.add(0,row1);


				for(String day : daysList){
					Row row = new Row();
					List<String> dataList = new ArrayList<>();
					WhModel value = subMap.get(day);
					dataList.add(day);
					if(subMap.containsKey(day)){
						dataList.add(value.totalOrders);
						dataList.add(value.dispatched);
						dataList.add(value.delivered);
					}else{
						dataList.add("0");
						dataList.add("0");
						dataList.add("0");
					}
					row.setColList(dataList);
					rowList.add(row);
				}

				Row row = new Row();
				List<String> dataList = new ArrayList<>();
				dataList.add( "Last 14 days");
				if(masterMap14.containsKey(entry.getKey())){
					WhModel value2 = masterMap14.get(entry.getKey());
					dataList.add(value2.totalOrders);
					dataList.add(value2.dispatched);
					dataList.add(value2.delivered);
					masterMap14.remove(entry.getKey());
				}else{
					dataList.add("0");
					dataList.add("0");
					dataList.add("0");
				}
				row.setColList(dataList);
				rowList.add(row);
				rowListList.add(rowList);
			}

			for (List<Row> rowList1 : rowListList)
			{
				Row heading = new Row();
				heading.setColList("Date", "Total Orders", "Shipped", "Delivered");
				rowList1.add(1, heading);
			}


		} catch (Exception exception) {
			System.out.println(exception.toString());
			throw exception;
		} finally {
			Database.INSTANCE.closeStatement(preparedStatement5);
			Database.INSTANCE.closeStatement(preparedStatement14);
			Database.INSTANCE.closeResultSet(resultSet5);
			Database.INSTANCE.closeResultSet(resultSet14);
			Database.INSTANCE.closeConnection(connection);
		}
		return rowListList;
	}

	private static List<String> getPrevDaysList(int intervalDays, String format, int length){
		List<String> days = new ArrayList<>();
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		Calendar calendar = Calendar.getInstance();

		int x;
		for(x=0; x<length; x++){

			calendar.add(Calendar.DATE, -intervalDays);
			Date date = calendar.getTime();
			days.add(dateFormat.format(date));
		}
		return days;
	}

	static class WhModel{
		private String totalOrders;
		private String dispatched;
		private String delivered;

		WhModel(String totalOrders, String dispatched, String delivered){
			this.totalOrders = totalOrders;
			this.dispatched = dispatched;
			this.delivered = delivered;
		}

		@Override public String toString()
		{
			return "WhModel{" + "totalOrders='" + totalOrders + '\'' + ", dispatched='" + dispatched + '\''
					+ ", delivered='" + delivered + '\'' + '}';
		}
	}

}

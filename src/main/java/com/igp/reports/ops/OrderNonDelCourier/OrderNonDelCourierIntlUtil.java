package com.igp.reports.ops.OrderNonDelCourier;

import com.igp.reports.models.Row;
import com.igp.reports.util.database.Database;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;

//2.B
public class OrderNonDelCourierIntlUtil
{

	public static List<Row> getData(int interval) throws Exception{
		Connection connection = null;
		PreparedStatement preparedStatement5 = null;
		PreparedStatement preparedStatement14 = null;
		ResultSet resultSet5 = null;
		ResultSet resultSet14 = null;
		List<Row> rowList = new ArrayList<>();
		Map<String, Map<String, BigDecimal>> masterMap = new HashMap<>(); //Map<courier_name, Map<date, orders>>
		Map<String, BigDecimal> masterMap14 = new HashMap<>();

		try {
			connection = Database.INSTANCE.getReadOnlyConnection();
			String statement5Days = "select c.courier_name, date_format(tos.releaseDate,'%Y-%m-%d') as day, count(*) as orders from orders_products op join trackorders tos on tos.orders_products_id = op.orders_products_id and tos.orders_id = op.orders_id join orders o on o.orders_id = op.orders_id left join courier c on c.courier_id = op.orders_products_courierid where o.delivery_country = 'India' AND tos.releaseDate >= date_format(NOW() - INTERVAL 5 day,'%Y-%m-%d') and tos.releaseDate < date_format(NOW(),'%Y-%m-%d') and op.fk_associate_id in (4,318,354,383) and op.orders_product_status in ('Shipped') and op.orders_awbnumber_associatewise <> '' and o.fk_associate_id = 5 and op.delivery_status = 0  group by c.courier_name,day order by c.courier_name";

			String statement14Days = "select c.courier_name, count(*) as orders from orders_products op join trackorders tos on tos.orders_products_id = op.orders_products_id and tos.orders_id = op.orders_id join orders o on o.orders_id = op.orders_id left join courier c on c.courier_id = op.orders_products_courierid where o.delivery_country <> 'India' AND tos.releaseDate >= date_format(NOW() - INTERVAL 20 day,'%Y-%m-%d') and tos.releaseDate < date_format(NOW()  - INTERVAL 5 day,'%Y-%m-%d') and op.fk_associate_id in (4,318,354,383) and op.orders_product_status in ('Shipped') and op.orders_awbnumber_associatewise <> '' and o.fk_associate_id = 5 and op.delivery_status = 0  group by c.courier_name order by c.courier_name";

			preparedStatement5 = connection.prepareStatement(statement5Days,ResultSet.TYPE_SCROLL_SENSITIVE);
			resultSet5 = preparedStatement5.executeQuery();

			preparedStatement14 = connection.prepareStatement(statement14Days);
			resultSet14 = preparedStatement14.executeQuery();

			List<String> daysList = getPrevDaysList(1,"yyyy-MM-dd",5);

			while (resultSet14.next()){
				masterMap14.put(resultSet14.getString("courier_name"), resultSet14.getBigDecimal("orders"));
			}

			while(resultSet5.next()) {
				String currentCourier = resultSet5.getString("courier_name");
				System.out.println(" currentCourier " + currentCourier);
				if(masterMap.containsKey(currentCourier)){
					Map<String, BigDecimal> subMap = masterMap.get(currentCourier);
					subMap.put(resultSet5.getString("day"), resultSet5.getBigDecimal("orders"));
				}else{
					Map<String, BigDecimal> subMap = new HashMap<>();
					subMap.put(resultSet5.getString("day"), resultSet5.getBigDecimal("orders"));
					masterMap.put(currentCourier , subMap);
				}
			}
			System.out.println("masterMap = " + masterMap);

			BigDecimal totalOrders = BigDecimal.ZERO;
			for(Map.Entry<String, Map<String, BigDecimal>> entry : masterMap.entrySet()){
				String currentCourier = entry.getKey();
				Map<String, BigDecimal> subMap = entry.getValue();
				Row row = new Row();
				List<String> dataList = new ArrayList<>();
				for(String day : daysList){
					if(subMap.containsKey(day)){
						totalOrders = totalOrders.add(subMap.get(day));
						dataList.add( String.valueOf(subMap.get(day)));
					}else{
						dataList.add("0");
					}
				}
				//dataList.add(0, String.valueOf(totalOrders));
				if(masterMap14.containsKey(currentCourier)){
					dataList.add( String.valueOf(masterMap14.get(currentCourier)));
					masterMap14.remove(currentCourier);
				}else{
					dataList.add("0");
				}

				dataList.add(0, currentCourier);
				row.setColList(dataList);
				rowList.add(row);
				totalOrders = BigDecimal.ZERO;
			}

			for(Map.Entry<String, BigDecimal> entry : masterMap14.entrySet()){
				Row row = new Row();
				List<String> dataList = new ArrayList<>();
				for(String day : daysList){
					dataList.add("0");
				}
				dataList.add(0, String.valueOf(masterMap14.get(entry.getKey())));
				dataList.add(0, entry.getKey());
				row.setColList(dataList);
				rowList.add(row);
			}

			if (!rowList.isEmpty())
			{
				Row heading = new Row();
				daysList.add( "Last 14 days");
				daysList.add(0, "Courier");
				heading.setColList(daysList);
				rowList.add(0, heading);
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
		return rowList;
	}

	/*public static List<Row> getData(int interval) throws Exception{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Row> rowList = new ArrayList<>();
		Map<String, Map<String, BigDecimal>> masterMap = new HashMap<>(); //Map<courier_name, Map<date, orders>>

		try {
			connection = Database.INSTANCE.getReadOnlyConnection();
			//String statement = "select date_format(o.date_purchased, '%Y-%m-%d') as day, count(distinct(o.orders_id)) as total_orders, sum(case when tk.orderStatus = 'Dispatched' then 1 else 0 end) as dispached, sum(case when (tk.deliveredDate <> '0000-00-00 00:00:00' OR tk.deliveryDate <>'0000-00-00 00:00:00') then 1 else 0 end) as delivered from orders o left join trackorders tk ON o.orders_id = tk.orders_id where o.fk_associate_id = 5 AND orders_status <> 'Cancelled' AND date_format(o.date_purchased, '%Y-%m-%d') >= date_format(now() - INTERVAL 9 DAY, '%Y-%m-%d') group by day";
			String statement = "select c.courier_name, date_format(o.date_purchased,'%Y-%m-%d') as day, count(*) as orders from orders_products op join trackorders tos on tos.orders_products_id = op.orders_products_id and tos.orders_id = op.orders_id join orders o on o.orders_id = op.orders_id left join courier c on c.courier_id = op.orders_products_courierid where o.delivery_country = 'India' AND o.date_purchased >= date_format(NOW() - INTERVAL 8 day,'%Y-%m-%d') and o.date_purchased < date_format(NOW(),'%Y-%m-%d') and op.fk_associate_id in (4,318,354) and op.orders_product_status in ('Shipped') and op.orders_awbnumber_associatewise <> '' and o.fk_associate_id = 5 and op.delivery_status = 0  group by c.courier_name,day";
			preparedStatement = connection.prepareStatement(statement,ResultSet.TYPE_SCROLL_SENSITIVE);
			resultSet = preparedStatement.executeQuery();

			List<String> daysList = getPrevDaysList(1,"yyyy-MM-dd",8);

			while(resultSet.next()) {
				String currentCourier = resultSet.getString("courier_name");
				if(masterMap.containsKey(currentCourier)){
					Map<String, BigDecimal> subMap = masterMap.get(currentCourier);
					subMap.put(resultSet.getString("day"), resultSet.getBigDecimal("orders"));
				}else{
					Map<String, BigDecimal> subMap = new HashMap<>();
					subMap.put(resultSet.getString("day"), resultSet.getBigDecimal("orders"));
					masterMap.put(currentCourier , subMap);
				}
			}
			System.out.println("masterMap = " + masterMap);
			System.out.println("daysList = ");
			daysList.forEach(x -> System.out.println(x));

			BigDecimal totalOrders = BigDecimal.ZERO;
			for(Map.Entry<String, Map<String, BigDecimal>> entry : masterMap.entrySet()){
				Map<String, BigDecimal> subMap = entry.getValue();
				Row row = new Row();
				List<String> dataList = new ArrayList<>();
				for(String day : daysList){
					if(subMap.containsKey(day)){
						totalOrders = totalOrders.add(subMap.get(day));
						dataList.add( String.valueOf(subMap.get(day)));
					}else{
						dataList.add("0");
					}
				}
				dataList.add(0, String.valueOf(totalOrders));
				dataList.add(0, entry.getKey());
				row.setColList(dataList);
				rowList.add(row);
				totalOrders = BigDecimal.ZERO;
			}

			if (!rowList.isEmpty())
			{
				Row heading = new Row();
				daysList.add(0, "Total Orders");
				daysList.add(0, "Courier");
				heading.setColList(daysList);
				rowList.add(0, heading);
			}
		} catch (Exception exception) {
			System.out.println(exception.toString());
			throw exception;
		} finally {
			Database.INSTANCE.closeStatement(preparedStatement);
			Database.INSTANCE.closeResultSet(resultSet);
			Database.INSTANCE.closeConnection(connection);
		}
		return rowList;
	}*/

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

}

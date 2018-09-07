package com.igp.reports.ops.OrderNotShipWhWise;

import com.igp.reports.models.Row;
import com.igp.reports.util.database.Database;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;

//2.C
public class OrderNotShipWhWiseUtil
{

	public static List<Row> getData(int interval) throws Exception{
		Connection connection = null;
		PreparedStatement preparedStatement5 = null;
		PreparedStatement preparedStatement14 = null;
		ResultSet resultSet5 = null;
		ResultSet resultSet14 = null;
		List<Row> rowList = new ArrayList<>();
		Map<String, Map<String, BigDecimal>> masterMap = new HashMap<>(); //Map<fk_associate_id, Map<date, count>>
		Map<String, BigDecimal> masterMap14 = new HashMap<>();

		try {
			connection = Database.INSTANCE.getReadOnlyConnection();
			String statement5Days = "select op.fk_associate_id, date_format(o.date_purchased, '%Y-%m-%d') as day, count(distinct(o.orders_id)) as count from orders_products op join trackorders tos on tos.orders_products_id = op.orders_products_id join orders o on o.orders_id = op.orders_id where op.fk_associate_id in (4,318,354) and date_format(o.date_purchased, '%Y-%m-%d') >= date_format(now() - INTERVAL 5 DAY, '%Y-%m-%d') and date_format(o.date_purchased, '%Y-%m-%d') < date_format(now(), '%Y-%m-%d') and o.fk_associate_id = 5 and o.orders_status <> 'Cancelled' and o.delivery_country = 'India' and (op.delivery_status = 0 and op.orders_product_status not in ('Shipped', 'Rejected')) group by op.fk_associate_id,day order by op.fk_associate_id";

			String statement14Days = "select op.fk_associate_id, count(distinct(o.orders_id)) as count from orders_products op join trackorders tos on tos.orders_products_id = op.orders_products_id join orders o on o.orders_id = op.orders_id where op.fk_associate_id in (4,318,354) and date_format(o.date_purchased, '%Y-%m-%d') >= date_format(now() - INTERVAL 20 DAY, '%Y-%m-%d') and date_format(o.date_purchased, '%Y-%m-%d') < date_format(now() - INTERVAL 5 DAY, '%Y-%m-%d') and o.fk_associate_id = 5 and o.orders_status <> 'Cancelled' and o.delivery_country = 'India' and (op.delivery_status = 0 and op.orders_product_status not in ('Shipped','Rejected')) group by  op.fk_associate_id order by op.fk_associate_id";


			preparedStatement5 = connection.prepareStatement(statement5Days,ResultSet.TYPE_SCROLL_SENSITIVE);
			resultSet5 = preparedStatement5.executeQuery();

			preparedStatement14 = connection.prepareStatement(statement14Days,ResultSet.TYPE_SCROLL_SENSITIVE);
			resultSet14 = preparedStatement14.executeQuery();

			List<String> daysList = getPrevDaysList(1,"yyyy-MM-dd",5);

			while (resultSet14.next()){
				masterMap14.put(resultSet14.getString("fk_associate_id"), resultSet14.getBigDecimal("count"));
			}

			while(resultSet5.next()) {
				String currentWarehouse = resultSet5.getString("fk_associate_id");
				if(masterMap.containsKey(currentWarehouse)){
					Map<String, BigDecimal> subMap = masterMap.get(currentWarehouse);
					subMap.put(resultSet5.getString("day"), resultSet5.getBigDecimal("count"));
				}else{
					Map<String, BigDecimal> subMap = new HashMap<>();
					subMap.put(resultSet5.getString("day"), resultSet5.getBigDecimal("count"));
					masterMap.put(currentWarehouse , subMap);
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
				//dataList.add(0, String.valueOf(totalOrders));
				if(masterMap14.containsKey(entry.getKey())){
					dataList.add( String.valueOf(masterMap14.get(entry.getKey())));
					masterMap14.remove(entry.getKey());
				}else{
					dataList.add( "0");
				}
				dataList.add(0, entry.getKey());
				row.setColList(dataList);
				rowList.add(row);
				totalOrders = BigDecimal.ZERO;
			}

			if (!rowList.isEmpty())
			{
				Row heading = new Row();
				daysList.add( "Last 14 days");
				daysList.add(0, "Warehouse");
				heading.setColList(daysList);
				rowList.add(0, heading);
			}
		} catch (Exception exception) {
			System.out.println(exception.toString());
			throw exception;
		} finally {
			Database.INSTANCE.closeStatement(preparedStatement5);
			Database.INSTANCE.closeResultSet(resultSet5);
			Database.INSTANCE.closeStatement(preparedStatement14);
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
		Map<String, Map<String, BigDecimal>> masterMap = new HashMap<>(); //Map<fk_associate_id, Map<date, count>>

		try {
			connection = Database.INSTANCE.getReadOnlyConnection();
			String statement = "select op.fk_associate_id, date_format(o.date_purchased, '%Y-%m-%d') as day, count(distinct(o.orders_id)) as count from orders_products op join trackorders tos on tos.orders_products_id = op.orders_products_id and tos.orders_id = op.orders_id join orders o on o.orders_id = op.orders_id where op.fk_associate_id in (4,318,354) and date_format(o.date_purchased, '%Y-%m-%d') >= date_format(now() - INTERVAL 8 DAY, '%Y-%m-%d') and date_format(o.date_purchased, '%Y-%m-%d') < date_format(now(), '%Y-%m-%d') and o.fk_associate_id = 5 and o.orders_status <> 'Cancelled' and o.delivery_country = 'India' and (op.delivery_status <> 1  or op.orders_product_status in ('Confirmed','Dispatched','Processed','Processing')) group by  op.fk_associate_id,day;";


			preparedStatement = connection.prepareStatement(statement,ResultSet.TYPE_SCROLL_SENSITIVE);
			resultSet = preparedStatement.executeQuery();

			List<String> daysList = getPrevDaysList(1,"yyyy-MM-dd",8);

			while(resultSet.next()) {
				String currentWarehouse = resultSet.getString("fk_associate_id");
				if(masterMap.containsKey(currentWarehouse)){
					Map<String, BigDecimal> subMap = masterMap.get(currentWarehouse);
					subMap.put(resultSet.getString("day"), resultSet.getBigDecimal("count"));
				}else{
					Map<String, BigDecimal> subMap = new HashMap<>();
					subMap.put(resultSet.getString("day"), resultSet.getBigDecimal("count"));
					masterMap.put(currentWarehouse , subMap);
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
				daysList.add(0, "Warehouse");
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

package com.igp.reports.ops.OrderdispVDelIndia;

import com.igp.reports.models.Row;
import com.igp.reports.util.database.Database;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

//dvd
//2.A Shipped vs Delivered India
public class OrderDispVDelIndUtil
{

	public static List<Row> getData(int interval) throws Exception{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Row> rowList = new ArrayList<>();
		BigDecimal totalOrders = BigDecimal.ZERO;
		BigDecimal totalDispatched = BigDecimal.ZERO;
		BigDecimal totalDelivered = BigDecimal.ZERO;

		try {//, o.customers_id
			connection = Database.INSTANCE.getReadOnlyConnection();
			String statement = "select date_format(o.date_purchased, '%Y-%m-%d') as day, count(distinct(o.orders_id)) as total_orders, sum(case when tk.orderStatus = 'Dispatched' then 1 else 0 end) as dispached, sum(case when (tk.deliveredDate <> '0000-00-00 00:00:00' OR tk.deliveryDate <>'0000-00-00 00:00:00') then 1 else 0 end) as delivered from orders o left join trackorders tk ON o.orders_id = tk.orders_id where o.fk_associate_id = 5 AND orders_status <> 'Cancelled' AND date_format(o.date_purchased, '%Y-%m-%d') < date_format(now(), '%Y-%m-%d') AND date_format(o.date_purchased, '%Y-%m-%d') >= date_format(now() - INTERVAL 5 DAY, '%Y-%m-%d') group by day order by day desc";

			String statement14 = "select count(distinct(o.orders_id)) as total_orders, sum(case when tk.orderStatus = 'Dispatched' then 1 else 0 end) as dispached, sum(case when (tk.deliveredDate <> '0000-00-00 00:00:00' OR tk.deliveryDate <>'0000-00-00 00:00:00') then 1 else 0 end) as delivered from orders o left join trackorders tk ON o.orders_id = tk.orders_id where o.fk_associate_id = 5 AND orders_status <> 'Cancelled' AND date_format(o.date_purchased, '%Y-%m-%d') < date_format(now() - INTERVAL 5 DAY, '%Y-%m-%d') AND date_format(o.date_purchased, '%Y-%m-%d') >= date_format(now() - INTERVAL 20 DAY, '%Y-%m-%d')";

			/*String statement = "select date_format(o.date_purchased, '%Y-%m-%d') as day, count(distinct(o.orders_id)) as total_orders, sum(case when tk.orderStatus = 'Dispatched' then 1 else 0 end) as dispached, sum(case when (tk.deliveredDate <> '0000-00-00 00:00:00' OR tk.deliveryDate <>'0000-00-00 00:00:00') then 1 else 0 end) as delivered from orders o left join trackorders tk ON o.orders_id = tk.orders_id where o.fk_associate_id = 5 AND orders_status <> 'Cancelled' AND date_format(o.date_purchased, '%Y-%m-%d') >= date_format(now() - INTERVAL 200 DAY, '%Y-%m-%d') group by day";*/
			preparedStatement = connection.prepareStatement(statement);
			resultSet = preparedStatement.executeQuery();

			while(resultSet.next()) {
				Row row = new Row();
				BigDecimal orders = resultSet.getBigDecimal("total_orders");
				BigDecimal dispatched = resultSet.getBigDecimal("dispached");
				BigDecimal delivered = resultSet.getBigDecimal("delivered");

				totalOrders = totalOrders.add(orders);
				totalDispatched = totalDispatched.add(dispatched);
				totalDelivered = totalDelivered.add(delivered);

				row.setColList(resultSet.getString("day"), String.valueOf(orders) , String.valueOf(dispatched), String.valueOf(delivered));
				rowList.add(row);
			}

			preparedStatement = connection.prepareStatement(statement14);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()){
				Row row = new Row();
				row.setColList(" Last 14 days ", String.valueOf(resultSet.getBigDecimal("total_orders")) , String.valueOf(resultSet.getBigDecimal("dispached")), String.valueOf(resultSet.getBigDecimal("delivered")));
				//row.setColList(" Total ", String.valueOf(totalOrders) , String.valueOf(totalDispatched), String.valueOf(totalDelivered));
				rowList.add( row);
			}

			if (!rowList.isEmpty())
			{
				Row heading = new Row();
				heading.setColList("Date", "Total Orders", "Shipped", "Delivered");
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
	}

}

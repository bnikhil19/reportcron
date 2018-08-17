package com.igp.reports.hourlyOrders;

import com.igp.reports.models.Row;
import com.igp.reports.util.database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HourlyOrdersUtil
{

	public static List<Row> getData(int interval) throws Exception{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Row> rowList = new ArrayList<>();

		try {
			connection = Database.INSTANCE.getReadOnlyConnection();
			String statement = "select date_format(date_purchased, '%H') as hour, sum(case when date_format(date_purchased, '%Y-%m-%d') = date_format(now() - INTERVAL " + (interval+1) + " DAY, '%Y-%m-%d') then 1 else 0 end) as `orders(T-" + (interval+1) + ")`, sum(case when date_format(date_purchased, '%Y-%m-%d') = date_format(now() - INTERVAL " + (interval) + " DAY, '%Y-%m-%d') then 1 else 0 end) as `orders(T-" + (interval) + ")`, round(sum(case when date_format(date_purchased, '%Y-%m-%d') = date_format(now() - INTERVAL " + (interval+1) + " DAY, '%Y-%m-%d') then (shipping_cost_in_inr + orders_product_total_inr - orders_product_discount) else 0 end)) as `rev(T-" + (interval+1) + ")`, round(sum(case when date_format(date_purchased, '%Y-%m-%d') = date_format(now() - INTERVAL " + (interval) + " DAY, '%Y-%m-%d') then (shipping_cost_in_inr + orders_product_total_inr - orders_product_discount) else 0 end)) as `rev(T-" + (interval) + ")` from orders where fk_associate_id = 5 and date_format(date_purchased, '%Y-%m-%d') in (date_format(now() - INTERVAL " + (interval+1) + " DAY, '%Y-%m-%d'), date_format(now() - INTERVAL " + (interval) + " DAY, '%Y-%m-%d')) and orders_status <> 'Cancelled' group by hour;";
			preparedStatement = connection.prepareStatement(statement);
			resultSet = preparedStatement.executeQuery();

			while(resultSet.next()) {
				Row row = new Row();
				row.setColList(resultSet.getString("hour"), resultSet.getString("orders(T-" + (interval+1) + ")"),
						resultSet.getString("orders(T-" + (interval) + ")"), resultSet.getString("rev(T-" + (interval+1) + ")"),
						resultSet.getString("rev(T-" + (interval) + ")"));
				rowList.add(row);
			}
			if (!rowList.isEmpty())
			{
				Row heading = new Row();
				heading.setColList("hour", "orders(T-" + (interval+1) + ")","orders(T-" + (interval) + ")", "rev(T-" + (interval+1) + ")","rev(T-" + (interval) + ")");
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

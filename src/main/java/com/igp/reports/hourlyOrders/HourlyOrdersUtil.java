package com.igp.reports.hourlyOrders;

import com.igp.reports.models.Row;
import com.igp.reports.util.database.Database;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HourlyOrdersUtil
{

	private static BigDecimal MIN = BigDecimal.TEN;

	public static List<Row> getData(int interval) throws Exception{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Row> rowList = new ArrayList<>();

		try {
			connection = Database.INSTANCE.getReadOnlyConnection();
			String statement = "select sub.* , 100-((sub.orders" + (interval+1) + "/sub.orders" + (interval) + ")*100) as '%diffcount' from (select date_format(date_purchased, '%H') as hour, sum(case when date_format(date_purchased, '%Y-%m-%d') = date_format(now() - INTERVAL " + (interval+1) + " DAY, '%Y-%m-%d') then 1 else 0 end) as `orders" + (interval+1) + "`, sum(case when date_format(date_purchased, '%Y-%m-%d') = date_format(now() - INTERVAL " + (interval) + " DAY, '%Y-%m-%d') then 1 else 0 end) as `orders" + (interval) + "`, round(sum(case when date_format(date_purchased, '%Y-%m-%d') = date_format(now() - INTERVAL " + (interval+1) + " DAY, '%Y-%m-%d') then (shipping_cost_in_inr + orders_product_total_inr - orders_product_discount) else 0 end)) as `rev(T-" + (interval+1) + ")`, round(sum(case when date_format(date_purchased, '%Y-%m-%d') = date_format(now() - INTERVAL " + (interval) + " DAY, '%Y-%m-%d') then (shipping_cost_in_inr + orders_product_total_inr - orders_product_discount) else 0 end)) as `rev(T-" + (interval) + ")` from orders where fk_associate_id = 5 and date_format(date_purchased, '%Y-%m-%d') in (date_format(now() - INTERVAL " + (interval+1) + " DAY, '%Y-%m-%d'), date_format(now() - INTERVAL " + (interval) + " DAY, '%Y-%m-%d')) and orders_status <> 'Cancelled' group by hour) as sub";
			preparedStatement = connection.prepareStatement(statement);
			resultSet = preparedStatement.executeQuery();

			while(resultSet.next()) {
				Row row = new Row();
				row.setColList(resultSet.getString("hour"), resultSet.getString("orders" + (interval+1)),
						resultSet.getString("orders" + (interval)), resultSet.getString("rev(T-" + (interval+1) + ")"),
						resultSet.getString("rev(T-" + (interval) + ")"), resultSet.getString("%diffcount"));
				rowList.add(row);
			}
			if (!rowList.isEmpty())
			{
				Row heading = new Row();
				heading.setColList("hour", "orders(T-" + (interval+1) + ")","orders(T-" + (interval) + ")", "rev(T-" + (interval+1) + ")","rev(T-" + (interval) + ")", "% count difference");
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

	static List<Row> filterThreshold(List<Row> rowList){
		int cnt = 0;
		List<Row> filteredList = new ArrayList<>();
		for(Row row : rowList){
			if(cnt++ > 0){
				BigDecimal perc = new BigDecimal(row.getColList().get(4));
				if(perc.compareTo(MIN) <= 0){
					filteredList.add(row);
				}
			}else{
				filteredList.add(row);//adding heading row
			}
		}
		return filteredList;
	}

}

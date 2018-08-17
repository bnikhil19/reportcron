package com.igp.reports.domestic;

import com.igp.reports.models.Row;
import com.igp.reports.util.database.Database;

import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DomesticUtil
{

	public static List<List<Row>> getData(int interval) throws Exception{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String statement ;
		List<List<Row>> rowListList = new ArrayList<>();
		Row row ;

		try {
			connection = Database.INSTANCE.getReadOnlyConnection();
			statement = "select *, round(sub.total/sub.count) as AOV from (select case when customers_country in ('India', 'USA', 'Canada', 'UK') then customers_country else 'other' end as cust_country, count(*) as count,sum(shipping_cost_in_inr+orders_product_total_inr-orders_product_discount) as total "
					+ "from orders where (date_purchased >= date_format((NOW() - INTERVAL "+interval+" DAY),'%Y-%m-%d') "
					+ "AND fk_associate_id = 5 AND orders_status <> 'Cancelled') group by cust_country order by count desc) as sub";

			preparedStatement = connection.prepareStatement(statement);
			resultSet = preparedStatement.executeQuery();

			List<Row> rowList = new ArrayList<>();

			Row heading = new Row();
			heading.setColList("Country(Ordered)","Count","Total", "AOV");
			rowList.add(heading);


			while(resultSet.next()) {
				row = new Row();
				row.setColList(resultSet.getString("cust_country"), resultSet.getString("count"), String.valueOf(resultSet.getBigDecimal("total").setScale(2,RoundingMode.HALF_UP)), resultSet.getString("AOV"));
				rowList.add(row);
			}

			if (!rowList.isEmpty())	{
				rowListList.add(rowList);
			}

			//now for receiver
			statement = "select *, round(sub.total/sub.count) as AOV from (select case when delivery_country in ('India', 'USA', 'Canada', 'UK') then delivery_country else 'other' end as del_country, count(*) as count,sum(shipping_cost_in_inr+orders_product_total_inr-orders_product_discount) as total "
					+ "from orders where (date_purchased >= date_format((NOW() - INTERVAL "+interval +" DAY),'%Y-%m-%d') "
					+ "and fk_associate_id = 5 AND orders_status <> 'Cancelled') "
					+ "group by del_country order by count desc ) as sub";

			rowList = new ArrayList<>();
			heading = new Row();
			heading.setColList("Country(Delivered)", "Count","Total", "AOV");
			rowList.add(heading);

			preparedStatement = connection.prepareStatement(statement);
			resultSet = preparedStatement.executeQuery();

			while(resultSet.next()) {
				row = new Row();
				row.setColList(resultSet.getString("del_country"), resultSet.getString("count"), String.valueOf(resultSet.getBigDecimal("total").setScale(2,RoundingMode.HALF_UP)), resultSet.getString("AOV"));
				rowList.add(row);
			}

			if (!rowList.isEmpty())	{
				rowListList.add(rowList);
			}

		} catch (Exception exception) {
			System.out.println(exception.toString());
			throw exception;
		} finally {
			Database.INSTANCE.closeStatement(preparedStatement);
			Database.INSTANCE.closeResultSet(resultSet);
			Database.INSTANCE.closeConnection(connection);
		}
		return rowListList;
	}

}

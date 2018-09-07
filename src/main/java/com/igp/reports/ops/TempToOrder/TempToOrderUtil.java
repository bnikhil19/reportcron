package com.igp.reports.ops.TempToOrder;

import com.igp.reports.models.Row;
import com.igp.reports.util.database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


//1.A Payments
public class TempToOrderUtil
{
	public static List<Row> getData(int interval) throws Exception
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Row> rowList = new ArrayList<>();

		try
		{
			connection = Database.INSTANCE.getReadOnlyConnection();
			/*String statement = " select date_format(ot.orders_temp_date, '%Y-%m-%d') as day, sum(case when o.orders_temp_id is not NULL AND o.orders_status <> 'Cancelled' then 1 else 0 end) as converted, sum(case when o.orders_id is NULL then 1 else 0 end) failed from orders_temp ot left outer join orders o on ot.orders_temp_id = o.orders_temp_id where ot.p_options_code is not null AND ot.fk_associate_id = 5 AND date_format(ot.orders_temp_date, '%Y-%m-%d') >= date_format(now() - INTERVAL 1 DAY, '%Y-%m-%d') AND date_format(ot.orders_temp_date, '%Y-%m-%d') < date_format(now(), '%Y-%m-%d');";*/

			String statement = "select date_format(ot.orders_temp_date, '%Y-%m-%d') as day, sum(case when o.orders_temp_id is not NULL AND o.orders_status <> 'Cancelled' then 1 else 0 end) as converted, sum(case when o.orders_id is NULL then 1 else 0 end) failed from (select distinct customers_id, orders_temp_id, ot.orders_temp_date from orders_temp ot where ot.p_options_code is not null AND ot.fk_associate_id = 5 AND date_format(ot.orders_temp_date, '%Y-%m-%d') >= date_format(now() - INTERVAL 1 DAY, '%Y-%m-%d') AND date_format(ot.orders_temp_date, '%Y-%m-%d') < date_format(now(), '%Y-%m-%d') group by customers_id order by orders_temp_date desc) as ot left outer join  orders o on ot.orders_temp_id = o.orders_temp_id ";

			preparedStatement = connection.prepareStatement(statement);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next())
			{
				Row row = new Row();
				row.setColList(resultSet.getString("day"), resultSet.getString("converted"), resultSet.getString("failed"));
				rowList.add(row);
			}

			if (!rowList.isEmpty())
			{
				Row heading = new Row();
				heading.setColList("Date", "Order Generated", "Order not Generated");
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

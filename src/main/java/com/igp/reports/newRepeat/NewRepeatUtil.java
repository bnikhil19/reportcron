package com.igp.reports.newRepeat;

import com.igp.reports.models.Row;
import com.igp.reports.util.database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class NewRepeatUtil
{

	public static List<Row> getData(int interval) throws Exception{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Row> rowList = new ArrayList<>();

		try {
			connection = Database.INSTANCE.getReadOnlyConnection();
			String statement = "select dt, new, total, (total-new) as rep, (new/total)*100 as '%new' "
					+ "from (select dt, sum(case when old is null then 1 else 0 end) as new, count(1) as total from (select date(n.date_purchased) as dt, "
					+ "n.orders_id as new, o.orders_id as old "
					+ "from orders n left join orders o on o.customers_id = n.customers_id and o.date_purchased < date_format((NOW() - INTERVAL "+interval+" DAY),'%Y-%m-%d') "
					+ "where n.date_purchased >= date_format((NOW() - INTERVAL "+interval+" DAY),'%Y-%m-%d') and n.fk_associate_id = 5 "
					+ "and n.orders_status <> 'Cancelled' and n.customers_country = 'India' "
					+ "group by dt, n.orders_id) tt group by dt) tt;";
			preparedStatement = connection.prepareStatement(statement);
			resultSet = preparedStatement.executeQuery();

			while(resultSet.next()) {
				Row row = new Row();
				row.setColList(resultSet.getString("dt"), resultSet.getString("new"),resultSet.getString("rep"),
						resultSet.getString("total"), resultSet.getString("%new"));
				rowList.add(row);
			}
			if (!rowList.isEmpty())
			{
				Row heading = new Row();
				heading.setColList("Date", "New count", "Repeat Count", "Total", "% new");
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

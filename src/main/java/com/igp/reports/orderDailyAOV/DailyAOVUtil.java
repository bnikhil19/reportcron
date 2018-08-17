package com.igp.reports.orderDailyAOV;

import com.igp.reports.models.Row;
import com.igp.reports.util.database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DailyAOVUtil
{

	public static List<Row> getData(int interval) throws Exception{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Row> rowList = new ArrayList<>();

		try {
			connection = Database.INSTANCE.getReadOnlyConnection();
			String statement = "select dt, cnt, round(rev/cnt) as 'aov' "
					+ "from (select date(date_purchased) as dt, count(1) as 'cnt', sum((orders_product_total+shipping_cost)*days_conversion_factor) as 'rev' "
					+ "from orders where fk_associate_id = 5 and orders_status <> 'Cancelled' and date_purchased >= date_format(NOW() - INTERVAL "+interval+" DAY, '%Y-%m-%d') "
					+ "group by dt) tt";

			preparedStatement = connection.prepareStatement(statement);
			//System.out.println(preparedStatement.toString());
			resultSet = preparedStatement.executeQuery();

			while(resultSet.next()) {
				Row row = new Row();
				row.setColList(resultSet.getString("dt"), resultSet.getString("cnt"), resultSet.getString("aov"));
				rowList.add(row);
			}
			if (!rowList.isEmpty())
			{
				Row heading = new Row();
				heading.setColList("Date", "Count", "AOV");
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

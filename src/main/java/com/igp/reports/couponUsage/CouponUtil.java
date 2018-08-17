package com.igp.reports.couponUsage;

import com.igp.reports.models.Row;
import com.igp.reports.util.database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CouponUtil
{
	private static final int threshold = 2 ; // should be 2%

	public static List<Row> getData(int interval) throws Exception{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Row> rowList = new ArrayList<>();

		try {
			connection = Database.INSTANCE.getReadOnlyConnection();
			String statement = "select oei.coupon_code, count(o.orders_id) as usage_count "
					+ "from orders o LEFT JOIN orders_extra_info oei "
					+ "on o.orders_id = oei.orders_id "
					+ "where (date_purchased >= date_format((NOW() - INTERVAL "+interval+" DAY),'%Y-%m-%d') AND date_purchased < date_format(NOW() - INTERVAL "+(interval - 1)+" DAY,'%Y-%m-%d') "
					+ "AND oei.coupon_code is not NULL AND oei.coupon_code != '' AND o.fk_associate_id = 5 AND o.orders_status <> 'Cancelled') "
					+ "group by oei.coupon_code order by usage_count DESC limit 5";

			preparedStatement = connection.prepareStatement(statement);
			//System.out.println(preparedStatement.toString());
			resultSet = preparedStatement.executeQuery();

			while(resultSet.next()) {
				Row row = new Row();
				row.setColList(resultSet.getString("oei.coupon_code"), resultSet.getString("usage_count"));
				rowList.add(row);
			}
			if (!rowList.isEmpty())
			{
				Row heading = new Row();
				heading.setColList("Coupon code", "Usage count");
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

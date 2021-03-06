package com.igp.reports.discount;

import com.igp.reports.models.Row;
import com.igp.reports.util.database.Database;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DiscountUtil
{
	private static final BigDecimal THRESHOLD = new BigDecimal("4");

	public static List<Row> getData(int interval) throws Exception{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Row> rowList = new ArrayList<>();

		try {
			connection = Database.INSTANCE.getReadOnlyConnection();
			String statement = "select dt date, disc/rev as disc from (select date(date_purchased) as dt, sum(orders_product_discount) as disc, "
					+ "sum(orders_product_total) as rev from orders "
					+ "where date_purchased >= date_format((NOW() - INTERVAL 5 DAY),'%Y-%m-%d') and fk_associate_id = 5 and orders_status <> 'Cancelled' "
					+ "group by dt) tt group by dt order by dt desc";
			preparedStatement = connection.prepareStatement(statement);
			resultSet = preparedStatement.executeQuery();

			while(resultSet.next()) {
				Row row = new Row();
				row.setColList(resultSet.getString("date"), String.valueOf(resultSet.getBigDecimal("disc").multiply(BigDecimal.valueOf(100))));
				rowList.add(row);
			}
			if (!rowList.isEmpty())
			{
				Row heading = new Row();
				heading.setColList("Date", "Discount");
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
		//check if discount percent is  greater than OR equal to 4

		int cnt = 0;
		List<Row> filteredList = new ArrayList<>();

		for(Row row : rowList){
			if(cnt++ > 0){
				if(new BigDecimal(row.getColList().get(1)).compareTo(THRESHOLD) >= 0 ){
					filteredList.add(row);
				}
			}
			if(!filteredList.isEmpty()){
				filteredList.add(0,row);//adding heading row
			}
		}

		return filteredList;

		/*return rowList.stream()
				.filter(row -> new BigDecimal(row.getColList().get(1)).compareTo(BigDecimal.valueOf(4)) >= 0 )
				.collect(Collectors.toList());*/
	}
}

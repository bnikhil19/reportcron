package com.igp.reports.productMappings;

import com.igp.reports.models.Row;
import com.igp.reports.util.database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductMappinngUtil
{

	public static List<Row> getData(int interval) throws Exception{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Row> rowList = new ArrayList<>();

		try {
			connection = Database.INSTANCE.getReadOnlyConnection();
			String statement = "select cu.country_city, inc_countries, substr(pe.product_sku_id,1,2) as wh, flag_fs, "
					+ "count(distinct(pr.prod_id)) as count "
					+ "from newigp_product_extra_info pe "
					+ "join prod_rank pr on pr.prod_id = pe.products_id "
					+ "join cards_url cu on cu.card_id = pr.card_id "
					+ "where cu.type2 = '12' and cu.country_city <> '' and cu.category <> 'Cities' "
					+ "group by cu.country_city, inc_countries, wh, flag_fs";

			preparedStatement = connection.prepareStatement(statement);
			//System.out.println(preparedStatement.toString());
			resultSet = preparedStatement.executeQuery();

			while(resultSet.next()) {
				Row row = new Row();
				row.setColList(resultSet.getString("country_city"), resultSet.getString("inc_countries"),
						resultSet.getString("wh"), resultSet.getString("flag_fs"), resultSet.getString("count"));
				rowList.add(row);
			}
			if (!rowList.isEmpty())
			{
				Row heading = new Row();
				heading.setColList("Country City", "Inc Countries", "wh", "flag_fs", "Count");
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

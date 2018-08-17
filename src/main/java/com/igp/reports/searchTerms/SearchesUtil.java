package com.igp.reports.searchTerms;

import com.igp.reports.util.database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SearchesUtil
{

	public static void getData() throws Exception{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = Database.INSTANCE.getReadOnlyConnection();
			String statement = "select initial_query,count(initial_query) as Count from search_terms where  no_of_results=0 and ip not in ('203.92.48.%','110.173.187.%') and ts>= date_format((NOW() - INTERVAL 1 DAY),'%Y-%m-%d') group by initial_query order by Count desc;";
			preparedStatement = connection.prepareStatement(statement);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {

			}
		} catch (Exception exception) {
			System.out.println(exception.toString());
			throw exception;
		} finally {
			Database.INSTANCE.closeStatement(preparedStatement);
			Database.INSTANCE.closeResultSet(resultSet);
			Database.INSTANCE.closeConnection(connection);
		}
	}

}

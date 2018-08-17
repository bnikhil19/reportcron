package com.igp.reports.PaymentConversion;

import com.igp.reports.util.database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PayConversionUtil
{

	public static void getData() throws Exception{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = Database.INSTANCE.getReadOnlyConnection();
			String statement = "";

			preparedStatement = connection.prepareStatement(statement);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				System.out.println(resultSet.getString(0));
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

package com.igp.reports.warehouseWiseOrders;

import com.igp.reports.models.Row;
import com.igp.reports.util.database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class WHWiseOrdersUtil
{
	private static final int threshold = 2 ; // should be 2%

	public static List<Row> getData(int interval) throws Exception{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Row> rowList = new ArrayList<>();

		try {
			connection = Database.INSTANCE.getReadOnlyConnection();
			String statement = "select WH, Orders, OrdersWithAddons, round((OrdersWithAddons/Orders)*100,1) as Ratio1, ProdRev, AddonsRev, round((AddonsRev/ProdRev)*100,1) as Ratio2, round(ProdRev/Orders) as 'AOVexShip&Disc' from (select substr(op.products_code,1,2) as 'WH', count(distinct(o.orders_id)) as 'Orders', count(distinct(case when ps.festival_config like '%202047%' then o.orders_id else 0 end)) - 1 as 'OrdersWithAddons', sum(op.products_quantity*op.products_price_inr) as 'ProdRev', sum(case when ps.festival_config like '%202047%' then op.products_quantity else 0 end) as '#Addons', sum(case when ps.festival_config like '%202047%' then op.products_quantity*op.products_price_inr else 0 end) as 'AddonsRev' from orders_products op join orders o on o.orders_id = op.orders_id join prep_ticket_index pt on pt.fk_products_id = op.products_id join prep_sku_attribution ps on ps.prep_id = pt.id where o.fk_associate_id = 5 and o.date_purchased >= date_format((NOW() - INTERVAL "+interval+" DAY),'%Y-%m-%d') and o.orders_status <> 'Cancelled' group by WH having OrdersWithAddons > 0) t order by WH desc;";

			preparedStatement = connection.prepareStatement(statement);
			//System.out.println(preparedStatement.toString());
			resultSet = preparedStatement.executeQuery();

			while(resultSet.next()) {
				Row row = new Row();
				row.setColList(resultSet.getString("WH"), resultSet.getString("Orders"),
						resultSet.getString("OrdersWithAddons"), resultSet.getString("Ratio1"),
						resultSet.getString("ProdRev"), resultSet.getString("AddonsRev"),
						resultSet.getString("Ratio2"), resultSet.getString("AOVexShip&Disc")
							);
				rowList.add(row);
			}
			if (!rowList.isEmpty())
			{
				Row heading = new Row();
				heading.setColList(" WH ", "Orders", "Orders With Addons","Ratio1","Prod Rev", "Addons Rev","Ratio2","AOVexShip&Disc");
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

package global;

import java.sql.*;

public class MysqlConnection {

	Connection conn;

	public MysqlConnection() throws Exception {
		try {

			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String url = "jdbc:mysql://localhost/hyperplanning";

			conn = DriverManager.getConnection(url,"root","");

		} catch (SQLException e) {

			// Handle error

			e.printStackTrace();

		}

	}

	public ResultSet queryTable(String query) throws Exception {
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(query);
		return rs;

	}

	public void executeTable(String query) throws Exception {
		Statement st = conn.createStatement();
		st.executeUpdate(query);
	}
	
	public int insertTableReturnId(String query) throws Exception {
		Statement st = conn.createStatement();
		int numero = st.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
		int id = 0;
		
		ResultSet rs = st.getGeneratedKeys();
        if (rs.next()){
            id=rs.getInt(1);
        }
        rs.close();

        return id;
	}

	public int countElement(ResultSet rs) {
		try {
			rs.last();
			int count = rs.getRow();
			rs.first();
			return count;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public void endConnection() throws Exception {
		conn.close();
		conn = null;
	}
}
package tramcity_server_connection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataSource {

	private static JDBCConnectionPool connectionPool ;
	public DataSource() throws SQLException, ClassNotFoundException{
		this.connectionPool = new JDBCConnectionPool();
	}
	public static ResultSet executeQuery(String sql) throws SQLException {
		Connection conn = getConnection();
		Statement st =  conn.createStatement();
		ResultSet rs = st.executeQuery(sql);
		returnConnection(conn); 
		return rs;
	}

	public void executeUpdate(String sql) throws SQLException {
		
		// TODO Auto-generated method stub
		Connection conn = getConnection();
		Statement st =  conn.createStatement();
		st.executeUpdate(sql);
		returnConnection(conn); 
	}
	
	public static Connection getConnection() throws SQLException {
		return connectionPool.getConnection();
	}

	public static void returnConnection(Connection conn) throws SQLException {
		connectionPool.returnConnection(conn);
	}
	public static void closeAllConnection() throws SQLException {
		connectionPool.closeAllConnection();
	}
	public static int getSize() {
		return connectionPool.getSize();
	}
}

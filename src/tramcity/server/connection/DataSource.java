package tramcity.server.connection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataSource {

	private static JDBCConnectionPool connectionPool ;
	public DataSource() throws SQLException, ClassNotFoundException{
		this.connectionPool = new JDBCConnectionPool();
	}
	public DataSource(int minConnection, int maxConnection) throws SQLException, ClassNotFoundException{
		this.connectionPool = new JDBCConnectionPool(minConnection, maxConnection);
	}
	public static ResultSet executeQuery(String sql) throws SQLException, InterruptedException {
		Connection conn = getConnection();
		Statement st =  conn.createStatement();
		ResultSet rs = st.executeQuery(sql);
		returnConnection(conn); 
		return rs;
	}

	public static void executeUpdate(String sql) throws SQLException, InterruptedException {
		
		// TODO Auto-generated method stub
		Connection conn = getConnection();
		Statement st =  conn.createStatement();
		st.executeUpdate(sql);
		returnConnection(conn); 
	}
	
	public static Connection getConnection() throws SQLException, InterruptedException {
		return connectionPool.getConnection();
	}

	public static void returnConnection(Connection conn) throws SQLException, InterruptedException {
		connectionPool.returnConnection(conn);
	}
	public static void closeAllConnection() throws SQLException {
		connectionPool.closeAllConnection();
	}
	public static int getSize() {
		return connectionPool.getSize();
	}
}

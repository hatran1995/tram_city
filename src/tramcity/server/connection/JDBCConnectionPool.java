package tramcity.server.connection;
import java.sql.*;
import java.util.ArrayList;

public class JDBCConnectionPool {
	
	public static int countConnectionsUsing = 0;
	public ArrayList<Connection> ConnectionsReadyToUse = new ArrayList<Connection>();
	private static String CONNECT_LINK = "jdbc:mysql://127.0.0.1:3306/puzzle_db?serverTimezone=UTC";
	//	private static String CONNECT_LINK = "jdbc:mysql://172.31.249.177:3306/puzzle_01?serverTimezone=UTC";
	private static int MAX_CONNEXION=1;
	private static int MIN_CONNEXION=0;
    public JDBCConnectionPool() {
		// TODO Auto-generated constructor stub    	
    	while( getSize()< MIN_CONNEXION) {
    		initConnection();
        	System.out.println("Add Connection: ReadyToUse:"+ ConnectionsReadyToUse.size() + " - InUse:" + countConnectionsUsing);
    	}
	}
    public JDBCConnectionPool(int minConnection, int maxConnection) {
		// TODO Auto-generated constructor stub  
    	MIN_CONNEXION = minConnection;
    	MAX_CONNEXION = maxConnection;
    	while( getSize()< MIN_CONNEXION) {
    		initConnection();
        	System.out.println("Add Connection: ReadyToUse:"+ ConnectionsReadyToUse.size() + " - InUse:" + countConnectionsUsing);
    	}
	}
    //return
    public synchronized Connection getConnection() throws InterruptedException {

        System.out.println("Start get connection!");
    	while (ConnectionsReadyToUse.isEmpty()) {
			// create new connection pool
        	if(countConnectionsUsing < MAX_CONNEXION ) {
        		initConnection();
	        	System.out.println("Add Connection: ReadyToUse:"+ ConnectionsReadyToUse.size() + " - InUse:" + countConnectionsUsing);
			}
			else {
				// max 
				
				//break;
				 try {
					 System.out.println("Nombre excessif de connexions. Veuillez patientez.");
		                wait();		                
		            } catch (InterruptedException e) {
		                e.printStackTrace();
		            }
			}
			
		}
		Connection tempConnection = ConnectionsReadyToUse.get(0); 
		ConnectionsReadyToUse.remove(0);
		countConnectionsUsing += 1;
    	System.out.println("Use Connection: ReadyToUse:"+ ConnectionsReadyToUse.size() + " - InUse:" + countConnectionsUsing);
		return tempConnection;
    }
    public synchronized void returnConnection (Connection c) throws InterruptedException, SQLException {
    	if (c.isClosed()) {
    		System.out.println("Connection is Closed");
        	countConnectionsUsing = countConnectionsUsing>0?countConnectionsUsing-1:0;
    		initConnection();
    		notifyAll();
        }else {
        	ConnectionsReadyToUse.add(c);
        	countConnectionsUsing = countConnectionsUsing>0?countConnectionsUsing-1:0;
        	System.out.println("Return Connection: ReadyToUse:"+ ConnectionsReadyToUse.size() + " - InUse:" + countConnectionsUsing);
        }
	}      
    public Connection initConnection() {
    	try{
    		//   		Connection conn = DriverManager.getConnection(CONNECT_LINK, "root", "toto");
    		Connection conn = DriverManager.getConnection(CONNECT_LINK, "root", "");
	    	if (conn != null) {
	    		while (getSize()<MAX_CONNEXION) {
		            System.out.println("Add new connection pool success!");
		            ConnectionsReadyToUse.add(conn);
	            }
	    		if(countConnectionsUsing > 0)
	    			notifyAll();
	            return conn;
	        } else {
	            System.out.println("Failed to create new connection pool!");
	            return null;
	        }
	
	    } catch (SQLException e) {
	        System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            return null;
	    } catch (Exception e) {
	        e.printStackTrace();
            return null;
	    }
	}
    public void closeAllConnection() throws SQLException {
		for(Connection c: ConnectionsReadyToUse) {
			c.close();
		}
		ConnectionsReadyToUse.clear();
	}
	public int getSize() {
		// TODO Auto-generated method stub
		return ConnectionsReadyToUse.size()+countConnectionsUsing;
	}
}

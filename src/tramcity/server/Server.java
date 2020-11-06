package tramcity.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

import tramcity.server.connection.DataSource;



@SuppressWarnings("unused")
public class Server extends Thread {

	// initialize socket and input stream
	private Socket socket = null;
	private ServerSocket server = null;
	// constructor with port
	public Server(int port) {

		// starts server and waits for a connection
		try {
			server = new ServerSocket(port);
			server.setReuseAddress(true);
			System.out.println("Server started on port " + port);
			System.out.println("Waiting for a client ...");			
			while (true) {
				socket = server.accept();				
				ClientHandler clientSock = new ClientHandler(socket);
				new Thread(clientSock).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (server != null) {
				try {
					server.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
;
		
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
			System.out.println("Hello from server"); 
			int minConnection = 0;
			int maxConnection = 5;//default
			if(System.getProperty("maxConnection") != null && System.getProperty("maxConnection") != "")
				{maxConnection = Integer.parseInt( System.getProperty("maxConnection"));
				System.out.println("Start server with maxConnection="+maxConnection); }
			else
				System.out.println("Start default server!"); 
			DataSource dst = new DataSource(minConnection, maxConnection);
			new Server(1995);
			
		}

}

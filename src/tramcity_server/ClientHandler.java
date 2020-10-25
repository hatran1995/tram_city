package tramcity_server;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;

import org.json.*;


public class ClientHandler implements Runnable {

	private final Socket socket;
	private DataInputStream in = null;

	public ClientHandler(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		// takes input from the client socket
		String line = "";

		try {

			in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			System.out.println(
					"  New client connected  in host   :    " 
					+ socket.getInetAddress().getHostAddress()
					+ "     Id Client   :   " 
					+ in.readUTF());
			
			while (!line.equals("0")) {
				try {
					String response = "";
					line = in.readUTF();	
					if(!line.equals("0")) {
						JSONObject input;
						System.out.println(line);
						input = new JSONObject(line);		
						System.out.println("json send from client:"+line);
						String api = input.getString("api").toUpperCase(); 
						if( api == "CLOSE_CONNECTION".toUpperCase()) {							
							line = "0";
							System.out.println("Client close connection");
						}else {
							// reponse du serveur pour le client format json					
							response = Router.router(input);
							System.out.println(response.toString());
						}
					}
					DataOutputStream oos = new DataOutputStream(socket.getOutputStream());
					// write object to Socket
					oos.writeUTF(response);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch (SocketException e) {
					// TODO Auto-generated catch block
					System.out.println("Client reset socket connection");
					//e.printStackTrace();
					break;
				}
			}
			System.out.println("Closing connection");

			socket.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}

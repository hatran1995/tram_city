package tramcity.server.test;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
// A Java program for a Client 
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.javafx.scene.paint.GradientUtils.Parser;

import tramcity.client.common.ApiEnum;
import tramcity.client.common.SendPackage;

public class ClientTest extends Thread {
	// Thread for socket
	private Thread t;
	private String threadName = "Connect Socket";
	// initialize socket and input output streams
	private Socket socket = null;
	private DataInputStream input = null;
	private DataOutputStream out = null;
	private PrintWriter outmsg;
	private BufferedReader inmsg;
	public SendPackage sendP = null;
	public JSONObject responseData = new JSONObject();
	private String UserName = "Client Name";

	// constructor to put ip address and port
	public ClientTest(String address, int port) {
		try {
			socket = new Socket(address, port);
			outmsg = new PrintWriter(socket.getOutputStream(), true);
			inmsg = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (UnknownHostException u) {
			System.out.println(u);
		} catch (IOException i) {
			System.out.println(i);
		}

	}
	
	public ClientTest(String address, int port, String name) {
		try {
			UserName = name;
			socket = new Socket(address, port);
			outmsg = new PrintWriter(socket.getOutputStream(), true);
			inmsg = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (UnknownHostException u) {
			System.out.println(u);
		} catch (IOException i) {
			System.out.println(i);
		}

	}
	
	private void showClientId() {
		try {
			System.out.println("Please enter id of the client ");
			//BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			Scanner scanner = new Scanner(System.in);
			String line = scanner.nextLine();
			out.writeUTF(line==""?UserName:line);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void closeConnection() {
		// close the connection
		try {
			out.writeUTF("0");
			System.out.println("Close socket");
			input.close();
			inmsg.close();
			out.close();
			outmsg.close();
			socket.close();
		} catch (IOException i) {
			System.out.println(i);
		}
	}

	public SendPackage getSendP() {
		return sendP;
	}

	public void setSendP(SendPackage sendPackage) {
		sendP = sendPackage;
	}

	public JSONObject getResponseData() {
		return responseData;
	}

	public void setResponseData(JSONObject resData) {
		responseData = resData;
	}

	@Override
	public void run() {
		// establish a connection
		try {
			System.out.println("Connected");
			// takes input from terminal
			input = new DataInputStream(System.in);
			// sends output to the socket
			out = new DataOutputStream(socket.getOutputStream());

//				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//				out.writeUTF("UserName");
		} catch (UnknownHostException u) {
			System.out.println(u);
		} catch (IOException i) {
			System.out.println(i);
		}

		showClientId();
		// sendP.setApi(ApiEnum.CITY_FIND_ALL);
		Boolean isEnd = false;
		while (!isEnd) {
			// if have new request from ui
			// System.out.println("SendPackage:"+ sendP);
			try {
				actionCmd();
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}			
			if (sendP != null) {
				if(sendP.getApi() == ApiEnum.CLOSE_CONNECTION) {
					isEnd = true;
					closeConnection();
				}else {
					System.out.println("SendPackage:" + sendP.toString());
					try {
						// get all city
						out.writeUTF(sendP.toString());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						isEnd = true;
						System.out.println("Server close connection!");						
						//e.printStackTrace();
						break;
					}	
					// safina chof lmok
					try {
						// System.out.println("Waiting for the result");
						DataInputStream oos = new DataInputStream(socket.getInputStream());
						String msg = oos.readUTF();
						try {
							JSONObject resd = new JSONObject(msg);
							responseData = resd;
							System.out.println("response: "+ resd.toString());
							this.setSendP(null);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	
						sendP = null;
						// isSend = true;	
					} catch (IOException i) {
						System.out.println(i);
					}
				}
			} else {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private void actionCmd() throws JSONException {
		System.out.println("Put number of Service:");
		System.out.println("1: Get all city");
		System.out.println("2: Get detail city");
		System.out.println("3: Get tramway city detail");
		System.out.println("0: Close");
		SendPackage sendPa = new SendPackage();		
		//step 1
		JSONObject bodyItem = new JSONObject();
		int cityID = -1;
		Scanner scanner = new Scanner(System.in);
		int iService  = scanner.nextInt();	
		switch (iService) {
		case 1:
			sendPa.setApi(ApiEnum.CITY_FIND_ALL);	
			break;
		case 2:
			System.out.println("Put City id:");
			cityID =  scanner.nextInt();
			bodyItem.put("ID", "" +cityID);			
			sendPa.setApi(ApiEnum.CITY_GET_ONE);		
			sendPa.setBody(bodyItem);
			break;
		case 3:
			System.out.println("Put City id:");
			cityID =  scanner.nextInt();
			bodyItem.put("ID", "" +cityID);			
			sendPa.setApi(ApiEnum.TRAMWAY_GET_ONE);		
			sendPa.setBody(bodyItem);
			break;
		case 0:
			sendPa.setApi(ApiEnum.CLOSE_CONNECTION);					
			break;
		}		
		this.setSendP(sendPa);		
	}
	
	public void sendMessage(String msg) throws IOException {
		outmsg.println(msg);
	}
	
	public String getMessage() throws IOException {
		String resp;
		resp = inmsg.readLine();
		return resp;
	}

	public void start() {
		System.out.println("Starting " + threadName);
		if (t == null) {
			t = new Thread(this, threadName);
			t.start();
		}
	}
	
	public static void main(String args[]) throws InterruptedException {

	//	ClientTest client = new ClientTest("127.0.0.1", 1995);
		ClientTest client = new ClientTest("172.31.249.169", 1995);
		client.start();
	}
}

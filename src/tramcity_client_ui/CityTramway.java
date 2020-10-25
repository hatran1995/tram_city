package tramcity_client_ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tramcity_client.Client;
import tramcity_client_common.ApiEnum;
import tramcity_client_common.SendPackage;

import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.geom.Path2D;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;
import java.awt.event.ActionEvent;

public class CityTramway {

	public JFrame frame;
	private JTextField txtBudget;
	private JTextField txtCostOne;
	public JPanel panel;
	public JPanel panel_cityinfo;
	public JPanel panel_2 ;
	private int cityID;
	private JLabel lbtMess;
	public 	Client client ;
	private JTextField txtRadius;
	private JButton btnUpdate ;
	int minRadius = 0;
	int maxRadius = 0;
	int minStation = 5;
	int maxStation = 5;
	int widthCity = 0;
	int heightCity = 0;

	/**
	 * Launch the application.
	 */
	//	public static void main(String[] args) {
	//		EventQueue.invokeLater(new Runnable() {
	//			public void run() {
	//				try {
	//					CityTramway window = new CityTramway();
	//					window.frame.setVisible(true);
	//				} catch (Exception e) {
	//					e.printStackTrace();
	//				}
	//			}
	//		});
	//	}

	/**
	 * Create the application.
	 * @throws InterruptedException 
	 */
	public CityTramway(Client socket, int id) throws InterruptedException {
		client = socket;
		cityID = id;
		initialize();
		getTramway();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(200, 100, 1000, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		panel = new JPanel();
		panel.setLayout(null);
		panel.setBackground(Color.WHITE);
		panel.setBounds(50, 50, 900, 700);
		frame.getContentPane().add(panel);

		JButton btnMenuDashboard = new JButton("Dashboard");
		btnMenuDashboard.setBounds(10, 64, 166, 23);
		panel.add(btnMenuDashboard);

		JButton btnMenuCityInfomation = new JButton("City Infomation");
		btnMenuCityInfomation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CityDetail ctDetail = null;
				try {
					ctDetail = new CityDetail(client, cityID);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				ctDetail.frame.setVisible(true);
				frame.dispose();
			}
		});
		//		btnMenuCityInfomation.setForeground(Color.BLACK);
		//		btnMenuCityInfomation.setFont(new Font("Tahoma", Font.PLAIN, 11));
		//		btnMenuCityInfomation.setBackground(Color.LIGHT_GRAY);
		btnMenuCityInfomation.setBounds(10, 98, 166, 23);
		panel.add(btnMenuCityInfomation);

		JButton btnMenuTramwayStation = new JButton("Tramway Station");
		btnMenuTramwayStation.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnMenuTramwayStation.setForeground(Color.WHITE);
		btnMenuTramwayStation.setBackground(Color.DARK_GRAY);
		btnMenuTramwayStation.setBounds(10, 132, 166, 23);
		panel.add(btnMenuTramwayStation);


		panel_cityinfo = new JPanel();
		panel_cityinfo.setLayout(null);
		panel_cityinfo.setBounds(186, 64, 684, 625);
		panel.add(panel_cityinfo);

		JLabel lblNewLabel_1_1 = new JLabel("Budget Network (\u20AC)");
		lblNewLabel_1_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_1_1.setBounds(10, 24, 113, 14);
		panel_cityinfo.add(lblNewLabel_1_1);

		txtBudget = new JTextField();
		txtBudget.setColumns(10);
		txtBudget.setBounds(145, 21, 165, 20);
		panel_cityinfo.add(txtBudget);

		JLabel lblNewLabel_1_1_1 = new JLabel("Cost of a station (\u20AC)");
		lblNewLabel_1_1_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_1_1_1.setBounds(345, 24, 113, 14);
		panel_cityinfo.add(lblNewLabel_1_1_1);

		txtCostOne = new JTextField();
		txtCostOne.setColumns(10);
		txtCostOne.setBounds(484, 21, 165, 20);
		panel_cityinfo.add(txtCostOne);

		btnUpdate = new JButton("Create");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					updateTramway();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnUpdate.setBounds(145, 77, 89, 23);
		panel_cityinfo.add(btnUpdate);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Dashboard cityList = new Dashboard(client,cityID);
				cityList.frame.setVisible(true);
				frame.dispose();
			}
		});
		btnCancel.setBounds(247, 77, 89, 23);
		panel_cityinfo.add(btnCancel);

		JButton btnRandomMap = new JButton("ReRandom");
		btnRandomMap.setBounds(345, 77, 134, 23);
		panel_cityinfo.add(btnRandomMap);

		lbtMess = new JLabel("");
		lbtMess.setBounds(145, 111, 436, 14);
		panel_cityinfo.add(lbtMess);

		txtRadius = new JTextField();
		txtRadius.setColumns(10);
		txtRadius.setBounds(145, 46, 165, 20);
		panel_cityinfo.add(txtRadius);

		JLabel lblNewLabel = new JLabel("Puzzle City");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel.setBounds(214, 11, 197, 27);
		panel.add(lblNewLabel);
		
		
		JLabel lblNewLabel_1_1_2 = new JLabel("Radius");
		lblNewLabel_1_1_2.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_1_1_2.setBounds(10, 49, 113, 14);
		panel_cityinfo.add(lblNewLabel_1_1_2);

		panel_2 = new JPanel();
		panel_2.setLayout(null);
		//panel_2.setBackground(new Color(60, 179, 113));
		panel_2.setBounds(47, 192, 610, 410);
		panel_cityinfo.add(panel_2);
		btnRandomMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					randomStation();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});



	}

	private void getTramway() throws InterruptedException {
		try {
			client.setResponseData(null);	
			JSONObject bodyItem = new JSONObject();
			bodyItem.put("ID", "" +cityID);

			SendPackage sendPa = new SendPackage();
			sendPa.setApi(ApiEnum.TRAMWAY_GET_ONE);		
			sendPa.setBody(bodyItem);
			client.setSendP(sendPa);

			JSONObject res = null;

			Object obj = new Object();
			synchronized (obj) {
				while(res == null) {
					res = client.getResponseData();
					System.out.println("wait res:"+res);
					if(res!= null) {
						// if success true - get data bind to table 
	
						boolean bSuccess = res.getBoolean("success");
						if(bSuccess) {
							// move to show map.
							//map
							JSONArray jaListPoint =  res.getJSONObject("data").getJSONArray("points");
							JSONArray jaListPath =  res.getJSONObject("data").getJSONArray("paths");
	
							setDataToField((res.getJSONObject("data").getJSONArray("tramways")).getJSONObject(0),jaListPoint,jaListPath);
						}
						System.out.println("return:"+res.toString());
	
					}else {
						//add new, hidden				
					}
					obj.wait(50);
				}
			}
			//CLOSE

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	

	private void setDataToField(JSONObject res, JSONArray jaListPoint, JSONArray jaListPath) {
		// TODO Auto-generated method stub
		try {
			txtBudget.setText(String.valueOf( res.getInt("Value")));
			txtCostOne.setText(String.valueOf( res.getInt("ValueStation")));
			txtRadius.setText(String.valueOf( res.getInt("Radius")));

			widthCity = res.getInt("Width");
			heightCity = res.getInt("Height");
			setMaxMinRadius();
			System.out.println(widthCity+"-"+heightCity);
			double offset =Math.min((double) 600/widthCity, (double) 400/heightCity) ;

			int lengListPoint = jaListPoint.length();
			if (lengListPoint > 0) {							
				Point[] lP = new Point[lengListPoint];
				for (int i = 0; i < lP.length; i++) {
					int x = jaListPoint.getJSONObject(i).getInt("Lat");
					int y = jaListPoint.getJSONObject(i).getInt("Long");
					lP[i] = new Point(x, y);
				}

				//System.out.println("loadMap: "+lP.length+"-"+offset); 
				panel_2.removeAll();
				panel_2.repaint();
				panel_2.setVisible(true);
				JPanel panel_map = new CityMap(lP,offset,jaListPath);
				//panel_map.setLayout(null);
				panel_map.setBackground(Color.BLACK);
				//panel_map.setBounds(0, 0, (int) (width*offset), (int) (height*offset));
				System.out.println(widthCity*offset + "-"+heightCity*offset );
				//panel_map.setBounds(0, 0, 600, 400);
				panel_map.setBounds(0, 0, (int) (widthCity*offset)+2, (int) (heightCity*offset)+2);
				
				//panel_map.setBackground(new Color(rgb));
				panel_map.setVisible(true);
				panel_2.add(panel_map);	
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	

	private void updateTramway() throws InterruptedException {
		if( true) {		//isValid() 	
			try {			
				client.setResponseData(null);		
				JSONObject bodyItem = new JSONObject();
				bodyItem.put("ID", "" +cityID);
				bodyItem.put("Value", "" +txtBudget.getText());
				bodyItem.put("ValueStation",  txtCostOne.getText());
			//bodyItem.put("NumberMaxStation", (int) (Double.parseDouble( txtBudget.getText())/ Double.parseDouble( txtCostOne.getText())));
				bodyItem.put("Radius",  txtRadius.getText());

				SendPackage sendPa = new SendPackage();
				sendPa.setApi(ApiEnum.TRAMWAY_UPDATE);		
				sendPa.setBody(bodyItem);
				client.setSendP(sendPa);

				JSONObject res = null;

				Object obj = new Object();
				synchronized (obj) {
					while(res == null) {
						res = client.getResponseData();
						System.out.println("wait res:"+res);
						if(res!= null) {
							// if success 
							boolean bSuccess = res.getBoolean("success");
							if(bSuccess) {
								lbtMess.setText("Render Map Success"); 
								btnUpdate.setText("Update");
								getTramway();
							}else {
								lbtMess.setText("Error :"+res.getString("msg") );	
								//hiddenmap
	
								panel_2.removeAll();
								panel_2.repaint();
								panel_2.setVisible(true);
								
							}
							System.out.println("Return:"+res.toString());
						}
						obj.wait(50);
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
	private void randomStation() throws InterruptedException {
		try {			
			client.setResponseData(null);		
			JSONObject bodyItem = new JSONObject();
			bodyItem.put("ID", "" +cityID);

			SendPackage sendPa = new SendPackage();
			sendPa.setApi(ApiEnum.TRAMWAY_MAP_RENDER);		
			sendPa.setBody(bodyItem);
			client.setSendP(sendPa);

			JSONObject res = null;

			Object obj = new Object();
			synchronized (obj) {
				while(res == null) {
					res = client.getResponseData();
					System.out.println("wait res:"+res);
					if(res!= null) {
						// if success 
						boolean sMess = res.getBoolean("success");
						if(sMess) {
							lbtMess.setText("Add Success");
							getTramway();
						}else {
							lbtMess.setText("Error :"+res.getString("msg") );						
						}
						System.out.println("Return:"+res.toString());
					}
					obj.wait(50);
				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void setMaxMinRadius() {
		// radius radius < min(width, height)
		maxRadius = Math.min(widthCity,heightCity);
	}

	private boolean isValid() {
		boolean valid = true;
		String textErr = "";
		int iBudget = 0;
		int iCostOne = 0;
		int iRadius = 0;
		// TODO Auto-generated method stub

		//check valid txtBudget
		try {	
			iBudget = Integer.parseInt( txtBudget.getText());
		} catch (Exception e) {
			textErr = "Budget value is not valid, please enter the data in numeric integer format";
			lbtMess.setText(textErr);
			return false;
		}

		//check valid txtCostOne
		try {	
			iCostOne = Integer.parseInt( txtCostOne.getText());
		} catch (Exception e) {
			textErr = "Budget value for one station is not valid, please enter the data in numeric integer format";
			lbtMess.setText(textErr);
			return false;

		}
		//check valid iRadius
		try {	
			iRadius = Integer.parseInt( txtRadius.getText());
		} catch (Exception e) {
			textErr = "Radius value is not valid, please enter the data in numeric integer format";
			lbtMess.setText(textErr);
			return false;

		}
		//check valid txtCostOne
		if (!(iBudget > 0) ) {
			textErr = "Budget value is not valid, please enter the budget valide";
			lbtMess.setText(textErr);
			return false;

		}


		if (!(iCostOne > 0) ) {
			textErr = "Budget value for one station is not valid, please enter the cost for one station valide";
			lbtMess.setText(textErr);
			return false;

		}
		if(iBudget<iCostOne) {
			textErr = "The cost of a station can't be higher than the city budget";

			lbtMess.setText(textErr);
			return false;

		}
		if(!(iCostOne > 0 && (iBudget/iCostOne >= 2))) {
			textErr = "You need at least 2 station to create a network";
			lbtMess.setText(textErr);
			return false;
		}
		if (!(iRadius > 0) ) {
			textErr = "Radius is not valid";
			lbtMess.setText(textErr);
			return false;

			
		}
		//check valid txtRadius
		try {	
			Integer.parseInt( txtRadius.getText());
		} catch (Exception e) {
			textErr = "Min distance between two point is not valid, please enter the data in numeric integer format";
			lbtMess.setText(textErr);
			return false;

		}


		lbtMess.setText(textErr);
		return valid;
	}
	
	
}

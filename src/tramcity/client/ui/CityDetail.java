package tramcity.client.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Point;
import javax.swing.SwingConstants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tramcity.client.Client;
import tramcity.client.common.ApiEnum;
import tramcity.client.common.SendPackage;

import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CityDetail {

	public JFrame frame;
	private JTextField txtCityName;
	private JTextField txtHeight;
	private JTextField txtWidth;
	private int cityID;
	private JLabel lbtMess;
	public JPanel panel_2 ;

	public 	Client client ;//= new Client("127.0.0.1", 4000);
	/**
	 * Launch the application.
	 */
	/**
	 * Create the application.
	 * @throws InterruptedException 
	 */
	public CityDetail(Client socket, int id) throws InterruptedException {
		client = socket;
		cityID = id;
		initialize();
		getCityInfo();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(200, 100, 1000, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBackground(Color.WHITE);
		panel.setBounds(50, 50, 900, 700);
		frame.getContentPane().add(panel);

		JButton btnMenuDashboard = new JButton("Dashboard");
		btnMenuDashboard.setBounds(10, 64, 166, 23);
		panel.add(btnMenuDashboard);

		JButton btnMenuCityInfomation = new JButton("City Infomation");
		btnMenuCityInfomation.setBackground(Color.DARK_GRAY);
		btnMenuCityInfomation.setForeground(Color.WHITE);
		btnMenuCityInfomation.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnMenuCityInfomation.setBounds(10, 98, 166, 23);
		panel.add(btnMenuCityInfomation);

		JButton btnMenuTramwayStation = new JButton("Tramway Station");
		btnMenuTramwayStation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				CityTramway windowCityTramway = null;
				try {
					windowCityTramway = new CityTramway(client, cityID);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				windowCityTramway.frame.setVisible(true);
				frame.dispose();
			}
		});
		btnMenuTramwayStation.setBounds(10, 132, 166, 23);
		panel.add(btnMenuTramwayStation);


		JPanel panel_cityinfo = new JPanel();
		panel_cityinfo.setBounds(186, 64, 684, 574);
		panel.add(panel_cityinfo);
		panel_cityinfo.setLayout(null);

		JLabel lblNewLabel_1 = new JLabel("City Name:");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_1.setBounds(10, 11, 97, 14);
		panel_cityinfo.add(lblNewLabel_1);

		txtCityName = new JTextField();
		txtCityName.setBounds(119, 8, 232, 20);
		panel_cityinfo.add(txtCityName);
		txtCityName.setColumns(10);

		txtHeight = new JTextField();
		txtHeight.setColumns(10);
		txtHeight.setBounds(119, 35, 232, 20);
		panel_cityinfo.add(txtHeight);

		JLabel lblNewLabel_1_1_1_1 = new JLabel("Height");
		lblNewLabel_1_1_1_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_1_1_1_1.setBounds(10, 38, 97, 14);
		panel_cityinfo.add(lblNewLabel_1_1_1_1);

		JLabel lblNewLabel_1_1_1_1_1 = new JLabel("Width");
		lblNewLabel_1_1_1_1_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_1_1_1_1_1.setBounds(10, 69, 97, 14);
		panel_cityinfo.add(lblNewLabel_1_1_1_1_1);

		txtWidth = new JTextField();
		txtWidth.setColumns(10);
		txtWidth.setBounds(119, 66, 232, 20);
		panel_cityinfo.add(txtWidth);


		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					updateCityInfo();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnUpdate.setBounds(119, 98, 89, 23);
		panel_cityinfo.add(btnUpdate);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Dashboard cityList = new Dashboard(client,cityID);
				cityList.frame.setVisible(true);
				frame.dispose();
			}
		});
		btnCancel.setBounds(221, 98, 89, 23);
		panel_cityinfo.add(btnCancel);

		lbtMess = new JLabel("");
		lbtMess.setBounds(47, 123, 315, 25);
		panel_cityinfo.add(lbtMess);

		JButton btnNewButton = new JButton("Delete");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					deleteCityInfo();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnNewButton.setBounds(322, 97, 97, 25);
		panel_cityinfo.add(btnNewButton);

		JLabel lblNewLabel = new JLabel("City Manager System");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel.setBounds(214, 11, 197, 27);
		panel.add(lblNewLabel);
		
		panel_2 = new JPanel();
		panel_2.setLayout(null);
		//panel_2.setBackground(new Color(60, 179, 113));
		panel_2.setBounds(46, 159, 610, 410);
		panel_cityinfo.add(panel_2);
	}


	private void getCityInfo() throws InterruptedException {
		try {
			client.setResponseData(null);		
			JSONObject bodyItem = new JSONObject();
			bodyItem.put("ID", "" +cityID);

			SendPackage sendPa = new SendPackage();
			sendPa.setApi(ApiEnum.CITY_GET_ONE);		
			sendPa.setBody(bodyItem);
			client.setSendP(sendPa);

			Object obj = new Object();
			synchronized (obj) {
				JSONObject res = null;
				while(res == null) {
					res = client.getResponseData();
	
					System.out.println("wait res:"+res);
					if(res!= null) {
						// if success true - get data bind to table 
						setDataToField((res.getJSONObject("data")));
						drawCity(Double.parseDouble(txtWidth.getText()), Double.parseDouble(txtHeight.getText()));
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
	private void setDataToField(JSONObject res) {
		// TODO Auto-generated method stub
		try {
			txtCityName.setText(res.getString("name"));
			txtHeight.setText(String.valueOf( res.getDouble("height")/1000));
			txtWidth.setText(String.valueOf( res.getDouble("width")/1000));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	private void deleteCityInfo() throws InterruptedException {
		try {
			client.setResponseData(null);		
			JSONObject bodyItem = new JSONObject();
			bodyItem.put("ID", "" +cityID);

			SendPackage sendPa = new SendPackage();
			sendPa.setApi(ApiEnum.CITY_DELETE);	
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
							lbtMess.setText("Delete Success");
							CityList listcity = new CityList(client);
							listcity.frame.setVisible(true);
							frame.dispose();						
	
						}else {
							lbtMess.setText("Error :"+res.getString("msg") );						
						}
						System.out.println("Result:"+res.toString());
					}
					obj.wait(50);
				} 
			} 	
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void updateCityInfo() throws InterruptedException {
		if(isValid()) {	
			try {			
				client.setResponseData(null);		
				JSONObject bodyItem = new JSONObject();
				bodyItem.put("ID", "" +cityID);
				bodyItem.put("name", "" +txtCityName.getText());
				bodyItem.put("height", Integer.parseInt( txtHeight.getText())*1000);
				bodyItem.put("width", Integer.parseInt( txtWidth.getText())*1000 );

				SendPackage sendPa = new SendPackage();
				sendPa.setApi(ApiEnum.CITY_UPDATE);		
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
								lbtMess.setText("Update Success");
								drawCity(Double.parseDouble(txtWidth.getText()), Double.parseDouble(txtHeight.getText()));
							}else {
								lbtMess.setText("Error :"+res.getString("msg") );						
							}
							System.out.println("Return:"+res.toString());
						}
						obj.wait(50);
					} 
				} 	
				getCityInfo();

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private void drawCity(double widthCity,double heightCity) {
		System.out.println(widthCity+"-"+heightCity);
		double offset =Math.min((double) 600/widthCity, (double) 400/heightCity) ;
		panel_2.removeAll();
		panel_2.repaint();
		panel_2.setVisible(true);
		Point[] jaListPoint = null;
		JSONArray jaListPath =  new JSONArray();
		JPanel panel_map = new CityMap(jaListPoint,offset,jaListPath);
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

	private boolean isValid() {
		boolean valid = true;
		String textErr = "";
		// TODO Auto-generated method stub
		System.out.println("txtCityName:"+txtCityName.getText());
		if(txtCityName.getText().isEmpty()) {
			textErr = "Name City is required.";
			valid = false;
		};	

		//check valid height
		try {	
			Double.parseDouble( txtHeight.getText());
		} catch (Exception e) {
			textErr = "Height value is not valid.";
			valid = false;
		}

		//check valid width
		try {	
			Double.parseDouble( txtWidth.getText());
		} catch (Exception e) {
			textErr = "Width value is not valid.";
			valid = false;
		}
	

		lbtMess.setText(textErr);
		return valid;
	}
}

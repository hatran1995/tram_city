package tramcity_client_ui;


import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.text.TableView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tramcity_client.Client;
import tramcity_client_common.ApiEnum;
import tramcity_client_common.CityTable;
import tramcity_client_common.SendPackage;

import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CityList {

	public JFrame frame;
	private JTable tblCity;
	public 	Client client ;//= new Client("127.0.0.1", 4000);

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					System.out.println("vao main");
//					
//					CityList window = new CityList();
//					window.frame.setVisible(true);
//
//					System.out.println("Load data");
//					
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
	public CityList(Client socket) throws InterruptedException {
		client = socket;
		initialize();
		getCityData();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		System.out.println("initialize");
		frame = new JFrame();
		frame.setBounds(100, 100, 700, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBackground(Color.WHITE);
		panel.setBounds(10, 11, 664, 439);
		frame.getContentPane().add(panel);
		//list city
		JPanel panel_cityinfo = new JPanel();
		panel_cityinfo.setBounds(10, 64, 644, 364);
		panel.add(panel_cityinfo);
		panel_cityinfo.setLayout(null);
		
		JLabel lblListCity = new JLabel("List City");
		lblListCity.setHorizontalAlignment(SwingConstants.LEFT);
		lblListCity.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblListCity.setBounds(10, 11, 99, 27);
		panel_cityinfo.add(lblListCity);
		
		//table
		CityTable tv = new CityTable();
		tblCity = new JTable(tv);
		tblCity.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int row = tblCity.getSelectedRow();
				int cID = Integer.parseInt(tblCity.getModel().getValueAt(row, 0).toString()) ;

				Dashboard ctDetail =	new Dashboard(client, cID);
				Dashboard.frame.setVisible(true);
				frame.dispose();

			}
		});

		// create scrollpane with param which is the table needed (make it display )
        JScrollPane jsp = new JScrollPane(tblCity);
        jsp.setBounds(20, 49, 593, 278);
		panel_cityinfo.add(jsp);

		JButton btnCreateButton = new JButton("Add new City");
		btnCreateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CityAddNew ctAdd =	new CityAddNew(client);
				ctAdd.frame.setVisible(true);
				frame.dispose();
			}
		});
		btnCreateButton.setBackground(Color.WHITE);
		btnCreateButton.setBounds(471, 11, 142, 23);
		panel_cityinfo.add(btnCreateButton);
		

//		//set data  for table	
		// add city
		JPanel panel_cityinfo_1 = new JPanel();
		panel_cityinfo_1.setLayout(null);
		panel_cityinfo_1.setBounds(10, 64, 644, 364);
		panel.add(panel_cityinfo_1);
		
		JLabel lblListCity_1 = new JLabel("List City");
		lblListCity_1.setHorizontalAlignment(SwingConstants.LEFT);
		lblListCity_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblListCity_1.setBounds(10, 11, 99, 27);
		panel_cityinfo_1.add(lblListCity_1);
		
		JLabel lblNewLabel = new JLabel("City Manager System");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel.setBounds(266, 11, 197, 27);
		panel.add(lblNewLabel);
		
	}
	
	private void getCityData() throws InterruptedException {
		// TODO Auto-generated method stub		
		client.setResponseData(null);
		SendPackage sendP = new SendPackage();
		sendP.setApi(ApiEnum.CITY_FIND_ALL);		
		client.setSendP(sendP);
		JSONObject res = null;

		Object obj = new Object();
		synchronized (obj) {
			while(res == null) {
				res = client.getResponseData();
				System.out.println("waiting:"+res);
				if(res!= null) {
					// if success true - get data bind to table 
					System.out.println(res.toString());
					boolean sMess;
					try {
						sMess = res.getBoolean("success");				
						if(sMess) {
							bindDataToTable(res.getJSONArray("data"));
						}else {						
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				obj.wait(50);
			} 
		} 
		//
		
		client.setResponseData(null);
	}
	
	
	private void bindDataToTable(JSONArray jArray) {
		// TODO Auto-generated method stub	
		DefaultTableModel model = new DefaultTableModel();		
		String[] columnNames = {
				  "ID",  "City Name", "Width", "Height"
			    };
		model.setColumnIdentifiers(columnNames);

		ArrayList arrRows = new ArrayList();  
		for (int i = 0; i < jArray.length(); i++) {
            JSONObject jb;
			try {
				jb = jArray.getJSONObject(i);
			
				Object[] rowData = {
					jb.getInt("ID"),
					jb.getString("name"),
					""+jb.getDouble("width"),
					""+jb.getDouble("height")
				};
	    		
				model.addRow(rowData);
				arrRows.clear();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	

		tblCity.setModel(model); 
		
	}
}

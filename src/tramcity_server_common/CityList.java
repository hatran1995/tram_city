package tramcity_server_common;

import java.sql.*;
import java.util.ArrayList;

import tramcity_server_connection.DataSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class CityList {

	static DataSource dts;
	static ArrayList<City> listCity;
	public CityList() {
		// TODO Auto-generated constructor stub
		try {
			dts = new DataSource();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//get all 
	public static ApiResponse getAll() {
		try {
			String sql = "select * from tblcity";
			ResultSet rs = dts.executeQuery(sql);

			//JSONArray cityAll = new JSONArray();
       	 	 listCity = new ArrayList<City>();
			while(rs.next()){
				int cID = rs.getInt("cId");
				String cName = rs.getString("cName");
				double height = rs.getFloat("cHeight");
				double width =  rs.getFloat("cWidth");
				listCity.add(new City(cID,cName,height,width));


			}
			ApiResponse resturn = new ApiResponse(true, listCity, "Success");
			System.out.println("return data:"+resturn.toString());
			return resturn;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			try {
				return new ApiResponse(false, null, e.getMessage());
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return null;
			}

		}

	}

	//get byID 
	public static ApiResponse getByID(int id) {
		try {

			String sql = "select * from tblcity where cId = " + id;
			ResultSet rs = dts.executeQuery(sql);
    	

			JSONArray cityAll = new JSONArray();
			if(rs.next() == false) {
				return new ApiResponse(false, cityAll, "Not Found");
			}else {
				do {
					JSONObject resItem = new JSONObject();                	

					resItem.put("ID", rs.getInt("cId"));
					resItem.put("Name",  rs.getString("cName") );
					resItem.put("Height", rs.getFloat("cHeight") );
					resItem.put("Width", rs.getFloat("cWidth") );                
					cityAll.put(resItem);                    
				}	while(rs.next());
				return new ApiResponse(true, cityAll, "Success");
			}     	
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			try {
				return new ApiResponse(false, null, e.getMessage());
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return null;
			}

		}
	}
	private static JSONObject checkCityValid (JSONObject record) {
		JSONObject resValid = new JSONObject();

		String txtResText = "";
		Boolean isValid = true;
		String cName = "";
		double cHeight = 0;
		double cWidth = 0;

		//check valid width
		try {	
			cWidth = record.getDouble("width");
		} catch (Exception e) {
			txtResText = "Width value is not valid.";
			isValid = false;
		}

		//check valid height
		try {	
			cHeight = record.getDouble("height");
		} catch (Exception e) {
			txtResText = "Height value is not valid.";
			isValid = false;
		}
		if (!(cHeight > 0) ) {
			txtResText = "Height must be greater than 0";
			isValid = false;
		}else		
			if (!(cWidth > 0) ) {
				txtResText = "Width must be greater than 0";
				isValid = false;
			}
		try {

			resValid.put("isValid", isValid);
			resValid.put("txtResText", txtResText);
			return resValid;
		}catch (Exception e) {
			// TODO: handle exception		
			return resValid;
		}
	}


	//create
	public static ApiResponse create(JSONObject record) {
		try {
			System.out.println(record);

			JSONObject checkCityValid = checkCityValid(record);
			String txtResText = checkCityValid.getString("txtResText");
			Boolean isValid = checkCityValid.getBoolean("isValid");
			if(isValid) {


				Connection conn = dts.getConnection();

				PreparedStatement pstmt = conn.prepareStatement("INSERT INTO tblcity values (null, ?, ?, ? )");

				String Name =  record.getString("name");
				Double Height = record.getDouble("height");
				Double Width = record.getDouble("width");
				//long date_of_birth = Date.valueOf(date).getTime();
				pstmt.setString(1, Name);
				pstmt.setDouble(2, Height);
				pstmt.setDouble(3, Width);

				pstmt.executeUpdate();
				
				dts.returnConnection(conn);
				// add success
				return new ApiResponse(true, null, "Create success");
			}	else {
				return new ApiResponse(false, null, txtResText);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			try {
				return new ApiResponse(false, null, e.getMessage());
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return null;
			}

		}

	}

	//update
	public static ApiResponse update(JSONObject record) throws SQLException {
		Connection conn ;
		try {        	
			conn = dts.getConnection();

			PreparedStatement pstmt = conn.prepareStatement("UPDATE tblcity SET cName = ?, cHeight = ?,cWidth = ? WHERE cId = ?");
			System.out.println(record);
			int ID =  record.getInt("ID");

			String Name =  record.getString("name");
			Double Height = record.getDouble("height");
			Double Width = record.getDouble("width");
			//long date_of_birth = Date.valueOf(date).getTime();
			pstmt.setString(1, Name);
			pstmt.setDouble(2, Height);
			pstmt.setDouble(3, Width);
			pstmt.setInt(7, ID);

			pstmt.executeUpdate();			
			dts.returnConnection(conn);
			// add success
			return new ApiResponse(true, null, "Create success");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			try {
				return new ApiResponse(false, null, e.getMessage());
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return null;
			}

		}

	}
	public static ApiResponse delete() {
		try {
			String sql = "DELETE FROM tblcity WHERE 1";
			dts.executeUpdate(sql);  
			return new ApiResponse (true, null, "Delete success");
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			try {
				return new ApiResponse(false, null, e.getMessage());
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return null;
			}

		}


	}
	//main for test
}
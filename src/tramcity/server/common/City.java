package tramcity.server.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tramcity.server.connection.DataSource;

public class City {
	
	int ID;
	String Name;
	double Height ; 
	double Width;
	private void contructor() {
		// TODO Auto-generated method stub
	}

	public City() {
		this.ID = 0;
		this.Name = "";
		this.Height = 0;
		this.Width = 0;		
	}

	public City(int iD, String name, double height, double width) {		
		this.ID = iD;
		this.Name = name;
		this.Height = height;
		this.Width = width;
	}

	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		this.ID = iD;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		this.Name = name;
	}
	public double getHeight() {
		return Height;
	}
	public void setHeight(int height) {
		this.Height = height;
	}
	public double getWidth() {
		return Width;
	}
	public void setWidth(int width) {
		this.Width = width;
	}
	

	@Override
	public String toString() {
		JSONObject thisObject = new JSONObject(this);
		return thisObject.toString();
	}
	
	//get byID 
	public static ApiResponse getCityByID(int id) throws JSONException {
		try {

			String sql = "select * from tblcity where cId = " + id;
			ResultSet rs = DataSource.executeQuery(sql);
    	
			if(rs.next() == false) {
			//	return null;
				return new ApiResponse(false, null, "Not Found");
			}else {
				JSONObject resItem = new JSONObject();                	

                resItem.put("ID", rs.getInt("cId"));

                resItem.put("name",  rs.getString("cName") );
                resItem.put("height", rs.getFloat("cHeight") );
                resItem.put("width", rs.getFloat("cWidth") );  

				return new ApiResponse(true, resItem, "Not Found");
               // return resItem;
			}     	
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ApiResponse(false, null, e.getMessage());

		}
	}
	

	//create
	public static ApiResponse createNewCity(JSONObject record) {
			try {
				System.out.println(record);

				JSONObject checkCityValid = checkCityValid(record);
				String txtResText = checkCityValid.getString("txtResText");
				Boolean isValid = checkCityValid.getBoolean("isValid");
				if(isValid) {


					Connection conn = DataSource.getConnection();

					PreparedStatement pstmt = conn.prepareStatement("INSERT INTO tblcity values (null, ?, ?, ? )");

					String Name =  record.getString("name");
					Double Height = record.getDouble("height");
					Double Width = record.getDouble("width");
					//long date_of_birth = Date.valueOf(date).getTime();
					pstmt.setString(1, Name);
					pstmt.setDouble(2, Height);
					pstmt.setDouble(3, Width);

					pstmt.executeUpdate();
					
					DataSource.returnConnection(conn);
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
	public static ApiResponse updateCity(JSONObject record) throws SQLException {
			Connection conn ;
			try {        	
				conn = DataSource.getConnection();

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
				pstmt.setInt(4, ID);

				pstmt.executeUpdate();			
				DataSource.returnConnection(conn);
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
	
	//delete
	public static ApiResponse deleteCity() {
		try {
			String sql = "DELETE FROM tblcity WHERE 1";
			DataSource.executeUpdate(sql);  
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
	
	//extend
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
			//check max min width height
			if(cWidth <10 || cWidth >50) {				
				txtResText = "Width range from 10 to 50";
				isValid = false;
			}
			if(cHeight <10 || cHeight >50) {				
				txtResText = "Height range from 10 to 50";
				isValid = false;
			}

			resValid.put("isValid", isValid);
			resValid.put("txtResText", txtResText);
			return resValid;
		}catch (Exception e) {
			// TODO: handle exception		
			return resValid;
		}
	}
	
	//get all 
//	public static ArrayList<City> getAllCity() {
//		try {
//			String sql = "select * from tblcity";
//			ResultSet rs = DataSource.executeQuery(sql);
//
//			//JSONArray cityAll = new JSONArray();
//			ArrayList<City> listCity = new ArrayList<City>();
//			while(rs.next()){
//				int cID = rs.getInt("cId");
//				String cName = rs.getString("cName");
//				double height = rs.getFloat("cHeight");
//				double width =  rs.getFloat("cWidth");
//				listCity.add(new City(cID,cName,height,width));
//			}
//			return listCity;			
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//			return new ArrayList<City>();//ApiResponse(false, null, e.getMessage());
//		}
//
//	}
	
	public static ApiResponse getAllCity() throws JSONException {
		try {
			String sql = "select * from tblcity";
			ResultSet rs = DataSource.executeQuery(sql);

			JSONArray cityAll = new JSONArray();
			//ArrayList<City> listCity = new ArrayList<City>();
			while(rs.next()){
				JSONObject resItem = new JSONObject();                	

                resItem.put("ID", rs.getInt("cId"));

                resItem.put("name",  rs.getString("cName") );
                resItem.put("height", rs.getFloat("cHeight") );
                resItem.put("width", rs.getFloat("cWidth") );                
                cityAll.put(resItem);
			}
			return new ApiResponse(true,cityAll,"Success");			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ApiResponse(false, null, e.getMessage());
		}

	}



	
}

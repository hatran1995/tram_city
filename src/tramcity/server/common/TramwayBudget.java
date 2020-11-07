package tramcity.server.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tramcity.server.connection.DataSource;

public class TramwayBudget {
	int ID;
	int ValueStation;
	int Value;
	int NumberMaxStation;
	int Radius;
	int CityWidth;
	int CityHeight;
	

	public TramwayBudget() {
		this.ID = 0;
		this.ValueStation = 0;
		this.Value = 0;
		this.NumberMaxStation = 0;
		this.Radius = 0;
		this.CityWidth = 0;
		this.CityHeight = 0;	
	}

	public TramwayBudget(int iD, int valueStation,int value, int numberMaxStation, int radius, int width, int height) {		
		this.ID = iD;
		this.ValueStation = valueStation;
		this.Value = value;
		this.NumberMaxStation = numberMaxStation;
		this.Radius = radius;
		this.CityWidth = width;
		this.CityHeight = height;
	}
	private static JSONObject checkTramWayValid (JSONObject record) {
		JSONObject resValid = new JSONObject();
		
	        String txtResText = "";
	        Boolean isValid = true;
			int iBudget = 0;
			int iCostOne = 0;
			int iRadius = 0;
	        //check 
	       // isValid = false;
	        
	      //check valid txtBudget
			try {	
				iBudget = record.getInt("Value");
				if (!(iBudget > 0) ) {
					txtResText = "Budget value must be greater than 0";
					isValid = false;
				}
			} catch (Exception e) {
				txtResText = "Budget value is not valid, please enter the data in numeric integer format";
				isValid = false;
				
			}
			
			//check valid txtCostOne
			try {	
				iCostOne = record.getInt("ValueStation");
				
				if (isValid && !(iCostOne > 0) ) {
					txtResText = "The cost of a station must be greater than 0";
					isValid = false;
				}
			} catch (Exception e) {
				txtResText = "Budget value for one station is not valid, please enter the data in numeric integer format";
				isValid = false;
			}
			//check valid txtRadius
			try {	
				iRadius = record.getInt("Radius");
				if (isValid && !(iRadius > 0) ) {
					txtResText = "The distance between station must be greater than 0";
					isValid = false;
				}
			} catch (Exception e) {
				txtResText = "Min distance between two point is not valid, please enter the data in numeric integer format";
				isValid = false;
			}
			if(isValid) {
				
				//check valid txtCostOne
				if(iBudget<iCostOne) {
					txtResText = "The cost of a station can't be higher than the city budget";
					isValid = false;
				}else
				if(!(iBudget/iCostOne >= 2)) {
					txtResText = "You need at least 2 station to create a network";
					isValid = false;
				}else
					if (!(iRadius > 0)) {
					txtResText = "Min distance between two point must be greater than 0";
					isValid = false;
				}

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
	public static ApiResponse createAndUpdate(JSONObject record) {
		try {		
			System.out.println(record);
            JSONObject checkTramWayValid = checkTramWayValid(record);
            String txtResText = checkTramWayValid.getString("txtResText");
            Boolean isValid = checkTramWayValid.getBoolean("isValid");
            if(isValid) {
				String sql = "select * from tblbudgetstation where bIdCity = " + record.getInt("ID");

				ResultSet rs = DataSource.executeQuery(sql);        	
	
	    		PreparedStatement pstmt  ;
                int ID =  record.getInt("ID");
                int Budget = record.getInt("Value");
                int ValStation = record.getInt("ValueStation");
                int NumberMaxStation = (int) Budget/ValStation;
                int RadiusMin = record.getInt("Radius");
                //long date_of_birth = Date.valueOf(date).getTime();
                Connection conn = DataSource.getConnection();
	    		if(rs.next() == false) {
	    			//create
	    			pstmt = conn.prepareStatement("INSERT INTO tblbudgetstation values (?, ?, ?, ?,? )");
	
	
	                pstmt.setDouble(1, ID);
	                pstmt.setInt(2, Budget);
	                pstmt.setInt(3, ValStation);
	                pstmt.setInt(4, NumberMaxStation);
	                pstmt.setInt(5, RadiusMin);
	    			
	    		}else {
	    			//update
	    			pstmt = conn.prepareStatement("UPDATE tblbudgetstation SET bValue = ?, bValueStation = ?,bNumberMaxStation = ?,bRadius = ?  WHERE bIdCity = ?");
	
	                pstmt.setInt(1, Budget);
	                pstmt.setInt(2, ValStation);
	                pstmt.setInt(3, NumberMaxStation);
	                pstmt.setInt(4, RadiusMin);
	                pstmt.setInt(5, ID);
	    		} 
				
	            
	            pstmt.executeUpdate();
	            DataSource.returnConnection(conn);
	            // random Station
	            Station.renderStation(ID);
	        	// add success
	        	return new ApiResponse(true, null, "Create success");
	        	
            }
            else {
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
	
	//get byID 
	public static ApiResponse getTramWayByCityID(int id) {
		try {
			
        	String sql = "select * from tblcity JOIN tblbudgetstation on tblcity.cId = tblbudgetstation.bIdCity where tblcity.cId = " + id;
        //	System.out.println(sql);

        	ResultSet rs = DataSource.executeQuery(sql);        	

    		JSONArray tramwayAll = new JSONArray();
    		if(rs.next() == false) {
        		return new ApiResponse(false, tramwayAll, "Not Found");
    		}else {
    			 do {
                	JSONObject resItem = new JSONObject();                	

                    resItem.put("ID", rs.getInt("bIdCity"));
                    resItem.put("ValueStation",  rs.getString("bValueStation") );
                    resItem.put("Value", rs.getFloat("bValue") );
                    resItem.put("NumberMaxStation", rs.getFloat("bNumberMaxStation") );  
                    resItem.put("Radius", rs.getFloat("bRadius") );             
                    resItem.put("Width", (int) rs.getDouble("cWidth") );     
                    resItem.put("Height",  (int) rs.getDouble("cHeight") );        
                    tramwayAll.put(resItem);                    
                }	while(rs.next());
    			 
    			 JSONObject data = new JSONObject();
    			 data.put("tramways", tramwayAll);
    			// get point
    			 data.put("points", Station.getStationByCityID(id));

    			 data.put("paths", Line.getPathByCityID(id));
    			 
        		return new ApiResponse(true, data, "Success");
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

	public static void deleteTramWayBudgetByCityID(int id)  {
		String sql = "DELETE FROM tblbudgetstation where tblbudgetstation.bIdCity  = " + id;
		try {
			DataSource.executeUpdate(sql);
		} catch (SQLException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
}

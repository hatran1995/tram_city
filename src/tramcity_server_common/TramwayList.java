package tramcity_server_common;

import java.awt.Point;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tramcity_server_connection.DataSource;

public class TramwayList {

	static DataSource dts;
	
	public TramwayList() {
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

				ResultSet rs = dts.executeQuery(sql);        	
	
	    		PreparedStatement pstmt  ;
                int ID =  record.getInt("ID");
                int Budget = record.getInt("Value");
                int ValStation = record.getInt("ValueStation");
                int NumberMaxStation = (int) Budget/ValStation;
                int RadiusMin = record.getInt("Radius");
                //long date_of_birth = Date.valueOf(date).getTime();
                Connection conn = dts.getConnection();
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
	            dts.returnConnection(conn);
	            // random Station
	            randomStation(ID);
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

        	ResultSet rs = dts.executeQuery(sql);        	

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
    			 data.put("points", getStationByCityID(id));

    			 data.put("paths", getPathByCityID(id));
    			 
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
	
	//get byID 
	private static JSONArray getStationByCityID(int id) {
		try {
			
        	String sql = "select * from tblstation where sIdCity = " + id;
        //	System.out.println(sql);
        	ResultSet rs = dts.executeQuery(sql);        	
    		JSONArray stationAll = new JSONArray();
    		if(rs.next() == false) {
        		return stationAll;
    		}else {
    			 do {
                	JSONObject resItem = new JSONObject();                	
                    resItem.put("ID", rs.getInt("sId"));
                    resItem.put("Name",  rs.getString("sName") );
                    resItem.put("IdCity", rs.getInt("sIdCity") );
                    resItem.put("Lat", rs.getInt("sLat") );
                    resItem.put("Long", rs.getInt("sLong") );
                    resItem.put("IdLine", rs.getInt("sIdLine") );
                    resItem.put("Position", rs.getInt("sPosition") );          
                    stationAll.put(resItem);                    
                }	while(rs.next());
        		return stationAll;
			}     	
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	//create station
	public static ApiResponse createAndUpdateStation(Point[] record, int cityID) {
		try {
			//get cityID
			//delete all by city ID
            int ID =  cityID;
			String sql = "DELETE FROM tblstation where sIdCity = " + ID;
			//System.out.println(sql);			
        	dts.executeUpdate(sql);       
        	// add new point
        	Connection conn = dts.getConnection();
        	String query = "INSERT INTO tblstation values ";
        	for (int i = 0; i < record.length; i++) {
        		Point pointAdd = record[i];
                Double pointX = pointAdd.getX();
                Double pointY = pointAdd.getY();
                
                if(i>0) {
            		query = query + " , ";
                }
        		String queryItem = " (null,'point' , "+ID+" , "+pointX+" ,"+pointY+"  ,0 ,0 ) ";
        		query = query + queryItem;
			}
			System.out.println(query);
        	PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.executeUpdate();
        	            
        	// add success
        	return new ApiResponse(true, null, "Add success");
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
	
	
	public static ApiResponse randomStation(int cityID) {
		//get width height
		try {
			String sql = "select * from tblcity JOIN tblbudgetstation on tblcity.cId = tblbudgetstation.bIdCity where tblcity.cId = " + cityID;
        	//System.out.println(sql);
        	ResultSet rs = dts.executeQuery(sql);        	
        	if(rs.next() == false) {
            	return new ApiResponse(true, null, "Add success");
    		}else {
            	JSONObject resItem = new JSONObject();                	
            	int Height = (int)  rs.getDouble("cHeight") ;
            	int Width = (int) rs.getDouble("cWidth") ;
            	int Budget = rs.getInt("bValue") ;
            	int ValueStation = rs.getInt("bValueStation") ;
            	int maxPoint = ((int) Budget/ValueStation);
            	int r = rs.getInt("bRadius") ;
            	//random
            	RandomPoint newRandom = new RandomPoint(Width, Height, maxPoint, r, 0, 2*Math.PI);
            	JSONObject resRanDomPoint =	newRandom.getListPoint(); 
            //	Point[] points = newRandom.getListPoint(); 
            	JSONArray resListPoint  = resRanDomPoint.getJSONArray("ListPoint");
				Point[] lP = new Point[resListPoint.length()];
				for (int i = 0; i < lP.length; i++) {
					int x = resListPoint.getJSONObject(i).getInt("x");
					int y = resListPoint.getJSONObject(i).getInt("y");
					lP[i] = new Point(x, y);
				}
            	if(lP.length > 0) {
            		// update tblstation
            		createAndUpdateStation(lP,cityID);            		
            	}
            	
            	//add path            	
            	JSONArray resListPath  = resRanDomPoint.getJSONArray("ListPath");
            	createAndUpdatePath(resListPath,cityID);
                //return list Station
            	return new ApiResponse(true, getTramWayByCityID(cityID), "Random success");
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
	public static JSONArray getPathByCityID(int id) {
		try {
			
        	String sql = "select * from tblline where lIdCity = " + id;
        //	System.out.println(sql);
        	ResultSet rs = dts.executeQuery(sql);        	
    		JSONArray stationAll = new JSONArray();
    		if(rs.next() == false) {
        		return stationAll;
    		}else {
    			 do {
                	JSONObject resItem = new JSONObject();                	
                    resItem.put("ID", rs.getInt("lId"));
                    resItem.put("Name",  rs.getString("lName") );
                    resItem.put("FromX", rs.getInt("lFromX") );
                    resItem.put("FromY", rs.getInt("lFromY") );
                    resItem.put("ToX", rs.getInt("lToX") );
                    resItem.put("ToY", rs.getInt("lToY") );        
                    stationAll.put(resItem);                    
                }	while(rs.next());
        		return stationAll;
			}     	
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	//create path
	public static ApiResponse createAndUpdatePath(JSONArray listPath, int cityID) {
		try {
			//get cityID
			//delete all by city ID
            int ID =  cityID;
			String sql = "DELETE FROM tblline where lIdCity = " + ID;
			//System.out.println(sql);
			
        	 dts.executeUpdate(sql);       
        	// add new point
        	 Connection conn = dts.getConnection();
        	String query = "INSERT INTO tblline values ";
        	for (int i = 0; i < listPath.length(); i++) {
        		JSONObject pathAdd = listPath.getJSONObject(i);
                int fromX = pathAdd.getInt("fromX");
                int fromY = pathAdd.getInt("fromY");
                int toX = pathAdd.getInt("toX");
                int toY = pathAdd.getInt("toY");
                
                if(i>0) {
            		query = query + " , ";
                }
        		String queryItem = " (null,"+ID+" ,'path' ,  "+fromX+" ,"+fromY+" ,"+toX+" ,"+toY+"   ) ";
        		query = query + queryItem;
			}
			System.out.println(query);
        	PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.executeUpdate();
            dts.returnConnection(conn);
        	// add success
        	return new ApiResponse(true, null, "Add path success");
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
	
}
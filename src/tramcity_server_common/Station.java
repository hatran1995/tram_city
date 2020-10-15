package tramcity_server_common;

import java.awt.Point;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tramcity_server_connection.DataSource;

public class Station {
	int ID;
	String Name;
	int IdCity;
	int Lat;
	int Long;
	int IdLine;
	int Position;
	

	public Station() {
		this.ID = 0;
		this.Name = "";
		this.IdCity = 0;
		this.Lat = 0;
		this.Long = 0;
		this.IdLine = 0;
		this.Position = 0;
	}

	public 	Station(int iD, String name,int idCity, int latitude, int longtitude, int idLine, int position) {		
		this.ID = iD;
		this.Name = name;
		this.IdCity = idCity;
		this.Lat = latitude;
		this.Long = longtitude;
		this.IdLine = idLine;;
		this.Position = position;
	}
	

	//get byID 
	public static JSONArray getStationByCityID(int id) {
		try {
			
        	String sql = "select * from tblstation where sIdCity = " + id;
        //	System.out.println(sql);
        	ResultSet rs = DataSource.executeQuery(sql);        	
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
			DataSource.executeUpdate(sql);       
        	// add new point
        	Connection conn = DataSource.getConnection();
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
        	ResultSet rs = DataSource.executeQuery(sql);        	
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
            	Line.createAndUpdatePath(resListPath,cityID);
                //return list Station
            	return new ApiResponse(true, TramwayBudget.getTramWayByCityID(cityID), "Random success");
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

}

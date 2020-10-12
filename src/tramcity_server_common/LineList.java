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

public class LineList {

	static DataSource dts;
	
	public LineList() {
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

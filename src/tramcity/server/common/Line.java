package tramcity.server.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tramcity.server.connection.DataSource;

public class Line {
	int ID;
	String Name;
	int FromX;
	int FromY;
	int ToX;
	int ToY;
	int Line;


	public Line() {
		this.ID = 0;
		this.Name = "";
		this.FromX = 0;
		this.FromY = 0;
		this.ToX = 0;
		this.ToY = 0;
		this.Line = 0;
	}

	public Line(int iD, String name,int fromX, int fromY, int toX, int toY, int line) {		
		this.ID = iD;
		this.Name = name;
		this.FromX = fromX;
		this.FromY = fromY;
		this.ToX = toX;
		this.ToY = toY;
		this.Line = line;
	}
	public static JSONArray getPathByCityID(int id) {
		try {

			//DataSource dts = new DataSource();
			String sql = "select * from tblline where lIdCity = " + id;
			//	System.out.println(sql);
			ResultSet rs = DataSource.executeQuery(sql);        	
			JSONArray stationAll = new JSONArray();
			if(rs.next() == false) {
				return stationAll;
			}else {
				do {
					JSONObject resItem = new JSONObject();                	
					//                    resItem.put("ID", rs.getInt("lId"));
					//                    resItem.put("Name",  rs.getString("lName") );
					resItem.put("FromX", rs.getInt("lFromX") );
					resItem.put("FromY", rs.getInt("lFromY") );
					resItem.put("ToX", rs.getInt("lToX") );
					resItem.put("ToY", rs.getInt("lToY") );     
					resItem.put("Line", rs.getInt("line") );        
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

			//DataSource dts = new DataSource();
			DataSource.executeUpdate(sql);       
			// add new point
			Connection conn = DataSource.getConnection();
			String query = "INSERT INTO tblline values ";
			for (int i = 0; i < listPath.length(); i++) {
				JSONObject pathAdd = listPath.getJSONObject(i);
				int fromX = pathAdd.getInt("fromX");
				int fromY = pathAdd.getInt("fromY");
				int toX = pathAdd.getInt("toX");
				int toY = pathAdd.getInt("toY");
				int line = pathAdd.getInt("line");

				if(i>0) {
					query = query + " , ";
				}
				String queryItem = " (null,"+ID+" ,'path' ,  "+fromX+" ,"+fromY+" ,"+toX+" ,"+toY+" ,"+line+" ) ";
				query = query + queryItem;
			}
			System.out.println(query);
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.executeUpdate();
			DataSource.returnConnection(conn);
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
	public static int deletePathByCity( int cityID) {
		try {
			String sql = "DELETE FROM tblline where lIdCity = " + cityID;			
			DataSource.executeUpdate(sql);			 
			return 1;
		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		} 
	}
}


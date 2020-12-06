package tramcity.finaltest;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tramcity.server.common.DividePoint;

public class TestAlgoDimension {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Start Test Check Dimension of City");
		try (FileReader reader = new FileReader("C:\\Users\\H\\eclipse-workspace\\tramcity_project\\src\\tramcity\\finaltest\\TestAlgoDimension.json"))
		{ 
			String fileContent= "";
			int i;    
			while((i=reader.read())!=-1)   {	 
				fileContent += (char)i;
			} 
			reader.close();    			 
			JSONArray list = new JSONArray(fileContent);
			System.out.println("TEST Render TRAMWAY:");
			for (int j = 0; j < list.length(); j++) {
				//get json testcase
				JSONObject testItem = list.getJSONObject(j);
				System.out.println("TEST CASE "+j+": "+ testItem.getString("testname") );
				// call func with param input
				JSONObject rs = testItem.getJSONObject("input");
            	int Height = (int)  rs.getDouble("cHeight") ;
            	int Width = (int) rs.getDouble("cWidth") ;
            	
            	int maxPoint = (int) rs.getInt("maxPoint"); ;
            	
            	int r = (int) ((Width + Height)/(2 * Math.sqrt(maxPoint))); //(int) rs.getInt("bRadius");
//            	//random
            	DividePoint newRandom = new DividePoint();
            	JSONObject resRanDomPoint =	DividePoint.getListPoint(Width, Height, maxPoint, r, 0, 2*Math.PI); 
            	JSONArray resListPoint  = resRanDomPoint.getJSONArray("ListPoint");
            	JSONArray resListPath  = resRanDomPoint.getJSONArray("ListPath");
            	
				//compare resItem.body.success with testItem.output 
				System.out.println("Desired output :" + testItem.getInt("output"));
				System.out.println("Received output :" + resListPoint.length());
				if(  resListPoint.length() == testItem.getInt("output")) {
					//test ok
					System.out.println("Test success with input is: "+testItem.getJSONObject("input").toString() ); 
				}else {
					//test fail
					System.err.println("Test false with: "+testItem.getJSONObject("input").toString());

				}
			};


		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}




}
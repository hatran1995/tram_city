package tramcity.finaltest;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tramcity.server.common.ApiResponse;
import tramcity.server.common.TramwayBudget;
import tramcity.server.connection.DataSource;

public class TestCostStation {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		// test
		System.out.println("Start test valid cost station");

		try (FileReader reader = new FileReader("C:\\Users\\H\\eclipse-workspace\\tramcity_project\\src\\tramcity\\finaltest\\TestCostStation.json"))
		{ 
			String fileContent= "";
			int i;    
			while((i=reader.read())!=-1)   {	 
				fileContent += (char)i;
			} 
			reader.close(); 
			DataSource dst = new DataSource();
			JSONArray list = new JSONArray(fileContent);
			for (int j = 0; j < list.length(); j++) {
				//get json testcase
				JSONObject testItem = list.getJSONObject(j);
				System.out.println("TEST CASE "+j+": "+ testItem.getString("testname") );
				// call func with param input
				TramwayBudget tramway = new TramwayBudget();
				ApiResponse resItem =  tramway.createAndUpdate(testItem.getJSONObject("input"));

				//compare resItem.body.success with testItem.output 
				if( resItem.getBody().getBoolean("success") == testItem.getBoolean("output")) {
					//test ok
					System.out.println("----------------------------");
					System.out.println("Desired output :" + testItem.getBoolean("output"));
					System.out.println("Received output :" + resItem.getBody().getBoolean("success"));
					System.out.println("----------------------------");
					System.out.println("message : " + resItem.getBody().getString("msg"));
					System.out.println("Test success with input is: "+testItem.getJSONObject("input").toString() ); 
					System.out.println("****************************");
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

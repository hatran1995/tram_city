package tramcity_server_test;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;

import tramcity_server_common.CityList;
import tramcity_server_common.ApiResponse;

public class TestGetAllCity {
	public static void main(String[] args) {
		// test getAllCity()
		System.out.println("Start Test Get List City");
		try {
			CityList city = new CityList();
			ApiResponse resItem =  city.getAll();

			//compare resItem.body.success with testItem.output 
			if( resItem.getBody().getBoolean("success") == true) {
				JSONArray resList = resItem.getBody().getJSONArray("data");
				//test ok
				if(resList.length() > 0) {

					//	System.out.println("Desired output :" + testItem.getBoolean("output"));
					//	System.out.println("Received output :" + resItem.getBody().getBoolean("success"));
					System.out.println("There are " + resList.length() + " city in the list");
					System.out.println("Test success"); 
				}else {
					//test fail
					System.err.println("Test false");

				}
			}
			//
			//resItem.getBody().getBoolean("success")
			;



		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

	}




}
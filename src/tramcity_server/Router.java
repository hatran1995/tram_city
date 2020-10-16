package tramcity_server;

import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import tramcity_server_common.Station;
import tramcity_server_common.City;
import tramcity_server_common.TramwayBudget;
import tramcity_server_common.ApiResponse;
public class Router {

	public static String createCity(JSONObject city) {
		return City.createNewCity(city).toString();
	}

	public static String updateCity(JSONObject city) throws SQLException {
		return City.updateCity(city).toString();
	}

	public static String deleteCity() {
		return City.deleteCity().toString();
	}

	public static String findAllCity() {

		try {
			ApiResponse cityTemp = City.getAllCity();
		//	String resturn = (new ApiResponse(cityTemp!=null?true:false, cityTemp, "Success")).toString();
			return cityTemp.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String findOneCityByID(int cityID) throws JSONException {
			City cityTemp = City.getCityByID(cityID);
			String resturn = (new ApiResponse(cityTemp!=null?true:false, new JSONObject(cityTemp), "Success")).toString();
			return resturn;		
	}

	// Tramway

	public static String findOneTramwayByCityID(int cityID) {

		try {
			return TramwayBudget.getTramWayByCityID(cityID).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String createUpdateTramway(JSONObject inp) {
		return TramwayBudget.createAndUpdate(inp).toString();
	}

	public static String randomStation(JSONObject inp) {
		int cityID;
		try {
			cityID = (int) inp.getInt("ID");
			return Station.randomStation(cityID).toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				return new ApiResponse(false, null, e.getMessage()).toString();
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return null;
			}
		}
	}


	// input {api:"CITYSave",body:{}}
	public static String router(JSONObject input) throws SQLException {
		String api;
		JSONObject body;
		try {
			api = input.get("api").toString();
			// Converting the Object to JSONString

			switch (api) {
			// CITY
			case "CITY_CREATE":
				System.out.println(input);
				return createCity((JSONObject) input.get("body"));

			case "CITY_UPDATE":
				return updateCity((JSONObject) input.get("body"));

			case "CITY_GET_ONE":
				body = input.getJSONObject("body");
				System.out.println(body.getString("ID"));
				return findOneCityByID((int) body.getInt("ID"));

			case "CITY_FIND_ALL":
				return findAllCity();

			case "CITY_DELETE":
				return deleteCity();

			// tramway
			case "TRAMWAY_GET_ONE":
				body = input.getJSONObject("body");
				return findOneTramwayByCityID((int) body.getInt("ID"));

			case "TRAMWAY_UPDATE":
				return createUpdateTramway((JSONObject) input.get("body"));
			// random
			case "TRAMWAY_MAP_RANDOM":
				return randomStation((JSONObject) input.get("body"));

			default:
				return new ApiResponse(false, null, "Not found API").toString();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				return new ApiResponse(false, null, e.getMessage()).toString();
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();

				return null;
			}
		}
	}

//	public static String route() {
//		String s = String.valueOf(CountAirSensor());
//		return s;
//	}
}
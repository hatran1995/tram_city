package tramcity_server;

import java.io.IOException;
import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import tramcity_server_common.CityList;
import tramcity_server_common.TramwayList;
import tramcity_server_common.ApiResponse;
public class Router {

	static CityList cityP = new CityList();
	static TramwayList tramway = new TramwayList();

	public static String createCity(JSONObject city) {
		return cityP.create(city).toString();
	}

	public static String updateCity(JSONObject city) throws SQLException {
		return cityP.update(city).toString();
	}

	public static String deleteCity() {
		return cityP.delete().toString();
	}

	public static String findAllCity() {

		try {
			return cityP.getAll().toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String findOneCityByID(int cityID) {

		try {
			return cityP.getByID(cityID).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Tramway

	public static String findOneTramwayByCityID(int cityID) {

		try {
			return tramway.getTramWayByCityID(cityID).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String createUpdateTramway(JSONObject inp) {
		return tramway.createAndUpdate(inp).toString();
	}

	public static String randomStation(JSONObject inp) {
		int cityID;
		try {
			cityID = (int) inp.getInt("ID");
			return tramway.randomStation(cityID).toString();
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
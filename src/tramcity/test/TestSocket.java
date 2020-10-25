package tramcity.test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tramcity.client.Client;
import tramcity.client.common.ApiEnum;
import tramcity.client.common.SendPackage;
public class TestSocket {
	
	static Client client;
	public static void main(String args[]) throws InterruptedException {

		client = new Client("127.0.0.1", 1995);
//		client = new Client("172.249.31.169", 1995);
		client.start();
		getAllCityData();
	}
	
	

	public static void getAllCityData() throws InterruptedException {
		// TODO Auto-generated method stub
		client.setResponseData(null);
		SendPackage sendP = new SendPackage();
		sendP.setApi(ApiEnum.CITY_FIND_ALL);		
		client.setSendP(sendP);
		JSONObject res = null;
		Object obj = new Object();
		  synchronized (obj) {
			while(res == null) {
				res = client.getResponseData();
				if(res!= null) {
					// if success true - get data bind to table 
					System.out.println(res.toString());
					boolean sMess;
					try {
						sMess = res.getBoolean("success");				
						if(sMess) {
							JSONArray jArray = res.getJSONArray("data");
							if(jArray.length()>0) {
								int cID = jArray.getJSONObject(jArray.length()-1).getInt("ID");
							}else{
								System.out.println("Add new");
							};
						}else {						
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				obj.wait(50);
			} 
		  }
		//
		
		client.setResponseData(null);
	}
	
}

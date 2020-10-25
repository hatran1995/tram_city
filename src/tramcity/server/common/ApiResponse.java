package tramcity.server.common;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("unused")

public class ApiResponse {
	JSONObject body ;
	public ApiResponse() throws JSONException {
		// TODO Auto-generated constructor stub
		body.put("success", false);
		body.put("msg", "");
	}
	public ApiResponse(Boolean success,Object data, String msg) throws JSONException {
		// TODO Auto-generated constructor stub
		JSONObject item = new JSONObject();
		item.put("success", success);
		item.put("data", data);
		item.put("msg", msg);
		setBody(item);
	}
	
	public JSONObject getBody() {
		return body;
	}
	public void setBody(JSONObject body) {
		this.body = body;
	}
	
	@Override
	public String toString() {
		return getBody().toString();
	}
}

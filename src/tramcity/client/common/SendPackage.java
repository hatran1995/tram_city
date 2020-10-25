package tramcity.client.common;
import org.json.JSONException;
import org.json.JSONObject;

public class SendPackage {
	private JSONObject body;	
	private ApiEnum api;

	public SendPackage() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SendPackage(ApiEnum api) {
		super();
		this.api = api;
	}

	public SendPackage(JSONObject body, ApiEnum api) {
		super();
		this.body = body;
		this.api = api;
	}

	public JSONObject getBody() {
		return body;
	}

	public void setBody(JSONObject bd) {
		body = bd;
	}

	public ApiEnum getApi() {
		return api;
	}

	public void setApi(ApiEnum api) {
		this.api = api;	
	}

	@Override
	public String toString() {
		JSONObject send = new JSONObject();
		try {
			send.put("api", this.api);
			send.put("body", this.body);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return send.toString() ;
	}

}

package impex.editors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class ImpexHttpClient {

	private static final String USER_AGENT = "Mozilla/5.0";
	private final static Pattern pattern = Pattern
			.compile("JSESSIONID=[a-zA-Z0-9_-]*");
	private String jsessionId;

	public static void main(String[] args) throws Exception {
		ImpexHttpClient impexHttpClient = new ImpexHttpClient();
		final long startTime = System.currentTimeMillis();
		
		Map<String,JsonArray> allatypes=new HashMap<String,JsonArray>();
		for (JsonValue type : impexHttpClient.getAllTypes()) {
			allatypes.put(type.asString(), impexHttpClient.getTypeandAttribute(type.asString()).get("attributes").asArray());
		}
		for (String type : allatypes.keySet()) {
			System.out.println("type :"+type);
			 for (JsonValue string : allatypes.get(type)) {
				System.out.println("---- " +string.asString());
			}
		}
		final long stopTime = System.currentTimeMillis();
		final long elapsedTime = stopTime - startTime;
		System.out.println("Success elapsed time in seconde " + TimeUnit.MILLISECONDS.toSeconds(elapsedTime));
	}

	public JsonObject getTypeandAttribute(String type) throws Exception {
		List<BasicNameValuePair> params = Arrays
				.asList(new BasicNameValuePair[] {
						new BasicNameValuePair("type", type)});
		
		HttpResponse response = makeHttpPostRequest("http://localhost:9001/hac/console/impex/typeAndAttributes", getJsessionId(), params);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));
		JsonObject impexJsonType = JsonObject.readFrom(rd);
		return impexJsonType;
	}
	
	public JsonArray getAllTypes() throws Exception {
		HttpResponse response = makeHttpPostRequest("http://localhost:9001/hac/console/impex/allTypes", getJsessionId(), Collections.<BasicNameValuePair> emptyList());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));
		JsonObject impexJsonType = JsonObject.readFrom(rd);
		boolean isExist = impexJsonType.get("exists").asBoolean();
		JsonArray types=null;
		if (isExist) {
			 types = impexJsonType.get("types").asArray();
		}
		return types;
	}
	

	private String sendLoginPost() {
		List<BasicNameValuePair> params = Arrays
				.asList(new BasicNameValuePair[] {
						new BasicNameValuePair("j_username", "admin"),
						new BasicNameValuePair("j_password", "nimda") });
		String jssessionId = null;
		HttpResponse response = makeHttpPostRequest(
				"http://localhost:9001/hac/j_spring_security_check", null,
				params);
		for (Header header : response.getAllHeaders()) {
			if ("Set-Cookie".equals(header.getName())) {
				Matcher m = pattern.matcher(header.getValue());
				if (m.find()) {
					jssessionId = m.group(0);
				}
			}
		}

		return jssessionId;
	}

	public String getJsessionId() {
		if (jsessionId != null) {
			return jsessionId;
		} else {
			jsessionId = sendLoginPost();
			return jsessionId;
		}
	}

	private HttpResponse makeHttpPostRequest(String actionUrl,
			String connectionToken, List<BasicNameValuePair> params) {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(actionUrl);
		// add header
		post.setHeader("User-Agent", USER_AGENT);
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		for (BasicNameValuePair nameValuePair : params) {
			urlParameters.add(nameValuePair);
		}
		if (connectionToken!=null) {
			post.setHeader("Cookie", connectionToken);
		}
		HttpEntity entity;
		HttpResponse response = null;
		try {
			entity = new UrlEncodedFormEntity(urlParameters, "utf-8");
			post.setEntity(entity);
			response = client.execute(post);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;

	}
}

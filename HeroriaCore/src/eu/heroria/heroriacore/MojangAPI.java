package eu.heroria.heroriacore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

public class MojangAPI {
	public static String getName(String uuid) {
		String url = "https://api.mojang.com/user/profiles/" + uuid.replace("-", "") + "/names";
		try {
			String nameJson = httpGet(url);
			JSONArray nameValue = (JSONArray) JSONValue.parseWithException(nameJson);
            String playerSlot = nameValue.get(nameValue.size()-1).toString();
            JSONObject nameObject = (JSONObject) JSONValue.parseWithException(playerSlot);
            return nameObject.get("name").toString();
			
		} catch (JsonSyntaxException | IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getUUID(String playerName) {
        try {
            String url = "https://api.mojang.com/users/profiles/minecraft/" + playerName;
            String UUIDJson = httpGet(url);
            JsonObject UUIDObject = (JsonObject) JSONValue.parseWithException(UUIDJson);
            String uuid = UUIDObject.get("id").toString();
            return uuid;
        } catch (Exception e) {
            return "";
        }
    }
	
	public static String httpGet(String requestURL) throws IOException {
		String type = "GET";
		StringBuffer response = new StringBuffer();
		int responseStat = 0;
		/*
		for (String string : this.parameter) {
			if(parameter == null) parameter = string;
			else parameter = parameter + "&" + string;
		}
		*/
		URL url = new URL(requestURL);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod(type);
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		responseStat = con.getResponseCode();
		if (responseStat == HttpURLConnection.HTTP_OK) {
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			return response.toString();
		}
		return null;
	}
}

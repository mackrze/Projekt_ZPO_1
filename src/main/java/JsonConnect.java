import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;

public class JsonConnect {

    public static List<Measurement> getResponse(String url) {
        StringBuffer response = new StringBuffer();

        try {
            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (MalformedURLException ex) {
            System.out.println("Bad url");
        } catch (IOException e) {
            System.out.println("Connection failed");
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Map m1 = gson.fromJson(response.toString(), Map.class); // mapuje caly json


        JsonArray jsonArray = gson.toJsonTree(m1.get("results")).getAsJsonArray(); // oddzielam odpowiedzi z result na pojedyncze Stringi do klasy JsonArray

        if(jsonArray.size() == 0) // jesli jest 0 to blad bo nic nie zwrocil
            throw new InvalidParameterException("Bad url - empty result");

        Type typeToken = new TypeToken<List<Measurement>>(){}.getType(); // lista typów jakimi sa poszczegolne pola w kalsie Measuremet

        List<Measurement> measurements = gson.fromJson(jsonArray,typeToken);

        return measurements;
    }

}

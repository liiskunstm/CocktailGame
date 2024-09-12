package com.ridango.game;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

@Service
public class DrinkApiService {
    public Drink getRandomDrinkFromApi() {
        try  {
            URL url = new URL("https://www.thecocktaildb.com/api/json/v1/1/random.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String content = br.lines().collect(Collectors.joining());
                Drink[] drinks = new Gson().fromJson(JsonParser.parseString(content).getAsJsonObject().get("drinks"),
                        Drink[].class);
                conn.disconnect();

                if (drinks != null && drinks.length != 0) {
                    return drinks[0];
                }
                else {
                    System.out.println("Error: There are no drinks in the response.");
                    return null;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

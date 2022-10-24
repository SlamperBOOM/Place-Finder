package placeFinderLib;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;
import org.json.*;
import placeFinderLib.infos.WeatherInfo;

public class WeatherFinder implements Supplier<WeatherInfo> {
    private final Double lat;
    private final Double lng;

    public WeatherFinder(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    @Override
    public WeatherInfo get() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "http://api.openweathermap.org/data/2.5/" +
                                "weather?lat="+lat+"&lon="+lng+"&appid=26d20616cee0e19dd8789b57ef4e9f08&units=metric&lang=ru"))
                .build();
        CompletableFuture<HttpResponse<String>> future =
                client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response;
        try {
            response = future.get();
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
        WeatherInfo weatherInfo = new WeatherInfo();
        JSONObject weather = new JSONObject(response.body());
        weatherInfo.setDescription(weather.getJSONArray("weather").getJSONObject(0).getString("description"));

        JSONObject temp = weather.getJSONObject("main");
        weatherInfo.setTemp(temp.getDouble("temp"));
        weatherInfo.setMinTemp(temp.getDouble("temp_min"));
        weatherInfo.setMaxTemp(temp.getDouble("temp_max"));
        weatherInfo.setFeelTemp(temp.getDouble("feels_like"));
        weatherInfo.setPressure(temp.getInt("pressure"));
        weatherInfo.setHumidity(temp.getInt("humidity"));

        weatherInfo.setVisibility(weather.getInt("visibility"));

        JSONObject wind = weather.getJSONObject("wind");
        weatherInfo.setWindSpeed(wind.getDouble("speed"));
        weatherInfo.setWindDirection(wind.getInt("deg"));
        try {
            weatherInfo.setWindGust(wind.getDouble("gust"));
        }catch (JSONException e){
            weatherInfo.setWindGust(0.0);
        }

        weatherInfo.setCloudiness(weather.getJSONObject("clouds").getInt("all"));
        weatherInfo.setTime(weather.getLong("dt"));
        weatherInfo.setTimeShift(weather.getInt("timezone"));
        weatherInfo.setSunRise(weather.getJSONObject("sys").getLong("sunrise"));
        weatherInfo.setSunSet(weather.getJSONObject("sys").getLong("sunset"));

        return weatherInfo;
    }
}

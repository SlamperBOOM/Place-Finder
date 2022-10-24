package placeFinderLib;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;
import org.json.*;
import placeFinderLib.infos.PlaceInfo;

class GeocodeFinder implements Supplier<List<PlaceInfo>> {
    private String placeName;
    private final String locale;

    public GeocodeFinder(String placeName, String locale){
        this.placeName = placeName;
        this.locale = locale;
    }

    @Override
    public List<PlaceInfo> get() {
        placeName = placeName.replaceAll(" ", "%20");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "https://graphhopper.com/api/1/geocode?q=" + placeName
                                + "&locale=" + locale + "&key=2195560d-d929-4608-a82c-143219db3cd9&limit=10"))
                .build();
        CompletableFuture<HttpResponse<String>> future =
                client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response;
        try {
            response = future.get();
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
        JSONObject json = new JSONObject(response.body());
        JSONArray places = json.getJSONArray("hits");
        List<PlaceInfo> placesInfo = new ArrayList<>();
        for(int i=0; i<places.length(); ++i){
            JSONObject place = places.getJSONObject(i);
            JSONObject coords = place.getJSONObject("point");
            PlaceInfo placeInfo = new PlaceInfo();
            try {
                placeInfo.setGeocode(place.getInt("osm_id"));
            }catch (JSONException e){
                placeInfo.setGeocode(-1);
            }
            try {
                placeInfo.setType(place.getString("osm_value"));
            }catch (JSONException e){
                placeInfo.setType("");
            }
            try {
                placeInfo.setName(place.getString("name"));
            }catch (JSONException e){
                placeInfo.setName("");
            }
            try {
                placeInfo.setCity(place.getString("city"));
            }catch (JSONException e){
                placeInfo.setCity("");
            }
            try {
                placeInfo.setCountry(place.getString("country"));
            }catch (JSONException e){
                placeInfo.setCountry("");
            }
            try {
                placeInfo.setLng(coords.getDouble("lng"));
            }catch (JSONException e){
                placeInfo.setLng(200.0);
            }
            try {
                placeInfo.setLat(coords.getDouble("lat"));
            }catch (JSONException e){
                placeInfo.setLat(100.0);
            }
            try {
                placeInfo.setPostcode(place.getInt("postcode"));
            }catch (JSONException e){
                placeInfo.setPostcode(-1);
            }
            try {
                placeInfo.setStreet(place.getString("street"));
            }catch (JSONException e){
                placeInfo.setStreet("");
            }
            try{
                placeInfo.setState(place.getString("state"));
            }catch (JSONException e){
                placeInfo.setState("");
            }
            placesInfo.add(placeInfo);
        }
        return placesInfo;
    }
}

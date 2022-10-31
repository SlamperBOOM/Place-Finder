package placeFinderLib;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import placeFinderLib.infos.POIInfo;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

public class POIFinder implements Supplier<List<POIInfo>> {
    private final Double lat;
    private final Double lng;

    public POIFinder(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    @Override
    public List<POIInfo> get() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "http://api.opentripmap.com/0.1/ru/places/radius" +
                                "?radius=10000&lon="+lng+"&lat="+lat + "&apikey=5ae2e3f221c38a28845f05b6d44667038fc303ce72d5087cf03135ae"))
                .build();
        CompletableFuture<HttpResponse<String>> future =
                client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response;
        try {
            response = future.get();
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }

        JSONArray poiArray = (new JSONObject(response.body())).getJSONArray("features");
        List<CompletableFuture<HttpResponse<String>>> poiResponses = new ArrayList<>();
        List<POIInfo> poiInfos = new ArrayList<>();
        for(int i=0; i<poiArray.length(); ++i){
            JSONObject poi = poiArray.getJSONObject(i).getJSONObject("properties");
            if(!poi.getString("name").equals("") && poi.getInt("rate") >= 3){
                HttpClient poiInfoClient = HttpClient.newHttpClient();
                HttpRequest infoRequest = HttpRequest.newBuilder()
                        .uri(URI.create(
                                "https://api.opentripmap.com/0.1/ru/places/xid/"+ poi.getString("xid") +"?apikey=5ae2e3f221c38a28845f05b6d44667038fc303ce72d5087cf03135ae"))
                        .build();
                CompletableFuture<HttpResponse<String>> infoFuture =
                        poiInfoClient.sendAsync(infoRequest, HttpResponse.BodyHandlers.ofString());
                poiResponses.add(infoFuture);

                POIInfo info = new POIInfo();
                info.setName(poi.getString("name"));
                poiInfos.add(info);
            }
        }

        for(int i=0; i<poiInfos.size(); ++i){
            CompletableFuture<HttpResponse<String>> infoFuture = poiResponses.get(i);
            POIInfo info = poiInfos.get(i);
            HttpResponse<String> infoResponse;
            try {
                infoResponse = infoFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return null;
            }
            JSONObject json = new JSONObject(infoResponse.body());
            try {
                JSONObject coords = json.getJSONObject("point");
                info.setLng(coords.getDouble("lon"));
                info.setLat(coords.getDouble("lat"));
            }catch (JSONException e){
                info.setLat(200.0);
                info.setLat(200.0);
            }
            try{
                JSONObject address = json.getJSONObject("address");
                StringBuilder builder = new StringBuilder();
                try{
                    builder.append(address.getString("city")).append(", ");
                }catch (JSONException ignored){

                }
                try{
                    builder.append("ул. ").append(address.getString("road")).append(", ");
                    builder.append("дом ").append(address.getString("house_number"));
                }catch (JSONException e){
                    builder.delete(Math.max(builder.indexOf(","), 0), builder.length()-1);
                }
                info.setAddress(builder.toString());
            }catch (JSONException e){
                info.setAddress("");
            }
            try{
                info.setDescription(json.getJSONObject("info").getString("descr"));
            }catch (JSONException e){
                info.setDescription("Отсутствует");
            }
            try{
                info.setWikiLink(json.getString("wikipedia"));
            }catch (JSONException e){
                info.setWikiLink("");
            }
        }
        return poiInfos;
    }
}

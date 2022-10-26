package placeFinderLib;

import placeFinderLib.infos.POIInfo;
import placeFinderLib.infos.PlaceInfo;
import placeFinderLib.infos.WeatherInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class PlaceFinder {
    private List<PlaceInfo> placesInfo;

    public List<String> findPlace(String placeName, String locale){
        CompletableFuture<List<PlaceInfo>> places = CompletableFuture.supplyAsync(new GeocodeFinder(placeName, locale));
        List<String> list = new ArrayList<>();
        try {
            placesInfo = places.get();
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
        if(placesInfo == null){
            return null;
        }
        for(PlaceInfo place : placesInfo){
            list.add(place.toString());
        }
        return list;
    }

    public List<String> findInfoAboutPlace(int id){
        PlaceInfo place = placesInfo.get(id);
        CompletableFuture<WeatherInfo> weather = CompletableFuture.supplyAsync(new WeatherFinder(place.getLat(), place.getLng()));
        CompletableFuture<List<POIInfo>> pois = CompletableFuture.supplyAsync(new POIFinder(place.getLat(), place.getLng()));
        List<String> text = new ArrayList<>();
        WeatherInfo weatherInfo;
        try {
            weatherInfo = weather.get();
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
        text.add("Название места: " + place.getName() + "\n" + weatherInfo.toString());
        List<POIInfo> poiInfos;
        try{
            poiInfos = pois.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            text.add("Не найдено информации об интересных местах");
            return text;
        }
        text.add("Найденные интересные места в радиусе 10 км:");
        for(POIInfo poi : poiInfos){
            StringBuilder builder = new StringBuilder();
            builder.append("Название места: ").append(poi.getName()).append("\n");
            if(!poi.getAddress().equals("")){
                builder.append("Адрес: ").append(poi.getAddress()).append("\n");
            }
            if(poi.getLat() != 200.0) {
                builder.append("Координаты: широта ").append(poi.getLat()).append(", долгота ").append(poi.getLng()).append("\n");
            }else{
                builder.append("Координаты: отсутствуют\n");
            }
            if(!poi.getWikiLink().equals("")) {
                builder.append("Ссылка на Wiki: ").append(poi.getWikiLink()).append("\n");
            }
            builder.append("Описание: ").append(poi.getDescription()).append("\n");
            text.add(builder.toString());
        }
        return text;
    }
}

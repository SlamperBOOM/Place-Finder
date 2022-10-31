package placeFinderLib.infos;

public class PlaceInfo {
    private Integer geocode;
    private String name;
    private String type;
    private String city;
    private String country;
    private Double lng;
    private Double lat;
    private Integer postcode;
    private String street;
    private String state;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public PlaceInfo(){

    }

    public Integer getGeocode() {
        return geocode;
    }

    public void setGeocode(Integer geocode) {
        this.geocode = geocode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Integer getPostcode() {
        return postcode;
    }

    public void setPostcode(Integer postcode) {
        this.postcode = postcode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if(!name.equals("")){
            builder.append("Название: ").append(name).append(", ");
        }
        if(!type.equals("")){
            builder.append("тип: ").append(type).append("\n");
        }
        builder.append("   Местоположение: ");
        if(!city.equals("")){
            builder.append("город: ").append(city).append(", ");
        }
        if(!street.equals("")){
            builder.append("улица: ").append(street).append(", ");
        }
        if(!state.equals("")){
            builder.append("регион: ").append(state).append(", ");
        }
        if(!country.equals("")){
            builder.append("страна: ").append(country).append(", ");
        }
        if(postcode != -1){
            builder.append("почтовый индекс: ").append(postcode).append(", ");
        }
        builder.deleteCharAt(builder.length()-2);
        builder.append("\n");
        if(lat != 200.0){
            builder.append("   Координаты: широта ").append(lat).append(", долгота ").append(lng);
        }
        return builder.toString();
    }
}

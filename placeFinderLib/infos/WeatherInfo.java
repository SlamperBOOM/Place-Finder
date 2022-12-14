package placeFinderLib.infos;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class WeatherInfo {
    private String description;
    private Double temp;
    private Double feelTemp;
    private Double minTemp;
    private Double maxTemp;
    private Integer pressure;
    private Integer humidity;
    private Integer visibility;
    private Double windSpeed;
    private Integer windDirection;
    private Double windGust;
    private Integer cloudiness;
    private Long time;
    private Integer timeShift;
    private Long sunSet;
    private Long sunRise;

    public void setTime(Long time) {
        this.time = time * 1000;
    }

    public void setTimeShift(Integer timeShift) {
        this.timeShift = timeShift * 1000;
    }

    public void setSunSet(Long sunSet) {
        this.sunSet = sunSet * 1000;
    }

    public void setSunRise(Long sunRise) {
        this.sunRise = sunRise * 1000;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public void setFeelTemp(Double feelTemp) {
        this.feelTemp = feelTemp;
    }

    public void setMinTemp(Double minTemp) {
        this.minTemp = minTemp;
    }

    public void setMaxTemp(Double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public void setPressure(Integer pressure) {
        this.pressure = pressure;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public void setWindDirection(Integer windDirection) {
        this.windDirection = windDirection;
    }

    public void setWindGust(Double windGust) {
        this.windGust = windGust;
    }

    public void setCloudiness(Integer cloudiness) {
        this.cloudiness = cloudiness;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("?????????????? ????????????: ").append(description).append("\n");
        builder.append("??????????????????????: ??????. ").append(temp).
                append(" ??C, ?????????????????? ?????? ").append(feelTemp).
                append(" ??C, ??????. ").append(minTemp).
                append(" ??C, ????????. ").append(maxTemp).append(" ??C\n");
        builder.append("????????????????: ").append(pressure).append(" ?????? (").append(Math.round(pressure * 0.75)).append(" ????.????.????)\n");
        builder.append("??????????????????: ").append(humidity).append("%\n");
        if(visibility < 1050) {
            builder.append("??????????????????: ").append(visibility).append(" ??.\n");
        }else{
            builder.append("??????????????????: ").append(visibility / 1000.0).append(" ????.\n");
        }
        builder.append("????????????????????: ").append(cloudiness).append("%\n");
        builder.append("??????????: ???????????????? ").append(windSpeed).append(" ??/??, ");
        if((windDirection >= 337.5 && windDirection <= 360) || (windDirection >=0 && windDirection < 22.5)){
            builder.append("???????????????? (??, ");
        } else if (windDirection >= 22.5 && windDirection < 67.5) {
            builder.append("????????????-?????????????????? (????, ");
        } else if (windDirection >= 67.5 && windDirection < 112.5) {
            builder.append("?????????????????? (??, ");
        } else if (windDirection >= 112.5 && windDirection < 157.5){
            builder.append("??????-?????????????????? (????, ");
        } else if (windDirection >= 157.5 && windDirection < 202.5) {
            builder.append("?????????? (??, ");
        } else if (windDirection >= 202.5 && windDirection < 247.5) {
            builder.append("??????-???????????????? (????, ");
        } else if (windDirection >= 247.5 && windDirection < 292.5){
            builder.append("???????????????? (??, ");
        }else if(windDirection >= 292.5 && windDirection < 337.5){
            builder.append("????????????-???????????????? (????, ");
        }
        builder.append(windDirection).append("??)");
        if(windGust > 0.0) {
            builder.append(", ???????????? ???? ").append(windGust).append(" ??/??\n");
        }else{

            builder.append("\n");
        }

        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(time + timeShift);
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        builder.append("?????????????? ?????????? ?? ??????????: ");

        DateFormat format = new SimpleDateFormat("E HH:mm");
        builder.append(format.format(calendar.getTime())).append(", UTC");
        if(timeShift>=0){
            builder.append("+").append(timeShift / 3600/1000);
        }else{
            builder.append(timeShift/3600/1000);
        }
        format = new SimpleDateFormat("HH:mm");
        calendar.setTimeInMillis(sunRise + timeShift);
        builder.append("\n????????????: ??? ")
                .append(format.format(calendar.getTime())).append(", ");
        calendar.setTimeInMillis(sunSet + timeShift);
        builder.append("??????????: ??? ")
                .append(format.format(calendar.getTime())).append("\n");
        return builder.toString();
    }
}

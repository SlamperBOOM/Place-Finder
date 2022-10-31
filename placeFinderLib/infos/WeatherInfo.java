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
        builder.append("Текущая погода: ").append(description).append("\n");
        builder.append("Температура: тек. ").append(temp).
                append(" °C, ощущается как ").append(feelTemp).
                append(" °C, мин. ").append(minTemp).
                append(" °C, макс. ").append(maxTemp).append(" °C\n");
        builder.append("Давление: ").append(pressure).append(" гПа (").append(Math.round(pressure * 0.75)).append(" мм.рт.ст)\n");
        builder.append("Влажность: ").append(humidity).append("%\n");
        if(visibility < 1050) {
            builder.append("Видимость: ").append(visibility).append(" м.\n");
        }else{
            builder.append("Видимость: ").append(visibility / 1000.0).append(" км.\n");
        }
        builder.append("Облачность: ").append(cloudiness).append("%\n");
        builder.append("Ветер: скорость ").append(windSpeed).append(" м/с, ");
        if((windDirection >= 337.5 && windDirection <= 360) || (windDirection >=0 && windDirection < 22.5)){
            builder.append("Северный (С, ");
        } else if (windDirection >= 22.5 && windDirection < 67.5) {
            builder.append("Северо-восточный (СВ, ");
        } else if (windDirection >= 67.5 && windDirection < 112.5) {
            builder.append("Восточный (В, ");
        } else if (windDirection >= 112.5 && windDirection < 157.5){
            builder.append("Юго-восточный (ЮВ, ");
        } else if (windDirection >= 157.5 && windDirection < 202.5) {
            builder.append("Южный (Ю, ");
        } else if (windDirection >= 202.5 && windDirection < 247.5) {
            builder.append("Юго-западный (ЮЗ, ");
        } else if (windDirection >= 247.5 && windDirection < 292.5){
            builder.append("Западный (З, ");
        }else if(windDirection >= 292.5 && windDirection < 337.5){
            builder.append("Северо-западный (СЗ, ");
        }
        builder.append(windDirection).append("°)");
        if(windGust > 0.0) {
            builder.append(", порывы до ").append(windGust).append(" м/с\n");
        }else{

            builder.append("\n");
        }

        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(time + timeShift);
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        builder.append("Текущее время в месте: ");

        DateFormat format = new SimpleDateFormat("E HH:mm");
        builder.append(format.format(calendar.getTime())).append(", UTC");
        if(timeShift>=0){
            builder.append("+").append(timeShift / 3600/1000);
        }else{
            builder.append(timeShift/3600/1000);
        }
        format = new SimpleDateFormat("HH:mm");
        calendar.setTimeInMillis(sunRise + timeShift);
        builder.append("\nВосход: ↑ ")
                .append(format.format(calendar.getTime())).append(", ");
        calendar.setTimeInMillis(sunSet + timeShift);
        builder.append("Закат: ↓ ")
                .append(format.format(calendar.getTime())).append("\n");
        return builder.toString();
    }
}

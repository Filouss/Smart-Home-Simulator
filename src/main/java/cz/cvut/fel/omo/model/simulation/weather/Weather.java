package cz.cvut.fel.omo.model.simulation.weather;

import cz.cvut.fel.omo.model.simulation.Simulation;
import cz.cvut.fel.omo.model.simulation.TimeTracker;
import cz.cvut.fel.omo.model.simulation.logger.Log;
import cz.cvut.fel.omo.model.simulation.logger.LogType;

import java.util.Random;

/**
 * This class represents the Weather, or a Weather Station from the perspective of appliances etc.
 * Design pattern: Singleton
 */
public final class Weather {
    private static double temperature; // the temperature in degrees
    private static double humidity; // the humidity in the air
    private static WeatherType type; // the weather type (e.g. CLEAR, RAIN, SNOWING)

    private Weather() {
    }

    /**
     * This method updates the weather.
     */
    public static void update() {
        // determine the season
        String season = "winter";
        long daysPassed = TimeTracker.getDaysPassed();
        if (daysPassed > 270) {
            season = "autumn";
        } else if (daysPassed > 180) {
            season = "summer" ;
        } else if (daysPassed > 90) {
            season = "spring";
        }

        // determine the mix and max temperatures based on the season
        double minTemperature = 0;
        double maxTemperature = 0;
        switch (season) {
            case "winter":
                minTemperature = -6;
                maxTemperature = 1;
                break;
            case "autumn":
                minTemperature = -4;
                maxTemperature = 18;
                break;
            case "summer":
                minTemperature = 6;
                maxTemperature = 32;
                break;
            case "spring":
                minTemperature = -1;
                maxTemperature = 7;
                break;
            default:
                Simulation.log(new Log("Unimplemented season: in Weather.update: " + season, LogType.ERROR));
                break;
        }

        // determine the number of hours away from noon in either direction
        double hoursFromNoon = (double) Math.abs(12-TimeTracker.getHoursPassedThisDay());
        // set the temperature based on hoursFromNoon (the further away, the colder, the closer, the hotter)
        temperature = minTemperature + (maxTemperature-minTemperature)*(1-(hoursFromNoon)/12);

        // create a new rand with the current time as seed
        Random weatherRand  = new Random(TimeTracker.getMinutesPassed());

        // if it's raining or snowing, handle temperature and weather changes
        if (type == WeatherType.RAIN || type == WeatherType.SNOWING) {
            // 10% chance to stop raining or snowing
            if (weatherRand.nextInt(10) == 0) {
                 type = WeatherType.CLEAR;
             // else, if temperature is above 0
            } else if (temperature > 0) {
                // set the weather to rain
                type = WeatherType.RAIN;
            // else (if it's below 0)
            } else {
                // set the weather to snowing
                type = WeatherType.SNOWING;
            }
        // else if it's neither raining nor snowing, 5% chance to start raining or snowing
        } else if (weatherRand.nextInt(20) == 0) {
            type = WeatherType.RAIN;
            if (temperature < 0) {
                type = WeatherType.SNOWING;
            }
        }

        // determine min and max humidity
        double minHumidity = 70;
        double maxHumidity = 90;
        // set humidity as a function of temperature (higher temperature = lower humidity;
        if (temperature < -10) {
            humidity = minHumidity;
        } else if (temperature > 35) {
            humidity = maxHumidity;
        } else {
            humidity = minHumidity + (maxHumidity-minHumidity)*((temperature+10)/45);
        }
    }

    public static WeatherType getType() {
        return type;
    }

    public static double getHumidity() {
        return humidity;
    }

    public static double getTemperature() {
        return temperature;
    }
}

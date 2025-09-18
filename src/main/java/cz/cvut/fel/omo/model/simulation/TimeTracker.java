package cz.cvut.fel.omo.model.simulation;

import java.sql.Time;

/**
 * This class keeps track of the minutes passed since the start of the simulation.
 * Design pattern: Singleton
 */
public final class TimeTracker {
    private static long minutesPassed = 0;

    private TimeTracker() {

    }

    /**
     * This method resets the time tracker.
     */
    public static void reset() {
        minutesPassed = 0;
    }

    /**
     * This method adds 10 minutes to minutesPassed.
     */
    public static void tenMinutesPassed() {
        minutesPassed += 10;
    }

    /**
     * This method returns the amount of minutes passed since the start of the simulation.
     * @return the amount of minutes passed since the start of the simulation
     */
    public static long getMinutesPassed() {
        return minutesPassed;
    }

    /**
     * This method returns the hours passed this day rounded down.
     * @return the hours passed this day rounded down
     */
    public static long getHoursPassedThisDay() {
        return (TimeTracker.getMinutesPassedThisDay()-TimeTracker.getMinutesPassedThisDay()%60) /60;
    }


    /**
     * This method returns the minutes passed this day.
     * @return the minutes passed this day
     */
    public static long getMinutesPassedThisDay() {
        return (long) (((float)TimeTracker.getMinutesPassed())%(24*60));
    }

    /**
     * This method returns the number of days passed since the start of the simulation.
     * @return the number of days passed since the start of the simulation
     */
    public static long getDaysPassed() {
        return (long) Math.floor((double) (minutesPassed / 60) /24);
    }

    /**
     * This method returns the current year, starting at year 1.
     * @return the current year, starting at year 1
     */
    public static long getCurrentYear() {
        return 1 + (long) Math.floor(((double)TimeTracker.getDaysPassed())/360);
    }

    /**
     * This method returns the current month  within the year, starting at month 1.
     * @return the current month within the year, starting at month 1
     */
    public static long getCurrentMonthThisYear() {
        return 1 + (long) Math.floor(((double)TimeTracker.getDaysPassed())/30)%12;
    }
}

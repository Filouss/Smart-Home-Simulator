package cz.cvut.fel.omo.model.simulation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TimeTrackerTest {
    @Test
    void tenMinutesPassed_changesMinutesPassedByTen() {
        long originalMinutesPassed = TimeTracker.getMinutesPassed();
        TimeTracker.tenMinutesPassed();
        long minutesPassed = TimeTracker.getMinutesPassed();
        Assertions.assertEquals(originalMinutesPassed+10, minutesPassed);
    }

    @Test
    void reset_setsMinutesPassedToZero() {
        TimeTracker.tenMinutesPassed();
        TimeTracker.reset();
        Assertions.assertEquals(0, TimeTracker.getMinutesPassed());
    }

    @Test
    void getHoursPassedThisDay_afterPassingTenMinutesFiveTimes_returnsZero() {
        TimeTracker.reset();
        TimeTracker.tenMinutesPassed();
        TimeTracker.tenMinutesPassed();
        TimeTracker.tenMinutesPassed();
        TimeTracker.tenMinutesPassed();
        TimeTracker.tenMinutesPassed();
        Assertions.assertEquals(TimeTracker.getHoursPassedThisDay(), 0);
    }

    @Test
    void getHoursPassedThisDay_afterPassingTenMinutesSixTimes_returnsOne() {
        TimeTracker.reset();
        TimeTracker.tenMinutesPassed();
        TimeTracker.tenMinutesPassed();
        TimeTracker.tenMinutesPassed();
        TimeTracker.tenMinutesPassed();
        TimeTracker.tenMinutesPassed();
        TimeTracker.tenMinutesPassed();
        Assertions.assertEquals(TimeTracker.getHoursPassedThisDay(), 1);
    }

    @Test
    void getHoursPassedThisDay_afterPassingTenMinutesTwelveTimes_returnsTwo() {
        TimeTracker.reset();
        TimeTracker.tenMinutesPassed();
        TimeTracker.tenMinutesPassed();
        TimeTracker.tenMinutesPassed();
        TimeTracker.tenMinutesPassed();
        TimeTracker.tenMinutesPassed();
        TimeTracker.tenMinutesPassed();
        TimeTracker.tenMinutesPassed();
        TimeTracker.tenMinutesPassed();
        TimeTracker.tenMinutesPassed();
        TimeTracker.tenMinutesPassed();
        TimeTracker.tenMinutesPassed();
        TimeTracker.tenMinutesPassed();
        Assertions.assertEquals(TimeTracker.getHoursPassedThisDay(), 2);
    }

    @Test
    void getHoursPassedThisDay_afterPassingTenMinutesSevenTimes_returnsOne() {
        TimeTracker.reset();
        TimeTracker.tenMinutesPassed();
        TimeTracker.tenMinutesPassed();
        TimeTracker.tenMinutesPassed();
        TimeTracker.tenMinutesPassed();
        TimeTracker.tenMinutesPassed();
        TimeTracker.tenMinutesPassed();
        TimeTracker.tenMinutesPassed();
        Assertions.assertEquals(TimeTracker.getHoursPassedThisDay(), 1);
    }

    @Test
    void getHoursPassedThisDay_afterPassingTenMinutesOneHundredAndFiftyTimes_returnsOne() {
        TimeTracker.reset();
        for (int i = 0; i < 150; i++) {
            TimeTracker.tenMinutesPassed();
        }
        Assertions.assertEquals(TimeTracker.getHoursPassedThisDay(), 1);
    }

    @Test
    void getDaysPassed_afterPassingTenMinutesOnce_returnsZero() {
        TimeTracker.reset();
        TimeTracker.tenMinutesPassed();
        Assertions.assertEquals(TimeTracker.getDaysPassed(), 0);
    }

    @Test
    void getDaysPassed_afterPassingTenMinutesOneHundredAndFiftyTimes_returnsOne() {
        TimeTracker.reset();
        for (int i = 0; i < 150; i++) {
            TimeTracker.tenMinutesPassed();
        }
        Assertions.assertEquals(TimeTracker.getDaysPassed(), 1);
    }

    @Test
    void getDaysPassed_afterPassingTenMinutesThreeHundredTimes_returnsTwo() {
        TimeTracker.reset();
        for (int i = 0; i < 300; i++) {
            TimeTracker.tenMinutesPassed();
        }
        Assertions.assertEquals(TimeTracker.getDaysPassed(), 2);
    }
}
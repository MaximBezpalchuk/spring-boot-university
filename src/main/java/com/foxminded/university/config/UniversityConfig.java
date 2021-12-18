package com.foxminded.university.config;

import com.foxminded.university.model.Degree;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "university")
public class UniversityConfig {
    private int minLectureDurationInMinutes;
    private int maxGroupSize;
    private int startWorkingDay;
    private int endWorkingDay;
    private Map<Degree, Integer> maxVacation;

    public int getMinLectureDurationInMinutes() {
        return minLectureDurationInMinutes;
    }

    public void setMinLectureDurationInMinutes(int minLectureDurationInMinutes) {
        this.minLectureDurationInMinutes = minLectureDurationInMinutes;
    }

    public int getMaxGroupSize() {
        return maxGroupSize;
    }

    public void setMaxGroupSize(int maxGroupSize) {
        this.maxGroupSize = maxGroupSize;
    }

    public int getStartWorkingDay() {
        return startWorkingDay;
    }

    public void setStartWorkingDay(int startWorkingDay) {
        this.startWorkingDay = startWorkingDay;
    }

    public int getEndWorkingDay() {
        return endWorkingDay;
    }

    public void setEndWorkingDay(int endWorkingDay) {
        this.endWorkingDay = endWorkingDay;
    }

    public Map<Degree, Integer> getMaxVacation() {
        return maxVacation;
    }

    public void setMaxVacation(Map<Degree, Integer> maxVacation) {
        this.maxVacation = maxVacation;
    }
}

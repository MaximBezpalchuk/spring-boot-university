package com.foxminded.university.config;

import com.foxminded.university.model.Degree;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.*;
import java.util.Map;

@ConfigurationProperties(prefix = "university")
public class UniversityConfigProperties {

    @NotNull
    @Positive
    private int minLectureDurationInMinutes;

    @NotNull
    @Positive
    private int maxGroupSize;

    @Min(value = 0)
    @Max(value = 23)
    private int startWorkingDay;

    @Min(value = 0)
    @Max(value = 23)
    private int endWorkingDay;

    @NotEmpty
    private Map<Degree, @Positive Integer> maxVacation;

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

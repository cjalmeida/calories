/*
 * Copyright (c) 2016. Cloves Almeida. All rights reserved.
 */

package tt.calories.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.data.rest.core.config.Projection;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Time;

/**
 * Represents a meal a user had.
 *
 * @author Cloves Almeida
 * @version 1.0.0
 */
@Entity
public class Meal {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @NotNull
    private User user;

    @NotNull
    private java.sql.Time mealTime;

    @NotNull
    private java.sql.Date mealDate;

    @NotNull
    private Integer calories;

    private String description;

    public Meal() { }

    /**
     * Convenience constructor.
     */
    public Meal(User user, String mealDate, String mealTime, Integer calories, String description) {
        this.user = user;
        this.mealTime = java.sql.Time.valueOf(mealTime);
        this.mealDate = java.sql.Date.valueOf(mealDate);
        this.calories = calories;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Meal)) return false;
        Meal x = (Meal) o;
        return new EqualsBuilder()
            .append(user.getId(), x.user.getId())
            .append(mealTime, x.mealTime)
            .append(mealDate, x.mealDate)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(user.getId())
            .append(mealTime)
            .append(mealDate)
            .toHashCode();
    }

    @Transient
    public Long getUserId() {
        return user.getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Time getMealTime() {
        return mealTime;
    }

    public void setMealTime(Time mealTime) {
        this.mealTime = mealTime;
    }

    public Date getMealDate() {
        return mealDate;
    }

    public void setMealDate(Date mealDate) {
        this.mealDate = mealDate;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

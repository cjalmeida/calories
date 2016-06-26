package tt.calories.domain.projections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import tt.calories.domain.Meal;

import java.sql.Date;
import java.sql.Time;

/**
 * TODO
 */
@Projection(name = "list", types = Meal.class)
public interface MealListProjection {

    @Value("#{target.user.fullName}")
    String getUser();

    Long getId();

    Time getMealTime();

    Date getMealDate();

    Integer getCalories();

    String getDescription();

}

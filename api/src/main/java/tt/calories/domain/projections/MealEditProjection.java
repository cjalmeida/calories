package tt.calories.domain.projections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import tt.calories.domain.Meal;

/**
 * Created by cjalmeida on 26/06/16.
 */
@Projection(name = "edit", types = Meal.class)
public interface MealEditProjection extends MealListProjection {

    @Value("/users/#{target.user.id}")
    String getUser();

}

/*
 * Copyright (c) 2016. Cloves Almeida. All rights reserved.
 */

package tt.calories.domain.projections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import tt.calories.domain.Meal;

/**
 * Projection for editing a meal.
 *
 * @author Cloves J. G. Almeida <cjalmeida@gmail.com>
 * @version 1.0.0
 */
@Projection(name = "edit", types = Meal.class)
public interface MealEditProjection extends MealListProjection {

    @Value("/users/#{target.user.id}")
    String getUser();

}

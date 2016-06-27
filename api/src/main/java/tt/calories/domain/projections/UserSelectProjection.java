/*
 * Copyright (c) 2016. Cloves Almeida. All rights reserved.
 */

package tt.calories.domain.projections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import tt.calories.domain.User;

/**
 * Projection suited for HTML SELECT controls.
 *
 * @author Cloves J. G. Almeida <cjalmeida@gmail.com>
 * @version 1.0.0
 */
@Projection(name = "select", types = User.class)
public interface UserSelectProjection {

    @Value("/users/#{target.id}")
    String getHref();

    String getFullName();

}

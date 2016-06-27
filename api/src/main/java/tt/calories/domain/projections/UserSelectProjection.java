package tt.calories.domain.projections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import tt.calories.domain.User;

/**
 * Created by cjalmeida on 26/06/16.
 */
@Projection(name = "select", types = User.class)
public interface UserSelectProjection {

    @Value("/users/#{target.id}")
    String getHref();

    String getFullName();

}

package tt.calories.domain.projections;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import tt.calories.domain.User;

import java.util.Set;

/**
 * "simple" projection for user.
 *
 * @author Cloves Almeida
 * @version 1.0.0
 */
@Projection(name = "simple", types = {User.class})
public interface UserSimple {

    Long getId();

    String getFullName();

    String getEmail();

    boolean isActive();

    @JsonProperty("roles")
    Set<String> getRolesString();

    Integer getCaloriesPerDay();
}

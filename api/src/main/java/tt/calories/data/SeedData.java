package tt.calories.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tt.calories.domain.Role;
import tt.calories.domain.User;

import javax.persistence.EntityManager;

/**
 * Class holding required seed data.
 *
 * To simplify testing, some data is shared as (friend) static fields.
 *
 * @author Cloves Almeida
 * @version 1.0.0
 */
@Component
public class SeedData {

    public static final String ADMIN_DEFAULT_EMAIL = "admin@example.com";
    public static final String ADMIN_DEFAULT_PASSWORD = "admin";

    @Autowired
    EntityManager em;

    private static final Logger log = LoggerFactory.getLogger(SeedData.class);

    @Transactional
    public void load() {

        log.info("Loading seed data.");

        Role adminRole = new Role("ROLE_ADMIN", "Administrator privileges");
        em.persist(adminRole);

        Role managerRole = new Role("ROLE_MANAGER", "User manager");
        em.persist(managerRole);

        Role userRole = new Role("ROLE_USER", "App user");
        em.persist(userRole);

        User admin = new User("Administrator", ADMIN_DEFAULT_EMAIL, ADMIN_DEFAULT_PASSWORD);
        admin.getRoles().add(adminRole);
        em.persist(admin);
    }
}

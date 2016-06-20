package tt.calories.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tt.calories.domain.Meal;
import tt.calories.domain.Role;
import tt.calories.domain.User;

import javax.persistence.EntityManager;

/**
 * Class holding optional demo data.
 * <p>
 * To simplify testing, some data is shared as (friend) static fields.
 *
 * @author Cloves Almeida
 * @version 1.0.0
 */
@Component
public class DemoData {

    static User u1;
    static User u2;
    static User m1;

    @Autowired
    EntityManager em;

    private static final Logger log = LoggerFactory.getLogger(DemoData.class);


    @Transactional
    public void load() {

        log.info("Loading demo data.");

        Role userRole = em.createQuery("from Role where name = 'ROLE_USER'", Role.class).getSingleResult();
        Role managerRole = em.createQuery("from Role where name = 'ROLE_MANAGER'", Role.class).getSingleResult();

        m1 = new User("Manager", "manager@example.com", "manager");
        m1.getRoles().add(managerRole);
        em.persist(m1);

        u1 = new User("Dummy #1", "dummy1@example.com", "dummy");
        u1.getRoles().add(userRole);
        em.persist(u1);

        u2 = new User("Dummy #2", "dummy2@example.com", "dummy");
        u2.getRoles().add(userRole);
        em.persist(u2);

        Meal[] meals = new Meal[]{
            // Meals for dummy 1
            new Meal(u1, "2016-06-15", "08:00:00", 500, "Breakfast"),
            new Meal(u1, "2016-06-15", "12:00:00", 700, "Lunch"),
            new Meal(u1, "2016-06-15", "18:00:00", 700, "Diner"),
            new Meal(u1, "2016-06-15", "22:00:00", 300, "Snacks"),

            new Meal(u1, "2016-06-16", "08:00:00", 500, "Breakfast"),
            new Meal(u1, "2016-06-16", "12:00:00", 700, "Lunch"),
            new Meal(u1, "2016-06-16", "18:00:00", 700, "Diner"),
            new Meal(u1, "2016-06-16", "22:00:00", 300, "Snacks"),

            new Meal(u1, "2016-06-17", "08:00:00", 500, "Breakfast"),
            new Meal(u1, "2016-06-17", "12:00:00", 1000, "Barbecue!"),
            new Meal(u1, "2016-06-17", "18:00:00", 700, "Diner"),
            new Meal(u1, "2016-06-17", "22:00:00", 300, "Snacks"),

            new Meal(u1, "2016-06-18", "08:00:00", 500, "Breakfast"),
            new Meal(u1, "2016-06-18", "12:00:00", 700, "Lunch"),
            new Meal(u1, "2016-06-18", "18:00:00", 700, "Diner"),
            new Meal(u1, "2016-06-18", "22:00:00", 300, "Snacks"),

            // Meals for dummy 2
            new Meal(u2, "2016-05-15", "08:00:00", 500, "Breakfast"),
            new Meal(u2, "2016-05-15", "12:00:00", 700, "Lunch"),
            new Meal(u2, "2016-05-15", "18:00:00", 700, "Diner"),
            new Meal(u2, "2016-05-15", "22:00:00", 300, "Snacks"),

            new Meal(u2, "2016-05-16", "08:00:00", 500, "Breakfast"),
            new Meal(u2, "2016-05-16", "12:00:00", 700, "Lunch"),
            new Meal(u2, "2016-05-16", "18:00:00", 700, "Diner"),
            new Meal(u2, "2016-05-16", "22:00:00", 300, "Snacks"),

            new Meal(u2, "2016-05-17", "08:00:00", 500, "Breakfast"),
            new Meal(u2, "2016-05-17", "12:00:00", 1000, "Barbecue!"),
            new Meal(u2, "2016-05-17", "18:00:00", 700, "Diner"),
            new Meal(u2, "2016-05-17", "22:00:00", 300, "Snacks"),
        };

        for (Meal meal : meals) {
            em.persist(meal);
        }
    }
}

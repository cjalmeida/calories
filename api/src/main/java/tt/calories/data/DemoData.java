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

    public static final String MEAL_DATE_U1_0 = "2016-06-15";
    public static final String MEAL_TIME_0 = "08:00:00";
    public static User u1;
    public static final String DUMMY_PASS = "dummy";
    public static User u2;
    public static User m1;

    @Autowired
    EntityManager em;

    private static final Logger log = LoggerFactory.getLogger(DemoData.class);


    @Transactional
    public void load() {

        log.info("Loading demo data.");

        Role userRole = em.createQuery("from Role where name = 'ROLE_USER'", Role.class).getSingleResult();
        Role managerRole = em.createQuery("from Role where name = 'ROLE_MANAGER'", Role.class).getSingleResult();

        m1 = new User("Manager", "manager@example.com", DUMMY_PASS);
        m1.getRoles().add(managerRole);
        em.persist(m1);

        u1 = new User("Dummy #1", "dummy1@example.com", DUMMY_PASS);
        u1.getRoles().add(userRole);
        em.persist(u1);

        u2 = new User("Dummy #2", "dummy2@example.com", DUMMY_PASS);
        u2.getRoles().add(userRole);
        em.persist(u2);

        Meal[] meals = new Meal[]{
            // Meals for dummy 1
            new Meal(u1, MEAL_DATE_U1_0, MEAL_TIME_0, 500, "Breakfast"),
            new Meal(u1, MEAL_DATE_U1_0, "12:00:00", 700, "Lunch"),
            new Meal(u1, MEAL_DATE_U1_0, "18:00:00", 700, "Diner"),
            new Meal(u1, MEAL_DATE_U1_0, "22:00:00", 300, "Snacks"),

            new Meal(u1, "2016-06-16", MEAL_TIME_0, 500, "Breakfast"),
            new Meal(u1, "2016-06-16", "12:00:00", 700, "Lunch"),
            new Meal(u1, "2016-06-16", "18:00:00", 700, "Diner"),
            new Meal(u1, "2016-06-16", "22:00:00", 300, "Snacks"),

            new Meal(u1, "2016-06-17", MEAL_TIME_0, 500, "Breakfast"),
            new Meal(u1, "2016-06-17", "12:00:00", 1000, "Barbecue!"),
            new Meal(u1, "2016-06-17", "18:00:00", 700, "Diner"),
            new Meal(u1, "2016-06-17", "22:00:00", 300, "Snacks"),

            new Meal(u1, "2016-06-18", MEAL_TIME_0, 500, "Breakfast"),
            new Meal(u1, "2016-06-18", "12:00:00", 700, "Lunch"),
            new Meal(u1, "2016-06-18", "18:00:00", 700, "Diner"),
            new Meal(u1, "2016-06-18", "22:00:00", 300, "Snacks"),

            // Meals for dummy 2
            new Meal(u2, "2016-05-15", MEAL_TIME_0, 500, "Breakfast"),
            new Meal(u2, "2016-05-15", "12:00:00", 700, "Lunch"),
            new Meal(u2, "2016-05-15", "18:00:00", 700, "Diner"),
            new Meal(u2, "2016-05-15", "22:00:00", 300, "Snacks"),

            new Meal(u2, "2016-05-16", MEAL_TIME_0, 500, "Breakfast"),
            new Meal(u2, "2016-05-16", "12:00:00", 700, "Lunch"),
            new Meal(u2, "2016-05-16", "18:00:00", 700, "Diner"),
            new Meal(u2, "2016-05-16", "22:00:00", 300, "Snacks"),

            new Meal(u2, "2016-05-17", MEAL_TIME_0, 500, "Breakfast"),
            new Meal(u2, "2016-05-17", "12:00:00", 1000, "Barbecue!"),
            new Meal(u2, "2016-05-17", "18:00:00", 700, "Diner"),
            new Meal(u2, "2016-05-17", "22:00:00", 300, "Snacks"),
        };

        for (Meal meal : meals) {
            em.persist(meal);
        }
    }
}

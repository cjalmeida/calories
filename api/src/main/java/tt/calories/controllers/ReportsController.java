package tt.calories.controllers;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tt.calories.domain.QMeal;
import tt.calories.security.SecurityService;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for custom reports (ie. not returning a REST resource)
 *
 * @author Cloves Almeida
 * @version 1.0.0
 */
@RestController
@RequestMapping(path = "/api/reports")
public class ReportsController {

    @Autowired
    SecurityService sec;

    @Autowired
    EntityManager entityManager;

    /**
     * Query returning sum of numbers of calories grouped per day, ordered by day asc.
     */
    @RequestMapping(path = "/caloriesPerDay")
    public List<ObjectNode> caloriesPerDay(
        @Param("mealDateStart") java.sql.Date mealDateStart,
        @Param("mealDateEnd") java.sql.Date mealDateEnd) {

        JPAQuery query = new JPAQuery(entityManager);
        QMeal m = QMeal.meal;

        BooleanBuilder where = new BooleanBuilder();
        where.and(m.mealDate.between(mealDateStart, mealDateEnd));
        if (!sec.isAdmin())
            where.and(m.user.id.eq(sec.getUserId()));

        return query
            .from(m)
            .where(where)
            .groupBy(m.mealDate)
            .orderBy(m.mealDate.asc())
            .list(m.mealDate, m.calories.sum())
            .stream()
            .map((tuple ->
                JsonNodeFactory.instance
                    .objectNode()
                    .put("date", tuple.get(m.mealDate).toString())
                    .put("calories", tuple.get(m.calories.sum()))))
            .collect(Collectors.toList());
    }

    /**
     * Count total users.
     */
    @RequestMapping("/userCount")
    @PreAuthorize("hasRole('ADMIN') || hasRole('MANAGER')")
    public ObjectNode userCount() {
        Long count = entityManager
            .createQuery("select count(u) from User as u", Long.class)
            .getSingleResult();
        return JsonNodeFactory.instance.objectNode()
            .put("userCount", count);
    }

    /**
     * Count total users.
     */
    @RequestMapping("/mealCount")
    public ObjectNode mealCount() {
        JPAQuery query = new JPAQuery(entityManager);
        QMeal m = QMeal.meal;

        BooleanBuilder where = new BooleanBuilder();
        if (!sec.isAdmin() && !sec.isManager()) {
            where.and(m.user.id.eq(sec.getUserId()));
        }

        Long count = query
            .from(m)
            .where(where)
            .singleResult(m.count());

        return JsonNodeFactory.instance.objectNode()
            .put("mealCount", count);
    }


}

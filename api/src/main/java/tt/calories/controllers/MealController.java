package tt.calories.controllers;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.path.PathBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tt.calories.domain.Meal;
import tt.calories.domain.QMeal;
import tt.calories.repo.MealRepository;
import tt.calories.security.SecurityService;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * TODO
 */
@RepositoryRestController
@RequestMapping(path = "/meals")
public class MealController {

    @Autowired
    SecurityService sec;

    @Autowired
    EntityManager entityManager;

    @Autowired
    MealRepository mealRepository;

    @Autowired
    PagedResourcesAssembler pagedResourcesAssembler;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public PagedResources<PersistentEntityResource> search(
        Pageable page, PersistentEntityResourceAssembler resourceAssembler,
        @RequestParam(required = false) java.sql.Date dateStart,
        @RequestParam(required = false) java.sql.Date dateEnd,
        @RequestParam(required = false) java.sql.Time timeStart,
        @RequestParam(required = false) java.sql.Time timeEnd
    ) throws Exception {


        if ((dateStart == null) != (dateEnd == null)) {
            throw new BadRequestException("Both or neither 'dateStart' and 'dateEnd' must be present");
        }

        if ((timeStart == null) != (timeEnd == null)) {
            throw new BadRequestException("Both or neither 'timeStart' and 'timeEnd' must be present");
        }

        QMeal m = QMeal.meal;
        BooleanBuilder where = new BooleanBuilder();

        if (!sec.isAdmin() && !sec.isManager()) {
            where.and(m.user.id.eq(sec.getUserId()));
        }

        if (dateStart != null) {
            where.and(m.mealDate.between(dateStart, dateEnd));
        }

        if (timeStart != null) {
            where.and(m.mealTime.between(timeStart, timeEnd));
        }


//        JPQLQuery q = new JPAQuery(entityManager);
//
//        q = q.from(m).where(where);
//
//        PathBuilder<Meal> pb = new PathBuilder<Meal>(Meal.class, "m");
//        Querydsl springQuerydsl = new Querydsl(entityManager, pb);
//        if (page != null) {
//            q = springQuerydsl.applyPagination(page, q);
//        }
//        if (page != null && page.getSort() != null) {
//            q = springQuerydsl.applySorting(page.getSort(), q);
//        }
//
//        List<Meal> res = q.list(m);

        Page<Meal> resPage = mealRepository.findAll(where, page);
        return pagedResourcesAssembler.toResource(resPage, resourceAssembler);
    }
}

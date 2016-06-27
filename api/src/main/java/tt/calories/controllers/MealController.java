/*
 * Copyright (c) 2016. Cloves Almeida. All rights reserved.
 */

package tt.calories.controllers;

import com.mysema.query.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tt.calories.domain.Meal;
import tt.calories.domain.QMeal;
import tt.calories.repo.MealRepository;
import tt.calories.security.SecurityService;

import javax.persistence.EntityManager;

/**
 * Custom controller for Meals
 */
@RepositoryRestController
@RequestMapping(path = "/meals")
public class MealController {

    @Autowired
    SecurityService sec;

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

        QMeal m = QMeal.meal;
        BooleanBuilder where = new BooleanBuilder();

        if (!sec.isAdmin() && !sec.isManager()) {
            where.and(m.user.id.eq(sec.getUserId()));
        }

        if (dateStart != null) {
            where.and(m.mealDate.goe(dateStart));
        }

        if (dateEnd != null) {
            where.and(m.mealDate.loe(dateEnd));
        }

        if (timeStart != null) {
            where.and(m.mealTime.goe(timeStart));
        }

        if (timeEnd != null) {
            where.and(m.mealTime.loe(timeEnd));
        }

        Page<Meal> resPage = mealRepository.findAll(where, page);
        return pagedResourcesAssembler.toResource(resPage, resourceAssembler);
    }
}

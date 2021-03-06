/*
 * Copyright (c) 2016. Cloves Almeida. All rights reserved.
 */

package tt.calories.repo;

import org.omg.CORBA.Object;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestParam;
import tt.calories.domain.Meal;

import java.util.Date;
import java.util.List;

/**
 * Spring Data repository for Meals.
 *
 * @author Cloves Almeida
 * @version 1.0.0
 */
@RepositoryRestResource(exported = true)
public interface MealRepository extends PagingAndSortingRepository<Meal, Long>, QueryDslPredicateExecutor<Meal> {


    @Override
    @RestResource(exported = false)
    Page<Meal> findAll(Pageable pageable);


    @Override
    @PreAuthorize("hasRole('ADMIN') || @sec.checkUser(#meal.user?.id)")
    <S extends Meal> S save(@P("meal") S meal);

    @Override
    @PreAuthorize("hasRole('ADMIN') || @sec.checkUser(#meals.![user?.id])")
    <S extends Meal> Iterable<S> save(@P("meals") Iterable<S> meals);

    @Override
    @PostAuthorize("hasRole('ADMIN') || @sec.checkUser(returnObject.user.id)")
    Meal findOne(Long aLong);

    @Override
    @PreAuthorize("hasRole('ADMIN') || @sec.checkUser('Meal', #id)")
    void delete(@P("id") Long id);

    @Override
    @PreAuthorize("hasRole('ADMIN') || @sec.checkUser(#meal.user?.id)")
    void delete(@P("meal") Meal meal);

    @Override
    @PreAuthorize("@sec.checkUser(#meals.![user.id])")
    void delete(@P("meals") Iterable<? extends Meal> meals);

    @Override
    @RestResource(exported = false)
    void deleteAll();

}

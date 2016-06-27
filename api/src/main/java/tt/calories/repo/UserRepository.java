package tt.calories.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import tt.calories.domain.User;

import javax.validation.Valid;

/**
 * Spring Data repository for Users.
 *
 * See {@link tt.calories.controllers.UserController} class for more fine-grained security
 * control.
 *
 * @author Cloves Almeida
 * @version 1.0.0
 */
@RepositoryRestResource
@Validated
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER') || @sec.checkUser(#user.id)")
    <S extends User> S save(@Valid @P("user") S user);

    @RestResource(exported = false)
    @Override
    <S extends User> Iterable<S> save(Iterable<S> entities);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER') || @sec.checkUser(#id)")
    @Override
    User findOne(@P("id") Long id);

    @Query("from User where id = ?#{@sec.userId}")
    User self();

    @Override
    boolean exists(Long aLong);

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER') ")
    Iterable<User> findAll();

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER') ")
    Iterable<User> findAll(Iterable<Long> longs);

    @Override
    @RestResource(exported = false)
    long count();

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER') ")
    void delete(Long aLong);

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER') ")
    void delete(User entity);

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER') ")
    void delete(Iterable<? extends User> entities);

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER') ")
    void deleteAll();

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER') ")
    Iterable<User> findAll(Sort sort);

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER') ")
    Page<User> findAll(Pageable pageable);
}

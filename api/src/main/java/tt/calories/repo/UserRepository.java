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
    @PreAuthorize("@sec.checkUser(#user.id)")
    <S extends User> S save(@P("user") S user);

    @RestResource(exported = false)
    @Override
    <S extends User> Iterable<S> save(Iterable<S> entities);

    @PreAuthorize("@sec.checkUser(#id)")
    @Override
    User findOne(@P("id") Long id);

    @Query("from User where id = ?#{@sec.userId}")
    User self();

    @Override
    boolean exists(Long aLong);

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    Iterable<User> findAll();

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    Iterable<User> findAll(Iterable<Long> longs);

    @Override
    @RestResource(exported = false)
    long count();

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    void delete(Long aLong);

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    void delete(User entity);

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    void delete(Iterable<? extends User> entities);

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    void deleteAll();

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    Iterable<User> findAll(Sort sort);

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    Page<User> findAll(Pageable pageable);
}

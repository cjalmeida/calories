package tt.calories.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import tt.calories.domain.Role;

/**
 * Spring Data repository for Roles.
 *
 * @author Cloves Almeida
 * @version 1.0.0
 */
@RepositoryRestResource(exported = true)
public interface RoleRepository extends CrudRepository<Role, Long> {


    @Override
    @RestResource(exported = false)
    <S extends Role> S save(S entity);

    @Override
    @RestResource(exported = false)
    <S extends Role> Iterable<S> save(Iterable<S> entities);

    @Override
    Role findOne(Long aLong);

    @Override
    boolean exists(Long aLong);

    @Override
    Iterable<Role> findAll();

    @Override
    Iterable<Role> findAll(Iterable<Long> longs);

    @Override
    long count();

    @Override
    @RestResource(exported = false)
    void delete(Long aLong);

    @Override
    @RestResource(exported = false)
    void delete(Role entity);

    @Override
    @RestResource(exported = false)
    void delete(Iterable<? extends Role> entities);

    @Override
    @RestResource(exported = false)
    void deleteAll();

    Role findByName(String name);
}

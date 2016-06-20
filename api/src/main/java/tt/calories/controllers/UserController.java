package tt.calories.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mapping.context.PersistentEntities;
import org.springframework.data.rest.core.RepositoryConstraintViolationException;
import org.springframework.data.rest.core.ValidationErrors;
import org.springframework.data.rest.core.event.BeforeSaveEvent;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.core.mapping.ResourceMappings;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.rest.webmvc.json.DomainObjectReader;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tt.calories.domain.User;
import tt.calories.repo.UserRepository;
import tt.calories.security.SecurityService;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.xml.ws.Response;
import java.util.HashMap;
import java.util.Set;

/**
 * Custom controller with fine grained control over User resources.
 *
 * @author Cloves Almeida
 * @version 1.0.0
 */
@RepositoryRestController
@RequestMapping(path = "/users")
@ResponseBody
@Validated
public class UserController {

    private DomainObjectReader reader;

    @Autowired
    SecurityService sec;

    @Autowired
    UserRepository repo;

    @Autowired
    @Qualifier("halObjectMapper")
    ObjectMapper objectMapper;

    @Autowired
    PersistentEntities persistentEntities;

    @Autowired
    ResourceMappings resourceMappings;

    @Autowired
    ValidatingRepositoryEventListener validatingRepositoryEventListener;

    @PostConstruct
    public void init() {
        // Hack to use Spring Data REST deserialization
        reader = new DomainObjectReader(persistentEntities, resourceMappings);
    }

    /**
     * Secure PATCH requests. Regular users should not change sensitive fields.
     */
    @RequestMapping(path = "/{id}", method = RequestMethod.PATCH)
    public ResponseEntity patch(@PathVariable("id") Long id, @RequestBody ObjectNode body) {
        if (!sec.isAdmin() && !sec.isManager()) {
            if (body.has("active") || body.has("roles")) {
                throw new ForbiddenException();
            }
        }

        User dbUser = repo.findOne(id);
        User mergedUser = reader.merge(body, dbUser, objectMapper);
        validatingRepositoryEventListener.onApplicationEvent(new BeforeSaveEvent(mergedUser));
        return ResponseEntity.ok(repo.save(mergedUser));
    }

    /**
     * Disable PUT requests. Use PATCH instead.
     */
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> put() {
        return ResponseEntity.status(403).build();
    }



}

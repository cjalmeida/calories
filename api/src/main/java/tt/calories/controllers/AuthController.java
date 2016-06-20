package tt.calories.controllers;

import com.auth0.jwt.JWTVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tt.calories.domain.Role;
import tt.calories.domain.User;
import tt.calories.repo.RoleRepository;
import tt.calories.repo.UserRepository;
import tt.calories.security.JWTService;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

/**
 * Authentication controller for login/logout using JWT tokens
 *
 * @author Cloves Almeida
 * @version 1.0.0
 */
@RestController
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JWTService jwtService;

    @Autowired
    EntityManager entityManager;

    @Autowired
    RoleRepository roleRepository;


    @RequestMapping(value = "/api/login", method = {RequestMethod.POST})
    public Map<String, Object> login(@RequestBody LoginBody body, HttpServletResponse response) {
        HashMap<String, Object> out = new HashMap<String, Object>();

        if (body == null || body.username.isEmpty() || body.password.isEmpty()) {
            out.put("status", 401);
            out.put("msg", "Unauthorized");
            response.setStatus(401);
            return out;
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(body.username, body.password);
        authentication = authenticationManager.authenticate(authentication);
        if (authentication == null || !authentication.isAuthenticated()) {
            out.put("status", 401);
            out.put("msg", "Unauthorized");
            response.setStatus(401);
            return out;
        }

        String token = jwtService.createToken(body.username);
        out.put("status", 200);
        out.put("token", token);
        return out;
    }

    @RequestMapping(value = "/api/signup", method = {RequestMethod.POST})
    @Transactional
    public Map<String, Object> signup(@RequestBody User rUser, HttpServletResponse response) {
        HashMap<String, Object> out = new HashMap<String, Object>();

        if (rUser.getEmail() == null || rUser.getPassword() == null || rUser.getFullName() == null) {
            out.put("status", 400);
            out.put("msg", "Bad Request. Fields 'email', 'password' and 'fullName' are required.");
            response.setStatus(400);
            return out;
        }

        // We're picking only a few fields from the request.
        User user = new User();
        user.setEmail(rUser.getEmail());
        user.setFullName(rUser.getFullName());

        // Encrypt password
        user.setPlainPassword(rUser.getPassword());

        // Set user role
        Role userRole = roleRepository.findByName("ROLE_USER");
        user.getRoles().add(userRole);
        user.setActive(true);

        entityManager.persist(user);

        out.put("status", 200);
        out.put("msg", "OK");
        return out;
    }

    private static class LoginBody {
        private String username = "";
        private String password = "";

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}


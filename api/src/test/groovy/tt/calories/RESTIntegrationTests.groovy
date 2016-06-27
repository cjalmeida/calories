/*
 * Copyright (c) 2016. Cloves Almeida. All rights reserved.
 */

package tt.calories

import com.jayway.restassured.specification.RequestSpecification
import groovy.json.JsonBuilder
import groovy.transform.CompileStatic
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.springframework.context.ConfigurableApplicationContext
import tt.calories.controllers.UnauthorizedException
import tt.calories.data.DemoData
import tt.calories.data.SeedData
import tt.calories.repo.RoleRepository

import static com.jayway.restassured.RestAssured.given
import static com.jayway.restassured.http.ContentType.JSON
import static org.hamcrest.Matchers.*
import static org.junit.Assert.*

/**
 * Class holding all REST based integration tests.
 *
 * @author Cloves J. G. Almeida <cjalmeida@gmail.com>
 * @version 1.0.0
 */
@RunWith(JUnit4.class)
@CompileStatic
public class RESTIntegrationTests {
    private static ConfigurableApplicationContext ctx;
    private String adminUser = SeedData.ADMIN_DEFAULT_EMAIL;
    private String adminPass = SeedData.ADMIN_DEFAULT_PASSWORD;

    Map<String, String> tokens = [:];
    Map<String, Object> u3json;

    RequestSpecification adminSpec;
    RequestSpecification managerSpec;

    @Before
    public void reloadData() {
        ctx = TestServer.start();
        fixtures();
    }

    private void fixtures() {
        def roleRepo = ctx.getBean(RoleRepository.class);
        def userRole = roleRepo.findByName("ROLE_USER");

        u3json = [
                fullName      : 'Dummy3',
                email         : 'dummy3@example.com',
                plainPassword : DemoData.DUMMY_PASS,
                caloriesPerDay: 2000,
                active        : true,
                roles         : ["/roles/${userRole.id}"],
                userTimeZone  : 'UTC'
        ];

        // We'll use admin and manager on this test.
        adminSpec = login(adminUser, adminPass)
        managerSpec = login(DemoData.m1.email, DemoData.DUMMY_PASS);
    }

    RequestSpecification givenAdmin() {
        return given().spec(adminSpec)
    };

    RequestSpecification givenManager() {
        return given().spec(managerSpec)
    };


    @After
    public void stopServer() {
        ctx.close();
    }

    private String json(Object value) {
        return new JsonBuilder(value).toPrettyString();
    }

    private RequestSpecification givenBase() {
        return given().port(Integer.valueOf(ctx.getEnvironment().getProperty("server.port", "9000")));
    }

    /**
     * Helper method to get Spring environment value
     */
    public String env(String key) {
        return env(key, null);
    }

    /**
     * Helper method to get Spring environment value
     */
    public String env(String key, String defaultValue) {
        return ctx.getEnvironment().getProperty(key, defaultValue);
    }

    public RequestSpecification login(String username, String password) {
        def res = givenBase()
                .body(json([username: username, password: password]))
                .contentType(JSON)
            .post('/api/login');
        if (res.statusCode() == 200) {
            def token = res.jsonPath().get("token");
            return givenBase().header('Authentication', "Bearer $token");
        } else if (res.statusCode == 401) {
            throw new UnauthorizedException();
        } else {
            throw new Exception("Unknown response code: ${res.statusCode}");
        }

    }

    /**
     * Test 401 errors and that we're able to login with admin and test machines
     * using Form login (cookies!)
     */
    @Test
    public void testLogin() {
        // Check we need authorization
        givenBase()
                .when()
                .get("/api/meals")
                .then().assertThat()
                .statusCode(401);

        // Login with wrong credentials
        try {
            login(adminUser, "wrongvalue")
            fail("Should throw unauthorized exception.");
        } catch (UnauthorizedException ignored) {

        }

        // Login with admin and get data
        login(adminUser, adminPass)
                .when()
                .get("/api/meals")
                .then().assertThat()
                .statusCode(200)
                .body("page.totalElements", greaterThan(2));

        // Login with manager and get other users data
        login(DemoData.m1.email, DemoData.DUMMY_PASS)
                .when()
                .get("/api/users/")
                .then().assertThat()
                .statusCode(200)
                .body("page.totalElements", greaterThan(2));

        // Login with dummy1 and fail to list users
        login(DemoData.u1.email, DemoData.DUMMY_PASS)
                .when()
                .get("/api/users/")
                .then().assertThat()
                .statusCode(403)

        // Login with dummy1 and get self
        login(DemoData.u1.email, DemoData.DUMMY_PASS)
                .when()
                .get("/api/users/${DemoData.u1.id}")
                .then().assertThat()
                .statusCode(200)
                .body("fullName", is(DemoData.u1.fullName));

        // Login with dummy1 and fail to get other machine data
        login(DemoData.u1.email, DemoData.DUMMY_PASS)
                .when()
                .get("/api/users/${DemoData.u2.id}")
                .then().assertThat()
                .statusCode(403);

    }

    /**
     * Test operations on the /users resource
     */
    @Test
    public void testUsers() throws Exception {

        // Test list users for admin and manager
        [givenAdmin(), givenManager()].each {
            it.when()
                .get("/api/users")
                .then().assertThat()
                    .statusCode(200)
                    .body("page.totalElements", greaterThan(2));
        }

        // Test create user with missing data. It should
        // output error with proper validation info.
        u3json['fullName'] = null;
        String u3body = json(u3json);
        givenAdmin()
                .body(u3body)
                .contentType("application/json")
            .post("/api/users/")
            .then().assertThat()
                .statusCode(400)
                .body("errors[0].entity", is("User"))
                .body("errors[0].property", is("fullName"));

        // Fix error and try again
        u3json['fullName'] = 'Dummy 3';
        u3body = json(u3json);
        Long u3Id = givenManager()
                .body(u3body)
                .contentType("application/json")
            .post("/api/users")
            .then().assertThat()
                .statusCode(isOneOf(200, 201))
            .extract()
                .jsonPath()
                .getString("_links.self.href").split("/").last().toLong();

        // Test update value via PATCH
        def caloriesData = json([caloriesPerDay: 1500])
        givenAdmin()
                .body(caloriesData)
                .contentType("application/json")
            .patch("/api/users/{id}", u3Id)
            .then().assertThat()
                .statusCode(isOneOf(200, 204));

        // Check update
        givenAdmin()
            .get("/api/users/$u3Id")
            .then().assertThat()
                .statusCode(200)
                .body("caloriesPerDay", is(1500));

        // Removal
        givenManager()
            .delete("/api/users/$u3Id")
            .then().assertThat()
                .statusCode(204);

        // Check removal
        givenAdmin()
            .get("/api/machines/$u3Id")
            .then().assertThat()
                .statusCode(404);

        // Test other users cannot create users
        def u1spec = login(DemoData.u1.email, DemoData.DUMMY_PASS);
        given().spec(u1spec)
                .body(u3body)
                .contentType("application/json")
            .post("/api/users")
            .then().assertThat()
                .statusCode(403);

        // Cannot update other machine
        given().spec(u1spec)
                .body(caloriesData)
                .contentType("application/json")
            .patch('/api/users/{id}', DemoData.u2.id)
            .then().assertThat()
                .statusCode(403);

        // But can update self
        given().spec(u1spec)
                .body(caloriesData)
                .contentType("application/json")
            .patch("/api/users/${DemoData.u1.id}")
            .then().assertThat()
                .statusCode(200);

        // Check updated
        given().spec(u1spec)
            .get("/api/users/${DemoData.u1.id}")
            .then().assertThat()
                .statusCode(200)
                .body("caloriesPerDay", is(1500));

        // Cannot delete self
        given().spec(u1spec)
            .delete("/api/users/${DemoData.u1.id}")
            .then().assertThat()
                .statusCode(403);

        // Cannot update sensitive data
        def activePatch = json([active: false]);
        def rolesPatch = json([roles: ['/roles/1']]);
        [activePatch, rolesPatch].each { sensitivePatch ->
            given().spec(u1spec)
                    .body(sensitivePatch)
                    .contentType("application/json")
                .patch("/api/users/{id}", DemoData.u1.id)
                .then().assertThat()
                    .statusCode(403);
        }
    }

    @Test
    public void testMeals() {

        // Test admin can list meals from all users
        def res = givenAdmin().get("/api/meals?projection=list&size=1000")
        assertEquals(200, res.statusCode);
        assertThat(res.jsonPath().getList('_embedded.meals').size(), greaterThan(2));
        assertThat(res.jsonPath().getList('_embedded.meals').groupBy { it['user'] }.size(), greaterThan(1));

        def meal = [
                user       : "/users/${DemoData.u1.id}",
                mealTime   : "13:00:00",
                mealDate   : "2016-01-01",
                calories   : 2500,
                description: 'Big lunch'
        ];

        // Test admin can create meals
        def mealId = givenAdmin()
                .body(json(meal))
                .contentType(JSON)
            .post('/api/meals')
            .then().assertThat()
                .statusCode(201)
            .extract()
                .jsonPath().getString("_links.self.href").split("/").last().toLong();

        //Test admin can update meals
        def mealPatch = [calories: 1000, description: 'Small lunch']
        givenAdmin()
                .body(json(mealPatch))
                .contentType(JSON)
            .patch("/api/meals/$mealId")
            .then().assertThat()
                .statusCode(200);

        // Test admin can delete meals
        givenAdmin()
            .delete("/api/meals/$mealId")
            .then().assertThat()
                .statusCode(204);

        def u1spec = login(DemoData.u1.email, DemoData.DUMMY_PASS);
        def givenUser = { given().spec(u1spec) };
        def u2spec = login(DemoData.u2.email, DemoData.DUMMY_PASS);
        def givenAnotherUser = { given().spec(u2spec) };

        // Test user list meals and only it's meals
        res = givenUser()
                .get('/api/meals?projection=list')
        assertEquals(200, res.statusCode);

        res.jsonPath().getList('_embedded.meals').each {
            assertEquals(it['user'], DemoData.u1.fullName);
        };

        // Test user can create meals
        mealId = givenUser()
                .body(json(meal))
                .contentType(JSON)
            .post('/api/meals/')
            .then().assertThat()
                .statusCode(201)
            .extract()
                .jsonPath()
                .getString("_links.self.href").split("/").last().toLong();

        // Test user can update meals
        givenUser()
                .body(json(mealPatch))
                .contentType(JSON)
            .patch("/api/meals/$mealId")
            .then().assertThat()
                .statusCode(200)

        // Test another user can't delete meal
        givenAnotherUser()
            .delete("/api/meals/$mealId")
            .then().assertThat()
                .statusCode(403)

        // Test user can delete meals
        givenUser()
            .delete("/api/meals/$mealId")
            .then().assertThat()
                .statusCode(204)

    }

    @Test
    public void testSignup() {
        // Test we can signup a new user
        def u = [
                fullName: 'From Signup Test',
                email: 'signup@auto.com',
                password: 'signup'
        ]
        givenBase()
                .body(json(u))
                .contentType(JSON)
            .post('/api/signup')
            .then().assertThat()
                .statusCode(200);


        // Check login
        login(u.email, u.password)
            .get('/api/users/search/self')
            .then().assertThat()
                .statusCode(200)
                .body('fullName', is(u.fullName))
    }

    @Test
    public void testMealSearch() {
        def u1spec = login(DemoData.u1.email, DemoData.DUMMY_PASS);
        def givenUser = { given().spec(u1spec) };

        // Get meal count
        Integer count = givenUser().get("/api/meals").body().jsonPath().getInt("page.totalElements");

        def sizeBetween = {Comparable a, Comparable b -> allOf(hasSize(greaterThanOrEqualTo(a)), hasSize(lessThanOrEqualTo(b)))}

        // Test filter by date
        givenUser()
                .queryParam("dateStart", DemoData.MEAL_DATE_U1_0)
                .queryParam("dateEnd", DemoData.MEAL_DATE_U1_0)
            .get("/api/meals")
            .then().assertThat()
                .statusCode(200)
                .body("_embedded.meals", sizeBetween(1, count-1))

        // Test filter by time
        givenUser()
                .queryParam("timeStart", DemoData.MEAL_TIME_0)
                .queryParam("timeEnd", DemoData.MEAL_TIME_0)
            .get("/api/meals")
            .then().assertThat()
                .statusCode(200)
                .body("_embedded.meals", sizeBetween(1, count-1))

        // Test filter by date and time
        givenUser()
                .queryParam("dateStart", DemoData.MEAL_DATE_U1_0)
                .queryParam("dateEnd", DemoData.MEAL_DATE_U1_0)
                .queryParam("timeStart", DemoData.MEAL_TIME_0)
                .queryParam("timeEnd", DemoData.MEAL_TIME_0)
            .get("/api/meals")
            .then().assertThat()
                .statusCode(200)
                .body("_embedded.meals", sizeBetween(1, count-1))
    }

}

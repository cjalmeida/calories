/*
 * Copyright (c) 2016. Cloves Almeida. All rights reserved.
 */

package tt.calories;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Class holding all REST based integration tests.
 *
 * @author Cloves J. G. Almeida <cjalmeida@gmail.com>
 * @version 1.0.0
 */
@RunWith(JUnit4.class)
public class RESTIntegrationTests {
//    private static ConfigurableApplicationContext ctx;
//    private FormAuthConfig loginForm;
//    private String adminUser;
//    private String adminPass;
//
//
//    @BeforeClass
//    public static void startServer() {
//        ctx = TestServer.start();
//    }
//
//    @Before
//    public void reloadData() {
//        ctx.getBean(DataLoader.class).loadSchema();
//        ctx.getBean(DataLoader.class).loadTestData();
//        loginForm = new FormAuthConfig("/api/login", "username", "password");
//        adminUser = "admin";
//        adminPass = "admin";
//        //fixtures();
//    }
//
//    private RequestSpecification givenBase() {
//        return given().port(Integer.valueOf(ctx.getEnvironment().getProperty("server.port", "9000")));
//    }
//
//
//    /**
//     * Helper method to get Spring environment value
//     */
//    public String env(String key){
//        return env(key, null);
//    }
//
//    /**
//     * Helper method to get Spring environment value
//     */
//    public String env(String key, String defaultValue) {
//        return ctx.getEnvironment().getProperty(key, defaultValue);
//    }
//
//    public RequestSpecification login(String username, String password) {
//        return givenBase().auth().form(username, password, loginForm);
//    }
//
//    /**
//     * Test 401 errors and that we're able to login with admin and test machines
//     * using Form login (cookies!)
//     */
//    @Test()
//    public void testFormLogin() {
//        // Check we need authorization
//        givenBase()
//            .when()
//                .get("/api/machines")
//            .then().assertThat()
//                .statusCode(401);
//
//        // Login with wrong credentials
//        login("wrongvalue", "wrongvalue")
//            .when()
//                .get("/api/machines")
//            .then().assertThat()
//                .statusCode(401);
//
//        // Login with admin and get machines
//        login(env("security.admin.username"), env("security.admin.password"))
//            .when()
//                .get("/api/machines")
//            .then().assertThat()
//                .statusCode(200)
//                .body("[0].name", not(isEmptyOrNullString()));
//
//        // Login with machine1 and get self
//        login("machine1", "testpass")
//            .when()
//                .get("/api/machines/1")
//            .then().assertThat()
//                .statusCode(200)
//                .body("name", is("machine1"))
//                .body("ipv4", is("192.168.0.21"));
//
//        // Login with machine1 and fail to get other machine data
//        login("machine1", "testpass")
//            .when()
//                .get("/api/machines/2")
//            .then().assertThat()
//                .statusCode(403);
//
//        // Login with machine2;
//        login("machine2", "testpass")
//            .when()
//                .get("/api/machines/2")
//            .then().assertThat()
//                .statusCode(200);
//
//    }
//
//
//    /**
//     * Test 401 errors and that we're able to login with admin and test machines
//     * using preemptive Basic Authorization
//     */
//    @Test
//    public void testBasicLogin() {
//        // Check we need authorization
//        givenBase()
//            .when()
//                .get("/api/machines")
//            .then().assertThat()
//                .statusCode(401);
//
//        // Login with wrong credentials
//        givenBase()
//            .auth()
//                .preemptive().basic("wrongvalue", "wrongvalue")
//            .when()
//                .get("/api/machines")
//            .then().assertThat()
//                .statusCode(401);
//
//        // Basic login with admin credentials
//        givenBase()
//            .auth()
//                .preemptive().basic(adminUser, adminPass)
//            .when()
//                .get("/api/machines")
//            .then().assertThat()
//                .statusCode(200);
//
//        // Basic login with machine credentials
//        givenBase()
//            .auth()
//                .preemptive().basic("machine1", "testpass")
//            .when()
//                .get("/api/machines")
//            .then().assertThat()
//                .statusCode(200);
//
//        // We can get current id from basic auth
//        givenBase()
//            .auth()
//                .preemptive().basic("machine1", "testpass")
//            .when()
//                .get("/api/machines/current-id")
//            .then().assertThat()
//                .body(isOneOf("1", 1));
//    }
//
//    /**
//     * Test operations on the /machines resource
//     */
//    @Test
//    public void testMachines() throws Exception {
//        ObjectMapper json = json().build();
//
//        // Test list machines
//        login(adminUser, adminPass)
//            .when()
//                .get("/api/machines")
//            .then().assertThat()
//                .statusCode(200)
//                .body("[0].name", not(isEmptyOrNullString()));
//
//        // Test create machine with missing data. It should
//        // output error with proper validation info.
//        machine3.setName(null);
//        String data = json.writeValueAsString(machine3);
//        login(adminUser, adminPass)
//                .body(data)
//                .contentType("application/json")
//            .when()
//                .post("/api/machines")
//            .then().assertThat()
//                .statusCode(400)
//                .body("errors[0].objectName", is("machine"))
//                .body("errors[0].field", is("name"));
//
//        // Fix error and try again
//        machine3.setName("machine3");
//        data = json.writeValueAsString(machine3);
//        Long id3 = Long.valueOf(
//            login(adminUser, adminPass)
//                .body(data)
//                .contentType("application/json")
//            .when()
//                .post("/api/machines")
//            .then().assertThat()
//                .statusCode(200)
//            .extract().body().asString());
//        machine3.setId(id3);
//
//        // Test update value via PUT
//        machine3.setSourceFolder("D:\\test");
//        login(adminUser, adminPass)
//                .body(machine3)
//                .contentType("application/json")
//            .when()
//                .put("/api/machines/" + id3)
//            .then().assertThat()
//                .statusCode(isOneOf(200, 204));
//
//        // Check update
//        login(adminUser, adminPass)
//            .when()
//                .get("/api/machines/" + id3)
//            .then().assertThat()
//                .statusCode(200)
//                .body("sourceFolder", is("D:\\test"));
//
//        // Removal
//        login(adminUser, adminPass)
//            .when()
//                .delete("/api/machines/" + id3)
//            .then().assertThat()
//                .statusCode(200);
//
//        // Check removal
//        login(adminUser, adminPass)
//            .when()
//                .get("/api/machines/" + id3)
//            .then().assertThat()
//                .statusCode(404);
//
//        // Test other machines cannot create machine data
//        RequestSpecification m1spec = login("machine1", "testpass");
//        given().spec(m1spec)
//            .body(data).contentType("application/json")
//            .post("/api/machines")
//            .then().statusCode(403);
//
//        // Cannot update other machine
//        given().spec(m1spec)
//            .body(machine3).contentType("application/json")
//            .put("/api/machines/2")
//            .then().statusCode(403);
//
//        // Nor can update self
//        given().spec(m1spec)
//            .body(machine3).contentType("application/json")
//            .put("/api/machines/1")
//            .then().statusCode(403);
//
//        // Cannot delete even self
//        given().spec(m1spec)
//            .delete("/api/machines/1")
//            .then().statusCode(isOneOf(403, 401));
//
//        // Can update IP via special endpoint
//        given().spec(m1spec)
//            .post("/api/machines/1/update-ip")
//            .then().statusCode(200);
//
//        // Check updated
//        given().spec(m1spec)
//                .get("/api/machines/1")
//            .then()
//                .statusCode(200)
//            .body("ipv4", is("127.0.0.1"));
//    }
//
//    @Test
//    public void testBackups() {
//        RequestSpecification adminSpec = login(adminUser, adminPass).contentType(ContentType.JSON);
//        RequestSpecification m1Spec = login("machine1", "testpass").contentType(ContentType.JSON);
//        JsonNodeFactory json = JsonNodeFactory.instance;
//
//        // Test admin list backups
//        given().spec(adminSpec)
//                .get("/api/backups")
//            .then()
//                .statusCode(200)
//                .body("[0].state", not(isEmptyOrNullString()));
//
//        // Test admin list backup by machine
//        ObjectNode filters = json.objectNode();
//        filters.put("machineId", 1);
//        given().spec(adminSpec)
//                .queryParam("_filters", filters)
//                .get("/api/backups")
//            .then()
//                .statusCode(200);
//
//        // Test machine create backup
//        ObjectNode m1bak = json.objectNode();
//        m1bak.put("machine", "1");
//        m1bak.put("state", BackupState.STARTED.name());
//        m1bak.put("sourceFolder", "c:\\test");
//        m1bak.put("destinationFolder", "\\\\test\\bak");
//        m1bak.put("startedAt", Instant.now().toString());
//        Long bakId = Long.valueOf(
//            given().spec(m1Spec)
//                .body(m1bak)
//                .post("/api/backups")
//            .then()
//                .statusCode(200)
//            .extract().body().asString());
//
//        // Test machine update backup
//        m1bak.put("state", BackupState.FINISHED.name());
//        m1bak.put("finishedAt", Instant.now().plusSeconds(60).toString());
//        given().spec(m1Spec)
//                .body(m1bak)
//                .put("/api/backups/" + bakId)
//            .then()
//                .statusCode(isOneOf(200,204));
//
//        // Check it's updated
//        given().spec(m1Spec)
//                .get("/api/backups/" + bakId)
//            .then()
//                .statusCode(200)
//                .body("state", is(BackupState.FINISHED.name()));
//
//    }
//
//    @Test
//    public void testLogs() {
//        RequestSpecification adminSpec = login(adminUser, adminPass).contentType(ContentType.JSON);
//        RequestSpecification m1Spec = login("machine1", "testpass").contentType(ContentType.JSON);
//        RequestSpecification m2Spec = login("machine2", "testpass").contentType(ContentType.JSON);
//        JsonNodeFactory json = JsonNodeFactory.instance;
//
//        // Test machine2 create new logs
//        ObjectNode data = json.objectNode();
//        data.put("objectId", 3); // We know backup@3 is owned by machine@2
//        data.put("objectClass", "Backup");
//        data.put("level", "INFO");
//        data.put("data", "Just connected.");
//        data.put("timestamp", "2016-05-05T12:15:00Z");
//        String m2log1 =
//            given().spec(m2Spec)
//                .body(data)
//                .post("/api/logs")
//            .then()
//                .statusCode(200)
//            .extract()
//                .header("Location");
//
//        // Fail when machine 2 create a new log for a backup it does not own
//        data.put("objectId", 1);
//        given().spec(m2Spec)
//                .body(data)
//                .post("/api/logs")
//            .then()
//                .statusCode(403);
//
//        // Test machine 2 can get logs of a given backup it owns
//        ObjectNode filter = json.objectNode();
//        filter.put("id", 3);
//        given().spec(m2Spec)
//                .queryParam("_filters", filter)
//                .get("/api/logs")
//            .then()
//                .statusCode(200)
//                .body("[0].objectId", not(isEmptyOrNullString()));
//
//
//        // Test admin can read logs by machine
//        filter = json.objectNode();
//        filter.put("machineId", 1);
//        given().spec(adminSpec)
//                .queryParam("_filters", filter)
//                .get("/api/logs")
//            .then()
//                .statusCode(200)
//                .body("[0].objectId", not(isEmptyOrNullString()));
//    }
}



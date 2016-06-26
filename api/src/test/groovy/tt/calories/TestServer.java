/*
 * Copyright (c) 2016. Cloves Almeida. All rights reserved.
 */

package tt.calories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Manage a test server with fixture data and in-memory database
 *
 * @author Cloves J. G. Almeida <cjalmeida@gmail.com>
 * @version 1.0.0
 */
public class TestServer {

    public static ConfigurableApplicationContext start(String ...args) {
        List<String> myargs = new ArrayList<>();
        myargs.add("--application.loadDemoData=true");
        myargs.add("--spring.datasource.url=jdbc:h2:mem:test;DB_CLOSE_ON_EXIT=FALSE");
        myargs.add("--spring.datasource.username=sa");
        myargs.add("--spring.datasource.password=''");
        myargs.add("--spring.datasource.driver-class-name=org.h2.Driver");
        myargs.add("--spring.jpa.properties.hibernate.hbm2ddl.auto=create-drop");
        myargs.addAll(Arrays.asList(args));
        Application.main(myargs.toArray(new String[0]));
        return Application.context;
    }


}

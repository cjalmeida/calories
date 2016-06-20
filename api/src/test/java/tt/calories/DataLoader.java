/*
 * Copyright (c) 2016. Cloves Almeida. All rights reserved.
 */

package tt.calories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.File;

/**
 * Class to load test data fixtures.
 *
 * @author Cloves J. G. Almeida <cjalmeida@gmail.com>
 * @version 1.0.0
 */
@SpringBootApplication
public class DataLoader {

    @Autowired
    EntityManager entityManager;

    @Transactional
    public void loadTestData() {
        entityManager.createNativeQuery("RUNSCRIPT FROM 'classpath:/testdata.sql'").executeUpdate();
    }

    @Transactional
    public void loadSchema() {
        String schema = new File("schema/schema.sql").getAbsolutePath();
        entityManager.createNativeQuery("RUNSCRIPT FROM '" + schema + "'").executeUpdate();
    }

    public static void main(String[] args) {
        try (ConfigurableApplicationContext ctx = SpringApplication.run(DataLoader.class, args)) {
            DataLoader loader = ctx.getBean(DataLoader.class);
            if (args[0].toLowerCase().equals("testdata")) {
                loader.loadSchema();
                loader.loadTestData();
            } else if (args[0].toLowerCase().equals("schema")) {
                loader.loadSchema();
            } else {
                throw new RuntimeException("Unknown command " + args[0]);
            }
        }
    }
}

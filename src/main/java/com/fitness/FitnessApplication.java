package com.fitness;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FitnessApplication {

    public static void main(String[] args) {
        String dbUrl = System.getenv("SPRING_DATASOURCE_URL");
        if (dbUrl == null) dbUrl = System.getenv("JDBC_DATABASE_URL");
        if (dbUrl == null) {
            String host = System.getenv("PGHOST");
            String port = System.getenv("PGPORT");
            String db = System.getenv("PGDATABASE");
            if (host != null) {
                dbUrl = "jdbc:postgresql://" + host + ":" + (port != null ? port : "5432") + "/" + db;
            }
        }
        
        System.out.println("[DEBUG_LOG] Iniciando aplicação...");
        if (dbUrl != null) {
            System.out.println("[DEBUG_LOG] Tentando conectar ao banco: " + dbUrl.split("\\?")[0]);
        } else {
            System.out.println("[DEBUG_LOG] Nenhuma variável de banco detectada, usando padrão de application.properties");
        }
        
        SpringApplication.run(FitnessApplication.class, args);
    }

}

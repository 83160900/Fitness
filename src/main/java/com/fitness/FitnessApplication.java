package com.fitness;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@EnableAsync
@EnableScheduling
@SpringBootApplication
public class FitnessApplication {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        String dbUrl = System.getenv("SPRING_DATASOURCE_URL");
        if (dbUrl == null) dbUrl = System.getenv("JDBC_DATABASE_URL");
        if (dbUrl == null) {
            String host = System.getenv("PGHOST") != null ? System.getenv("PGHOST") : System.getenv("POSTGRES_HOST");
            String port = System.getenv("PGPORT") != null ? System.getenv("PGPORT") : System.getenv("POSTGRES_PORT");
            String db = System.getenv("PGDATABASE") != null ? System.getenv("PGDATABASE") : System.getenv("POSTGRES_DB");
            if (host != null) {
                host = host.trim();
                port = (port != null) ? port.trim() : "5432";
                db = (db != null) ? db.trim() : "fitnessdb";
                dbUrl = "jdbc:postgresql://" + host + ":" + port + "/" + db;
            }
        }
        
        String user = System.getenv("SPRING_DATASOURCE_USERNAME");
        if (user == null) user = System.getenv("PGUSER");
        if (user == null) user = System.getenv("POSTGRES_USER");
        if (user != null) user = user.trim();
        
        String pass = System.getenv("SPRING_DATASOURCE_PASSWORD");
        if (pass == null) pass = System.getenv("PGPASSWORD");
        if (pass == null) pass = System.getenv("POSTGRES_PASSWORD");
        if (pass != null) pass = pass.trim();
        
        System.out.println("[DEBUG_LOG] Iniciando aplicacao...");
        if (dbUrl != null) {
            String sanitizedUrl = dbUrl.split("\\?")[0];
            System.out.println("[DEBUG_LOG] === CONFIGURACAO DE BANCO DETECTADA ===");
            System.out.println("[DEBUG_LOG] URL: " + sanitizedUrl);
            if (user != null) {
                System.out.println("[DEBUG_LOG] Usuario: '" + user + "'");
            }
            System.out.println("[DEBUG_LOG] =======================================");
        } else {
            System.out.println("[DEBUG_LOG] Nenhuma variavel de banco detectada, usando padrao de localhost.");
        }
        
        SpringApplication.run(FitnessApplication.class, args);
    }

}
// Test commit to verify configuration
// Test commit to verify configuration (fixed)


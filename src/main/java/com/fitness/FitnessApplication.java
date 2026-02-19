package com.fitness;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
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

    @Bean
    public CommandLineRunner initDatabase(JdbcTemplate jdbcTemplate) {
        return args -> {
            System.out.println("[DEBUG_LOG] === VERIFICANDO TABELAS NO BANCO ===");
            try {
                jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS public.users (" +
                        "id UUID PRIMARY KEY, email VARCHAR(255) NOT NULL UNIQUE, " +
                        "password VARCHAR(255) NOT NULL, name VARCHAR(255), role VARCHAR(255), " +
                        "specialty VARCHAR(255), registration_number VARCHAR(255), " +
                        "formation TEXT, experience TEXT, photo_url VARCHAR(1024), " +
                        "lgpd_consent BOOLEAN NOT NULL DEFAULT FALSE, active BOOLEAN NOT NULL DEFAULT TRUE)");
                
                jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS public.exercises (" +
                        "id UUID PRIMARY KEY, external_id VARCHAR(255), name VARCHAR(255) NOT NULL, " +
                        "primary_muscles VARCHAR(255), equipment VARCHAR(255), " +
                        "image_url TEXT, video_url TEXT, last_synced_at TIMESTAMP, " +
                        "updated_at TIMESTAMP, source VARCHAR(255) DEFAULT 'ascendapi')");

                jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS public.user_professionals (" +
                        "student_id UUID NOT NULL REFERENCES public.users(id), " +
                        "professional_id UUID NOT NULL REFERENCES public.users(id), " +
                        "PRIMARY KEY (student_id, professional_id))");
                
                System.out.println("[DEBUG_LOG] Tabelas verificadas/criadas com sucesso via JdbcTemplate.");
            } catch (Exception e) {
                System.err.println("[DEBUG_LOG] Erro ao criar tabelas manualmente: " + e.getMessage());
            }
        };
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


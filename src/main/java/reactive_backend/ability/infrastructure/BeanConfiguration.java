package reactive_backend.ability.infrastructure;

import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactive_backend.ability.application.client.adapter.TechnologyClient;
import reactive_backend.ability.application.jpa.adapter.AbilityJpaAdapter;
import reactive_backend.ability.application.jpa.adapter.BootcampJpaAdapter;
import reactive_backend.ability.application.jpa.mapper.IAbilityEntityMapper;
import reactive_backend.ability.application.jpa.mapper.IBootcampEntityMapper;
import reactive_backend.ability.application.jpa.repository.IAbilityRepository;
import reactive_backend.ability.application.jpa.repository.IBootcampRepository;
import reactive_backend.ability.domain.api.IAbilityServicePort;
import reactive_backend.ability.domain.api.IBootcampServicePort;
import reactive_backend.ability.domain.spi.IAbilityPersistencePort;
import reactive_backend.ability.domain.spi.IBootcampPersistencePort;
import reactive_backend.ability.domain.spi.ITechnologyClientPort;
import reactive_backend.ability.domain.usecase.AbilityCase;
import reactive_backend.ability.domain.usecase.BootcampCase;

@Configuration
@AllArgsConstructor
public class BeanConfiguration {
    private final WebClient.Builder webClientBuilder;
    private final IAbilityEntityMapper abilityEntityMapper;
    private final IAbilityRepository abilityRepository;
    private final IBootcampRepository bootcampRepository;
    private final IBootcampEntityMapper bootcampEntityMapper;

    @Bean
    public IAbilityServicePort abilityServicePort() {
        return new AbilityCase(abilityPersistencePort(), technologyClientPort());
    }

    @Bean
    public ITechnologyClientPort technologyClientPort() {
        return new TechnologyClient(webClientBuilder);
    }

    @Bean
    public IAbilityPersistencePort abilityPersistencePort() {
        return new AbilityJpaAdapter(abilityRepository, abilityEntityMapper);
    }

    @Bean
    public IBootcampServicePort bootcampServicePort() {
        return new BootcampCase(bootcampPersistencePort(),abilityPersistencePort());
    }

    @Bean
    public IBootcampPersistencePort bootcampPersistencePort() {
        return new BootcampJpaAdapter(bootcampRepository, bootcampEntityMapper);
    }

    @Bean
    public ApplicationRunner initializer(DatabaseClient client) {
        return args -> {
            client.sql("""
                    
                                CREATE TABLE IF NOT EXISTS ability_entity (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        description TEXT,
                        CONSTRAINT uk_technology_name UNIQUE (name)
                    )
                    """).fetch().rowsUpdated().subscribe();

            client.sql("""
                        CREATE TABLE IF NOT EXISTS bootcamp_abilities (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            bootcamp_id INTEGER NOT NULL,
                            ability_id INTEGER NOT NULL
                        );
                    """).fetch().rowsUpdated().subscribe();
        };
    }
}


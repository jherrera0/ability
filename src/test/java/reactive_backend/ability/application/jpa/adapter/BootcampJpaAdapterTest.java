package reactive_backend.ability.application.jpa.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactive_backend.ability.application.jpa.entity.BootcampEntity;
import reactive_backend.ability.application.jpa.mapper.IBootcampEntityMapper;
import reactive_backend.ability.application.jpa.repository.IBootcampRepository;
import reactive_backend.ability.domain.model.Bootcamp;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class BootcampJpaAdapterTest {

    @Mock
    private IBootcampRepository bootcampRepository;

    @Mock
    private IBootcampEntityMapper bootcampEntityMapper;

    private BootcampJpaAdapter bootcampJpaAdapter;

    AutoCloseable closeable;
    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        bootcampJpaAdapter = new BootcampJpaAdapter(bootcampRepository, bootcampEntityMapper);
    }

    @Test
    void addBootcampShouldHandleEmptyAbilityIds() {
        StepVerifier.create(bootcampJpaAdapter.addBootcamp(1, List.of()))
                .verifyComplete();
    }

    @Test
    void getAllAbilitiesByBootcampIdShouldHandleNonExistentBootcampId() {
        when(bootcampRepository.findAllByBootcampId(999))
                .thenReturn(Flux.empty());

        StepVerifier.create(bootcampJpaAdapter.getAllAbilitiesByBootcampId(999))
                .expectNext(List.of())
                .verifyComplete();
    }

    @Test
    void addBootcampShouldSaveAllAbilitiesForMultipleBootcamps() {
        List<Integer> abilityIds = List.of(1, 2, 3);
        BootcampEntity bootcampEntity = new BootcampEntity();
        bootcampEntity.setBootcampId(2);
        bootcampEntity.setAbilityId(1);
        when(bootcampRepository.save(any(BootcampEntity.class)))
                .thenReturn(Mono.just(bootcampEntity));
        when(bootcampEntityMapper.toDomain(any(BootcampEntity.class)))
                .thenReturn(new Bootcamp(2, 1, 1));

        StepVerifier.create(bootcampJpaAdapter.addBootcamp(2, abilityIds))
                .expectNextCount(3)
                .verifyComplete();
    }
}
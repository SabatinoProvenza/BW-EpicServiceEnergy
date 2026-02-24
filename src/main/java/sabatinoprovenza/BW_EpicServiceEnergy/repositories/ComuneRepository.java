package sabatinoprovenza.BW_EpicServiceEnergy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sabatinoprovenza.BW_EpicServiceEnergy.entities.Comune;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ComuneRepository extends JpaRepository<Comune, UUID> {

    Optional<Comune> findByNome(String nome);

}

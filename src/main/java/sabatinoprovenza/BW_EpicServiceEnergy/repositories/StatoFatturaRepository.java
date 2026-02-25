package sabatinoprovenza.BW_EpicServiceEnergy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sabatinoprovenza.BW_EpicServiceEnergy.entities.StatoFattura;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StatoFatturaRepository extends JpaRepository<StatoFattura, UUID> {
	Optional<StatoFattura> findByNomeStato(String nomeStato);
}

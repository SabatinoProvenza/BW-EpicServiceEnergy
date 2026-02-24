package sabatinoprovenza.BW_EpicServiceEnergy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sabatinoprovenza.BW_EpicServiceEnergy.entities.Fattura;

import java.util.UUID;

@Repository
public interface FatturaRepository extends JpaRepository<Fattura, UUID> {
}

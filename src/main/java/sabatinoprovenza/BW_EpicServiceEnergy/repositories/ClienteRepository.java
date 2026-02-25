package sabatinoprovenza.BW_EpicServiceEnergy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import sabatinoprovenza.BW_EpicServiceEnergy.entities.Cliente;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, UUID>, JpaSpecificationExecutor<Cliente> {

    Optional<Cliente> findByEmail(String email);

    Optional<Cliente> findByPartitaIva(String partitaIva);

    Optional<Cliente> findByPec(String pec);

    Optional<Cliente> findByTelefono(String telefono);

}

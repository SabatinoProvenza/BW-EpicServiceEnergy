package sabatinoprovenza.BW_EpicServiceEnergy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sabatinoprovenza.BW_EpicServiceEnergy.entities.Utente;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, UUID> {

    Optional<Utente> findByEmail(String email);

    Optional<Utente> findByNomeAndCognome(String nome, String cognome);

    Optional<Utente> findByUsername(String username);

}

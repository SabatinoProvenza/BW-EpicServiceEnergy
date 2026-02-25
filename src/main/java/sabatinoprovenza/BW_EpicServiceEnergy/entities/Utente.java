package sabatinoprovenza.BW_EpicServiceEnergy.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "utenti")
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"password", "authorities", "createdEvents", "bookings", "accountNonExpired", "accountNonLocked", "credentialsNonExpired", "enabled"})
public class Utente implements UserDetails {
    @Id
    @GeneratedValue
    private UUID id;
    private String nome;
    private String cognome;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    private String avatar;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "utenti_ruoli",
            joinColumns = @JoinColumn(name = "utente_id"),
            inverseJoinColumns = @JoinColumn(name = "ruolo_id")
    )
    private Set<Ruolo> ruoli = new HashSet<>();

    public Utente(String username, String email, String password, String nome, String cognome) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.nome = nome;
        this.cognome = cognome;
        this.avatar = "https://ui-avatars.com/api/?name=" + nome + "+" + cognome;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.ruoli.stream().map(role -> new SimpleGrantedAuthority(role.getNomeRuolo())).toList();
    }
}

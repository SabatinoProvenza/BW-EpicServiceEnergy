package sabatinoprovenza.BW_EpicServiceEnergy.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "clienti")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Cliente {
	@Id
	@GeneratedValue
	@Setter(AccessLevel.NONE)
	private UUID id;

	private String ragioneSociale;
	private String partitaIva;
	private String email;
	private LocalDate dataInserimento;
	private LocalDate dataUltimoContatto;
	private double fatturatoAnnuale;
	private String pec;
	private String telefono;
	private String emailContatto;
	private String nomeContatto;
	private String cognomeContatto;
	private String telefonoContatto;
	private String logoAzienda;

	@Enumerated(EnumType.STRING)
	private TipoCliente tipoCliente;

	@OneToOne
	@JoinColumn(name = "sede_legale_id")
	private Indirizzo sedeLegale;

	@OneToOne
	@JoinColumn(name = "sede_operativa_id")
	private Indirizzo sedeOperativa;

	public Cliente(String ragioneSociale, String partitaIva, String email, LocalDate dataInserimento,
	               double fatturatoAnnuale, String pec, String telefono, String emailContatto,
	               String nomeContatto, String cognomeContatto, String telefonoContatto,
	               String logoAzienda, TipoCliente tipoCliente) {
		this.ragioneSociale = ragioneSociale;
		this.partitaIva = partitaIva;
		this.email = email;
		this.dataInserimento = dataInserimento;
		this.fatturatoAnnuale = fatturatoAnnuale;
		this.pec = pec;
		this.telefono = telefono;
		this.emailContatto = emailContatto;
		this.nomeContatto = nomeContatto;
		this.cognomeContatto = cognomeContatto;
		this.telefonoContatto = telefonoContatto;
		this.logoAzienda = logoAzienda;
		this.tipoCliente = tipoCliente;
	}
}

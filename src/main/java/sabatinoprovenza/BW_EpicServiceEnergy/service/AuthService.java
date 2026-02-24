package sabatinoprovenza.BW_EpicServiceEnergy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sabatinoprovenza.BW_EpicServiceEnergy.entities.Utente;
import sabatinoprovenza.BW_EpicServiceEnergy.exceptions.UnauthorizedException;
import sabatinoprovenza.BW_EpicServiceEnergy.payload.LoginDTO;
import sabatinoprovenza.BW_EpicServiceEnergy.security.JWTTools;

@Service
public class AuthService {

    private final UtenteService utenteService;
    private final JWTTools jwtTools;
    private final PasswordEncoder bcrypt;

    @Autowired
    public AuthService(UtenteService utenteService, JWTTools jwtTools, PasswordEncoder bcrypt) {
        this.utenteService = utenteService;
        this.jwtTools = jwtTools;
        this.bcrypt = bcrypt;
    }


    public String checkCredentialsAndGenerateToken(LoginDTO body) {

        Utente found = this.utenteService.findByUsername(body.username());

        if (bcrypt.matches(body.password(), found.getPassword())) {

            String accessToken = jwtTools.generateToken(found);

            return accessToken;

        } else {
            throw new UnauthorizedException("Le credenziali inserite sono errate!");
        }
    }

}

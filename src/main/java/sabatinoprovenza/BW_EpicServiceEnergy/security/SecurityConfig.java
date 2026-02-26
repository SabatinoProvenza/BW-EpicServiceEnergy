package sabatinoprovenza.BW_EpicServiceEnergy.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {


        httpSecurity.formLogin(formLogin -> formLogin.disable());
        httpSecurity.csrf(csrf -> csrf.disable());
        httpSecurity.sessionManagement(sessions -> sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        httpSecurity.authorizeHttpRequests(req -> req.requestMatchers("/**").permitAll());

        httpSecurity.cors(Customizer.withDefaults()); // TODO: OBBLIGATORIA se vogliamo usare la configurazione per il frontend (che abbiamo scritto nell'ultimo @Bean in fondo)

        return httpSecurity.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration(); // Questo è l'oggetto che creo per la configurazione

        // Nella prossima riga (riga 93) definisco una WHITELIST di uno o più indirizzi FRONTEND che voglio possano accedere a questo backend senza i problemi di CORS
        // Volendo ma rischioso si potrebbe anche mettere '*' però questo permetterebbe l'accesso a tutti (utile solo nel caso di API pubbliche)
        configuration.setAllowedOrigins(List.of("http://localhost:5174", "https://exampleFronrend.com")); // List.of -> Lista di stringhe
        // configuration.setAllowedOrigins(List.of("*")); <--- Questo per Api pubbliche perché dà accesso a tutti a questo Backend (rischioso in caso di Api non pubbliche)

        configuration.setAllowedMethods(List.of("*")); // Qui inserisco i metodi a cui si può avere accesso
        configuration.setAllowedHeaders(List.of("*"));

        // E infine qui sotto lo impostiamo su di un URL
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}

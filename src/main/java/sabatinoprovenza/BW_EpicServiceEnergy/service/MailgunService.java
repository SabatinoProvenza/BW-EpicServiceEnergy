package sabatinoprovenza.BW_EpicServiceEnergy.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class MailgunService {

    @Value("${mailgun.api.key}")
    private String apiKey;

    @Value("${mailgun.domain}")
    private String domain;

    @Value("${mailgun.base.url}")
    private String baseUrl;

    public void sendSimpleMessage(String to, String subject, String text) throws UnirestException {
        HttpResponse<String> response = Unirest.post(baseUrl + "/" + domain + "/messages")
                .basicAuth("api", apiKey) // -> autenticazione HTTP Basic con la API key
                .field("from", "noreply@" + domain) // -> mittente
                .field("to", to)// -> destinatario
                .field("subject", subject)// -> oggetto dell’email
                .field("text", text)// -> corpo dell’email
                .asString();

        System.out.println("Status: " + response.getStatus());
        System.out.println("Body: " + response.getBody());
    }

}

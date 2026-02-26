package sabatinoprovenza.BW_EpicServiceEnergy.controllers;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sabatinoprovenza.BW_EpicServiceEnergy.service.MailgunService;

@RestController
public class MailgunTestController {

    private final MailgunService mailgunService;

    @Autowired
    public MailgunTestController(MailgunService mailgunService) {
        this.mailgunService = mailgunService;
    }

    @GetMapping("/send-test-email")
    public String sendTestEmail(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String body
    ) {
        try {
            mailgunService.sendSimpleMessage(to, subject, body);
            return "Email inviata con successo a " + to;
        } catch (UnirestException e) {
            e.printStackTrace();
            return "Errore nell'invio: " + e.getMessage();
        }
    }

}

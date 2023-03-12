package com.example.webmailcore.services.mail;

import com.example.webmailcore.enums.MailboxMailType;
import com.example.webmailcore.enums.TicketStatus;
import com.example.webmailcore.models.FlightTicket;
import com.example.webmailcore.models.Mailbox;
import com.example.webmailcore.repositories.FlightTicketRepository;
import com.example.webmailcore.repositories.MailboxRepository;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class MailSenderService {

    //Mandrill API key: md-IwJMkQoqJBqpW4o0eZxl-Q

    @Autowired
    MailboxRepository mailboxRepository;

    @Autowired
    FlightTicketRepository ticketRepository;

    public ResponseEntity sendMail() throws EmailException {
        List<FlightTicket> tickets = ticketRepository.findAll();
        for (FlightTicket ticket : tickets) {
            if (ticket != null && ticket.getStatus() != null && ticket.getStatus().equals(TicketStatus.DELAYED)) {
                HtmlEmail email = new HtmlEmail();
                email.setHostName("smtp.gmail.com");
                email.setSmtpPort(587);
                email.setAuthenticator(new DefaultAuthenticator("astraairairlines@gmail.com", "sglvmosysbshkjot"));
                email.setSSLOnConnect(true);
                email.setFrom("astraairairlines@gmail.com");
                InternetAddress internetAddress = new InternetAddress();
                internetAddress.setAddress(ticket.getUser().getEmail());
                List<InternetAddress> internetAddressList = new ArrayList<>();
                internetAddressList.add(internetAddress);
                email.setTo(internetAddressList);
                email.setSubject("Ticket with flight number " + ticket.getFlightNumber() + " has been delayed");
                String htmlMsg = "<html>\n" +
                        "\t<body>\n" +
                        "\t\t<h1>Flight delayed</h1>\n" +
                        "\t\t<p>Dear " + ticket.getUser().getFirstName() + " " + ticket.getUser().getLastName() +",</p>\n" +
                        "\t\t<p>We regret to inform you that your flight with flight number " + ticket.getFlightNumber() + " has been delayed.</p>\n" +
                        "\t\t<p>We apologize for any inconvenience this may have caused.</p>\n" +
                        "\t\t <p>As a token of our appreciation for your patience and understanding, we are pleased to offer you a discount of 30% on your next booking with us. Simply use the code " + ticket.getFlightNumber() + " at checkout to redeem your discount.</p>\n" +
                        "\t\t <p>Thank you for choosing Astra Air and we look forward to serving you in the future.</p>\n" +
                        "\t\t <p>Sincerely,</p>\n" +
                        "\t\t<p>The Astra Air Team</p>\n" +
                        "\t</body>\n" +
                        "<html>";
                String msg = "Dear " + ticket.getUser().getFirstName() + " "  + ticket.getUser().getLastName() + " " + "We regret to inform you that your flight with flight number " + ticket.getFlightNumber() + " has been delayed. We apologize for any inconvenience this may have caused. " +
                        "As a token of our appreciation for your patience and understanding, we are pleased to offer you a discount of 30% on your next booking with us. Simply use the code " + ticket.getFlightNumber()  + " at checkout to redeem your discount. Thank you for choosing Astra Air and we look forward to serving you in the future." +
                        "Sincerely, The Astra Air Team";
                email.setHtmlMsg(htmlMsg);
                email.setCharset("UTF-8");
                email.send();

                Mailbox mailbox = new Mailbox();
                mailbox.setMailType(MailboxMailType.OUTGOING);
                mailbox.setSender("Astra Air <astraairairlines@gmail.com>");
                mailbox.setRead(false);
                mailbox.setSubject("Ticket with flight number " + ticket.getFlightNumber() + " has been delayed");
                mailbox.setDateSent(new Date());
                mailbox.setContent(htmlMsg);
                mailbox.setContentTextOnly(msg);
                mailbox.setHasTickets(false);
                mailbox.setReceiver(ticket.getUser().getEmail());
                mailbox.setArchived(false);
                mailboxRepository.save(mailbox);
            }
        }
        return ResponseEntity.ok("Mail sent");
    }
}




package com.example.webmailcore.services.mail;

import com.example.webmailcore.enums.MailboxMailType;
import com.example.webmailcore.enums.TicketStatus;
import com.example.webmailcore.models.FlightTicket;
import com.example.webmailcore.models.Mailbox;
import com.example.webmailcore.repositories.FlightTicketRepository;
import com.example.webmailcore.repositories.MailboxRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class MailSenderService {

    //Mandrill API key: md-IwJMkQoqJBqpW4o0eZxl-Q

    @Value("${mailboxEmailSMTPServer}")
    private String emailSMTPServer;

    @Value("${mailSender}")
    private String mailSender;

    @Value("${mailPassword}")
    private String mailPassword;

    @Autowired
    MailboxRepository mailboxRepository;

    @Autowired
    FlightTicketRepository ticketRepository;

//    @Scheduled(fixedRate = 60000)
    public ResponseEntity sendMailToDelayedTickets() throws EmailException {
        List<FlightTicket> tickets = ticketRepository.findAll();
        for (FlightTicket ticket : tickets) {
            if (ticket != null && ticket.getStatus() != null && !ticket.getMailSentForDelayedTicket() && ticket.getStatus().equals(TicketStatus.DELAYED)) {
                HtmlEmail email = new HtmlEmail();
                email.setHostName(emailSMTPServer);
                email.setSmtpPort(587);
                email.setAuthenticator(new DefaultAuthenticator(mailSender, mailPassword));
                email.setSSLOnConnect(true);
                email.setFrom(mailSender);
                InternetAddress internetAddress = new InternetAddress();
                internetAddress.setAddress(ticket.getUser().getEmail());
                List<InternetAddress> internetAddressList = new ArrayList<>();
                internetAddressList.add(internetAddress);
                email.setTo(internetAddressList);
                email.setSubject("Flight Delay Notification");
                String htmlMsg = "<html>\n" +
                        "\t<body>\n" +
                        "\t\t<h1>Flight delayed</h1>\n" +
                        "\t\t<p>Dear " + ticket.getUser().getFirstName() + " " + ticket.getUser().getLastName() + ",</p>\n" +
                        "\t\t<p>We regret to inform you that your flight with flight number " + ticket.getFlightNumber() + " has been delayed.</p>\n" +
                        "\t\t<p>We apologize for any inconvenience this may have caused.</p>\n" +
                        "\t\t <p>As a token of our appreciation for your patience and understanding, we are pleased to offer you a discount of 30% on your next booking with us. Simply use the code " + ticket.getFlightNumber() + " at checkout to redeem your discount.</p>\n" +
                        "\t\t <p>Thank you for choosing Astra Air and we look forward to serving you in the future.</p>\n" +
                        "\t\t <p>Sincerely,</p>\n" +
                        "\t\t<p>The " + ticket.getAirplaneCompany().getName() + " Team</p>\n" +
                        "\t</body>\n" +
                        "<html>";
                String msg = "Dear " + ticket.getUser().getFirstName() + " " + ticket.getUser().getLastName() + " " + "We regret to inform you that your flight with flight number " + ticket.getFlightNumber() + " has been delayed. We apologize for any inconvenience this may have caused. " +
                        "As a token of our appreciation for your patience and understanding, we are pleased to offer you a discount of 30% on your next booking with us. Simply use the code " + ticket.getFlightNumber() + " at checkout to redeem your discount. Thank you for choosing Astra Air and we look forward to serving you in the future." +
                        "Sincerely, The " + ticket.getAirplaneCompany().getName() + " Team";
                email.setHtmlMsg(htmlMsg);
                email.setCharset("UTF-8");
                email.send();

                Mailbox mailbox = new Mailbox();
                mailbox.setMailType(MailboxMailType.OUTGOING);
                mailbox.setSender("Astra Air " + mailSender);
                mailbox.setRead(false);
                mailbox.setSubject("Flight Delay Notification");
                mailbox.setDateSent(new Date());
                mailbox.setContent(htmlMsg);
                mailbox.setContentTextOnly(msg);
                mailbox.setHasTickets(false);
                mailbox.setReceiver(ticket.getUser().getEmail());
                mailbox.setArchived(false);
                mailboxRepository.save(mailbox);
                ticket.setMailSentForDelayedTicket(true);
                ticketRepository.save(ticket);
            }
        }
        return ResponseEntity.ok("Ok");
    }

//    @Scheduled(fixedRate = 60000)
    public ResponseEntity sendMailToCanceledTickets() throws EmailException {
        List<FlightTicket> tickets = ticketRepository.findAll();
        for (FlightTicket ticket : tickets) {
            if (ticket != null && ticket.getStatus() != null && !ticket.getMailSentForCanceledTicket() && ticket.getStatus().equals(TicketStatus.CANCELED)) {
                HtmlEmail email = new HtmlEmail();
                email.setHostName(emailSMTPServer);
                email.setSmtpPort(587);
                email.setAuthenticator(new DefaultAuthenticator(mailSender, mailPassword));
                email.setSSLOnConnect(true);
                email.setFrom(mailSender);
                InternetAddress internetAddress = new InternetAddress();
                internetAddress.setAddress(ticket.getUser().getEmail());
                List<InternetAddress> internetAddressList = new ArrayList<>();
                internetAddressList.add(internetAddress);
                email.setTo(internetAddressList);
                email.setSubject("Flight Cancellation Notification");
                String htmlMsg = "<html>\n" +
                        "\t<body>\n" +
                        "\t\t<h1>Flight cancelled</h1>\n" +
                        "\t\t<p>Dear " + ticket.getUser().getFirstName() + " " + ticket.getUser().getLastName() + "</p>\n" +
                        "\t\t<p>We regret to inform you that your flight with flight number " + ticket.getFlightNumber() + " has been cancelled. We understand\n" +
                        "\t\tthat this may cause inconvenience and disruption to your travel plans, and we apologize for the\n" +
                        "\t\tinconvenience caused.</p>\n" +
                        "\t\t<p>We would like to offer the following options to accommodate you:</p>\n" +
                        "\t\t<ol>\n" +
                        "\t\t\t<li>Rebook your flight: We can assist you in rebooking your flight to a later date or time that is convenient for you. Please reply to this email or contact our customer service team to rebook your flight.</li>\n" +
                        "\t\t\t<li>Refund: If you choose not to travel, we can provide a full refund for your ticket. Please reply to this email or contact our customer service team to request a refund.</li>\n" +
                        "\t\t</ol>\n" +
                        "\t\t <p>As a token of our appreciation for your patience and understanding, we are pleased to offer you a discount of 20% on your next booking with us. Simply use the code " + ticket.getFlightNumber() + " at checkout to redeem your discount.</p>\n" +
                        "\t\t <p>Thank you for choosing " + ticket.getAirplaneCompany().getName() + " and we look forward to serving you in the future.</p>\n" +
                        "\t\t <p>Sincerely,</p>\n" +
                        "\t\t<p>The " + ticket.getAirplaneCompany().getName() + " Team</p>\n" +
                        "\t</body>\n" +
                        "<html>";
                String msg = "Dear " + ticket.getUser().getFirstName() + " " + ticket.getUser().getLastName() + ", We regret to inform you that your flight with flight" +
                        "number " + ticket.getFlightNumber() + " has been cancelled. We understand that this may cause inconvenience and disruption to your travel plans," +
                        "and we apologize for the inconvenience caused. We would like to offer the following options to accommodate you: 1. Rebook your flight: We can assist you " +
                        "in rebooking your flight to a later date or time that is convenient for you. Please reply to this email or contact our customer service team to rebook" +
                        "your flight. 2. Refund: If you choose ot to travel, we can provide a full refund for your ticket. Please reply to this email or contact our customer service" +
                        "team to request a refund. As a token of our appreciation for you patience and understanding, we are pleased to offer you a discount of 20% on your next " +
                        "booking with us. Simply use the code " + ticket.getFlightNumber() + " at checkout to redeem your discount. Thank you for choosing " + ticket.getAirplaneCompany().getName() +
                        " and we look forward to serving you in the future. Sincerely, The " + ticket.getAirplaneCompany().getName() + " Team";
                email.setHtmlMsg(htmlMsg);
                email.setCharset("UTF-8");
                email.send();

                Mailbox mailbox = new Mailbox();
                mailbox.setMailType(MailboxMailType.OUTGOING);
                mailbox.setSender("Astra Air " + mailSender);
                mailbox.setRead(false);
                mailbox.setSubject("Flight Cancellation Notification");
                mailbox.setDateSent(new Date());
                mailbox.setContent(htmlMsg);
                mailbox.setContentTextOnly(msg);
                mailbox.setHasTickets(false);
                mailbox.setReceiver(ticket.getUser().getEmail());
                mailbox.setArchived(false);
                mailboxRepository.save(mailbox);
                ticket.setMailSentForCanceledTicket(true);
                ticketRepository.save(ticket);
            }
        }
        return ResponseEntity.ok("Ok");
    }

    //    @Scheduled(fixedRate = 60000)
    public ResponseEntity sendMailToBookedTickets() throws EmailException, FileNotFoundException, DocumentException {
        List<FlightTicket> tickets = ticketRepository.findAll();
        FlightTicket ticket = ticketRepository.getById("b4324951-e6b4-4ec5-8e38-e52707fb7a15");
//        for (FlightTicket ticket : tickets) {
//            if (ticket != null) {
        HtmlEmail email = new HtmlEmail();
        email.setHostName(emailSMTPServer);
        email.setSmtpPort(587);
        email.setAuthenticator(new DefaultAuthenticator(mailSender, mailPassword));
        email.setSSLOnConnect(true);
        email.setFrom(mailSender);
        InternetAddress internetAddress = new InternetAddress();
//                internetAddress.setAddress(ticket.getUser().getEmail());
        internetAddress.setAddress("anchovasimona11@gmail.com");
        List<InternetAddress> internetAddressList = new ArrayList<>();
        internetAddressList.add(internetAddress);
        email.setTo(internetAddressList);
        email.setSubject("Flight Confirmation Notification");
        String htmlMsg = "<html>\n" +
                "\t<body>\n" +
                "\t\t<h3>Flight Ticket Confirmation</h3>\n" +
                "\t\t<p>Dear " + ticket.getUser().getFirstName() + " " + ticket.getUser().getLastName() + "</p>\n" +
                "\t\t<p>We are pleased to inform you that your flight with flight number " + ticket.getFlightNumber() + " on " + ticket.getDepartureDate() + " has been\n" +
                "\t\tsuccessfully processed and is attached to this email.</p>\n" +
                "\t\t<p>Please ensure that you review your flight details carefully and note the flight number, date and time \n" +
                "\t\tto avoid any confusion. </p>\n" +
                "\t\t <p>If you have any questions or concerns regarding your flight, please do not hesitate to \n" +
                "\t\t contact us. We would be happy to assist you.</p>\n" +
                "\t\t <p>Thank you for choosing " + ticket.getAirplaneCompany().getName() + " for your travel needs. We wish you a safe and pleasant journey.</p>\n" +
                "\t\t <p>Sincerely,</p>\n" +
                "\t\t<p>The " + ticket.getAirplaneCompany().getName() + " Team</p>\n" +
                "\t</body>\n" +
                "<html>";
        String msg = "Dear " + ticket.getUser().getFirstName() + " " + ticket.getUser().getLastName() + ", We are pleased to inform you that your flight" +
                "with flight number " + ticket.getFlightNumber() + " on " + ticket.getDepartureDate() + " has been successfully processed and is attached to this email." +
                "Please ensure that you review your flight details carefully and note the flight number, date and time to avoid any confusion. If you have any questions or" +
                "concerns regarding your flight, please do not hesitate to contact us. We would be happy to assist you. Thank you for choosing " + ticket.getAirplaneCompany().getName() + "for" +
                "your travel needs. We wish you a safe and pleasant journey. Sincerely, The " + ticket.getAirplaneCompany().getName() + " Team";
        email.setHtmlMsg(htmlMsg);
        email.setCharset("UTF-8");

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("flightConfirmation.pdf"));

        document.open();

        //Flight confirmation header
        Font headingFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK);
        Chunk heading = new Chunk("Flight Confirmation", headingFont);
        Paragraph headingParagraph = new Paragraph(heading);
        headingParagraph.add(new Chunk("\n"));
        headingParagraph.add(new Chunk("\n"));
        document.add(headingParagraph);

        //Passenger information
        Font h2 = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
        Paragraph passengerParagraph = new Paragraph("Passenger information", h2);
        passengerParagraph.setSpacingAfter(-10f);
        Paragraph lineSeparatorParagraph = new Paragraph();
        lineSeparatorParagraph.setSpacingBefore(2f);
        lineSeparatorParagraph.add(new Chunk(new LineSeparator()));
        document.add(passengerParagraph);
        document.add(lineSeparatorParagraph);

        Font font = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
        Paragraph informationParagraph = new Paragraph();
        informationParagraph.add(new Chunk("Name of passenger: " + ticket.getUser().getFirstName() + " " + ticket.getUser().getLastName(), font));
        informationParagraph.add(new Chunk("\n"));
        informationParagraph.add(new Chunk("Email: " + ticket.getUser().getEmail(), font));
        informationParagraph.add(new Chunk("\n"));
        informationParagraph.add(new Chunk("\n"));
        informationParagraph.add(new Chunk("\n"));
        document.add(informationParagraph);


        // Departure information
        Paragraph departure = new Paragraph("Departure information", h2);
        departure.setSpacingAfter(-10f);
        Paragraph departureParagraph = new Paragraph();
        departureParagraph.setSpacingBefore(2f);
        departureParagraph.add(new Chunk(new LineSeparator()));
        document.add(departure);
        document.add(departureParagraph);

        Paragraph departureInfo = new Paragraph();
        DateFormat departureDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String departureDate = departureDateFormat.format(ticket.getDepartureDate());

        DateFormat departureTimeFormat = new SimpleDateFormat("HH:mm");
        String departureTime = departureTimeFormat.format(ticket.getDepartureDate());

        departureInfo.add(new Chunk("Departure date: " + departureDate, font));
        departureInfo.add(new Chunk("\n"));
        departureInfo.add(new Chunk("Departure time: " + departureTime, font));
        departureInfo.add(new Chunk("\n"));
        departureInfo.add(new Chunk("Departure destination: " + ticket.getCityFrom().getName() + " (" + ticket.getCountryFrom().getNameEn() + ")", font));
        departureInfo.add(new Chunk("\n"));
        departureInfo.add(new Chunk("Arrival destination: " + ticket.getCityTo().getName() + " (" + ticket.getCountryTo().getNameEn() + ")", font));
        departureInfo.add(new Chunk("\n"));
        document.add(departureInfo);

        document.close();
        File pdfFile = new File("flightConfirmation.pdf");
        email.attach(pdfFile);
        email.send();

        Mailbox mailbox = new Mailbox();
        mailbox.setMailType(MailboxMailType.OUTGOING);
        mailbox.setSender("Astra Air " + mailSender);
        mailbox.setRead(false);
        mailbox.setSubject("Flight Confirmation Notification");
        mailbox.setDateSent(new Date());
        mailbox.setContent(htmlMsg);
        mailbox.setContentTextOnly(msg);
        mailbox.setHasTickets(false);
        mailbox.setReceiver(ticket.getUser().getEmail());
        mailbox.setArchived(false);
        mailboxRepository.save(mailbox);
//                ticket.setMailSentForCanceledTicket(true);
        ticketRepository.save(ticket);
//            }
//        }
        return ResponseEntity.ok("Ok");
    }
}




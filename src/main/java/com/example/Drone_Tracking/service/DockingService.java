package com.example.Drone_Tracking.service;

import com.example.Drone_Tracking.model.*;
import com.example.Drone_Tracking.repository.*;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class DockingService {

    private final DockingSessionRepository dockingRepo;
    private final DronePadRepository padRepo;
    private final InvoiceRepository invoiceRepo;

    public DockingService(DockingSessionRepository dockingRepo, DronePadRepository padRepo, InvoiceRepository invoiceRepo) {
        this.dockingRepo = dockingRepo;
        this.padRepo = padRepo;
        this.invoiceRepo = invoiceRepo;
    }

    public void landDrone(DockingSession session) {
        session.setLandedAt(LocalDateTime.now());
        session.setStatus("LANDED");

        DronePad pad = padRepo.findById(session.getDronePad().getId()).orElseThrow();
        pad.setStatus("OCCUPIED");
        padRepo.save(pad);

        session.setDronePad(pad);
        dockingRepo.save(session);
    }

    public void departDrone(Long sessionId) {
        DockingSession session = dockingRepo.findById(sessionId).orElseThrow();
        LocalDateTime departedTime = LocalDateTime.now();
        session.setDepartedAt(departedTime);

        long minutes = Duration.between(session.getLandedAt(), departedTime).toMinutes();
        if (minutes < 1) minutes = 1; // 1-minute minimum for demo testing

        double hourlyRate = session.getDronePad().getHourlyRate();
        double fee = Math.round(((minutes / 60.0) * hourlyRate) * 100.0) / 100.0;

        session.setTotalDurationMinutes((double) minutes);
        session.setCalculatedFee(fee);
        session.setStatus("DEPARTED");

        // Release the pad
        DronePad pad = session.getDronePad();
        pad.setStatus("AVAILABLE");
        padRepo.save(pad);

        // Generate customer invoice
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber("INV-" + System.currentTimeMillis() % 100000);
        invoice.setType("CUSTOMER_INVOICE");
        invoice.setDescription("Docking charge: Drone " + session.getDroneIdentifier() + " at " + pad.getPadCode());
        invoice.setAmount(fee);
        invoice.setIssueDate(LocalDate.now());
        invoice.setPaymentStatus("PENDING");
        invoice.setOperator(session.getOperator());
        invoiceRepo.save(invoice);

        dockingRepo.save(session);
    }
}
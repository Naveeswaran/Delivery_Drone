package com.example.Drone_Tracking.controller;

import com.example.Drone_Tracking.model.*;
import com.example.Drone_Tracking.repository.*;
import com.example.Drone_Tracking.service.DockingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
public class HubController {

    private final DronePadRepository padRepo;
    private final OperatorRepository operatorRepo;
    private final DockingSessionRepository dockingRepo;
    private final InvoiceRepository invoiceRepo;
    private final DockingService dockingService;

    public HubController(DronePadRepository padRepo, OperatorRepository operatorRepo,
                         DockingSessionRepository dockingRepo, InvoiceRepository invoiceRepo,
                         DockingService dockingService) {
        this.padRepo = padRepo;
        this.operatorRepo = operatorRepo;
        this.dockingRepo = dockingRepo;
        this.invoiceRepo = invoiceRepo;
        this.dockingService = dockingService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("totalPads", padRepo.count());
        model.addAttribute("activeDocks", dockingRepo.findAll().stream().filter(d -> "LANDED".equals(d.getStatus())).count());
        model.addAttribute("totalInvoices", invoiceRepo.count());
        return "index";
    }

    @GetMapping("/pads")
    public String listPads(Model model) {
        model.addAttribute("pads", padRepo.findAll());
        model.addAttribute("newPad", new DronePad()); // Required by th:object="${newPad}"
        return "pads";
    }

    @PostMapping("/pads/save")
    public String savePad(@ModelAttribute("newPad") DronePad pad) {
        if (pad.getStatus() == null || pad.getStatus().trim().isEmpty()) {
            pad.setStatus("AVAILABLE");
        }
        padRepo.save(pad);
        return "redirect:/pads";
    }

    @GetMapping("/pads/delete/{id}")
    public String deletePad(@PathVariable Long id) {
        padRepo.deleteById(id);
        return "redirect:/pads";
    }

    @GetMapping("/dockings")
    public String listDockings(Model model) {
        model.addAttribute("dockings", dockingRepo.findAll());
        model.addAttribute("pads", padRepo.findAll().stream().filter(p -> "AVAILABLE".equals(p.getStatus())).toList());
        model.addAttribute("operators", operatorRepo.findAll().stream().filter(o -> "CUSTOMER".equals(o.getType())).toList());
        model.addAttribute("newDocking", new DockingSession());
        return "dockings";
    }

    @PostMapping("/dockings/land")
    public String landDrone(@ModelAttribute DockingSession session) {
        dockingService.landDrone(session);
        return "redirect:/dockings";
    }

    @GetMapping("/dockings/depart/{id}")
    public String departDrone(@PathVariable Long id) {
        dockingService.departDrone(id);
        return "redirect:/dockings";
    }

    @GetMapping("/invoices")
    public String listInvoices(Model model) {
        model.addAttribute("invoices", invoiceRepo.findAll());
        model.addAttribute("vendors", operatorRepo.findAll().stream().filter(o -> "VENDOR".equals(o.getType())).toList());
        model.addAttribute("newBill", new Invoice());
        return "invoices";
    }

    @PostMapping("/invoices/add-bill")
    public String addVendorBill(@ModelAttribute Invoice bill) {
        bill.setType("VENDOR_BILL");
        bill.setIssueDate(LocalDate.now());
        bill.setPaymentStatus("PENDING");
        invoiceRepo.save(bill);
        return "redirect:/invoices";
    }

    @GetMapping("/invoices/pay/{id}")
    public String payInvoice(@PathVariable Long id) {
        Invoice inv = invoiceRepo.findById(id).orElseThrow();
        inv.setPaymentStatus("PAID");
        invoiceRepo.save(inv);
        return "redirect:/invoices";
    }

    @GetMapping("/reports")
    public String reports(Model model) {
        double revenue = invoiceRepo.findByType("CUSTOMER_INVOICE").stream()
                .mapToDouble(Invoice::getAmount).sum();
        double expenses = invoiceRepo.findByType("VENDOR_BILL").stream()
                .mapToDouble(Invoice::getAmount).sum();

        model.addAttribute("revenue", Math.round(revenue * 100.0) / 100.0);
        model.addAttribute("expenses", Math.round(expenses * 100.0) / 100.0);
        model.addAttribute("netProfit", Math.round((revenue - expenses) * 100.0) / 100.0);
        return "reports";
    }
}
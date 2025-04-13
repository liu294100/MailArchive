package org.apollo.mail.controller;

import org.apollo.mail.entity.MailboxConfig;
import org.apollo.mail.service.MailboxConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class PageController {

    @Autowired
    private MailboxConfigService mailboxConfigService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    // Keep the generic loader for other simple pages if needed
    @GetMapping("/templates/{page}")
    public String loadTemplate(@PathVariable String page) {
        // Check if the page is mailbox-config, if so, redirect to the dedicated handler
        if ("mailbox-config".equals(page)) {
            return "redirect:/mailbox-config"; 
        }
         // Check if the page is dashboard, if so, redirect to the dedicated handler
        if ("dashboard".equals(page)) {
            return "redirect:/dashboard"; 
        }
        // Add more checks for other data-driven pages if necessary
        return page; // Render other simple templates directly
    }

    // Dedicated handler for mailbox configuration page
    @GetMapping("/mailbox-config")
    public String mailboxConfigPage(Model model) {
        List<MailboxConfig> mailboxes = mailboxConfigService.getAllConfigs(); // Use correct method name
        model.addAttribute("mailboxes", mailboxes); // Add to the model
        return "mailbox-config"; // Return the view name
    }
    
    // Add handlers for other data-driven pages like dashboard, email-archive etc.
    // Example for dashboard (needs implementation in service/repo):
    /*
    @GetMapping("/dashboard")
    public String dashboardPage(Model model) {
        // Fetch dashboard data (e.g., counts, recent emails)
        // model.addAttribute("activeCount", ...);
        // model.addAttribute("todayCount", ...);
        // model.addAttribute("totalCount", ...);
        // model.addAttribute("recentEmails", ...);
        return "dashboard";
    }
    */

} 
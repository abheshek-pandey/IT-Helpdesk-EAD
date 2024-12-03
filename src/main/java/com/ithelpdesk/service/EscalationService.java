package com.ithelpdesk.service;

import com.ithelpdesk.model.EscalationPath;

import java.util.HashMap;
import java.util.Map;

public class EscalationService {
    private final Map<String, String> escalationPaths;

    // Constructor
    public EscalationService() {
        escalationPaths = new HashMap<>();
        escalationPaths.put("Tech", "Admin");
        escalationPaths.put("Admin", "Head of IT");
        escalationPaths.put("Head of IT", "VP of Technology");
    }

    // Get escalation path for a role
    public EscalationPath getEscalationPath(String currentRole) {
        if (!escalationPaths.containsKey(currentRole)) {
            throw new IllegalArgumentException("No escalation path defined for role: " + currentRole);
        }
        return new EscalationPath(currentRole, escalationPaths.get(currentRole), "Standard escalation path");
    }
}

package org.confectionery.Controller;

import org.confectionery.Service.ConfectioneryService;

public class ConfectioneryController {
    private final ConfectioneryService confectioneryService;

    public ConfectioneryController(ConfectioneryService confectioneryService) {
        this.confectioneryService = confectioneryService;
    }
}

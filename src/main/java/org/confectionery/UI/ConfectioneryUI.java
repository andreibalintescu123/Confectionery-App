package org.confectionery.UI;

import org.confectionery.Controller.ConfectioneryController;

import java.util.Scanner;

public class ConfectioneryUI {
    private final ConfectioneryController controller;
    private final Scanner scanner;

    public ConfectioneryUI(ConfectioneryController controller, Scanner scanner) {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
    }
}

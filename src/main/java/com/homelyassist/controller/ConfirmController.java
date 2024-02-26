package com.homelyassist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class ConfirmController {
    @GetMapping("confirmregister")
    public String RegisterAssistPage() {
        return "confirmregister";
    }
    @GetMapping("confirmsubmit")
    public String SubmitAssistPage() {
        return "confirmsubmit";
    }
}

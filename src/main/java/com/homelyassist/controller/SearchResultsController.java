package com.homelyassist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class SearchResultsController {
    @GetMapping("/searchresults")
    public String RegisteredPage(){
        return "searchresults";
    }
}

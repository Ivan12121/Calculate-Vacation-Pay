package com.carShowroom.WebCarShowroom.controllers;

import com.carShowroom.WebCarShowroom.utilits.Functions;
import com.carShowroom.WebCarShowroom.utilits.WeekendsDays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

@Controller
public class HomeController {
    @Autowired
    WeekendsDays weekendsDays;

    @Autowired
    Functions functions;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Главная страница");
        return "home";
    }

    @PostMapping("/")
    public String homePost(Model model, @RequestParam String startDay, @RequestParam String endDay, @RequestParam(name = "salary", required = false) Double salary) throws ParseException {
        return functions.vacationPayCalculation(startDay, endDay, salary, model);
    }

}

package com.calculateHolidayPay.WebCalculateHolidayPay.controllers;

import com.calculateHolidayPay.WebCalculateHolidayPay.utilits.Functions;
import com.calculateHolidayPay.WebCalculateHolidayPay.utilits.WeekendsDays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;

@Controller
public class CalculateController {
    @Autowired
    WeekendsDays weekendsDays;

    @Autowired
    Functions functions;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Главная страница");
        return "calculate";
    }

    @PostMapping("/")
    public String homePost(Model model, @RequestParam String startDay, @RequestParam String endDay, @RequestParam(name = "salary", required = false) Double salary) throws ParseException {
        return functions.vacationPayCalculation(startDay, endDay, salary, model);
    }
}

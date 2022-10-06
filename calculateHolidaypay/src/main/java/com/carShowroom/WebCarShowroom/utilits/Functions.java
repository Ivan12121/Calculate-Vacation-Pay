package com.carShowroom.WebCarShowroom.utilits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class Functions {
    @Autowired
    WeekendsDays weekendsDays;

    public String vacationPayCalculation(String startDay, String endDay, Double salary, Model model) {
        try {
            String[] splitStartDay = startDay.split(",");
            String[] splitEndDay = endDay.split(",");

            Date startDayHoliday = new SimpleDateFormat("yyyy-MM-dd").parse(splitStartDay[1]);
            Date endDayHoliday = new SimpleDateFormat("yyyy-MM-dd").parse(splitEndDay[1]);

            int result = startDayHoliday.compareTo(endDayHoliday);
            if (result > 0) {
                model.addAttribute("test", "Vacation start date cannot be later than end date");
            } else {
                long startDayHolidayInMilli = startDayHoliday.getTime();
                long endDayHolidayInMilli = endDayHoliday.getTime();
                long amountOfVacationDaysInMilli = endDayHolidayInMilli - startDayHolidayInMilli;
                long amountOfVacationDays = amountOfVacationDaysInMilli / 1000 / 60 / 60 / 24;

                if (amountOfVacationDays > 28) {
                    model.addAttribute("test", "Maximum 28 vacation days");
                } else {
                    LocalDate startDayHolidayToLocalDate = Instant.ofEpochMilli(startDayHolidayInMilli).atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDate endDayHolidayToLocalDate = Instant.ofEpochMilli(endDayHolidayInMilli).atZone(ZoneId.systemDefault()).toLocalDate();

                    for (LocalDate ld : startDayHolidayToLocalDate.datesUntil(endDayHolidayToLocalDate).collect(Collectors.toList())) {
                        String DayAndMonth = ld.getDayOfMonth() + "." + ld.getMonthValue();
                        if (DayAndMonth.equals(weekendsDays.nativity)) {
                            amountOfVacationDays++;
                        } else if (DayAndMonth.equals(weekendsDays.russiaDay)) {
                            amountOfVacationDays++;
                        } else if (DayAndMonth.equals(weekendsDays.victoryDay)) {
                            amountOfVacationDays++;
                        }
                    }

                    if (salary <= 0) {
                        model.addAttribute("test", "Salary must be greater than zero");
                    } else {
                        double vacationPay = (salary / 351.6) * amountOfVacationDays;
                        model.addAttribute("test", "Vacation pay: " + Math.ceil(vacationPay));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return "home";
    }
}

package com.calculateHolidayPay.WebCalculateHolidayPay.utilits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.text.ParseException;
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

    public String vacationPayCalculation(String startDay, String endDay, Double salary, Model model) throws ParseException {
        Date startDayHoliday = new SimpleDateFormat("yyyy-MM-dd").parse(startDay);
        Date endDayHoliday = new SimpleDateFormat("yyyy-MM-dd").parse(endDay);

        int dayComparisonResult = startDayHoliday.compareTo(endDayHoliday);
        if (dayComparisonResult > 0) {
            model.addAttribute("test", "Vacation start date cannot be later than end date");
        }
        else if(dayComparisonResult == 0) {
            model.addAttribute("test", "Start and end of vacation cannot be equal");
        }
        else {
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
                    model.addAttribute("test", "Vacation pay: " + getVacationPayCost(salary, amountOfVacationDays));
                }
            }
        }
        return "calculate";
    }

    public double getVacationPayCost(Double salary, long amountOfVacationDays) {
        double vacationPay = ((salary * 12) / 351.6) * amountOfVacationDays;
        return Math.ceil(vacationPay);
    }
}

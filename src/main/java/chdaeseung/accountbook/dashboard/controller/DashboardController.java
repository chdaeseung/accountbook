package chdaeseung.accountbook.dashboard.controller;

import chdaeseung.accountbook.dashboard.dto.DashboardResponseDto;
import chdaeseung.accountbook.dashboard.service.DashboardService;
import chdaeseung.accountbook.user.dto.LoginUserDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(required = false) Integer year,
                            @RequestParam(required = false) Integer month,
                            HttpSession session, Model model) {
        LoginUserDto loginUser = (LoginUserDto) session.getAttribute("loginUser");

        LocalDate today = LocalDate.now();

        int targetYear = year != null ? year : today.getYear();
        int targetMonth = month != null ? month : today.getMonthValue();

        DashboardResponseDto dashboard = dashboardService.getDashboard(loginUser.getId(), targetYear, targetMonth);
        model.addAttribute("dashboard", dashboard);

        LocalDate curMonth = LocalDate.of(targetYear, targetMonth, 1);
        LocalDate prevMonth = curMonth.minusMonths(1);
        LocalDate nextMonth = curMonth.plusMonths(1);

        model.addAttribute("prevYear", prevMonth.getYear());
        model.addAttribute("prevMonth", prevMonth.getMonthValue());
        model.addAttribute("nextYear", nextMonth.getYear());
        model.addAttribute("nextMonth", nextMonth.getMonthValue());

        return "dashboard/dashboard";
    }
}

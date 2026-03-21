package chdaeseung.accountbook.dashboard.controller;

import chdaeseung.accountbook.dashboard.dto.DashboardResponseDto;
import chdaeseung.accountbook.dashboard.service.DashboardService;
import chdaeseung.accountbook.user.dto.LoginUserDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        LoginUserDto loginUser = (LoginUserDto) session.getAttribute("loginUser");
        if(loginUser == null) System.out.println("로긴유저 널!");

        DashboardResponseDto dashboard = dashboardService.getDashboard(loginUser.getId());
        model.addAttribute("dashboard", dashboard);

        return "dashboard/dashboard";
    }
}

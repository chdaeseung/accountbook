package chdaeseung.accountbook.recurring.controller;

import chdaeseung.accountbook.category.service.CategoryService;
import chdaeseung.accountbook.recurring.dto.RecurringTransactionCreateDto;
import chdaeseung.accountbook.recurring.dto.RecurringTransactionResponseDto;
import chdaeseung.accountbook.recurring.entity.RecurringTransaction;
import chdaeseung.accountbook.recurring.service.RecurringSchedulerService;
import chdaeseung.accountbook.recurring.service.RecurringTransactionService;
import chdaeseung.accountbook.user.dto.LoginUserDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/recurring")
public class RecurringTransactionController {

    private final RecurringTransactionService recurringTransactionService;
    private final CategoryService categoryService;
    private final RecurringSchedulerService recurringSchedulerService;

    @GetMapping
    public String list(HttpSession session, Model model) {
        LoginUserDto loginUser = (LoginUserDto) session.getAttribute("loginUser");

        List<RecurringTransactionResponseDto> recurringTransactions = recurringTransactionService.getRecurringTransactions(loginUser.getId());

        model.addAttribute("recurringTransactions", recurringTransactions);

        return "/recurring/list";
    }

    @GetMapping("/create")
    public String create(HttpSession session, Model model) {
        LoginUserDto loginUser = (LoginUserDto) session.getAttribute("loginUser");

        model.addAttribute("recurringTransactionCreateDto", new RecurringTransactionCreateDto());
        model.addAttribute("categories", categoryService.getCategories(loginUser.getId()));

        return "/recurring/create";
    }

    @PostMapping
    public String create(@ModelAttribute RecurringTransactionCreateDto recurringTransactionCreateDto, HttpSession session) {
        LoginUserDto loginUser = (LoginUserDto) session.getAttribute("loginUser");

        recurringTransactionService.createRecurringTransaction(loginUser.getId(), recurringTransactionCreateDto);

        return "redirect:/recurring";
    }

    @PostMapping("/{id}/toggle")
    public String toggle(@PathVariable Long id, HttpSession session) {
        LoginUserDto loginUser = (LoginUserDto) session.getAttribute("loginUser");

        recurringTransactionService.toggleDone(loginUser.getId(), id);

        return "redirect:/recurring/" + id;
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, HttpSession session, Model model) {
        LoginUserDto loginUser = (LoginUserDto) session.getAttribute("loginUser");

        RecurringTransactionResponseDto recurringTransaction = recurringTransactionService.getRecurringTransaction(loginUser.getId(), id);

        model.addAttribute("recurringTransaction", recurringTransaction);

        return "/recurring/detail";
    }

    @GetMapping("/{id}/update")
    public String update(@PathVariable Long id, HttpSession session, Model model) {
        LoginUserDto loginUser = (LoginUserDto) session.getAttribute("loginUser");

        RecurringTransactionCreateDto createDto = recurringTransactionService.getRecurringTransactionUpdate(loginUser.getId(), id);

        model.addAttribute("recurringTransactionCreateDto", createDto);
        model.addAttribute("categories", categoryService.getCategories(loginUser.getId()));
        model.addAttribute("recurringTransactionId", id);

        return "/recurring/update";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id, @ModelAttribute RecurringTransactionCreateDto createDto, HttpSession session) {
        LoginUserDto loginUser = (LoginUserDto) session.getAttribute("loginUser");

        recurringTransactionService.updateRecurringTransaction(loginUser.getId(), id, createDto);

        return "redirect:/recurring";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, HttpSession session) {
        LoginUserDto loginUser = (LoginUserDto) session.getAttribute("loginUser");

        recurringTransactionService.deleteRecurringTransaction(loginUser.getId(), id);

        return "redirect:/recurring";
    }

    @PostMapping("/generate")
    public String generate() {
        recurringSchedulerService.generateTodayRecurringTransactions();
        return "redirect:/recurring";
    }

}

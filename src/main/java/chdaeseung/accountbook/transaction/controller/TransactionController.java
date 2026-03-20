package chdaeseung.accountbook.transaction.controller;

import chdaeseung.accountbook.transaction.dto.CreateDto;
import chdaeseung.accountbook.transaction.entity.TransactionType;
import chdaeseung.accountbook.transaction.service.TransactionService;
import chdaeseung.accountbook.user.dto.LoginUserDto;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public String getTransactions(HttpSession session, Model model) {
        LoginUserDto loginUser = (LoginUserDto) session.getAttribute("loginUser");

        if(loginUser == null) {
            return "redirect:/users/login";
        }

        model.addAttribute("transactions", transactionService.getTransactions(loginUser.getId()));
        return "/transactions/list";
    }

    @GetMapping("/create")
    public String create(Model model, HttpSession session) {
        LoginUserDto loginUserDto = (LoginUserDto) session.getAttribute("loginUser");

        if(loginUserDto == null) {
            return "redirect:/users/login";
        }

        model.addAttribute("createDto", new CreateDto());
        model.addAttribute("transactionTypes", TransactionType.values());
        return "/transactions/create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("createDto") CreateDto createDto, BindingResult bindingResult, HttpSession session, Model model) {
        LoginUserDto loginUser = (LoginUserDto) session.getAttribute("loginUser");

        if(loginUser == null) {
            return "redirect:/users/login";
        }

        if(bindingResult.hasErrors()) {
            model.addAttribute("transactionTypes", TransactionType.values());
            return "/transactions/create";
        }

        transactionService.createTransaction(createDto, loginUser.getId());
        return "redirect:/transactions";
    }
}

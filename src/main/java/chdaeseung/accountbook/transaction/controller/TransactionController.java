package chdaeseung.accountbook.transaction.controller;

import chdaeseung.accountbook.transaction.dto.CreateDto;
import chdaeseung.accountbook.transaction.dto.ResponseDto;
import chdaeseung.accountbook.transaction.dto.UpdateDto;
import chdaeseung.accountbook.transaction.entity.TransactionType;
import chdaeseung.accountbook.transaction.service.TransactionService;
import chdaeseung.accountbook.user.dto.LoginUserDto;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public String getTransactionDetail(@PathVariable("id") Long id, HttpSession session, Model model) {
        System.out.println("세부 요청 들어옴");
        LoginUserDto loginUser = (LoginUserDto) session.getAttribute("loginUser");

        if(loginUser == null) {
            return "redirect:/users/login";
        }

        ResponseDto transaction = transactionService.getTransactionDetail(id, loginUser.getId());

        model.addAttribute("transaction", transaction);

        return "/transactions/detail";
    }

    @GetMapping("/{id}/edit")
    public String editTransaction(@PathVariable Long id, HttpSession session, Model model) {
        LoginUserDto loginUser = (LoginUserDto) session.getAttribute("loginUser");

        if(loginUser == null) {
            return "redirect:/users/login";
        }

        UpdateDto transaction = transactionService.transactionUpdate(id, loginUser.getId());
        model.addAttribute("transaction", transaction);
        model.addAttribute("transactionId", id);

        return "/transactions/edit";
    }

    @PostMapping("/{id}/edit")
    public String editTransaction(@PathVariable Long id, @Valid @ModelAttribute("transaction") UpdateDto updateDto, BindingResult bindingResult, HttpSession session, Model model) {
        LoginUserDto loginUser = (LoginUserDto) session.getAttribute("loginUser");

        if(loginUser == null) {
            return "redirect:/users/login";
        }

        if(bindingResult.hasErrors()) {
            model.addAttribute("transactionId", id);
            return "/transactions/edit";
        }

        transactionService.updateTransaction(id, loginUser.getId(), updateDto);

        return "redirect:/transactions/" + id;
    }
}

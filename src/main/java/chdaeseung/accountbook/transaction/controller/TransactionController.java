package chdaeseung.accountbook.transaction.controller;

import chdaeseung.accountbook.category.service.CategoryService;
import chdaeseung.accountbook.transaction.dto.*;
import chdaeseung.accountbook.transaction.service.TransactionService;
import chdaeseung.accountbook.user.dto.LoginUserDto;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final CategoryService categoryService;

    @GetMapping
    public String getTransactionsList(@ModelAttribute TransactionSearchDto searchDto, HttpSession session, Model model) {
        LoginUserDto loginUser = (LoginUserDto) session.getAttribute("loginUser");

        Page<TransactionListResponseDto> transactionPage = transactionService.getTransactions(loginUser.getId(), searchDto);

        model.addAttribute("transactionPage", transactionPage);
        model.addAttribute("searchDto", searchDto);
        model.addAttribute("categories", categoryService.getCategories(loginUser.getId()));

        return "/transactions/list";
    }

    @PostMapping
    public String createTransaction(@ModelAttribute TransactionRequestDto transactionRequestDto, HttpSession session) {
        LoginUserDto loginUser = (LoginUserDto) session.getAttribute("loginUser");

        transactionService.createTransaction(transactionRequestDto, loginUser.getId());

        return "redirect:/dashboard";
    }

    @GetMapping("/create")
    public String create(Model model, HttpSession session) {
        LoginUserDto loginUser = (LoginUserDto) session.getAttribute("loginUser");

        model.addAttribute("transactionRequestDto", new TransactionRequestDto());
        model.addAttribute("categories", categoryService.getCategories(loginUser.getId()));

        return "/transactions/create";
    }

    @GetMapping("/{transactionId}")
    public String getTransactionDetail(@PathVariable Long transactionId, HttpSession session, Model model) {
        LoginUserDto loginUser = (LoginUserDto) session.getAttribute("loginUser");

        TransactionDetailResponseDto transaction = transactionService.getTransactionDetail(loginUser.getId(), transactionId);

        model.addAttribute("transaction", transaction);

        return "/transactions/detail";
    }

    @GetMapping("/{id}/update")
    public String updateTransaction(@PathVariable Long id, HttpSession session, Model model) {
        LoginUserDto loginUser = (LoginUserDto) session.getAttribute("loginUser");

        TransactionRequestDto transaction = transactionService.transactionUpdate(id, loginUser.getId());

        model.addAttribute("transactionId", id);
        model.addAttribute("transaction", transaction);
        model.addAttribute("categories", categoryService.getCategories(loginUser.getId()));

        return "/transactions/update";
    }

    @PostMapping("/{id}/update")
    public String updateTransaction(@PathVariable Long id, @ModelAttribute TransactionRequestDto transactionRequestDto, HttpSession session) {
        LoginUserDto loginUser = (LoginUserDto) session.getAttribute("loginUser");

        transactionService.updateTransaction(id, loginUser.getId(), transactionRequestDto);

        return "redirect:/transactions/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteTransaction(@PathVariable Long id, HttpSession session) {
        LoginUserDto loginuser = (LoginUserDto) session.getAttribute("loginUser");

        transactionService.deleteTransaction(id, loginuser.getId());

        return "redirect:/dashboard";
    }
}

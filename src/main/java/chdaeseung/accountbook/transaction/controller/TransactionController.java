package chdaeseung.accountbook.transaction.controller;

import chdaeseung.accountbook.category.service.CategoryService;
import chdaeseung.accountbook.global.exception.CustomException;
import chdaeseung.accountbook.global.exception.ErrorCode;
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
    private final CategoryService categoryService;

    @GetMapping
    public String getTransactions(HttpSession session, Model model) {
        LoginUserDto loginUser = (LoginUserDto) session.getAttribute("loginUser");

        model.addAttribute("transactions", transactionService.getTransactions(loginUser.getId()));
        return "/transactions/list";
    }

    @GetMapping("/create")
    public String create(Model model, HttpSession session) {
        LoginUserDto loginUser = (LoginUserDto) session.getAttribute("loginUser");

        model.addAttribute("createDto", new CreateDto());
        model.addAttribute("categories", categoryService.getCategories(loginUser.getId()));

        return "/transactions/create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("createDto") CreateDto createDto, BindingResult bindingResult, HttpSession session, Model model) {
        LoginUserDto loginUser = (LoginUserDto) session.getAttribute("loginUser");

        if(bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getCategories(loginUser.getId()));
            return "/transactions/create";
        }

        transactionService.createTransaction(createDto, loginUser.getId());
        return "redirect:/dashboard";
    }

    @GetMapping("/{id}")
    public String getTransactionDetail(@PathVariable("id") Long id, HttpSession session, Model model) {
        LoginUserDto loginUser = (LoginUserDto) session.getAttribute("loginUser");

        ResponseDto transaction = transactionService.getTransactionDetail(id, loginUser.getId());

        model.addAttribute("transaction", transaction);

        return "/transactions/detail";
    }

    @GetMapping("/{id}/edit")
    public String editTransaction(@PathVariable Long id, HttpSession session, Model model) {
        LoginUserDto loginUser = (LoginUserDto) session.getAttribute("loginUser");

        UpdateDto transaction = transactionService.transactionUpdate(id, loginUser.getId());
        model.addAttribute("transaction", transaction);
        model.addAttribute("transactionId", id);
        model.addAttribute("categories", categoryService.getCategories(loginUser.getId()));

        return "/transactions/edit";
    }

    @PostMapping("/{id}/edit")
    public String editTransaction(@PathVariable Long id, @Valid @ModelAttribute("transaction") UpdateDto updateDto, BindingResult bindingResult, HttpSession session, Model model) {
        LoginUserDto loginUser = (LoginUserDto) session.getAttribute("loginUser");

        if(bindingResult.hasErrors()) {
            model.addAttribute("transactionId", id);
            model.addAttribute("categories", categoryService.getCategories(loginUser.getId()));
            return "/transactions/edit";
        }

        transactionService.updateTransaction(id, loginUser.getId(), updateDto);

        return "redirect:/transactions/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteTransaction(@PathVariable Long id, HttpSession session) {
        LoginUserDto loginuser = (LoginUserDto) session.getAttribute("loginUser");

        transactionService.deleteTransaction(id, loginuser.getId());

        return "redirect:/transactions";
    }
}

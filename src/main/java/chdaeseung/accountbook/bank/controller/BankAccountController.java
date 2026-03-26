package chdaeseung.accountbook.bank.controller;

import chdaeseung.accountbook.bank.dto.BankAccountRequestDto;
import chdaeseung.accountbook.bank.dto.BankAccountResponseDto;
import chdaeseung.accountbook.bank.entity.BankAccountType;
import chdaeseung.accountbook.bank.service.BankAccountService;
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
@RequestMapping("/bank-account")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @GetMapping
    public String list(HttpSession session, Model model) {
        LoginUserDto loginUser = (LoginUserDto) session.getAttribute("loginUser");

        model.addAttribute("bankAccounts", bankAccountService.findAll(loginUser.getId()));
        return "/bank-account/list";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("bankAccountRequestDto", new BankAccountRequestDto());
        model.addAttribute("types", BankAccountType.values());

        return "/bank-account/create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute BankAccountRequestDto bankAccountRequestDto, BindingResult bindingResult, HttpSession session, Model model) {
        if (bindingResult.hasErrors()) {
            System.out.println("검증 에러 발생");
            System.out.println(bindingResult);
            model.addAttribute("types", BankAccountType.values());
            return "bank-account/create";
        }

        LoginUserDto loginUser = (LoginUserDto) session.getAttribute("loginUser");
        bankAccountService.create(loginUser.getId(), bankAccountRequestDto);

        return "redirect:/bank-account";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, HttpSession session, Model model) {
        LoginUserDto loginUser = (LoginUserDto) session.getAttribute("loginUser");

        model.addAttribute("bankAccount", bankAccountService.findById(loginUser.getId(), id));
        return "/bank-account/detail";
    }

    @GetMapping("/{id}/update")
    public String update(@PathVariable Long id, HttpSession session, Model model) {
        LoginUserDto loginUser = (LoginUserDto) session.getAttribute("loginUser");

        BankAccountResponseDto bankAccount = bankAccountService.findById(loginUser.getId(), id);

        BankAccountRequestDto requestDto = new BankAccountRequestDto(
                bankAccount.getBankName(),
                bankAccount.getAccountNumber(),
                bankAccount.getBalance(),
                bankAccount.getType(),
                bankAccount.isUsed()
        );

        model.addAttribute("bankAccountId", id);
        model.addAttribute("bankAccountRequestDto", requestDto);
        model.addAttribute("types", BankAccountType.values());

        return "/bank-account/update";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id, @Valid @ModelAttribute BankAccountRequestDto requestDto, BindingResult bindingResult, HttpSession session, Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("bankAccountId", id);
            model.addAttribute("types", BankAccountType.values());
            return "/bank-account/update";
        }

        LoginUserDto loginUser = (LoginUserDto) session.getAttribute("loginUser");
        bankAccountService.update(loginUser.getId(), id, requestDto);

        return "redirect:/bank-account/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, HttpSession session) {
        LoginUserDto loginUser = (LoginUserDto) session.getAttribute("loginUser");

        bankAccountService.delete(loginUser.getId(), id);

        return "redirect:/bank-account";
    }
}

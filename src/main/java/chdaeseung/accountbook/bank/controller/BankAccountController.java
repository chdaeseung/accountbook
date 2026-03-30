package chdaeseung.accountbook.bank.controller;

import chdaeseung.accountbook.bank.dto.BankAccountRequestDto;
import chdaeseung.accountbook.bank.dto.BankAccountResponseDto;
import chdaeseung.accountbook.bank.entity.BankAccountType;
import chdaeseung.accountbook.bank.service.BankAccountService;
import chdaeseung.accountbook.user.dto.LoginUserDto;
import chdaeseung.accountbook.user.service.CustomUserDetails;
import chdaeseung.accountbook.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/bank-account")
public class BankAccountController {

    private final BankAccountService bankAccountService;
    private final UserService userService;

    @GetMapping
    public String list(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        Long userId = userDetails.getUserId();

        model.addAttribute("bankAccounts", bankAccountService.findAll(userId));
        return "/bank-account/list";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("bankAccountRequestDto", new BankAccountRequestDto());
        model.addAttribute("types", BankAccountType.values());

        return "/bank-account/create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute BankAccountRequestDto bankAccountRequestDto, BindingResult bindingResult, @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("types", BankAccountType.values());
            return "bank-account/create";
        }

        Long userId = userDetails.getUserId();

        bankAccountService.create(userId, bankAccountRequestDto);

        return "redirect:/bank-account";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        Long userId = userDetails.getUserId();

        model.addAttribute("bankAccount", bankAccountService.findById(userId, id));
        return "/bank-account/detail";
    }

    @GetMapping("/{id}/update")
    public String update(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        Long userId = userDetails.getUserId();

        BankAccountResponseDto bankAccount = bankAccountService.findById(userId, id);

        BankAccountRequestDto requestDto = new BankAccountRequestDto(
                bankAccount.getBankName(),
                bankAccount.getAccountName(),
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
    public String update(@PathVariable Long id, @Valid @ModelAttribute BankAccountRequestDto requestDto, BindingResult bindingResult, @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("bankAccountId", id);
            model.addAttribute("types", BankAccountType.values());
            return "/bank-account/update";
        }

        Long userId = userDetails.getUserId();

        bankAccountService.update(userId, id, requestDto);

        return "redirect:/bank-account/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();

        bankAccountService.delete(userId, id);

        return "redirect:/bank-account";
    }
}

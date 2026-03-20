package chdaeseung.accountbook.user.controller;

import chdaeseung.accountbook.user.dto.SignupRequestDto;
import chdaeseung.accountbook.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("signupRequestDto", new SignupRequestDto());
        return "/users/signup";
    }

    @PostMapping("/signup")
    public String signup(@Valid SignupRequestDto requestDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "/users/signup";
        }

        try {
            userService.signup(requestDto);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("signupFail", e.getMessage());
            return "users/signup";
        }

        return "redirect:/";
    }


}

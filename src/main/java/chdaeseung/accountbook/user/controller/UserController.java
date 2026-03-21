package chdaeseung.accountbook.user.controller;

import chdaeseung.accountbook.user.dto.LoginRequestDto;
import chdaeseung.accountbook.user.dto.LoginUserDto;
import chdaeseung.accountbook.user.dto.SignupRequestDto;
import chdaeseung.accountbook.user.entity.User;
import chdaeseung.accountbook.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginRequestDto", new LoginRequestDto());
        return "users/login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginRequestDto") LoginRequestDto requestDto, BindingResult bindingResult, HttpSession session) {
        if(bindingResult.hasErrors()) {
            return "users/login";
        }

        try {
            User loginUser = userService.login(requestDto);
            session.setAttribute("loginUser", new LoginUserDto(loginUser));
        } catch (IllegalArgumentException e) {
            bindingResult.reject("loginFail", e.getMessage());
            return "users/login";
        }

        return "redirect:/dashboard";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }


}

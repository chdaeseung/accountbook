package chdaeseung.accountbook.user.controller;

import chdaeseung.accountbook.global.exception.CustomException;
import chdaeseung.accountbook.global.exception.ErrorCode;
import chdaeseung.accountbook.user.dto.LoginRequestDto;
import chdaeseung.accountbook.user.dto.LoginUserDto;
import chdaeseung.accountbook.user.dto.SignupRequestDto;
import chdaeseung.accountbook.user.entity.User;
import chdaeseung.accountbook.user.repository.UserRepository;
import chdaeseung.accountbook.user.service.CustomUserDetails;
import chdaeseung.accountbook.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

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

        return "redirect:/users/login";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, Model model) {
        if(error != null) {
            model.addAttribute("errorMessage", "아이디 또는 비밀번호가 일치하지 않습니다.");
        }
        model.addAttribute("loginRequestDto", new LoginRequestDto());
        return "users/login";
    }

    @GetMapping("/auth-check")
    @ResponseBody
    public String authCheck(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return "anonymous";
        }
        return "login userId = " + userDetails.getUserId() + ", username = " + userDetails.getUsername();
    }
}

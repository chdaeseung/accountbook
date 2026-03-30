package chdaeseung.accountbook.category.controller;

import chdaeseung.accountbook.category.dto.CategoryCreateDto;
import chdaeseung.accountbook.category.dto.CategoryResponseDto;
import chdaeseung.accountbook.category.service.CategoryService;
import chdaeseung.accountbook.global.exception.CustomException;
import chdaeseung.accountbook.global.exception.ErrorCode;
import chdaeseung.accountbook.user.dto.LoginUserDto;
import chdaeseung.accountbook.user.service.CustomUserDetails;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryApiController {

    private final CategoryService categoryService;

    @PostMapping
    public CategoryResponseDto createCategory(@RequestBody CategoryCreateDto createDto, @AuthenticationPrincipal CustomUserDetails userDetails) {

        return categoryService.createCategory(userDetails.getUserId(), createDto);
    }
}

package chdaeseung.accountbook.category.controller;

import chdaeseung.accountbook.category.dto.CategoryCreateDto;
import chdaeseung.accountbook.category.dto.CategoryResponseDto;
import chdaeseung.accountbook.category.service.CategoryService;
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
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public String getCategories(HttpSession session, Model model) {
        LoginUserDto loginUser = (LoginUserDto) session.getAttribute("loginUser");

        model.addAttribute("categories", categoryService.getCategories(loginUser.getId()));
        model.addAttribute("categoryForm", new CategoryCreateDto());

        return "/categories/list";
    }

    @PostMapping
    public CategoryResponseDto createCategory(@RequestBody CategoryCreateDto createDto,
                                              HttpSession session) {
        LoginUserDto loginuser = (LoginUserDto) session.getAttribute("loginUser");

        return categoryService.createCategory(loginuser.getId(), createDto);
    }

    @PostMapping("/{id}/delete")
    public String deleteCategory(@PathVariable Long id, HttpSession session) {
        LoginUserDto loginUser = (LoginUserDto) session.getAttribute("loginUser");

        categoryService.deleteCategory(id, loginUser.getId());
        return "redirect:/categories";
    }
}

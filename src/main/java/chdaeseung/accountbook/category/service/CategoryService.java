package chdaeseung.accountbook.category.service;

import chdaeseung.accountbook.category.dto.CategoryCreateDto;
import chdaeseung.accountbook.category.dto.CategoryResponseDto;
import chdaeseung.accountbook.category.entity.Category;
import chdaeseung.accountbook.category.repository.CategoryRepository;
import chdaeseung.accountbook.global.exception.CustomException;
import chdaeseung.accountbook.global.exception.ErrorCode;
import chdaeseung.accountbook.transaction.repository.TransactionRepository;
import chdaeseung.accountbook.transaction.service.TransactionService;
import chdaeseung.accountbook.user.entity.User;
import chdaeseung.accountbook.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public void createCategory(CategoryCreateDto createDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if(categoryRepository.existsByNameAndUserId(createDto.getName(), userId)) {
            throw new CustomException(ErrorCode.DUPLICATE_CATEGORY);
        }

        Category category = new Category(createDto.getName(), user);
        categoryRepository.save(category);
    }

    public List<CategoryResponseDto> getCategories(Long userId) {
        return categoryRepository.findAllByUserId(userId).stream()
                .map(e -> new CategoryResponseDto(e.getId(), e.getName()))
                .toList();
    }

    @Transactional
    public void deleteCategory(Long categoryId, Long userId) {
        Category category = categoryRepository.findByIdAndUserId(categoryId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        if(transactionRepository.existsByCategoryId(categoryId)) {
            throw new CustomException(ErrorCode.CATEGORY_IN_USE);
        }

        categoryRepository.delete(category);
    }
}

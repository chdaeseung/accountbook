package chdaeseung.accountbook.recurring.service;

import chdaeseung.accountbook.category.entity.Category;
import chdaeseung.accountbook.category.repository.CategoryRepository;
import chdaeseung.accountbook.global.exception.CustomException;
import chdaeseung.accountbook.global.exception.ErrorCode;
import chdaeseung.accountbook.recurring.dto.RecurringTransactionCreateDto;
import chdaeseung.accountbook.recurring.dto.RecurringTransactionResponseDto;
import chdaeseung.accountbook.recurring.entity.RecurringTransaction;
import chdaeseung.accountbook.recurring.repository.RecurringTransactionRepository;
import chdaeseung.accountbook.user.entity.User;
import chdaeseung.accountbook.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RecurringTransactionService {

    private final RecurringTransactionRepository recurringTransactionRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<RecurringTransactionResponseDto> getRecurringTransactions(Long userId) {
        return recurringTransactionRepository.findAllByUserIdOrderByDayOfMonthAsc(userId).stream()
                .map(RecurringTransactionResponseDto::from)
                .toList();
    }

    public void createRecurringTransaction(Long userId, RecurringTransactionCreateDto createDto) {
        validateRecurring(createDto);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Category category = categoryRepository.findById(createDto.getCategoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        validateCategory(userId, category);

        RecurringTransaction recurringTransaction = RecurringTransaction.builder()
                .memo(createDto.getMemo())
                .amount(createDto.getAmount())
                .dayOfMonth(createDto.getDayOfMonth())
                .startDate(createDto.getStartDate())
                .endDate(createDto.getEndDate())
                .isDone(createDto.isDone())
                .category(category)
                .user(user)
                .build();

        recurringTransactionRepository.save(recurringTransaction);
    }

    @Transactional(readOnly = true)
    public RecurringTransactionResponseDto getRecurringTransaction(Long userId, Long recurringId) {
        RecurringTransaction recurringTransaction = recurringTransactionRepository.findByIdAndUserId(recurringId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.RECURRING_NOT_FOND));

        return RecurringTransactionResponseDto.from(recurringTransaction);
    }

    public void updateRecurringTransaction(Long userId, Long recurringId, RecurringTransactionCreateDto createDto) {
        validateRecurring(createDto);

        RecurringTransaction recurringTransaction = recurringTransactionRepository.findByIdAndUserId(recurringId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.RECURRING_NOT_FOND));

        Category category = categoryRepository.findById(createDto.getCategoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        validateCategory(userId, category);

        recurringTransaction.update(
                createDto.getMemo(),
                createDto.getAmount(),
                createDto.getDayOfMonth(),
                createDto.getStartDate(),
                createDto.getEndDate(),
                createDto.isDone(),
                category
        );
    }

    public void deleteRecurringTransaction(Long userId, Long recurringId) {
        RecurringTransaction recurringTransaction = recurringTransactionRepository.findByIdAndUserId(recurringId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.RECURRING_NOT_FOND));

        recurringTransactionRepository.delete(recurringTransaction);
    }

    @Transactional(readOnly = true)
    public RecurringTransactionCreateDto getRecurringTransactionUpdate(Long userId, Long recurringId) {
        RecurringTransaction recurringTransaction = recurringTransactionRepository.findByIdAndUserId(recurringId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.RECURRING_NOT_FOND));

        RecurringTransactionCreateDto dto = new RecurringTransactionCreateDto();
        dto.setMemo(recurringTransaction.getMemo());
        dto.setAmount(recurringTransaction.getAmount());
        dto.setCategoryId(recurringTransaction.getCategory().getId());
        dto.setDayOfMonth(recurringTransaction.getDayOfMonth());
        dto.setStartDate(recurringTransaction.getStartDate());
        dto.setEndDate(recurringTransaction.getEndDate());
        dto.setDone(recurringTransaction.isDone());

        return dto;
    }

    private void validateRecurring(RecurringTransactionCreateDto createDto) {

        if(createDto.getAmount() == null || createDto.getAmount() <= 0) {
            throw new CustomException(ErrorCode.MINIMUM_AMOUNT);
        }

        if(createDto.getCategoryId() == null) {
            throw new CustomException(ErrorCode.CHOOSE_CATEGORY);
        }

        if(createDto.getDayOfMonth() == null || createDto.getDayOfMonth() < 1 || createDto.getDayOfMonth() > 31) {
            throw new CustomException(ErrorCode.INCORRECT_DAY);
        }

        if(createDto.getStartDate() == null) {
            throw new CustomException(ErrorCode.INPUT_START_DAY);
        }

        if(createDto.getEndDate() != null && createDto.getStartDate().isAfter(createDto.getEndDate())) {
            throw new CustomException(ErrorCode.CANT_BE_END_DATE);
        }
    }

    private void validateCategory(Long userId, Category category) {
        if(!category.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN_ACCESS);
        }
    }
}

package chdaeseung.accountbook.transaction.service;

import chdaeseung.accountbook.category.entity.Category;
import chdaeseung.accountbook.category.repository.CategoryRepository;
import chdaeseung.accountbook.global.exception.CustomException;
import chdaeseung.accountbook.global.exception.ErrorCode;
import chdaeseung.accountbook.transaction.dto.*;
import chdaeseung.accountbook.transaction.entity.ExpenseType;
import chdaeseung.accountbook.transaction.entity.Transaction;
import chdaeseung.accountbook.transaction.entity.TransactionType;
import chdaeseung.accountbook.transaction.repository.TransactionRepository;
import chdaeseung.accountbook.user.entity.User;
import chdaeseung.accountbook.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public void createTransaction(TransactionRequestDto requestDto, Long userId) {
        validateTransactionRequest(requestDto);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Category category = categoryRepository.findByIdAndUserId(requestDto.getCategoryId(), userId)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        validateCategoryOwner(userId, category);

        ExpenseType expenseType = resolveExpenseType(requestDto);

        String memo = requestDto.getMemo() == null ? "" : requestDto.getMemo().trim();

        Transaction transaction = Transaction.builder()
                .type(requestDto.getType())
                .expenseType(expenseType)
                .amount(requestDto.getAmount())
                .memo(memo)
                .date(requestDto.getDate())
                .category(category)
                .user(user)
                .recurringTransaction(null)
                .build();

        transactionRepository.save(transaction);
    }

    public List<TransactionResponseDto> getTransactions(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return transactionRepository.findAllByUserOrderByDateDescIdDesc(user)
                .stream()
                .map(TransactionResponseDto::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public TransactionDetailResponseDto getTransactionDetail(Long userId, Long transactionId) {
        Transaction transaction = transactionRepository.findByIdAndUserId(transactionId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.TRANSACTION_NOT_FOUND));

        return TransactionDetailResponseDto.from(transaction);
    }

    @Transactional(readOnly = true)
    public TransactionRequestDto transactionUpdate(Long transactionId, Long userId) {
        Transaction transaction = transactionRepository.findByIdAndUserId(transactionId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.TRANSACTION_NOT_FOUND));

        TransactionRequestDto dto = new TransactionRequestDto();
        dto.setType(transaction.getType());
        dto.setExpenseType(transaction.getExpenseType());
        dto.setAmount(transaction.getAmount());
        dto.setCategoryId(transaction.getCategory().getId());
        dto.setMemo(transaction.getMemo());
        dto.setDate(transaction.getDate());

        return dto;
    }

    @Transactional
    public void updateTransaction(Long transactionId, Long userId, TransactionRequestDto requestDto) {
        validateTransactionRequest(requestDto);

        Transaction transaction = transactionRepository.findByIdAndUserId(transactionId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.TRANSACTION_NOT_FOUND));

        Category category = categoryRepository.findByIdAndUserId(requestDto.getCategoryId(), userId)
                        .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        validateCategoryOwner(userId, category);

        ExpenseType expenseType = resolveExpenseType(requestDto);

        String memo = requestDto.getMemo() == null ? "" : requestDto.getMemo().trim();

        transaction.update(
                requestDto.getType(),
                expenseType,
                requestDto.getAmount(),
                category,
                memo,
                requestDto.getDate()
                );
    }

    @Transactional
    public void deleteTransaction(Long transactionId, Long userId) {
        Transaction transaction = transactionRepository.findByIdAndUserId(transactionId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.TRANSACTION_NOT_FOUND));

        transactionRepository.delete(transaction);
    }

    @Transactional(readOnly = true)
    public Page<TransactionListResponseDto> getTransactions(Long userId, TransactionSearchDto searchDto) {
        validateSearchDate(searchDto);

        Pageable pageable = PageRequest.of(searchDto.getPage(), searchDto.getSize());

        Page<Transaction> transactionPage = transactionRepository.searchTransactions(userId, searchDto, pageable);

        return transactionPage.map(TransactionListResponseDto::from);
    }

    private void validateCategoryOwner(Long userId, Category category) {
        if(!category.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN_ACCESS);
        }
    }

    private void validateSearchDate(TransactionSearchDto searchDto) {
        if(searchDto.getStartDate() != null && searchDto.getEndDate() != null
            && searchDto.getStartDate().isAfter(searchDto.getEndDate())) {
            throw new CustomException(ErrorCode.CANT_BE_END_DATE);
        }
    }

    private ExpenseType resolveExpenseType(TransactionRequestDto requestDto) {
        if(requestDto.getType() == TransactionType.INCOME) {
            return null;
        }

        return requestDto.getExpenseType();
    }

    private void validateTransactionRequest(TransactionRequestDto requestDto) {
        if(requestDto.getDate() == null) {
            throw new CustomException(ErrorCode.INSERT_DATE);
        }

        if(requestDto.getCategoryId() == null) {
            throw new CustomException(ErrorCode.CHOOSE_CATEGORY);
        }

        if(requestDto.getType() == null) {
            throw new CustomException(ErrorCode.CHOOSE_TYPE);
        }

        if(requestDto.getAmount() == null || requestDto.getAmount() <= 0) {
            throw new CustomException(ErrorCode.MINIMUM_AMOUNT);
        }

        if(requestDto.getType() == TransactionType.EXPENSE && requestDto.getExpenseType() == null) {
            throw new CustomException(ErrorCode.CHOOSE_EXPENSE_TYPE);
        }
    }
}

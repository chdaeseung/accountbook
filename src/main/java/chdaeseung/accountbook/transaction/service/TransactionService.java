package chdaeseung.accountbook.transaction.service;

import chdaeseung.accountbook.category.entity.Category;
import chdaeseung.accountbook.category.repository.CategoryRepository;
import chdaeseung.accountbook.global.exception.CustomException;
import chdaeseung.accountbook.global.exception.ErrorCode;
import chdaeseung.accountbook.transaction.dto.*;
import chdaeseung.accountbook.transaction.entity.Transaction;
import chdaeseung.accountbook.transaction.repository.TransactionRepository;
import chdaeseung.accountbook.user.entity.User;
import chdaeseung.accountbook.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public void createTransaction(TransactionCreateDto transactionCreateDto, Long userId) {
        System.out.println("createTransaction - service");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Category category = categoryRepository.findByIdAndUserId(transactionCreateDto.getCategoryId(), userId)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        Transaction transaction = new Transaction(
                transactionCreateDto.getType(),
                transactionCreateDto.getAmount(),
                category,
                transactionCreateDto.getMemo(),
                transactionCreateDto.getDate(),
                user
        );

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
    public TransactionDetailDto getTransactionDetail(Long userId, Long transactionId) {
        Transaction transaction = transactionRepository.findByIdAndUserId(transactionId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.TRANSACTION_NOT_FOUND));

        return TransactionDetailDto.from(transaction);
    }

    public TransactionUpdateDto transactionUpdate(Long transactionId, Long userId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new CustomException(ErrorCode.TRANSACTION_NOT_FOUND));

        if(!transaction.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN_ACCESS);
        }

        TransactionUpdateDto dto = new TransactionUpdateDto();
        dto.setType(transaction.getType());
        dto.setAmount(transaction.getAmount());
        dto.setCategoryId(transaction.getCategory().getId());
        dto.setMemo(transaction.getMemo());
        dto.setDate(transaction.getDate());

        return dto;
    }

    @Transactional
    public void updateTransaction(Long transactionId, Long userId, TransactionUpdateDto transactionUpdateDto) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new CustomException(ErrorCode.TRANSACTION_NOT_FOUND));

        if(!transaction.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN_ACCESS);
        }

        Category category = categoryRepository.findByIdAndUserId(transactionUpdateDto.getCategoryId(), userId)
                        .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        transaction.update(
                transactionUpdateDto.getType(),
                transactionUpdateDto.getAmount(),
                category,
                transactionUpdateDto.getMemo(),
                transactionUpdateDto.getDate()
        );
    }

    @Transactional
    public void deleteTransaction(Long transactionId, Long userId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new CustomException(ErrorCode.TRANSACTION_NOT_FOUND));

        if(!transaction.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN_ACCESS);
        }

        transactionRepository.delete(transaction);
    }

    public Page<TransactionListResponseDto> getTransactions(Long userId, TransactionSearchDto searchDto) {
        validateSearchDate(searchDto);

        Pageable pageable = PageRequest.of(searchDto.getPage(), searchDto.getSize());

        Page<Transaction> transactionPage = transactionRepository.searchTransactions(userId, searchDto, pageable);

        return transactionPage.map(TransactionListResponseDto::from);
    }

    private void validateSearchDate(TransactionSearchDto searchDto) {
        if(searchDto.getStartDate() != null && searchDto.getEndDate() != null
            && searchDto.getStartDate().isAfter(searchDto.getEndDate())) {
            throw new CustomException(ErrorCode.CANT_BE_END_DATE);
        }
    }
}

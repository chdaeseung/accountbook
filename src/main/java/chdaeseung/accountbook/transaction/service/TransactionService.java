package chdaeseung.accountbook.transaction.service;

import chdaeseung.accountbook.category.entity.Category;
import chdaeseung.accountbook.category.repository.CategoryRepository;
import chdaeseung.accountbook.global.exception.CustomException;
import chdaeseung.accountbook.global.exception.ErrorCode;
import chdaeseung.accountbook.transaction.dto.CreateDto;
import chdaeseung.accountbook.transaction.dto.ResponseDto;
import chdaeseung.accountbook.transaction.dto.UpdateDto;
import chdaeseung.accountbook.transaction.entity.Transaction;
import chdaeseung.accountbook.transaction.repository.TransactionRepository;
import chdaeseung.accountbook.user.entity.User;
import chdaeseung.accountbook.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public void createTransaction(CreateDto createDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Category category = categoryRepository.findByIdAndUserId(createDto.getCategoryId(), userId)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        Transaction transaction = new Transaction(
                createDto.getType(),
                createDto.getAmount(),
                category,
                createDto.getMemo(),
                createDto.getDate(),
                user
        );

        transactionRepository.save(transaction);
    }

    public List<ResponseDto> getTransactions(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return transactionRepository.findAllByUserOrderByDateDescIdDesc(user)
                .stream()
                .map(ResponseDto::new)
                .toList();
    }

    public ResponseDto getTransactionDetail(Long transactionId, Long userId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new CustomException(ErrorCode.TRANSACTION_NOT_FOUND));

        if(!transaction.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN_ACCESS);
        }

        return new ResponseDto(transaction);
    }

    public UpdateDto transactionUpdate(Long transactionId, Long userId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new CustomException(ErrorCode.TRANSACTION_NOT_FOUND));

        if(!transaction.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN_ACCESS);
        }

        UpdateDto dto = new UpdateDto();
        dto.setType(transaction.getType());
        dto.setAmount(transaction.getAmount());
        dto.setCategoryId(transaction.getCategory().getId());
        dto.setMemo(transaction.getMemo());
        dto.setDate(transaction.getDate());

        return dto;
    }

    @Transactional
    public void updateTransaction(Long transactionId, Long userId, UpdateDto updateDto) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new CustomException(ErrorCode.TRANSACTION_NOT_FOUND));

        if(!transaction.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN_ACCESS);
        }

        Category category = categoryRepository.findByIdAndUserId(updateDto.getCategoryId(), userId)
                        .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        transaction.update(
                updateDto.getType(),
                updateDto.getAmount(),
                category,
                updateDto.getMemo(),
                updateDto.getDate()
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

}

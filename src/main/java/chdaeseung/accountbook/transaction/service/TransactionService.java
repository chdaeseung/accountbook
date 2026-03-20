package chdaeseung.accountbook.transaction.service;

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

    public void createTransaction(CreateDto createDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Transaction transaction = new Transaction(
                createDto.getType(),
                createDto.getAmount(),
                createDto.getCategory(),
                createDto.getMemo(),
                createDto.getDate(),
                user
        );

        transactionRepository.save(transaction);
    }

    public List<ResponseDto> getTransactions(Long loginUserId) {
        User user = userRepository.findById(loginUserId).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return transactionRepository.findAllByUserOrderByDateDescIdDesc(user)
                .stream()
                .map(ResponseDto::new)
                .toList();
    }

    public ResponseDto getTransactionDetail(Long transactionId, Long userId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("거래내역이 없습니다."));

        if(!transaction.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("접근 권한이 없습니다.");
        }

        return new ResponseDto(transaction);
    }

    public UpdateDto transactionUpdate(Long transactionId, Long userId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("거래내역이 없습니다."));

        if(!transaction.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        UpdateDto dto = new UpdateDto();
        dto.setType(transaction.getType());
        dto.setAmount(transaction.getAmount());
        dto.setCategory(transaction.getCategory());
        dto.setMemo(transaction.getMemo());
        dto.setDate(transaction.getDate());

        return dto;
    }

    @Transactional
    public void updateTransaction(Long transactionId, Long userId, UpdateDto updateDto) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("거래내역이 없습니다."));

        if(!transaction.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        transaction.update(
                updateDto.getType(),
                updateDto.getAmount(),
                updateDto.getCategory(),
                updateDto.getMemo(),
                updateDto.getDate()
        );
    }

}

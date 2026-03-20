package chdaeseung.accountbook.transaction.service;

import chdaeseung.accountbook.transaction.dto.CreateDto;
import chdaeseung.accountbook.transaction.dto.ResponseDto;
import chdaeseung.accountbook.transaction.entity.Transaction;
import chdaeseung.accountbook.transaction.repository.TransactionRepository;
import chdaeseung.accountbook.user.entity.User;
import chdaeseung.accountbook.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}

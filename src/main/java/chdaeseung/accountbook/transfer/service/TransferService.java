package chdaeseung.accountbook.transfer.service;

import chdaeseung.accountbook.bank.entity.BankAccount;
import chdaeseung.accountbook.bank.repository.BankAccountRepository;
import chdaeseung.accountbook.category.entity.Category;
import chdaeseung.accountbook.category.repository.CategoryRepository;
import chdaeseung.accountbook.global.exception.CustomException;
import chdaeseung.accountbook.global.exception.ErrorCode;
import chdaeseung.accountbook.transaction.entity.ExpenseType;
import chdaeseung.accountbook.transaction.entity.Transaction;
import chdaeseung.accountbook.transaction.entity.TransactionType;
import chdaeseung.accountbook.transaction.repository.TransactionRepository;
import chdaeseung.accountbook.transfer.dto.TransferRequestDto;
import chdaeseung.accountbook.transfer.entity.Transfer;
import chdaeseung.accountbook.transfer.repository.TransferRepository;
import chdaeseung.accountbook.user.entity.User;
import chdaeseung.accountbook.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TransferService {

    private final TransferRepository transferRepository;
    private final UserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;

    public void createTransfer(Long userId, TransferRequestDto requestDto) {
        System.out.println(requestDto.getFromAccountId() + " >> " + requestDto.getToAccountId() + " amount = " + requestDto.getAmount());
        validateRequest(requestDto);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        BankAccount fromAccount = bankAccountRepository.findByIdAndUserId(requestDto.getFromAccountId(), userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        BankAccount toAccount = bankAccountRepository.findByIdAndUserId(requestDto.getToAccountId(), userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        String memo = requestDto.getMemo() == null ? "" : requestDto.getMemo().trim();

        if(fromAccount.getId().equals(toAccount.getId())) {
            throw new CustomException(ErrorCode.SAME_ACCOUNT_ID);
        }

        if(fromAccount.getBalance() < requestDto.getAmount()) {
            throw new CustomException(ErrorCode.INSUFFICIENT_BALANCE);
        }

        fromAccount.decreaseBalance(requestDto.getAmount());
        toAccount.increaseBalance(requestDto.getAmount());

        Transfer transfer = Transfer.builder()
                .amount(requestDto.getAmount())
                .memo(memo)
                .date(requestDto.getDate())
                .fromAccount(fromAccount)
                .toAccount(toAccount)
                .user(user)
                .build();

        transferRepository.save(transfer);
    }

    private void validateRequest(TransferRequestDto requestDto) {
        if(requestDto.getDate() == null) {
            throw new CustomException(ErrorCode.INSERT_DATE);
        }

        if(requestDto.getFromAccountId() == null || requestDto.getToAccountId() == null) {
            throw new CustomException(ErrorCode.CHOOSE_ACCOUNT);
        }

        if(requestDto.getAmount() == null || requestDto.getAmount() <= 0) {
            throw new CustomException(ErrorCode.MINIMUM_AMOUNT);
        }
    }
}

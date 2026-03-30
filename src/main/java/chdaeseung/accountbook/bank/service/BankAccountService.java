package chdaeseung.accountbook.bank.service;

import chdaeseung.accountbook.bank.dto.BankAccountOptionDto;
import chdaeseung.accountbook.bank.dto.BankAccountRequestDto;
import chdaeseung.accountbook.bank.dto.BankAccountResponseDto;
import chdaeseung.accountbook.bank.entity.BankAccount;
import chdaeseung.accountbook.bank.repository.BankAccountRepository;
import chdaeseung.accountbook.global.exception.CustomException;
import chdaeseung.accountbook.global.exception.ErrorCode;
import chdaeseung.accountbook.user.entity.User;
import chdaeseung.accountbook.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long create(Long userId, BankAccountRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        BankAccount bankAccount = BankAccount.builder()
                .bankName(requestDto.getBankName())
                .accountName(requestDto.getAccountName())
                .balance(requestDto.getBalance())
                .type(requestDto.getType())
                .used(requestDto.isUsed())
                .user(user)
                .build();

        return bankAccountRepository.save(bankAccount).getId();
    }

    public List<BankAccountResponseDto> findAll(Long userId) {
        return bankAccountRepository.findAllByUserIdOrderByIdDesc(userId)
                .stream()
                .map(BankAccountResponseDto::new)
                .toList();
    }

    public BankAccountResponseDto findById(Long userId, Long id) {
        BankAccount bankAccount = bankAccountRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        return new BankAccountResponseDto(bankAccount);
    }

    @Transactional
    public void update(Long userId, Long id, BankAccountRequestDto requestDto) {
        BankAccount bankAccount = bankAccountRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        bankAccount.update(
                requestDto.getBankName(),
                requestDto.getAccountName(),
                requestDto.getBalance(),
                requestDto.getType(),
                requestDto.isUsed()
        );
    }

    @Transactional
    public void delete(Long userId, Long id) {
        BankAccount bankAccount = bankAccountRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        bankAccountRepository.delete(bankAccount);
    }

    @Transactional(readOnly = true)
    public List<BankAccountOptionDto> getUsedOptions(Long userId) {
        return bankAccountRepository.findAllByUserIdAndUsedTrueOrderByIdDesc(userId)
                .stream()
                .map(account -> new BankAccountOptionDto(
                        account.getId(), account.getBankName() + " - " + account.getAccountName()
                ))
                .toList();
    }

}

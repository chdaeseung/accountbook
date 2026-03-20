package chdaeseung.accountbook.user.service;

import chdaeseung.accountbook.user.dto.SignupRequestDto;
import chdaeseung.accountbook.user.entity.User;
import chdaeseung.accountbook.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void signup(SignupRequestDto requestDto) {
        existsUser(requestDto);

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        User user = new User(requestDto.getUsername(), encodedPassword, requestDto.getEmail());

        userRepository.save(user);
    }

    private void existsUser(SignupRequestDto requestDto) {
        if(userRepository.existsByUsername(requestDto.getUsername())) {
            throw new IllegalArgumentException("사용중인 아이디입니다.");
        }

        if(userRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("사용중인 이메일입니다.");
        }
    }
}

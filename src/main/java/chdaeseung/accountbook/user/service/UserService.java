package chdaeseung.accountbook.user.service;

import chdaeseung.accountbook.user.dto.LoginRequestDto;
import chdaeseung.accountbook.user.dto.LoginUserDto;
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

    public User login(LoginRequestDto requestDto) {
        User user = userRepository.findByUsername(requestDto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."));

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        return user;
    }
}

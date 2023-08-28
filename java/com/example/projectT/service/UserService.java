package com.example.projectT.service;

import com.example.projectT.domain.entity.User;
import com.example.projectT.dto.UserDto;
import com.example.projectT.repository.UserHistoryRepository;
import com.example.projectT.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserDto register(UserDto userDto) {
        return entityToDto(userRepository.save(dtoToEntity(userDto)));
    }

    public User dtoToEntity(UserDto userDto) {
        var entity = User.builder()
                .email(userDto.getEmail())
                .nickName(userDto.getNickName())
                .pw(userDto.getPw())
                .build();
        return entity;
    }

    public UserDto entityToDto(User user) {
        var dto = UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickName(user.getNickName())
                .pw(user.getPw())
                .createAt(user.getCreateAt())
                .build();
        return dto;
    }

    private List<UserDto> toList(List<User> list) {
        return list.stream().map(entity -> entityToDto(entity)).collect(Collectors.toList());
    }

    //유저 중복 확인
    public boolean isExistsUserInfo(String name, String value) {

        boolean isExists = false;

        if (name.equals("email")) {
            isExists = userRepository.findByEmail(value).isPresent();
        } else if (name.equals("nickName")) {
            isExists = userRepository.findByNickName(value).isPresent();
        }

        return isExists;
    }

    public void update(UserDto userDto) {

        var userCheck = userRepository.findByEmailAndPw(userDto.getEmail(),userDto.getPw());

        userCheck.ifPresent(e->{
            e.setNickName(userDto.getNickName());
            e.setPw(userDto.getPw());
            userRepository.save(e);
        });
    }

    //user delete
    public void delete(UserDto userDto, String pw){
        var userCheck = userRepository.findByEmail(userDto.getEmail());

        if(userCheck.isPresent()){
            userCheck.ifPresent(e->{
                if(e.getPw().equals(pw)) {
                    userRepository.delete(e);
                    System.out.println("회원 탈퇴되었습니다.");
                } else {
                    System.out.println("회원정보가 일치하지 않습니다.");
                }
            });
        } else {
            System.out.println("회원정보가 일치하지 않습니다.");
        }
    }
}

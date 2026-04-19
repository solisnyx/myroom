package com.myroom.user;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myroom.common.NotFoundException;
import com.myroom.user.dto.UserRoleUpdateRequest;
import com.myroom.user.dto.UserUpdateRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("user.notFound", id));
    }

    @Transactional
    public User updateProfile(Long id, UserUpdateRequest request) {
        User user = findById(id);
        if (request.getNickname() != null && !request.getNickname().isBlank()) {
            user.setNickname(request.getNickname());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        return user;
    }

    @Transactional
    public User changeRole(Long id, UserRoleUpdateRequest request) {
        User user = findById(id);
        user.setRole(request.getRole());
        return user;
    }

    @Transactional
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("user.notFound", id);
        }
        userRepository.deleteById(id);
    }
}

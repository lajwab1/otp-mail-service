package com.OtpSending.Repositories;

import com.OtpSending.Dto.UserDTO;
import com.OtpSending.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    User findByEmail(String email);
}

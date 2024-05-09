package com.delmark.portfoilo.service.interfaces;

import com.delmark.portfoilo.models.DTO.UserDto;
import com.delmark.portfoilo.models.Portfolio;
import com.delmark.portfoilo.models.User;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void registration(UserDto registrationDto);
    User getUserByAuth(Authentication authentication);
    List<User> getAllUsers();
    List<User> getUsersWithPortfolio();
    Portfolio getPortfolioByUser(String username);
}

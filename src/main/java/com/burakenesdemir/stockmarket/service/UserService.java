package com.burakenesdemir.stockmarket.service;

import com.burakenesdemir.stockmarket.controller.UserController;
import com.burakenesdemir.stockmarket.controller.mapper.UserMapper;
import com.burakenesdemir.stockmarket.data.User;
import com.burakenesdemir.stockmarket.data.UserRepository;
import com.burakenesdemir.stockmarket.dto.UserDto;
import com.burakenesdemir.stockmarket.exception.BadRequestException;
import com.burakenesdemir.stockmarket.exception.NoDataFoundError;
import com.burakenesdemir.stockmarket.security.api.SecurityService;
import com.burakenesdemir.stockmarket.type.UserStatus;
import com.burakenesdemir.stockmarket.util.ConfigLoaderUtil;
import com.burakenesdemir.stockmarket.type.MailAttributes;
import com.burakenesdemir.stockmarket.util.RandomGenerate;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.burakenesdemir.stockmarket.util.ErrorUtil.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    private final MailService mailService;

    private final PasswordEncoder passwordEncoder;

    private final SecurityService securityService;

    public User getUserInfo(){
        User user = userRepository.findByUsername(securityService.getUsername());

        return user;
    }
    public User register(UserDto userDto) {
        User user = userMapper.toEntity(userDto);

        if (user.getCellphone() != null) {
            user.setCellphone(formatPhoneNumber(user.getCellphone()));
        }

        validateUser(user, userDto);

        String activationCode = new RandomGenerate().get(32);
        user.setActivationCode(activationCode);
        String activationLinkStr = ConfigLoaderUtil.getProperty("stock-market.host") + ConfigLoaderUtil.getProperty("stock-market.service.activate") + activationCode;
        String subject = MailAttributes.ACTIVATION_CODE.getName();
        mailService.sendUserRegisterActivationMail(user, subject, activationLinkStr);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return user;
    }

    public void activationControl(String code) {
        User user = userRepository.findByActivationCode(code);

        if (user == null) {
            LOGGER.info("activation link : user can not find");
            throw new BadRequestException(ACTIVATION_CODE_ERROR);
        }

        user.setUserStatus(UserStatus.ACTIVE);
        user.setActivationCode(null);
        String subject = MailAttributes.ACTIVATION_SUCCESS.getName();
        mailService.sendActivationSuccessMail(user, subject);
        userRepository.save(user);
    }

    public void resetPassword(String email) {
        User user = userRepository.findByUserEmail(email);

        if (user == null) {
            LOGGER.error("{} : [{message} ]", UserService.class.getName(), USER_NOT_FOUND_ERROR);
            throw new NoDataFoundError(USER_NOT_FOUND_ERROR);
        }

        String passwordResetCode = new RandomGenerate().get(32);
        user.setPasswordActivationCode(passwordResetCode);
        userRepository.save(user);
        String subject = MailAttributes.RESET_PASSWORD.getName();
        String passwordResetUri = ConfigLoaderUtil.getProperty("stock-market.host") + ConfigLoaderUtil.getProperty("stock-market.service.reset-password") + passwordResetCode;
        mailService.sendUserResetPasswordMail(user, passwordResetUri, subject);

    }

    public void changePassword(String key, String newPassword) {
        User user = userRepository.findByPasswordActivationCode(key);

        if (user == null) {
            LOGGER.error("{} : [{message} ]", UserService.class.getName(), USER_NOT_FOUND_ERROR);
            throw new NoDataFoundError(USER_NOT_FOUND_ERROR);
        }
        if (key == null || key.isEmpty() || key.isBlank()) {
            LOGGER.error("{} : [{message} ]", UserService.class.getName(), USER_KEY_NOT_NULL_OR_EMPTHY);
            throw new BadRequestException(USER_KEY_NOT_NULL_OR_EMPTHY);
        }
        String encryptedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encryptedPassword);
        user.setPasswordActivationCode(null);
        LOGGER.info("Password : " + newPassword);
        userRepository.save(user);
    }

    private void validateUser(User user, UserDto userDto) {
        if (userDto.getUsername().equals(userDto.getPassword())) {
            LOGGER.info("{}: [ Duplicate username and password, username: '{}' ]", UserController.class.getSimpleName(), user.getUsername());
            throw new BadRequestException(DUPLICATE_USERNAME_PASSWORD_ERROR);
        } else if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            LOGGER.info("{}: [ Password validation failed, username: '{}' ]", UserController.class.getSimpleName(), user.getUsername());
            throw new BadRequestException(PASSWORD_VALIDATION_ERROR);
        } else if (userRepository.findByUsername(user.getUsername()) != null) {
            LOGGER.info("{}: [ Username/email uniqueness violated, username: '{}']", UserController.class.getSimpleName(), user.getUsername());
            throw new BadRequestException(DUPLICATE_USERNAME_ERROR);
        } else if (userRepository.findByUserEmail(user.getUserEmail()) != null) {
            LOGGER.info("{}: [ Register form same user - email error username: '{}']", UserController.class.getSimpleName(), user.getUsername());
            throw new BadRequestException(DUPLICATE_EMAIL_ERROR);
        }
    }

    public static String formatPhoneNumber(String phoneNumber) {
        String phone = phoneNumber.replaceAll("^0|\\(|\\)|\\s", "");
        if (phone.length() > 10) {
            phone = phone.substring(phone.length() - 10);
        }
        if (phone.matches("[0-9]+") && phone.length() == 10) {
            return phone;
        }

        throw new BadRequestException(INVALID_CELLPHONE_ERROR);
    }
}
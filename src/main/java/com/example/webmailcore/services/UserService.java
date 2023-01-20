package com.example.webmailcore.services;

import com.example.webmailcore.auth.CustomUserDetails;
import com.example.webmailcore.enums.MailMessageStatus;
import com.example.webmailcore.enums.MailTemplateType;
import com.example.webmailcore.models.User;
import com.example.webmailcore.models.idm.ForgotPasswordToken;
import com.example.webmailcore.models.mail.MailMessage;
import com.example.webmailcore.repositories.UserRepository;
import com.example.webmailcore.repositories.specifications.UserSpecification;
import com.example.webmailcore.services.mail.MailMessageService;
import com.example.webmailcore.services.settings.SystemSettingsService;
import com.example.webmailcore.utils.BasicSystemSettingsProps;
import com.example.webmailcore.utils.Crypto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.net.URLDecoder;
import java.util.*;

@Service
public class UserService {

    @Autowired
    UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Lazy
    @Autowired
    MailMessageService mailMessageService;


    @Lazy
    @Autowired
    SystemSettingsService systemSettingsService;

    @Value("${app.base.url}")
    String BASE_URL;


    public Page<User> all(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<User> allAirplaneCompanyMembers(String airplaneCompanyId) {
        return repository.findAllByAirplaneCompany_Id(airplaneCompanyId);
    }

    public Page<User> all(Map<String, Object> params, Pageable pageable) {
        UserSpecification userSpecification = new UserSpecification();
        Iterator<Map.Entry<String, Object>> iterator = params.entrySet().iterator();
        return repository.findAll(userSpecification, pageable);
    }

    public User get(String id) {
        User user = repository.getById(id);
        return user;
    }

    public User getUserByUsername(String username) {
        return repository.findByUsername(username);
    }

    public Page<User> findAllByGroupId(String id, String username, Pageable pageable) {
        String orgId = null;
        CustomUserDetails customUserDetails = getCurrentUserDetails();
        if (!customUserDetails.getAuthorities().stream().anyMatch(r -> r.getAuthority().startsWith("ROLE_ADMINISTRATION"))) {
            orgId = getCurrentUser().getAirplaneCompany().getId();
        }
        return repository.findAllByGroups_IdAndUsernameContainsAndAirplaneCompany_IdContains(id, username, orgId, pageable);
    }

    public Page<User> findAllByGroupIdNot(String id, String username, Pageable pageable) {
        String orgId = null;
        CustomUserDetails customUserDetails = getCurrentUserDetails();
        if (!customUserDetails.getAuthorities().stream().anyMatch(r -> r.getAuthority().startsWith("ADMINISTRATION"))) {
            orgId = getCurrentUser().getAirplaneCompany().getId();
        }
        List<User> users = repository.findAllByGroups_IdAndUsernameContainsAndAirplaneCompany_IdContains(id, username, orgId);
        List<String> idList = new ArrayList<>();
        if (users != null && users.size() > 0) {
            for (User u : users
            ) {
                idList.add(u.getId());
            }
        } else {
            idList.add(null);
        }

        return repository.findAllByIdNotInAndUsernameContainsAndAirplaneCompany_IdContains(idList, username, orgId, pageable);
    }

    public User save(User user) {
        if (user.getId() == null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            User existingUser = get(user.getId());
            user.setGroups(existingUser.getGroups());
            if (!StringUtils.isEmpty(user.getNewPassword())) {
                user.setPassword(passwordEncoder.encode(user.getNewPassword()));
            } else {
                user.setPassword(repository.getById(user.getId()).getPassword());
            }
        }
        User savedUser = repository.save(user);
        return savedUser;
    }


    public CustomUserDetails getCurrentUserDetails() {
        if (SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null) {
            Object principal = SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            if (principal instanceof CustomUserDetails) {
                return ((CustomUserDetails) principal);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public User getCurrentUser() {
        if (SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null) {
            Object principal = SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            if (principal instanceof CustomUserDetails) {
                String userId = ((CustomUserDetails) principal).getUserId();
                return get(userId);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Transactional
    public void forgotPasswordRequest(String email) throws Exception {
        User user = repository.findByEmail(email);
        if (user != null) {
            Crypto crypto = new Crypto();
            ObjectMapper mapper = new ObjectMapper();
            ForgotPasswordToken forgotPasswordToken = new ForgotPasswordToken();
            forgotPasswordToken.setUserId(user.getId());
            //set valid until 30 minutes from now
            forgotPasswordToken.setValidUntil(new Date(System.currentTimeMillis() + 30 * 60 * 1000));
            String token = crypto.encrypt(mapper.writeValueAsString(forgotPasswordToken));
            //send email with token
            MailMessage mailMessage = new MailMessage();
            mailMessage.setSender(systemSettingsService.getSystemSettingsPropByKey(BasicSystemSettingsProps.SMTP_USERNAME).getValue());
            mailMessage.setReceivers(List.of(email));
            mailMessage.setStatus(MailMessageStatus.PENDING);
            HashMap<String, String> params = new HashMap<>();
            params.put("subject", "Forgot password request");
            params.put("subtitle", BASE_URL + "/reset-password?token=" + token);
            mailMessageService.save(mailMessage, MailTemplateType.MAIL_TEMPLATE_FORGOT_PASSWORD, params);
        }
    }

    public Boolean checkIfPasswordRequestIsValid(String token) throws Exception {
        Crypto crypto = new Crypto();
        ObjectMapper mapper = new ObjectMapper();
        try {
            ForgotPasswordToken forgotPasswordToken = mapper.readValue(crypto.decrypt(URLDecoder.decode(token.replaceAll("%20", "%2b"))), ForgotPasswordToken.class);
            if (forgotPasswordToken.getValidUntil().after(new Date())) {
                return Boolean.TRUE;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }


    public User forgotPasswordReset(String token, String newPassword) throws Exception {
        Crypto crypto = new Crypto();
        ObjectMapper mapper = new ObjectMapper();
        try {
            ForgotPasswordToken forgotPasswordToken = mapper.readValue(crypto.decrypt(URLDecoder.decode(token.replaceAll("%20", "%2b"))), ForgotPasswordToken.class);
            if (forgotPasswordToken.getValidUntil().after(new Date())) {
                User user = repository.getById(forgotPasswordToken.getUserId());
                user.setPassword(passwordEncoder.encode(newPassword));
                repository.save(user);
                return user;
            } else {
                throw new Exception("Token is not valid");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Token is not valid");
        }
    }

    public User resetPassword(String username, String newPassword, String oldPassword) {
        User tmp = repository.findByUsername(username);
        if (tmp != null) {
            if (passwordEncoder.matches(oldPassword, tmp.getPassword())) {
                tmp.setPassword(passwordEncoder.encode(newPassword));
                repository.save(tmp);
                return tmp;
            } else {
                throw new IllegalArgumentException("Old password is not correct");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public Boolean remove(String id) {
        User user = repository.getById(id);
        repository.delete(user);
        return Boolean.TRUE;
    }

    public User findByEmail(String email) {
        return repository.findByEmail(email);
    }
}

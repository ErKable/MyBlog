package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.dto.request.SignInRequest;
import it.cgmconsulting.myblog.dto.request.SignUpRequest;
import it.cgmconsulting.myblog.dto.response.JwtAuthenticationResponse;
import it.cgmconsulting.myblog.entity.enumerated.Role;
import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.exception.UserAlreadyExistsException;
import it.cgmconsulting.myblog.repository.UserRepository;
import it.cgmconsulting.myblog.exception.EmailAlreadyExistsException;
import it.cgmconsulting.myblog.exception.UserNotExistsException;
import it.cgmconsulting.myblog.service.mail.MailService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final MailService mailService;

    public String signup(SignUpRequest request) throws UserAlreadyExistsException {
        //isPresent ci dice se il wrapper optional e pieno o vuoto
        if (userRepository.existsByEmailOrUsername(request.email(), request.username())) {
            throw new UserAlreadyExistsException();
        }
        //se il wrapper è vuoto builda uno user e lo salva sul db
        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.GUEST)
                .firstname(request.firstname())
                .lastname(request.lastname())
                .build();
        String jwt = jwtService.generateToken(user);
        userRepository.save(user);
        try {
            mailService.sendMail(mailService.createMail(user,
                    "MyBlog: Confirm account",
                    "Click here to confirm your registration: " +
                            "http://localhost:8080/api/auth/confirm/"+jwt));
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
        }


        return "User Successfully registered: please check your mail to activate your account";
    }

    public JwtAuthenticationResponse signin(SignInRequest request) throws UserNotExistsException {
        var user = userRepository.findByEmail(request.email()).orElseThrow(() -> new UserNotExistsException("User "+request.email()+" not found"));
        String msg = "you are banned until "+user.getBannedUntil();

        if(!user.isEnabled() && user.getRole().equals(Role.GUEST)){
            msg = "Your email has not been validated yet";
            return new JwtAuthenticationResponse(null, msg);
        }

        if(user.getBannedUntil() != null && user.getBannedUntil().isAfter(LocalDateTime.now())){
            return new JwtAuthenticationResponse(null, msg);
        }
        if(user.getBannedUntil() != null && user.getBannedUntil().isBefore(LocalDateTime.now())){
            user.setBannedUntil(null);
            user.setEnabled(true);
            userRepository.save(user);
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(),
                request.password()));

        var jwt = jwtService.generateToken(user);

        return new JwtAuthenticationResponse(jwt, null);
    }
    @Transactional //questa notazione fa si che io posso raggruppare
    //un insieme di operazioni sul db in un'unica transazione, questo
    //fa si che se una delle operaizone non va a buon fine reverta tutto
    public boolean confirm(String jwt){

            User user = userRepository.findByEmail(jwtService.extractUserName(jwt)).orElseThrow();
            if(!user.isEnabled() && user.getRole().equals(Role.GUEST)) {
                user.setEnabled(true);
                user.setRole(Role.MEMBER);
                return true;
            } else
                return false;
    }
}
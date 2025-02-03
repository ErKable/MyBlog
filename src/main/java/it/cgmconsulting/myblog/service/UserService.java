package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.dto.request.ChangePwdRequest;
import it.cgmconsulting.myblog.dto.request.ModifyMeRequest;
import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.entity.enumerated.Role;
import it.cgmconsulting.myblog.repository.UserRepository;
import it.cgmconsulting.myblog.service.mail.Mail;
import it.cgmconsulting.myblog.service.mail.MailService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    public boolean modifyMe(UserDetails userDetails, ModifyMeRequest request){
        User userToModify = (User) userDetails;
        boolean send = false;

        if(userRepository.existsByEmailAndIdIsNot(request.email(), userToModify.getId()))
            return true;

        if(!userToModify.getEmail().equals(request.email())){
            userToModify.setEmail(request.email());
            userToModify.setRole(Role.GUEST);
            userToModify.setEnabled(false);
            send = true;
        }
        userToModify.setFirstname(request.firstName());
        userToModify.setLastname(request.lastName());
        userToModify.setBio(request.bio());
        userRepository.save(userToModify);

        if(send) {
            String jwt = jwtService.generateToken(userToModify);
            try {
                mailService.sendMail(
                        mailService.createMail(userToModify,
                                "MyBlog: mail update",
                                "Hello " + userToModify.getUsername() + "\n\n click here to confirm your new email: http://localhost8080api/auth/confirm/" + jwt));
                return true;
            } catch (MessagingException e) {
                System.out.println(e.getMessage());
            }
        }
        return false;
    }

    public String changePassword(ChangePwdRequest request, UserDetails userDetails){
        String msg = "Password has been updated";
        //recuperare la vecchia pw dell'utente
        String currentPwd = userDetails.getPassword();
        //verificare se oldpw == pwd db
        if(passwordEncoder.matches(request.oldPwd(), currentPwd))
            //se si, verificare che le due nuove siano uguali
            if(request.newPwd().equals(request.newPwd2())) {
                //aggiornare la pw nel db
                User user = (User) userDetails;
                user.setPassword(passwordEncoder.encode(request.newPwd()));
                userRepository.save(user);
            } else
                msg = "New password and Confirm password don't match";
        else
            msg = "wrong current password";
        return msg;
    }

    @Transactional
    public String changeRole(String username, Role newRole){
        if(newRole.equals(Role.GUEST))
            return "You can't downgrade a registered user to GUEST";

        User user = userRepository.findByUsername(username).orElseThrow();

        if(user.getRole().equals(Role.ADMIN))
            return "You can't change the role of another admin";

/*        inutile
            if(user.getRole().equals(Role.GUEST) && !user.isEnabled())
            return "You can't change the role of a GUEST user";*/

        if(user.getRole() != Role.GUEST  && !user.isEnabled())
            return "You can't change the role of a banned user";

        user.setRole(newRole);
        //userRepository.save(user); -> usiamo la notation transactional
        return username+"'s role has been changed to: "+newRole;
    }
}

package ejb;

import jsf.UserPrincipal;

import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;
import javax.xml.registry.infomodel.User;
import java.util.Optional;

@ApplicationScoped
public class UserServiceIdentityStore implements IdentityStore {

    @Inject
    private UserService userService;

    @Override
    CredentialValidationResult validate(Credential credential){
        usernamePasswordCredential login=
                (userNamePasswordCredential) credential;
        String email= login.getCaller();
        String password = login.getPasswordAsString();

        Optional<User> optionalUser =
                userService.findByEmailAndPassword(email, password);

        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            return new CredentialValidationResult(
                    new UserPrincipal(user),
                    user.getRoleAsStrings()

            );
        } else {
            return CredentialValidationResult.INVALID_RESULT;
        }
    }


}

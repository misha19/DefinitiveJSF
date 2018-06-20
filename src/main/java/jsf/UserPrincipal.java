package jsf;

import javax.security.auth.message.callback.CallerPrincipalCallback;
import javax.xml.registry.infomodel.User;

public class UserPrincipal extends CallerPrincipalCallback{

    private final User user;
    public UserPrincipal(User user){
        super(user.getEmail());
        this.user=user;

    }
    public User getUser(){
        return user;
    }

}

package jsf;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.core.SecurityContext;

import java.io.IOException;

import static javax.security.auth.message.AuthStatus.SEND_CONTINUE;
import static javax.security.auth.message.AuthStatus.SEND_FAILURE;
import static javax.security.auth.message.AuthStatus.SUCCESS;

@Named  @RequestScoped
public class LoginBean {

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min= 8 , message = "Password must be at leat 8 charachters")
    private String password;

    @Inject
    private SecurityContext securityContext;

    @Inject
    private ExternalContext externalContext;

    @Inject
    private FacesContext facesContext;

    @Inject
    private Flash flash;

    @Inject @ManagedProperty("#{param.new}")
    private boolean inNew;

    public void submit() throws IOException {
        switch (continueAuthentication()){
            case SEND_CONTINUE:
                facesContext.responseComplete();
                break;

            case SEND_FAILURE:
                facesContext.addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR
                ,"Login Failed",null));
                break;

            case SUCCESS:
                flash.setKeepMessages(true);
                facesContext.addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Login succeed",null));
                externalContext.redirect(
                        externalContext.getRequestContextPath()+"/index.xhtml");
                break;

            case NOT_DONE:
                //Does not happen here
        }
    }
    private AuthenticationStatus continueAuthentication() {
        return securityContext.authenticate(
                (HttpServletRequest) externalContext.getRequest(),
                (HttpServletResponse) externalContext.getResponse(),
                AuthenticationParameters.withParams()
                        .newAuthentication(inNew).credential(
                                new UsernamePasswordCredential(email, password)));

    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email= email;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password=password;
    }

}

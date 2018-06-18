package ejb;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.bean.ApplicationScoped;
import java.util.stream.Collectors;

@CustomFormAuthenticatiomMechanism(
        loginToContinue = @LoginToContinue(
                loginPage= "/login.xhtml",
                useForwardToLogin = false,
                errorPage = ""
        )
)
@FacesConfig @ApplicationScoped
@Alternative @Priority(500)
public class ApplicationConfigBean {

    @Produces
    public HttpAuthenticationMechanism produceAuthenticationMechanism(
            InterceptionFactory<HttpAuthenticationMechanismWrapper>
            interceptionFactory,BeanManager beanManager
    ){
        @RememberMe
                class RememberMeClass{};
        interceptionFactory.configure().add(
                RememberMeClass.class.getAnnotation(RememberMe.class));
        return interceptionFactory.createInterceptedInstance(
                new HttpAuthenticationMechanismWrapper(
                        (HttpAuthenticationMechanism)beanManager
                        .getReference(beanManager
                        .resolve(beanManager
                        .getBeans(HttpAuthenticationMechanism.class).stream()
                        .filter(b-> b.getBeanClass() !=ApplicationConfigBean.class)
                        .collect(Collectors.toSet())),
                                HttpAuthenticationMechanism.class,
                                beanManager.createCreationalContext(null))
                )
        );
    }



}

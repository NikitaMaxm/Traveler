package traveler.module.domain.usecases.user;

import traveler.module.domain.repository.UserDomainRepository;

public class UserLoginUserCase {

    private final UserDomainRepository repository;

    public UserLoginUserCase(UserDomainRepository repository) {
        this.repository = repository;
    }

    public String login(String login, String pass){
        return this.repository.login(login, pass);
    }
}

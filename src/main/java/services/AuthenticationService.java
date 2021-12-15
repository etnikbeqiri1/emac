package services;

import exceptions.GenericException;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import models.User;
import repositories.UserRepository;

import java.util.Optional;

public class AuthenticationService {
    private static AuthenticationService authenticationService = null;
    public static User user;

    public void login(String username,String password) throws GenericException {
        UserRepository userRepository = new UserRepository();
        if(username == null || username.isEmpty()){
            throw new GenericException("Please enter your username");
        }
        if(password == null || password.isEmpty()){
            throw new GenericException("Please enter your password");
        }
        if(!userRepository.checkLogin(username,password)){
            throw new GenericException("Please enter a valid username and password");
        }
    }

    private AuthenticationService(){

    }

    public static Optional<User> getUser() {
        return Optional.of(user);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static void logOut(){
        if(authenticationService.getUser().isPresent()){
            authenticationService.setUser(null);
        }
    }

    public static AuthenticationService getInstance()
    {
        if (authenticationService == null)
            authenticationService = new AuthenticationService();

        return authenticationService;
    }
}

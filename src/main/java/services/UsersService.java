package services;

import exceptions.GenericException;
import helpers.StringHelpers;
import lombok.Data;
import models.Group;
import models.User;
import repositories.UserRepository;

import java.util.Iterator;
import java.util.List;

@Data
public class UsersService {
    private int maxPages;
    private UserRepository userRepository;

    public UsersService(){
        userRepository = new UserRepository();
    }

    public List<User> search(String keyword, int page, int elementsPerPage, String sortBy, Boolean asc) {
        int from = (page - 1) * elementsPerPage;
        maxPages = 1+(int) Math.ceil(userRepository.countResults(keyword)/elementsPerPage);
        return userRepository.listUsers(keyword, from, elementsPerPage, StringHelpers.titleCaseToSnakeCase(sortBy), asc);
    }

    public String getTableUser(){
        List<User> users = userRepository.listAllUsers();
        Iterator<User> userIterator = users.iterator();
        String tableContent = "";
        while(userIterator.hasNext()){
            User user = userIterator.next();
            tableContent += "<tr>";
            tableContent += "<td>"+user.getId()+"</td>";
            tableContent += "<td>"+user.getFullName()+"</td>";
            tableContent += "<td>"+user.getUsername()+"</td>";
            tableContent += "<td>"+user.getRole()+"</td>";
            tableContent += "<td>"+user.getSubject()+"</td>";

            tableContent += "</tr>";
        }
        tableContent += "</tbody>";
        tableContent += "</table>";
        return tableContent;
    }

    public void addUser(User user) throws GenericException{
        validateUser(user);

        if(userRepository.existsByUsername(user.getUsername().toLowerCase()))
            throw new GenericException("A user with that username already exists");

        user.setUsername(user.getUsername().toLowerCase());

        userRepository.save(user);
    }

    public void removeUser(User user){
        userRepository.delete(user);
    }

    public void editUser(User user) throws GenericException {
        validateUser(user);

        userRepository.update(user);
    }

    private void validateUser(User user) throws GenericException {
        if(user.getFullName() == null || user.getFullName().isEmpty()) throw new GenericException("Please enter users Full name");
        if(user.getPassword() == null || user.getPassword().isEmpty()) throw new GenericException("Please enter a password for the user");
        if(user.getUsername() == null || user.getUsername().isEmpty()) throw new GenericException("Please enter a username for the user");
        if(user.getRole() == null) throw new GenericException("Please enter a role for the user");
        if(user.getSubject() == null) throw new GenericException("Please enter a role for the user");


    }


}

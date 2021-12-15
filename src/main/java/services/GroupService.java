package services;

import exceptions.GenericException;
import helpers.StringHelpers;
import javafx.scene.control.Alert;
import lombok.Data;
import models.Group;
import models.Student;
import models.Subject;
import repositories.GroupRepository;

import java.util.Iterator;
import java.util.List;

@Data
public class GroupService {
    private int maxPages;
    private GroupRepository groupRepository;

    public GroupService(){
        groupRepository = new GroupRepository();
    }

    public List<Group> search(String keyword, int page, int elementsPerPage, String sortBy, Boolean asc) {
        int from = (page - 1) * elementsPerPage;
        maxPages = 1+(int) Math.ceil(groupRepository.countResults(keyword)/elementsPerPage);
        return groupRepository.listGroups(keyword, from, elementsPerPage, StringHelpers.titleCaseToSnakeCase(sortBy), asc);
    }

    public List<Group> allGroups(){
        return groupRepository.getGroups();
    }

    public String getTableGroup(){
        List<Group> groups = groupRepository.listAllGroups();
        Iterator<Group> groupIterator = groups.iterator();
        String tableContent = "";
        while(groupIterator.hasNext()){
            Group group = groupIterator.next();
            tableContent += "<tr>";
            tableContent += "<td>"+group.getId()+"</td>";
            tableContent += "<td>"+group.getName()+"</td>";

            tableContent += "</tr>";
        }
        tableContent += "</tbody>";
        tableContent += "</table>";
        return tableContent;
    }

    public List<Group> myGroups(){
        return groupRepository.getGroups(AuthenticationService.getInstance().getUser().get());
    }

    public void addGroup(String group) throws GenericException {
        if(group == null || group.isEmpty()) throw new GenericException("Please enter group name");
        if(groupRepository.existsByName(StringHelpers.titleCase(group)))
            throw new GenericException("A group with that name already exists");
        Group group1 = new Group();
        group1.setName(StringHelpers.titleCase(group));
        group1.setProfessor(AuthenticationService.getInstance().getUser().get());
        groupRepository.save(group1);
    }

    public void removeGroup(Group group){
        groupRepository.delete(group);
    }

    public void editGroup(Group group) throws GenericException {
        if(group.getName() == null || group.getName().isEmpty()) throw new GenericException("Please enter group name");
        if(groupRepository.existsByName(StringHelpers.titleCase(group.getName())))
            throw new GenericException("A group with that name already exists");

        groupRepository.update(group);
    }
}

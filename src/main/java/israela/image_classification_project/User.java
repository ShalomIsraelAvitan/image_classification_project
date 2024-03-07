package israela.image_classification_project;

import org.springframework.data.mongodb.core.mapping.Document;

import com.vaadin.flow.component.template.Id;

@Document(collection = "users")
public class User {
    
    @Id
    private Long id;
    private String name;
    private int pw;
    private boolean isAdmin;

    public User(Long id, String name, int pw) {
        this.id = id;
        this.name = name;
        this.pw = pw;
        this.isAdmin = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPw() {
        return pw;
    }

    public void setPw(int pw) {
        this.pw = pw;
    }

    public boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Override
    public String toString() {
        return "User {id=" + id + ", name=" + name + ", pw=" + pw + ", isAdmin=" + isAdmin + "}";
    } 
}

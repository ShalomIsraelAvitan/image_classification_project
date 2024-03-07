package israela.image_classification_project;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "photos")
public class Photo {
    
    private String id;
    private String name;
    private String nameUser; //שם המשתמש שהעלה את התמונה
    private LocalDateTime dateTime;
    private byte[] contend; //photo file contend (pixels data)
    private String classification;
    private Long idOfUser;

    public String getID() { return this.id;}

    public Photo()
    {
        
    }
    public Photo(String name, String nameUser, byte[] contend) {
        this.name = name;
        this.nameUser = nameUser;
        this.dateTime = LocalDateTime.now();
        this.contend = contend;
        this.classification = "Null classification";
        this.idOfUser = null;
    }
    public Photo(String name, String nameUser, LocalDateTime dateTime, byte[] contend, Long idUser) {
        this.name = name;
        this.nameUser = nameUser;
        this.dateTime = dateTime;
        this.contend = contend;
        this.idOfUser = idUser;
    }
    public void setPhoto(String fileName, String nameUser, byte[] photoFileContend) {

        this.name = fileName;
        this.nameUser = nameUser;
        this.dateTime = LocalDateTime.now();
        this.contend = photoFileContend;
        this.classification = "Null classification";
        this.idOfUser = null;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getNameUser() {
        return nameUser;
    }
    public void setNameUser(String description) {
        this.nameUser = description;
    }
    public LocalDateTime getDateTime() {
        return dateTime;
    }
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
    public byte[] getContend() {
        return contend;
    }
    public void setContend(byte[] contend) {
        this.contend = contend;
    }

    public boolean setClassification(String classification)
    {
        System.out.println("Enter to setClassification==>>>");
        try {
            this.classification = classification;
            return true;
        } catch (Exception e) {
            System.out.println(e.toString());
            System.out.println("ERROR setClassification==>>");
            return false;
        }
       
    }

    public String getClassification()
    {
        return this.classification;
    }
    public Long getIdOfUser() {
        return idOfUser;

    }
    public void setIdOfUser(Long idUser) {
        this.idOfUser = idUser;
    }
    @Override
    public String toString() {
        return "Photo {name=" + name + ", description=" + nameUser + ", dateTime=" + dateTime + ", contend="
                + Arrays.toString(contend) + ", classification=" + classification + ", idOfUser=" + idOfUser + "}";
    }

    public void clire() {
        
        this.name=null;
        this.nameUser=null; //שם המשתמש שהעלה את התמונה
        this.dateTime=null;
        this.contend=null; //photo file contend (pixels data)
        this.classification=null;
        this.idOfUser=null;
    }

    
    
}

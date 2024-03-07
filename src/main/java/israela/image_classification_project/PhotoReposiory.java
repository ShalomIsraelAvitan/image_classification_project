package israela.image_classification_project;


import java.util.ArrayList;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoReposiory extends MongoRepository<Photo, ObjectId>{
    
    public ArrayList<Photo> findByIdOfUser(Long idOfUser);
    public void deleteByIdOfUser(Long idOfUser);
    public ArrayList<Photo> findByClassification(String classification);
    //public ArrayList<Image> findImgoByIdOfUser(Long id);
    
 }

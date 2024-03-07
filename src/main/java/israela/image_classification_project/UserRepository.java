package israela.image_classification_project;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User,Long>{
    
    public User findByName(String name);
    public User findUserById(Long id);

}
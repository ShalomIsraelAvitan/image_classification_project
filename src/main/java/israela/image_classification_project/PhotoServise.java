package israela.image_classification_project;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.bson.types.ObjectId;

import org.springframework.stereotype.Service;

@Service
public class PhotoServise {
    private PhotoReposiory photoRepo;


    public PhotoServise(PhotoReposiory photoRepo) {
        this.photoRepo = photoRepo;
    }


    public String addPhoto(Photo photo, Long idUser)
    {
        System.out.println("name of the photo = "+photo.getName());
        photo.setIdOfUser(idUser);
        try {
            //Photo p = photoRepo.insert(photo);
            Photo p = photoRepo.insert(photo);
            System.out.println("******************************");
            System.out.println("The Photo Send to DB=====>>");
            System.out.println("******************************\n");
            return p.getID();
        } catch (Exception e) {
            
            System.err.println("Error==========>>addPhoto\n"+e.toString());
            return null;
        }
        
    }

    public Photo getPhotoById(String photoID)
    {
        Optional<Photo> photo = photoRepo.findById(new ObjectId(photoID));
        return photo.get();
    }

    public ArrayList<Photo> getPhotoByUserId(Long idOfUser)
    {

        ArrayList<Photo> photoList = new ArrayList<>();
        try {
            System.out.println("start getPhotoById=======>>\n");
            try {
                //photoList.addAll(photoRepo.findByIdOfUser(idOfUser));
                photoList = photoRepo.findByIdOfUser(idOfUser);
                
            } catch (Exception e) {
               
                System.out.println("Not work\n"+e.toString());
            }
            
            //photoList.addAll( photoRepo.findByIdOfUser(idOfUser));
           
            
            
            //System.out.println("size= "+photoList.size());
            //System.out.println("Yes===>>getPhotoById");
        
            return photoList;
        } catch (Exception e) {
            
            System.out.println("ERROR in getPhotoById=====>>>\n");
            return photoList;
        }
        
        
        
    }


    public boolean removPhotoOfUserId(Long idOfUser) {
        try {
            
             photoRepo.deleteByIdOfUser(idOfUser);
    
            return true;
            
            
        } catch (Exception e) {
            System.out.println("\nERROR in PhotoServise==> removPhotoOfUserId===>>"+e.toString());
            return false;
        }
        
    }


    public ArrayList<Photo> getAllPhoto() {
        List<Photo> arrayPhotos = photoRepo.findAll();

        return (ArrayList<Photo>) arrayPhotos;
    }

    /*public Image generateImage(User user) {
    
    Long id = user.getId();
    StreamResource sr = new StreamResource("user", () ->  {
        User attached = userRepo.findUserById(id);
        ArrayList<Photo> arryPhoto = photoRepo.findPhotoByIdOfUser(id);
        return new ByteArrayInputStream(arryPhoto.get(0).getContend());
    });
    sr.setContentType("image/png");
    Image image = new Image(sr, "profile-picture");
    return image;
}*/

    /* 
    public Image getImg(Long id) {
        ArrayList<Image> arrayImg = photoRepo.findImgoByIdOfUser(id);
        System.err.println("size = "+arrayImg.size()+"\n");
        return arrayImg.get(0);
    }*/

    public boolean removPhotoById(String photoID)
    {
        Optional<Photo> photo = photoRepo.findById(new ObjectId(photoID));

        try {
            photoRepo.delete(photo.get());
            return true;
        } catch (Exception e) {
            System.out.println("ERORR in PhotoServise==> removPhotoById===>>"+e.toString());
            return false;
        }
    }


    public boolean setClassification(Photo photo, String strPredictions) {

        System.out.println("Enter to servise setClassification==>>\n");
        
        try {
            boolean res = photo.setClassification(strPredictions);
            if (res==true) {
                photoRepo.save(photo);
            System.out.println("YESSS in setClassification======>>");
            }
            else{
                System.out.println("משהו לא עובד במחלקה");
            }
            
            return true;
        } catch (Exception e) {
            System.out.println("ERORR in PhotoServise==> setClassification====>>"+e.toString());
            return false;
        }
        
    }


    public boolean removeAllPhotos() {
        System.out.println("\nEnter to removeAllPhotos===>>");
        try{
            photoRepo.deleteAll();
            return true;
        } catch (Exception e) {
            System.out.println("ERROR in PhotoServise==> removeAllPhotos===>>"+e.toString());
            return false;
        }
    }

    public boolean removePhotoWithoutClassification(Long idOfUser, String classification)
    {
        try {
            ArrayList<Photo> arrayPhoto = photoRepo.findByIdOfUser(idOfUser);
            arrayPhoto = photoRepo.findByClassification(classification);
            if(arrayPhoto.size()==0)
                return true;
            for(int i=0; i<arrayPhoto.size(); i++)
            {
                photoRepo.delete(arrayPhoto.get(i));
            }
            return true;
            
        } catch (Exception e) {
            System.out.println("ERROR in PhotoServise==> removePhotoWithoutClassification===>>"+e.toString());
            return false;
        }
    }

}

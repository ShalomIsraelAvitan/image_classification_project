package israela.image_classification_project.Pages;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;



import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;


import israela.image_classification_project.AppMainLayout;
import israela.image_classification_project.Photo;
import israela.image_classification_project.Services.*;
import israela.image_classification_project.User;

//@Route(value = "/allphotos",layout = AppMainLayout.class)
@Route (value = "/allphotos", layout = AppMainLayout.class)
@PageTitle(" All Photos")
public class AllPhotosPage extends VerticalLayout{
    private PhotoServise photoServise;
    private String userName;
    private UserServise userServise;
    private VerticalLayout v;
    private VerticalLayout v2;


    public AllPhotosPage(PhotoServise photoServise, UserServise userServise)
    {
        this.photoServise = photoServise;
        this.userServise = userServise;

        //admin בדיקה האם המשתמש הוא
        Long id = Long.parseLong((String)VaadinSession.getCurrent().getSession().getAttribute("userId"));
        User user = userServise.getUserById(id);
        if(user.getAdmin()==false)
        {
            UI.getCurrent().getPage().setLocation("/home");
        }
        
        if (!isUserAuthorized())
        {
            System.out.println("-------- User NOT Authorized - can't use! --------");
            //Notification.show("You need to login or register first",5000,Position.TOP_CENTER);
            UI.getCurrent().getPage().setLocation("/"); // Redirect to login page (HomePage).
            return;
        }

        v = new VerticalLayout();
        v2 = new VerticalLayout();
        
        

        Long idOfUser = Long.parseLong((String)VaadinSession.getCurrent().getSession().getAttribute("userId"));
        boolean res = photoServise.removePhotoWithoutClassification(idOfUser, "Null classification");
        if(res==true)
        {
            System.out.println("Uncategorized photos were deleted\n");
        }
        else
        {
            System.out.println("Uncategorized photos were not deleted\n");
        }

        userName = (String)VaadinSession.getCurrent().getSession().getAttribute("username");
        String welcomeMsg = "Welcome Admin!";
      if (userName != null)
         welcomeMsg = "Welcome Admin " + userName.toUpperCase();

        add(new H1(welcomeMsg));
        showAllPhotos();
        
        setAlignItems(Alignment.CENTER);

    }
    private void showAllPhotos() {
        try {
            ArrayList<Photo> arryPhoto = photoServise.getAllPhoto();
            if(arryPhoto.size()==0)
            {
                add(new H2("There are no images to display"));
                return;
            }

        for(int i =0; i<arryPhoto.size(); i++)
        {
            showPhotoOnPage(arryPhoto.get(i).getContend(), arryPhoto.get(i));
        }
            
        } catch (Exception e) {
            System.out.println("\nin showAllPhotosPage ERROR====>>"+e.toString());
        }
       
        


    }
    private boolean isUserAuthorized()
    {
        // try to get 'username' from session cookie (was created in the Welcome(login) page).
         userName = (String)VaadinSession.getCurrent().getSession().getAttribute("username");

        return (userName == null) ? false : true;
    }
    private void showPhotoOnPage(byte[] photoFileContend, Photo photo) {

        StreamResource resource = new StreamResource("stam.jpg", new InputStreamFactory() 
        {
            public java.io.InputStream createInputStream()
            {
                return new ByteArrayInputStream(photoFileContend);
            };
        });
        HorizontalLayout heder = new HorizontalLayout();
        HorizontalLayout heder2 = new HorizontalLayout();
        HorizontalLayout heder1 = new HorizontalLayout();
        HorizontalLayout heder3 = new HorizontalLayout();

         Image image = new Image(resource, photo.getName());
         image.setHeight("240px");
         //image.setWidth("200px");

         if(photo.getClassification().equals("Abstract"))
         {
            showPhotoAbstract(heder, heder2, heder1, photo, image);
         }
         else if(photo.getClassification().equals("Realism"))
         {
            showPhotoRealism(heder, heder2, heder1, photo, image);
         }
         add(heder2);
         
         
  
    }
    private void showPhotoRealism(HorizontalLayout heder, HorizontalLayout heder2, HorizontalLayout heder1, Photo photo,Image image) {

        String str0 = "ID of user";
         String strIdOfUser = photo.getIdOfUser()+"";

         String str = "Name User:";
         String str2 = photo.getNameUser();

         heder1.add(new H5(str));
         heder1.add(str2);
         //add(heder);
         v2.add(heder1);

        str = "Photo Name:";
        str2 = photo.getName();
         
         heder.add(new H5(str0));
         heder.add(strIdOfUser);
         v2.add(heder);

         heder1.add(new H5(str));
         heder1.add(str2);
         //add(heder);
         v2.add(heder1);
         
         str = "Classification:";
         str2 = photo.getClassification();
         heder1.add(new H5(str));
         heder1.add(str2);
         //add(heder2);
         //add(image);
         v2.add(heder1);
         v2.add(image);
         
         v2.setAlignItems(Alignment.CENTER);
         heder2.add(v,v2);
         
    }
    private void showPhotoAbstract(HorizontalLayout heder, HorizontalLayout heder2, HorizontalLayout heder1, Photo photo, Image image) {
        String str0 = "ID of user";
         String strIdOfUser = photo.getIdOfUser()+"";

         String str = "Name User:";
         String str2 = photo.getNameUser();

         heder1.add(new H5(str));
         heder1.add(str2);
         //add(heder);
         v.add(heder1);

        str = "Photo Name:";
        str2 = photo.getName();
         
         heder.add(new H5(str0));
         heder.add(strIdOfUser);
         v.add(heder);

         heder1.add(new H5(str));
         heder1.add(str2);
         //add(heder);
         v.add(heder1);
         
         str = "Classification:";
         str2 = photo.getClassification();
         heder1.add(new H5(str));
         heder1.add(str2);
         //add(heder2);
         //add(image);
         v.add(heder1);
         v.add(image);
         
         v.setAlignItems(Alignment.CENTER);
         heder2.add(v,v2);
         
    }
    
}

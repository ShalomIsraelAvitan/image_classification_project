package israela.image_classification_project.Pages;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;

import israela.image_classification_project.AppMainLayout;
import israela.image_classification_project.Photo;
import israela.image_classification_project.Services.PhotoServise;

@Route(value = "/gallery", layout = AppMainLayout.class)
public class PhotoGalleryPage extends VerticalLayout{

    private PhotoServise photoService;

    public PhotoGalleryPage(PhotoServise photoServise)
    {
        this.photoService = photoServise;
        if (!isUserAuthorized())
        {
            System.out.println("-------- User NOT Authorized - can't use! --------");
            UI.getCurrent().getPage().setLocation("/"); // Redirect to login page (HomePage).
            return;
        }
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
        String userName = (String)VaadinSession.getCurrent().getSession().getAttribute("username");
        Button btnDeleteAllPhoto = new Button("Delete all your photos" ,e -> deleteAllPhoto((String)VaadinSession.getCurrent().getSession().getAttribute("userId")));
        btnDeleteAllPhoto.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        add(new H1("Welcome "+userName.toUpperCase()+" To Your Photo Gallery"));
        add(btnDeleteAllPhoto);
        showAllPhotoUser();

        //setSizeFull();
        setAlignItems(Alignment.CENTER);
        
    }
    private void deleteAllPhoto(String attribute) {
        Long id = Long.parseLong(attribute);
        try {
             boolean b = photoService.removPhotoOfUserId(id);
             System.out.println("b = "+b);
             if(b==true){
                Notification.show("remove Succeeded",5000, Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
             }
            
        } catch (Exception e) {
            Notification.show("Remove Failed",5000, Position.TOP_CENTER);
        }
        UI.getCurrent().getPage().setLocation("/gallery");
    }
    private void showAllPhotoUser() {
         
        Long idUser = Long.parseLong((String)VaadinSession.getCurrent().getSession().getAttribute("userId"));
        System.out.println("\nid of user = "+idUser);
        ArrayList<Photo> list = photoService.getPhotoByUserId(idUser);
        System.out.println("**********************");
        System.out.println("size of photos = "+list.size());
        System.out.println("**********************");


        if(list.size()==0)
        {
            add(new H2("There are no images to display"));
            return; 
        }
        for(int i =0; i<list.size(); i++)
        {
            showPhotoOnPage(list.get(i).getContend(), list.get(i));
        }
            
    }
    private boolean isUserAuthorized()
    {
        // try to get 'username' from session cookie (was created in the Welcome(login) page).
    
        String userName = (String)VaadinSession.getCurrent().getSession().getAttribute("username");
        

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
        Image image = new Image(resource, photo.getName());
        image.setHeight("270px");
        //image.setWidth("200px");
        String str = "Name: ";
        String str2 = photo.getName()+"\n";
        
        heder.add(new H4(str));
        heder.add(str2);
        add(heder);
        
        str = "Classification: ";
        str2 = photo.getClassification()+"\n";
        heder2.add(new H4(str));
        heder2.add(str2);
        add(heder2);
        add(image);

        setAlignItems(Alignment.CENTER); 
  
    }
}

package israela.image_classification_project;


import java.io.ByteArrayInputStream;
import java.util.ArrayList;


import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;

import israela.image_classification_project.UserServise.UserGridChangeListener;



@Route(value = "/admin",layout = AppMainLayout.class)
@PageTitle("admin")
public class AdminPage extends VerticalLayout{
    private Grid<User> gridUser;
    //private Grid<Photo> gridPhotoUser;
    private PhotoServise photoServise;
    private String userName;
    private UserServise userServise;
    private VerticalLayout verLayout;

    private User userChos;
    public AdminPage(PhotoServise photoServise, UserServise userServise)
    {
        this.userServise = userServise;
        this.photoServise = photoServise;
        setAlignItems(Alignment.CENTER);
        userChos = new User(null, null, 0);

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

        
        if (!isUserAuthorized())
        {
            System.out.println("-------- User NOT Authorized - can't use! --------");
            //Notification.show("You need to login or register first",5000,Position.TOP_CENTER);
            UI.getCurrent().getPage().setLocation("/"); // Redirect to login page (HomePage).
            return;
        }
        verLayout = new VerticalLayout();
        Long id = Long.parseLong((String)VaadinSession.getCurrent().getSession().getAttribute("userId"));
        if(id != 111111111)
        {
            UI.getCurrent().getPage().setLocation("/home");
        }
        

        userName = (String)VaadinSession.getCurrent().getSession().getAttribute("username");
        
        HorizontalLayout bottonPanel = new HorizontalLayout();//כפתורים
        HorizontalLayout mainLayout = new HorizontalLayout();//ריכוז של הכל

        //Button
        Button refreshBtn = new Button("refresh",e -> refresh());
        Button deletBtn = new Button("Delet User",e -> deletUser(userChos));
        Button btnShowPhotoUser = new Button("Show Photo User", e ->showAllPhotoUser(userChos));
        Button btnShowAllPhotos = new Button("Show All Photos", e ->showAllPhotos());
        btnShowAllPhotos.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button btnRemoveAllPhotos = new Button("Remove All Photos", e -> removeAllPhotos());
        btnRemoveAllPhotos.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        Button btnMeasureModel = new Button("Measure Model", e -> nevigatMeasureModel());
        btnMeasureModel.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        bottonPanel.add(refreshBtn,deletBtn,btnShowPhotoUser,btnShowAllPhotos,btnRemoveAllPhotos,btnMeasureModel);
        

        //grid users
        gridUser = new Grid<User>(User.class);
        gridUser.setWidth("900px");
        gridUser.setColumns("id", "name", "pw");
        gridUser.setItems(userServise.getAllUser());
        gridUser.addItemClickListener(event -> nevegatPhotoUser(event.getItem()));
        
        //grid photo
        // gridPhotoUser = new Grid<Photo>(Photo.class);
        // gridPhotoUser.setWidth("1100px");
        // gridPhotoUser.setColumns("name", "description", "classification", "idOfUser");
        // List<Photo> list = photoServise.getPhotoByUserId(userChos.getId());
        // gridPhotoUser.setItems(list);
        // gridPhotoUser.addItemClickListener(event -> showAllPhotoUser(userChos));
        

      // if no 'username' attribute, this is a Guest.
      String welcomeMsg = "Welcome Admin!";
      if (userName != null)
         welcomeMsg = "Welcome Admin " + userName.toUpperCase();

        add(new H1(welcomeMsg));
        //showAllUsers();
        add(bottonPanel);
        mainLayout.add(gridUser);
        //mainLayout.setAlignItems(Alignment.BASELINE);//מצמיד אותם לבסיס
        add(mainLayout);
        //showAllPhotoUser();

        setSizeFull();
        setAlignItems(Alignment.CENTER);

        
        // listener for char changes
        userServise.addUserChangeListener(new UserGridChangeListener()
        {
            @Override
            public void onChange()
            {
                UI ui = getUI().orElseThrow();
                ui.access(() -> refresh());
                String msg = "\n---> " + userName + ": refreshChat() called from Listener ONLY when User Grid changed!";
                System.out.println(msg);
            }
        });

        // messageService.addChatChangeListener(() -> {
        //     UI ui = getUI().orElseThrow();
        //     ui.access(() -> refreshChat());
        //     String msg = "\n---> " + userName + ": refreshChat() called from Listener ONLY when Chat changed!";
        //     System.out.println(msg);
        // });

        System.out.println("\n=======> ChatPage('/chat') constructor ends!\n");
    }

    private void nevigatMeasureModel() {
        UI.getCurrent().getPage().setLocation("/measuremodel");
    }

    private void removeAllPhotos() {
        boolean res = photoServise.removeAllPhotos();
        if (res==true) {
            Notification.show("All Photos Deleted",5000,Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS);  
        }
        else
            Notification.show("Faild to delete all photos",5000,Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_ERROR);
    }

    private void showAllPhotos() {
        UI.getCurrent().getPage().setLocation("/allphotos");
    }

    private void nevegatPhotoUser(User user) {

        userChos = new User(user.getId(), user.getName(), user.getPw());
        userChos.setAdmin(user.getAdmin());
    }

    private void deletUser(User userChos2) {
        if(userChos.getId()==null)
        {
            // Notification notification = Notification.show("mast choose User",5000,Position.BOTTOM_START);
            // notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            Notification.show("mast choose User",5000,Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_WARNING);
            return;
        }
        System.out.println("isAdmin = "+userChos.getAdmin());
        if(userChos.getAdmin()==true)
        {
            Notification.show("The user you want to delete is admin",5000,Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_WARNING);
            return;
        } 

        boolean res = userServise.deletUser(userChos);
        boolean b = photoServise.removPhotoOfUserId(userChos.getId());

        if(res==true && b == true){
            Notification.show("User delete",5000,Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS);

            userChos = new User(null, null, 0);
            refresh();
        }
        else
            Notification.show("User Not delet",5000,Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
    private void refresh() {
        System.out.println("start refresh====>>\n");
        gridUser.setItems(userServise.getAllUser());
         //List<Photo> list = photoServise.getPhotoByUserId(userChos.getId());
        //gridPhotoUser.setItems(list);
        Notification.show("refresh", 5000,Position.BOTTOM_START);
    }
    // private void showAllUsers() {
        
    // }
    private boolean isUserAuthorized()
    {
        // try to get 'username' from session cookie (was created in the Welcome(login) page).
         userName = (String)VaadinSession.getCurrent().getSession().getAttribute("username");

        return (userName == null) ? false : true;
    }

    private void showAllPhotoUser(User user) {
       
        
        verLayout.removeAll();
         if(user.getName().equals(null))
         {
            Notification.show("mast choose User",5000,Position.BOTTOM_START).addThemeVariants(NotificationVariant.LUMO_WARNING);
            return;
         }
        ArrayList<Photo> list = photoServise.getPhotoByUserId(user.getId());

        if(list.size()==0)
        {
            H2 h = new H2("There are no images to display");
            verLayout.add(h);
            verLayout.setAlignItems(Alignment.CENTER);
            add(verLayout);
            Notification.show("showAllPhotoUser Succeeded",4000,Position.BOTTOM_START);
            return; 
        }
       
        for(int i =0; i<list.size(); i++)
        {
            showPhotoOnPage(list.get(i).getContend(), list.get(i));
        }
        Notification.show("showAllPhotoUser Succeeded",4000,Position.BOTTOM_START);
        userChos = new User(null, null, 0);
        //add(gridPhotoUser);
            
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
         image.setHeight("240px");
         //image.setWidth("200px");
         
         String str = "Photo Name: ";
         String str2 = photo.getName()+"\n";
         
         heder.add(new H4(str));
         heder.add(str2);
         //add(heder);
         verLayout.add(heder);
         
         str = "Classification: ";
         str2 = photo.getClassification()+"\n";
         heder2.add(new H4(str));
         heder2.add(str2);
         //add(heder2);
         //add(image);
         verLayout.add(heder2);
         verLayout.add(image);
         
         verLayout.setAlignItems(Alignment.CENTER);
         add(verLayout);
         
  
    }
}

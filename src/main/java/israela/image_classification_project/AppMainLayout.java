package israela.image_classification_project;
import org.springframework.boot.logging.logback.ColorConverter;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;

import israela.image_classification_project.Pages.*;
import israela.image_classification_project.Services.UserServise;


public class AppMainLayout extends AppLayout
{
    private UserServise userServise;
    private static final String LOGO_IMAGE_URL = "https://www.smorescience.com/wp-content/uploads/2023/08/Featured-Images-50.jpg";
    public AppMainLayout(UserServise userServise)
    {
        this.userServise = userServise;
        
        if (!isUserAuthorized())
        {
            System.out.println("-------- User NOT Authorized - can't use! --------");
            createHeaderNotConnect();
            //Notification.show("You need to login or register first",5000,Position.TOP_CENTER);
            //UI.getCurrent().getPage().setLocation("/"); // Redirect to login page (HomePage).
            return;
        }
        else
            createHeader();
    }

    private void createHeaderNotConnect() {

        Image imgLogo = new Image(LOGO_IMAGE_URL,"4");
        imgLogo.setHeight("55px");
        
        
        H3 nameApp = new H3("Image Classification");
        nameApp.getStyle().setColor("blue");
        //logo.getStyle().setColor("#");
        
        RouterLink linkHome = new RouterLink("Home", HomePage.class);
        RouterLink linkUpload = new RouterLink("Upload", UploadPhotoPage.class);
        RouterLink linkGallery = new RouterLink("Gallery", PhotoGalleryPage.class);

        Span last = new Span("");
        //Button btnLogin = new Button("Login", e->login());
        Button btnSignUp = new Button("SignUp",event -> signUp());
        btnSignUp.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnSignUp.getStyle().setBackground("hsl(150, 100%, 90%)");
        btnSignUp.getStyle().setColor("black");
        Button btnLogIn = new Button("Login",event -> login());
        btnLogIn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnLogIn.getStyle().setBackground("hsl(150, 100%, 90%)");
        btnLogIn.getStyle().setColor("black");
        Button btnLogout = new Button("Logout", e->logout());
        btnLogout.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR); // RED button

        HorizontalLayout header  = new HorizontalLayout();
        header.add(imgLogo,nameApp,linkHome,linkUpload,linkGallery,last,btnLogIn,btnSignUp);

        header.getStyle();
        header.setWidthFull();
        header.setAlignItems(Alignment.BASELINE);
        header.setPadding(true);//רווחים מסביב לכפתור
        header.expand(last);
        addToNavbar(header);
    }

    private void signUp() {
        VaadinSession.getCurrent().getSession().invalidate();
        UI.getCurrent().getPage().setLocation("/signup"); 
    }

    public void createHeader()
    {
        Image imgLogo = new Image(LOGO_IMAGE_URL,"4");
        imgLogo.setHeight("55px");
        
        
        H3 nameApp = new H3("Image Classification");
        nameApp.getStyle().setColor("blue");
        //logo.getStyle().setColor("#");
        
        RouterLink linkHome = new RouterLink("Home", HomePage.class);
        RouterLink linkUpload = new RouterLink("Upload", UploadPhotoPage.class);
        RouterLink linkGallery = new RouterLink("Gallery", PhotoGalleryPage.class);
        RouterLink linkAdmin = new RouterLink("Admin", AdminPage.class);

        Span last = new Span("");
        //Button btnLogin = new Button("Login", e->login());
        Button btnLogout = new Button("Logout", e->logout());
        btnLogout.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR); // RED button

        HorizontalLayout header  = new HorizontalLayout();

        //header.add(createLink("STAM"));
        
        Long idUser = Long.parseLong((String)VaadinSession.getCurrent().getSession().getAttribute("userId"));
        String userName = (String)VaadinSession.getCurrent().getSession().getAttribute("username");
        H3 nameuser = new H3(userName);
        nameuser.getStyle().setColor("hsl(160, 100%, 60%)");
        //nameuser.getStyle().setColor("black");
        //nameuser.getStyle().setBackground("hsl(160, 100%, 60%)");
        User user = userServise.getUserById(idUser);

        if(user.getAdmin())
        {
            try {
                nameuser.setText(nameuser.getText() +"(admin)");
                header.add(imgLogo,nameApp,linkHome,linkUpload,linkGallery,linkAdmin,last,nameuser,btnLogout);
                System.out.println("Admin Log in\n");
                
            } catch (Exception e) {
                System.out.println(e.toString());
                System.out.println("ERROR in createHeader====>>\n");
            }
            
        }
        else
            header.add(imgLogo,nameApp,linkHome,linkUpload,linkGallery,last,nameuser,btnLogout);
            //header.add(nameuser);
            //header.add(imgLogo,nameApp,linkHome,linkUpload,linkGallery,linkAdmin,last,btnLogout);
        //}
        
        
        
        header.getStyle();
        header.setWidthFull();
        header.setAlignItems(Alignment.BASELINE);
        header.setPadding(true);//רווחים מסביב לכפתור
        header.expand(last);
        addToNavbar(header);
        
    }
    private void login() {
        VaadinSession.getCurrent().getSession().invalidate();
        UI.getCurrent().getPage().setLocation("/login"); 
    }

    private void logout()
   {
      // Invalidate Session (delete the user-session-id and all its attributes)
      VaadinSession.getCurrent().getSession().invalidate();
      UI.getCurrent().getPage().setLocation("/"); 

      // Reload this page with new user-session-id
      //UI.getCurrent().getPage().reload();
   }

   private boolean isUserAuthorized()
    {
        // try to get 'username' from session cookie (was created in the Welcome(login) page).
        String userName = (String)VaadinSession.getCurrent().getSession().getAttribute("username");

        return (userName == null) ? false : true;
    }

    private RouterLink createLink(String viewName) {
        RouterLink link = new RouterLink();
        link.add(viewName);
        // Demo has no routes
        link.setRoute(HomePage.class);

        link.addClassNames(LumoUtility.Display.FLEX,
                LumoUtility.AlignItems.CENTER,
                LumoUtility.Padding.Horizontal.MEDIUM,
                LumoUtility.TextColor.SECONDARY, LumoUtility.FontWeight.MEDIUM);
        link.getStyle().set("blue", "blue");

        return link;
    }
}

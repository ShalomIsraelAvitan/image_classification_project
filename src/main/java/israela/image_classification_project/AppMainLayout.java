package israela.image_classification_project;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;

public class AppMainLayout extends AppLayout
{
    private static final String CHAT_IMAGE_URL = "https://www.smorescience.com/wp-content/uploads/2023/08/Featured-Images-50.jpg";
    public AppMainLayout()
    {
        createHeader();
    }

    public void createHeader()
    {
        Image imgLogo = new Image(CHAT_IMAGE_URL,"4");
        imgLogo.setHeight("55px");
        
        
        H3 nameApp = new H3("MyApp");
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
        //String idUser = (String)VaadinSession.getCurrent().getSession().getAttribute("username");
        // if(idUser.equals("shalom"))
        // {
        //     try {
        //         header.add(imgLogo,nameApp,linkHome,linkUpload,linkGallery,linkAdmin,last,btnLogout);
        //         System.out.println("Admin Log in\n");
                
        //     } catch (Exception e) {
        //         System.out.println(e.toString());
        //         System.out.println("ERROR in createHeader====>>\n");
        //     }
            
        // }
        // else
            //header.add(imgLogo,nameApp,linkHome,linkUpload,linkGallery,last,btnLogout);
            header.add(imgLogo,nameApp,linkHome,linkUpload,linkGallery,linkAdmin,last,btnLogout);
        
        header.getStyle();
        header.setWidthFull();
        header.setAlignItems(Alignment.BASELINE);
        header.setPadding(true);//רווחים מסביב לכפתור
        header.expand(last);
        addToNavbar(header);
        
    }
    private void logout()
   {
      // Invalidate Session (delete the user-session-id and all its attributes)
      VaadinSession.getCurrent().getSession().invalidate();
      UI.getCurrent().getPage().setLocation("/"); 

      // Reload this page with new user-session-id
      //UI.getCurrent().getPage().reload();
   }
}

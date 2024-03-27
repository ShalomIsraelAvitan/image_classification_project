package israela.image_classification_project.Pages;

import java.util.Date;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import israela.image_classification_project.AppMainLayout;
import israela.image_classification_project.Services.PhotoServise;

@Route(value = "/",layout = AppMainLayout.class)
@PageTitle("Home")
public class HomePage extends VerticalLayout{


    private String userName;

    private static final String IMAGE_URL = "https://www.smorescience.com/wp-content/uploads/2023/08/Featured-Images-50.jpg";
    public  HomePage(PhotoServise photoService) {
    setAlignItems(Alignment.CENTER);

    // Get from Session the 'username' attribute 
    userName = (String)VaadinSession.getCurrent().getSession().getAttribute("username");

      // if no 'username' attribute, this is a Guest.
      String welcomeMsg = null;
      if (userName != null)
         welcomeMsg = "Welcome " + userName.toUpperCase();

      // create image for Layout page   
      Image imgLogoLayout = new Image(IMAGE_URL, "Home image");
      imgLogoLayout.setHeight("250px");

      

      add(new H2("Home Page"));
      creatInformationForUser(welcomeMsg);

      // set all components in the Center of page
      setSizeFull();
      setAlignItems(Alignment.CENTER);
   }
   private void creatInformationForUser(String welcomeMsg) {

      Image imgLogo = new Image(IMAGE_URL, "Home image");
      imgLogo.setHeight("250px");
        HorizontalLayout helloPanel = new HorizontalLayout();
        helloPanel.setAlignItems(Alignment.BASELINE);
        //TextField fieldName = new TextField("Your Name");
        String str = "On this website, you can upload photos of your paintings and check whether the painting is a realism or abstract painting";
        String str2 = "In order to upload your drawings, you will have to click on the";
        String str3 = "button, located in the navigation bar on the top left.";
        String str4 = "If you want to see the photos you uploaded, you can click on the";


        H3 h = new H3("Upload");
        h.getStyle().setColor("blue");

        helloPanel.add(new H3(str));
        
        if(welcomeMsg!=null)
            add(new H1(welcomeMsg));
        add(imgLogo);
        //add(new H3("( SessionID: " + sessionId + " )"));
        add( helloPanel);
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(new H3(str2));
        horizontalLayout.add(h);
        horizontalLayout.add(new H3(str3));
        add(horizontalLayout);
        
        h = new H3("Gallery");
        h.getStyle().setColor("blue");

        HorizontalLayout horizontalLayout2 = new HorizontalLayout();
        horizontalLayout2.add(new H3(str4));
        horizontalLayout2.add(h);
        horizontalLayout2.add(new H3(str3));
        add(horizontalLayout2);

        addAdminGuidelines((String)VaadinSession.getCurrent().getSession().getAttribute("username"));

    }
    private void addAdminGuidelines(String userName) {
        if(userName==null)
            return;
        String str = "If you would like to enter your Admin page, you can click on the";
        H3 h = new H3("Admin");
        h.getStyle().setColor("blue");
        String str2 = "button, located in the navigation bar on the top left.";

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(new H3(str));
        horizontalLayout.add(h);
        horizontalLayout.add(new H3(str2));
        add(horizontalLayout);
    }
}

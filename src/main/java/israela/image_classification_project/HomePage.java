package israela.image_classification_project;

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

@Route(value = "/home",layout = AppMainLayout.class)
@PageTitle("Home")
public class HomePage extends VerticalLayout{


    private String userName;

    private static final String IMAGE_URL = "https://www.smorescience.com/wp-content/uploads/2023/08/Featured-Images-50.jpg";
    public  HomePage(PhotoServise photoService) {
    setAlignItems(Alignment.CENTER);
    if (!isUserAuthorized())
        {
            System.out.println("-------- User NOT Authorized - can't use! --------");
            //Notification.show("You need to login or register first",5000,Position.TOP_CENTER);
            UI.getCurrent().getPage().setLocation("/"); // Redirect to login page (HomePage).
            return;
        }

    

      // Get from Session the 'username' attribute 
    userName = (String)VaadinSession.getCurrent().getSession().getAttribute("username");

      // if no 'username' attribute, this is a Guest.
      String welcomeMsg = "Welcome Guest!";
      if (userName != null)
         welcomeMsg = "Welcome " + userName.toUpperCase();

      // create image for chat page   
      Image imgLogo = new Image(IMAGE_URL, "Home image");
      imgLogo.setHeight("250px");

      add(new H2("Home Page"));
      creatInformationForUser(welcomeMsg);

      // set all components in the Center of page
      setSizeFull();
      setAlignItems(Alignment.CENTER);
   }
   private void creatInformationForUser(String welcomeMsg) {
    HorizontalLayout helloPanel = new HorizontalLayout();
    helloPanel.setAlignItems(Alignment.BASELINE);
    //TextField fieldName = new TextField("Your Name");
    String str = "On this website, you can upload photos of your paintings and check whether the painting is a realism or abstract painting";
    String str2 = "In order to upload your drawings, you will have to click on the";
    String str3 = "button, located in the navigation bar on the top left.";
    String str4 = "If you want to see the photos you uploaded, you can click on the";

    H3 h = new H3("upload");
    h.getStyle().setColor("blue");
    //add(h );
    //helloPanel.add(fieldName, btnSayHello);

    //helloPanel.add(btnLogin,btnSignUp);
    helloPanel.add(new H3(str));
    
    add(new Text(new Date() + ""));
    //add(imgLogo);
    add(new H1(welcomeMsg));
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

    }
private boolean isUserAuthorized()
    {
        // try to get 'username' from session cookie (was created in the Welcome(login) page).
        String userName = (String)VaadinSession.getCurrent().getSession().getAttribute("username");

        return (userName == null) ? false : true;
    }
   
}

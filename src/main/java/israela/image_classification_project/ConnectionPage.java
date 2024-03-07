package israela.image_classification_project;

import java.util.Date;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route(value = "/")
@PageTitle("Connection")
public class ConnectionPage extends VerticalLayout{

    private String sessionId, userName;
    private UserServise userService;
    private static final String CHAT_IMAGE_URL = "https://www.smorescience.com/wp-content/uploads/2023/08/Featured-Images-50.jpg";
    public  ConnectionPage(UserServise userServise) {
        this.userService = userServise;
        System.out.println("Start ConnectionPage=======>\n");
    setAlignItems(Alignment.CENTER);

    add(new H2("Connectio Page"));

      //Image image = new Image("images/israel.jpeg", "יחד ננצח");
      //image.setWidth("600px");

      // Get from Session the user-session-id & 'username' attribute 
      sessionId = VaadinSession.getCurrent().getSession().getId();
      userName = (String)VaadinSession.getCurrent().getSession().getAttribute("username");

      // if no 'username' attribute, this is a Guest.
      String welcomeMsg = "Welcome Guest!";
      if (userName != null)
         welcomeMsg = "Welcome " + userName.toUpperCase();

      // create image for chat page   
      Image imgChat = new Image(CHAT_IMAGE_URL, "Connectio image");
      imgChat.setHeight("250px");


      HorizontalLayout helloPanel = new HorizontalLayout();
      helloPanel.setAlignItems(Alignment.BASELINE);
      //TextField fieldName = new TextField("Your Name");

      Button btnSignUp = new Button("sign up", event -> signUp());
      Button btnLogin = new Button("Log in", event -> logIn());
      Button btnTest = new Button("test", e -> loginTest());
      Button btnAdminTest = new Button("AdminTest", e -> loginAdminTest());
      //helloPanel.add(fieldName, btnSayHello);

      helloPanel.add(btnLogin,btnSignUp);

      add(new Text(new Date() + ""));
      add(imgChat);
      add(new H1(welcomeMsg));
      add(new H3("( SessionID: " + sessionId + " )"));
      add( helloPanel);
      add(btnTest);
      add(btnAdminTest);

      // set all components in the Center of page
      setSizeFull();
      setAlignItems(Alignment.CENTER);
   }

   private void loginAdminTest() {
        Long idUser = Long.parseLong("111111111");
        String userName = "shalom";
        
        int password = 123456;
        boolean exsits = userService.isUserExistsLogin(userName,idUser, password);
        System.out.println("login="+exsits);
        if(exsits)
        {
            VaadinSession.getCurrent().getSession().setAttribute("username", "shalom");
            VaadinSession.getCurrent().getSession().setAttribute("userId", "111111111");
            Notification.show("User Log In successfully",5000,Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS);;
            //UI.getCurrent().navigate("/upload");
            UI.getCurrent().getPage().setLocation("/home");
            return;

        }
        else{
            Notification.show("the User is not exsits", 5000, Position.TOP_CENTER);
            return;
        }
    }

private void loginTest() {

    Long idUser = Long.parseLong("214282518");
        String userName = "test";
        
        int password = 123456;
        boolean exsits = userService.isUserExistsLogin(userName,idUser, password);
        System.err.println("login="+exsits);
        if(exsits)
        {
            VaadinSession.getCurrent().getSession().setAttribute("username", "test");
            VaadinSession.getCurrent().getSession().setAttribute("userId", "214282518");
            Notification.show("User Log In successfully",5000,Position.TOP_CENTER);
            //UI.getCurrent().navigate("/upload");
            UI.getCurrent().getPage().setLocation("/home");
            return;

        }
        else{
            Notification.show("the User is not exsits or one of the fields is incorrect", 5000, Position.TOP_CENTER);
            return;
        }

    }

private void logIn() {
    UI.getCurrent().navigate("/login");
    }

private void signUp() {
        UI.getCurrent().navigate("/signup");
    }
   
}


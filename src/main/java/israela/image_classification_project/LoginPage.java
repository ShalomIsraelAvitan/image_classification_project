package israela.image_classification_project;


import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route(value = "/login")
@PageTitle("Login")
public class LoginPage extends VerticalLayout
{
    
    private UserServise userService;
    private TextField fieldName;
    private TextField fieldId;
    private PasswordField fieldPw;

    public LoginPage(UserServise userService)
    {
        System.err.println("LoginPage================>>\n");
        this.userService = userService;

        fieldId = createFieldId(fieldId);
        fieldName = createFieldName(fieldName);
        fieldPw = createFieldPw(fieldPw);

        

        VerticalLayout fieldsPanel = new VerticalLayout();//הכנסת ערכים
        fieldsPanel.add(fieldId,fieldName,fieldPw);
        fieldsPanel.setAlignItems(Alignment.CENTER);
        fieldsPanel.add(new Button("LogIn", e -> login(fieldId,fieldName,fieldPw)));

        add(fieldsPanel);
        //setAlignItems(Alignment.CENTER);
        
        
    }

    private PasswordField createFieldPw(PasswordField fieldPw) {

        fieldPw = new PasswordField("Password");
        fieldPw.setPlaceholder("Enter your Password");
        fieldPw.setHelperText("This Password will be your User Password");
        fieldId.setRequiredIndicatorVisible(false);
        fieldPw.setErrorMessage("Password MUST BE ONLY WITH NUMBERS!");
        fieldPw.setAllowedCharPattern("[0-9]"); // only letters & spaces
        //fieldId.setPattern("\\w+\\s\\w+"); // regx for two-words & one space between.  
        fieldPw.setMinLength(6); // min 6 chars
        fieldPw.setMaxLength(9); // max 9 chars
        fieldPw.setPrefixComponent(VaadinIcon.PASSWORD.create()); // add user icon
        fieldPw.setClearButtonVisible(true); // fast clear text (x)
        fieldPw.setValueChangeMode(ValueChangeMode.LAZY); // eed for ChangeListener.
        

        return fieldPw;
    }

    private TextField createFieldId(TextField fieldId) {

        fieldId = new TextField("ID");
        fieldId.setPlaceholder("Enter your ID");
        fieldId.setHelperText("This ID will be your User ID");
        //fieldId.setRequiredIndicatorVisible(true);
        fieldId.setErrorMessage("ID MUST BE ONLY WITH NUMBERS!");
        fieldId.setAllowedCharPattern("[0-9]"); // only letters & spaces
        //fieldId.setPattern("\\w+\\s\\w+"); // regx for two-words & one space between.  
        fieldId.setMinLength(9); // min 9 chars
        fieldId.setMaxLength(9); // max 9 chars
        fieldId.setPrefixComponent(VaadinIcon.USER_CARD.create()); // add user icon
        fieldId.setClearButtonVisible(true); // fast clear text (x)
        fieldId.setValueChangeMode(ValueChangeMode.LAZY); // eed for ChangeListener.

        return fieldId;
        
    }

    private TextField createFieldName(TextField fieldName2) {
        fieldName =  new TextField("Name");
        fieldName.setPlaceholder("Enter your name");
         fieldName.setHelperText("This name will be your User Name");
         fieldName.setRequiredIndicatorVisible(true);
         fieldName.setErrorMessage("Name MUST be with two words, one space between, 4-15 Letters!");
         fieldName.setAllowedCharPattern("[a-zA-Z _ 0-9]"); // only letters & spaces
         //fieldName.setPattern("\\w+\\s\\w+"); // regx for two-words & one space between.  
         fieldName.setMinLength(4); // min 4 chars
         fieldName.setMaxLength(15); // max 15 chars
         fieldName.setPrefixComponent(VaadinIcon.USER.create()); // add user icon
         fieldName.setClearButtonVisible(true); // fast clear text (x)
         fieldName.setValueChangeMode(ValueChangeMode.LAZY); // eed for ChangeListener.

         return fieldName;
    }

    public boolean login(TextField id, TextField name, PasswordField pw)
    {
        Long idUser = Long.parseLong(id.getValue());
        String userName = fieldName.getValue();
        
        int password = Integer.parseInt(pw.getValue());
        boolean exsits = userService.isUserExistsLogin(userName,idUser, password);
        System.err.println("login="+exsits);
        if(exsits)
        {
            VaadinSession.getCurrent().getSession().setAttribute("username", name.getValue());
            VaadinSession.getCurrent().getSession().setAttribute("userId", id.getValue());
            Notification.show("User Log In successfully",5000,Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS);;
            //UI.getCurrent().navigate("/upload");
            UI.getCurrent().getPage().setLocation("/home");
            return true;

        }
        else{
            Notification.show("the User is not exsits or one of the fields is incorrect", 5000, Position.TOP_CENTER);
            return false;
        }
        
    }
   
}

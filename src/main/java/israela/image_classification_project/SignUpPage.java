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

@Route(value = "/signup")
@PageTitle("signup")
public class SignUpPage extends VerticalLayout{

    private UserServise userService;
    private TextField fieldName;
    private PasswordField fieldPw;
    private TextField fieldId;

    public SignUpPage(UserServise userService)
    {
        System.err.println("SignUpPage===========>>\n");
        this.userService = userService;

        fieldId = createFieldId(fieldId);
        fieldName = createFieldName(fieldName);
        fieldPw = createFieldPw(fieldPw);

        VerticalLayout fieldsPanel = new VerticalLayout();//הכנסת ערכים
        fieldsPanel.add(fieldId,fieldName,fieldPw);
        fieldsPanel.add(new Button("SignUp", e -> SignUp(fieldId,fieldName,fieldPw)));
        fieldsPanel.setAlignItems(Alignment.CENTER);
        add(fieldsPanel);
    
        
    }
    private PasswordField createFieldPw(PasswordField fieldPw) {

        fieldPw = new PasswordField("Password");
        fieldPw.setPlaceholder("Enter your Password");
        fieldPw.setHelperText("This Password will be your User Password");
        //fieldId.setRequiredIndicatorVisible(true);
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
    
    public void SignUp(TextField id, TextField name, PasswordField pw)
    {
        User newUser = new User(Long.parseLong(id.getValue()), name.getValue(), Integer.parseInt(pw.getValue()));
        int password = Integer.valueOf(pw.getValue());
        Long iD = Long.parseLong(id.getValue());
        if (userService.isIdUsd(iD)) {
            Notification.show("This ID allready Usd",5000, Position.TOP_CENTER);
        }
        else if(userService.isUserExists(name.getValue(),  iD, password)!= false)
        {
            Notification.show("This User Exsist",5000, Position.TOP_CENTER);

            id.setValue("");
            name.setValue("");
            pw.setValue("");
            

        }else{

            boolean x = userService.addUser(newUser);
            VaadinSession.getCurrent().getSession().setAttribute("username", name.getValue());
            VaadinSession.getCurrent().getSession().setAttribute("userId", id.getValue());
            if(x==true )
            {
                Notification.show("User successfully Sign Up",5000,Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS);

                UI.getCurrent().navigate("/home");
            }
            else{
                Notification.show("User failed to Sign Up",5000,Position.TOP_CENTER);
            }
        }   
    }
}

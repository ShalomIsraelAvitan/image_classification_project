package israela.image_classification_project.Pages;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;

import israela.image_classification_project.AppMainLayout;
import israela.image_classification_project.Photo;
import israela.image_classification_project.User;
import israela.image_classification_project.Services.PhotoServise;
import israela.image_classification_project.Services.UploadPhotoServise;
import israela.image_classification_project.Services.UserServise;
import israela.image_classification_project.Services.UploadPhotoServise.LongTasklistener;


@Route(value = "/upload", layout = AppMainLayout.class)
public class UploadPhotoPage extends VerticalLayout{
    private PhotoServise photoService;
    private Upload singleFileUpload; //UI component (file upload)
    private Photo uploadPhoto;
    private String photoIDofMongo;
    private UploadPhotoServise uploadPhotoServise;

    private byte[] photoFileContend; 
    private UserServise userServise;

    private String strPredictions;

    private VerticalLayout notifiLayout;
    private VerticalLayout photoAndBtnLayout;
    private VerticalLayout strPredictedVerticalLayout;

    private UI ui;

    private Thread longTaskThread;

    public  UploadPhotoPage(PhotoServise photoService, UserServise userServise,UploadPhotoServise uploadPhotoServise) {
        this.photoService = photoService;
        this.userServise = userServise;
        this.uploadPhotoServise = uploadPhotoServise;
        strPredictions = "";
        notifiLayout = new VerticalLayout();
        photoAndBtnLayout = new VerticalLayout();
        strPredictedVerticalLayout = new VerticalLayout();
        

        if (!isUserAuthorized())
        {
            System.out.println("-------- User NOT Authorized - can't use! --------");
            //Notification.show("You need to login or register first",5000,Position.TOP_CENTER);
            UI.getCurrent().getPage().setLocation("/"); // Redirect to login page (HomePage).
            return;
        }

        creatPhotoUpload();//יוצר את התבנית להעלאת התמונה

        HorizontalLayout heder = new HorizontalLayout();
        HorizontalLayout heder2 = new HorizontalLayout();
        HorizontalLayout heder3 = new HorizontalLayout();
        VerticalLayout verticalLayout = new VerticalLayout();

        H4 h = new H4("Upload file");
        h.getStyle().setColor("blue");
        String str = "To upload a photo, click on the";
        String str1 = "button";
        String str2 = "choose a photo from your personal computer";

        heder2.add(new H4(str));
        heder2.add(h);
        heder2.add(new H4(str1));
        heder3.add(new H4(str2) );

        verticalLayout.add(heder2,heder3);

        heder.add(singleFileUpload);
        heder.add(verticalLayout);
       
        System.out.println("UploadPhotoPage=======>>\n");;
        add(new H1("Photo Upload"));
        
        add(heder);
        

        setSizeFull();
        setAlignItems(Alignment.CENTER);

    }

    private void remove(String attribute) {
        try {
            
             boolean b = photoService.removPhotoById(this.photoIDofMongo);
             System.out.println("b = "+b);
             if(b==true){
                Notification.show("remove Succeeded",5000, Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                this.photoIDofMongo = null;
             }
             photoAndBtnLayout.removeAll();
            strPredictedVerticalLayout.removeAll();
            
        } catch (Exception e) {
            Notification.show("Remove Failed",5000, Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
        
        
    }
    private void sendToCNN(){
        /* 
        if(this.photoID==null)
        {
            Notification.show("You must upload a photo before sending for evaluation",10000,Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_WARNING);
            return;
        }*/
        
        
        Photo photo = photoService.getPhotoById(this.photoIDofMongo);
        System.out.println("sendToNN==>>"+photo.getName());
        byte[] photoFileContend = uploadPhoto.getContend();
        System.out.println("========================>>"+photoFileContend.toString());
       
        String tmp = "tempPhotos";
        //שמירת התמונה על המחשב
        try {
            OutputStream out = new FileOutputStream(tmp+"\\"+photoIDofMongo+".jpg");
            out.write(photoFileContend);
            out.flush();
            out.close();
            
            System.out.println("YESSSSSSSSSSSSS");
        } catch (Exception e) {
            System.out.println("OutputStream =====>>"+e.toString());
        }
       
        String pathPython = "src\\main\\java\\israela\\image_classification_project\\CNN.py";
        String pathImage = tmp+"\\"+photoIDofMongo+".jpg";
        
        startLongTask(pathPython,pathImage);
        
    }
    private void creatPhotoUpload() {

        /* Example for MemoryBuffer */
        ui = UI.getCurrent();
        MemoryBuffer memoryBuffer = new MemoryBuffer();//מאגר נתונים (או סתם מאגר) הוא אזור בזיכרון המשמש לאחסון נתונים באופן זמני בזמן שהם מועברים ממקום אחד לאחר
        singleFileUpload = new Upload(memoryBuffer);
        singleFileUpload.setAcceptedFileTypes("image/*");
        //singleFileUpload.setMaxFileSize(0);//הגבלה של הגודל

        singleFileUpload.addSucceededListener(event -> {
            photoAndBtnLayout.removeAll();
            strPredictedVerticalLayout.removeAll();
            Notification.show("Photo Upload to Server Succeeded!", 5000, Position.BOTTOM_START);

            System.out.println("File name: "+event.getFileName());
            System.out.println("File size: "+event.getContentLength());
            System.out.println("File type: "+event.getMIMEType());
            System.out.println("");
        
            try {
                HorizontalLayout btnLayout = new HorizontalLayout();
                photoFileContend =  memoryBuffer.getInputStream().readAllBytes();
                
                Long idUser = Long.parseLong((String)VaadinSession.getCurrent().getSession().getAttribute("userId"));
                User user = userServise.getUserById(idUser);

                uploadPhoto = new Photo(event.getFileName(), user.getName(), photoFileContend);
                this.photoIDofMongo = photoService.addPhoto(uploadPhoto, idUser);
                Button btnSendToModel = new Button("Send to model", e -> sendToCNN());
                btnSendToModel.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
                btnLayout.add(btnSendToModel);
                Button btnRemovePhoto = new Button("Remove Photo", e -> remove((String)VaadinSession.getCurrent().getSession().getAttribute("userId")));
                Button btnTestMsg = new Button("Test Msg", e -> notifiay());
                btnRemovePhoto.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
                btnLayout.add(btnRemovePhoto);
                btnLayout.add(btnTestMsg);
                
                photoAndBtnLayout.add(btnLayout);
               
                try{
                ArrayList<Photo> list = photoService.getPhotoByUserId(idUser);
                System.out.println("*****************************");
                System.out.println("Size of photoUser = "+list.size());
                System.out.println("*****************************\n");

                showPhotoOnPage(uploadPhoto.getContend(), uploadPhoto);
                photoAndBtnLayout.setAlignItems(Alignment.CENTER);
                add(photoAndBtnLayout);
                }
                catch (Exception e){
                    System.out.println("error of photoService====>>\n");
                }
            } catch (Exception e) {
                
                System.out.println("ERROR=======>>creatPhotoUpload\n");
                this.photoIDofMongo = null;
            }
            
        });
        
    }
    
    private void notifiay() {
        
        H3 str = new H3("The model is calculating, please wait.");

        strPredictedVerticalLayout.add(str);
        strPredictedVerticalLayout.setAlignItems(Alignment.CENTER);
        add(strPredictedVerticalLayout);
        //verticalLayout.add(zzzString);
        setAlignItems(Alignment.CENTER);

    }

    private void showPhotoOnPage(byte[] photoFileContend, Photo uploadPhotoo) {

        StreamResource resource = new StreamResource("stam.jpg", new InputStreamFactory() 
        {
            public java.io.InputStream createInputStream() 
            {
                return new ByteArrayInputStream(photoFileContend);
            };
        });
       
        Image image = new Image(resource, uploadPhotoo.getName());
        image.setHeight("200px");
        image.setWidth("200px");

        photoAndBtnLayout.add(image);
  
    }

    private boolean isUserAuthorized()
    {
        // try to get 'username' from session cookie (was created in the Welcome(login) page).
        String userName = (String)VaadinSession.getCurrent().getSession().getAttribute("username");

        return (userName == null) ? false : true;
    }

    private void startLongTask(String pathPython, String pathImage)
   {
      longTaskThread = uploadPhotoServise.doLongTask1(pathPython, pathImage,new LongTasklistener()
      {

         @Override
         public void onStart(String msg)
         {
            updateUI(msg);
         }

         @Override
         public void onUpdate(String msg)
         {
            updateUI(msg);
         }

         @Override
         public void onInterupt(String msg)
         {
            updateUI(msg);
         }

         @Override
         public void onFinish(String msg)
         {
            ui.access(() ->updateUIOnFinish(msg));

            // UI ui = getUI().orElseThrow();
            // ui.access(() -> {
            //    btnCancel.setEnabled(false);
            //    btnStart.setEnabled(true);
            // });
         }

      });
      longTaskThread.start();
    //   btnCancel.setEnabled(true);
    //   btnStart.setEnabled(false);
   }

    private void updateUI(String msg)
   {
      UI ui = getUI().orElseThrow();
      //ui.access(() -> logMsgArea.setValue(logMsgArea.getValue() + "\n" + msg));
      ui.access(() -> notifiay());
   }

   private void updateUIOnFinish(String msg) {
    strPredictedVerticalLayout.removeAll();
    String tmp = "tempPhotos";
    strPredictions = msg;

    try {
        
        double d = Double.parseDouble((String)strPredictions.toString());
        System.out.println("double = "+d);
        if(d>50)
        {
            //Notification.show("Realism", 5000, Position.BOTTOM_START);
            strPredictions = "Realism";
        }
        else{
            //Notification.show("Abstract", 5000, Position.BOTTOM_START);
            strPredictions = "Abstract";
        }
        H1 h1 = new H1(strPredictions);
        h1.getStyle().setColor("green");
        strPredictedVerticalLayout.add(h1);
        strPredictedVerticalLayout.add(new H2("The model classified your image into a category: "+strPredictions));
        //add(strPredicted);
        if(d>50)
        {
            strPredictedVerticalLayout.add(new H2("with an accuracy of: "+d));
        }
        else{
            double p2 = 100-d;
            strPredictedVerticalLayout.add(new H2("with an accuracy of: "+p2));
        }
        strPredictedVerticalLayout.setAlignItems(Alignment.CENTER);
        add(strPredictedVerticalLayout);

        try {
            boolean b =photoService.setClassification(uploadPhoto, strPredictions);
            if(b==true)
                System.out.println("secsses");
            
        } catch (Exception e) {
            System.out.println("\nError is send to CNN==> setClassification=>"+e.toString());
        }

    } catch (Exception e) {
        System.out.println(e.toString());
        System.out.println("Cnut convert");
        System.out.println(strPredictions);
        }

        File f = new File(tmp+"\\"+photoIDofMongo+".jpg");
        try {   
            f.delete();
            System.out.println("\n"+f.getName() + " deleted");
            
        } catch (Exception e) {
            System.out.println("\nfailed to deleted the file"+e.toString());
        }

        notifiLayout.removeAll();
            
   }
 
}

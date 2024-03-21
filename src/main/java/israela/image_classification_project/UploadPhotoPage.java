package israela.image_classification_project;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.stream.Collectors;

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

import israela.image_classification_project.UploadPhotoServise.LongTasklistener;

//import israela.image_classification_project.UploadPhotoServise.LongTasklistener;

//import py4j.GatewayServer;
//from py4j.java_gateway import JavaGateway;

@Route(value = "/upload", layout = AppMainLayout.class)
public class UploadPhotoPage extends VerticalLayout{
    private PhotoServise photoService;
    private Upload singleFileUpload; //UI component (file upload)
    private Photo uploadPhoto;
    private String photoIDofMongo;
    private UploadPhotoServise uploadPhotoServise;

    private String strOfOutpotPhyton;
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
       // Notification.show("The model start working",5000, Position.TOP_CENTER);
       /*
       String currentDirectory = System.getProperty("Images");
        
       // Define the relative path to the Java file you want to execute
       String relativePath = "src\\main\\java\\israela\\image_classification_project\\";
       
       // Construct the absolute path
       String absolutePath = currentDirectory + File.separator + relativePath;
       System.out.println("\nabsolutePath = "+absolutePath);
       */
      String tmp = "tempPhotos"; //getClass().getResource("images").getPath();
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
       
        System.out.println("tmp = "+tmp);
        String pathPython = "src\\main\\java\\israela\\image_classification_project\\CNN.py";
        //String pathPython = "C:\\Users\\user\\Documents\\VSProj\\image_classification_project\\src\\main\\java\\israela\\image_classification_project\\CNN.py";
        String pathImage = tmp+"\\"+photoIDofMongo+".jpg";
        //System.out.println("src\\main\\resources\\META-INF\\resources\\images\\"+photoIDofMongo+".jpg");
        //String pathImage = "C:\\Users\\user\\Desktop\\savePhoto\\"+photoIDofMongo+".jpg";
        //String pathImage ="C:\\Users\\user\\Desktop\\savePhoto\\00c5774bc9883453a565f949e4b1e19b.jpg";
        startLongTask(pathPython,pathImage);
        // String [] cmd = new String[3];
        // cmd[0] = "python";
        // cmd[1] = pathPython;
        // cmd[2] = pathImage;
        
        // strPredictions ="";
        // Runtime r = Runtime.getRuntime();
        // System.out.println("Runtime==>>");
        // try {
        //     //מייצגת תהליך מערכת
        //     //כאשר פקודה מבוצעת באמצעות exec(), היא מחזירה אובייקט Process המייצג את התהליך החדש שנוצר.
        //     Process p = r.exec(cmd);
        //     BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        //     while((strOfOutpotPhyton=in.readLine()) != null){
        //         //Notification.show(strOfOutpotPhyton,10000,Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        //         System.out.println("java = "+strOfOutpotPhyton);
        //         strPredictions = strOfOutpotPhyton;
        //     }
        // } catch (Exception e) {
        //     System.out.println("sendToNN ERROR  Process p = r.exec(cmd);===>>"+e.toString());
        // }

        //Notification.show(strPredictions, 5000, Position.BOTTOM_START);

        // try {
        //     double d = Double.parseDouble((String)strPredictions.toString());
        //     System.out.println("double = "+d);
        //     if(d>50)
        //     {
        //         //Notification.show("Realism", 5000, Position.BOTTOM_START);
        //         strPredictions = "Realism";
        //     }
        //     else{
        //         //Notification.show("Abstract", 5000, Position.BOTTOM_START);
        //         strPredictions = "Abstract";
        //     }
        //     H1 h1 = new H1(strPredictions);
        //     h1.getStyle().setColor("green");
        //     strPredictedVerticalLayout.add(h1);
        //     strPredictedVerticalLayout.add(new H2("The model classified your image into a category: "+strPredictions));
        //     //add(strPredicted);
        //     if(d>50)
        //     {
        //         strPredictedVerticalLayout.add(new H2("with an accuracy of: "+d));
        //     }
        //     else{
        //         double p2 = 100-d;
        //         strPredictedVerticalLayout.add(new H2("with an accuracy of: "+p2));
        //     }
        //     strPredictedVerticalLayout.setAlignItems(Alignment.CENTER);
        //     add(strPredictedVerticalLayout);

        //     try {
        //         boolean b =photoService.setClassification(uploadPhoto, strPredictions);
        //         if(b==true)
        //             System.out.println("secsses");
                
        //     } catch (Exception e) {
        //         System.out.println("\nError is send to CNN==> setClassification=>"+e.toString());
        //     }

        // } catch (Exception e) {
        //     System.out.println(e.toString());
        //     System.out.println("Cnut convert");
        //     System.out.println(strPredictions);
        //     }

        //     File f = new File(tmp+"\\"+photoIDofMongo+".jpg");
        //     try {   
        //         f.delete();
        //         System.out.println("\n"+f.getName() + " deleted");
                
        //     } catch (Exception e) {
        //         System.out.println("\nfailed to deleted the file"+e.toString());
        //     }

        //     notifiLayout.removeAll();
        
    }
    private void creatPhotoUpload() {

        
        //photoAndBtnLayout = new VerticalLayout();
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
                Button btnSendToModel = new Button("Send to model", e -> {
                    //notifiay();
                    sendToCNN();
                    //startLongTask();
                    
                    // Thread t = new Thread(new Runnable() {
                    //     @Override
                    //     public void run() {
                    //         //System.out.println("UI.getCurrent()= "+UI.getCurrent().getId());
                    //         ui.access(() -> sendToCNN());
                    //     }

                    
                    // });
                    // t.start();

                    
                });
                btnSendToModel.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
                btnLayout.add(btnSendToModel);
                Button btnRemovePhoto = new Button("Remove Photo", e -> remove((String)VaadinSession.getCurrent().getSession().getAttribute("userId")));
                Button btnTestMsg = new Button("Test Msg", e -> notifiay());
                btnRemovePhoto.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
                btnLayout.add(btnRemovePhoto);
                btnLayout.add(btnTestMsg);
                
                //add(btnLayout);
                photoAndBtnLayout.add(btnLayout);
               
                //showPhotoOnPage(photoFileContend, uploadPhoto);
                try{
                ArrayList<Photo> list = photoService.getPhotoByUserId(idUser);
                System.out.println("*****************************");
                System.out.println("Size of photoUser = "+list.size());
                System.out.println("*****************************\n");
                //showPhotoOnPage(list.get(list.size()-1).getContend(), uploadPhoto);
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
        
        H3 zzzString = new H3("The model is calculating, please wait.");
        //Notification.show(zzzString,5000,Position.TOP_CENTER);
        strPredictedVerticalLayout.add(zzzString);
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
        //add(image);
        photoAndBtnLayout.add(image);
  
    }

    private boolean isUserAuthorized()
    {
        // try to get 'username' from session cookie (was created in the Welcome(login) page).
        String userName = (String)VaadinSession.getCurrent().getSession().getAttribute("username");

        return (userName == null) ? false : true;
    }

    // private void startLongTask(String pathPython, String pathImage)
    // {
    //     try {
    //         strPredictions =  this.uploadPhotoServise.doLongTask1(10,pathPython, pathImage);
            
    //     } catch (Exception e) {
    //         System.out.println("ERROR in startLongTask==>> "+e.toString());
    //     }
        
    //     LongTasklistener
    //    longTaskThread = this.uploadPhotoServise.doLongTask1(10,pathPython, pathImage,new LongTasklistener()
    //    {
 
    //       @Override
    //       public void onStart(String msg)
    //       {
    //          updateUI(msg);
    //       }
 
    //       @Override
    //       public void onUpdate(String msg)
    //       {
    //          updateUI(msg);
    //       }
 
    //       @Override
    //       public void onInterupt(String msg)
    //       {
    //          updateUI(msg);
    //       }
 
    //       @Override
    //       public void onFinish(String msg)
    //       {
    //          updateUIOnFinish(msg);
 
    //         //  UI ui = getUI().orElseThrow();
    //         //  ui.access(() -> {
    //         //     btnCancel.setEnabled(false);
    //         //     btnStart.setEnabled(true);
    //         //  });
    //       }
        
        
    


        
    //    });
    // }
    private void startLongTask(String pathPython, String pathImage)
   {
      longTaskThread = uploadPhotoServise.doLongTask1(10, pathPython, pathImage,new LongTasklistener()
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

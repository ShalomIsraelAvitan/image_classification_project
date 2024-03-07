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
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
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

//import py4j.GatewayServer;
//from py4j.java_gateway import JavaGateway;

@Route(value = "/upload", layout = AppMainLayout.class)
public class UploadPhotoPage extends VerticalLayout{
    private PhotoServise photoService;
    private Upload singleFileUpload; //UI component (file upload)
    private Photo uploadPhoto;
    private String photoIDofMongo;

    private String strOfOutpotPhyton;
    private byte[] photoFileContend; 
    private UserServise userServise;

    private String strPredictions;

    public  UploadPhotoPage(PhotoServise photoService, UserServise userServise) {
        this.photoService = photoService;
        this.userServise = userServise;
        strPredictions = "";
        

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
        StreamResource resource = new StreamResource("stam.jpg", new InputStreamFactory() 
        {
            public java.io.InputStream createInputStream() 
            {
                return new ByteArrayInputStream(photoFileContend);
            };
        });
       
        Image image = new Image(resource, uploadPhoto.getName());
        //שמירת התמונה על המחשב
        try {
            OutputStream out = new FileOutputStream("C:\\Users\\user\\Desktop\\savePhoto\\"+photoIDofMongo+".jpg");
            out.write(photoFileContend);
            out.flush();
            out.close();
            
            System.out.println("YESSSSSSSSSSSSS");
        } catch (Exception e) {
            System.out.println("OutputStream =====>>"+e.toString());
        }

        String pathPython = "C:\\Users\\user\\Documents\\VSProj\\milestone2\\src\\main\\java\\israela\\milestone2\\CNN.py";
        //String pathImage = "C:\\Users\\user\\Desktop\\savePhoto\\"+photoID+".png";
        String pathImage = "C:\\Users\\user\\Desktop\\savePhoto\\"+photoIDofMongo+".jpg";
        //String pathImage ="C:\\Users\\user\\Desktop\\savePhoto\\00c5774bc9883453a565f949e4b1e19b.jpg";

        String [] cmd = new String[3];
        cmd[0] = "python";
        cmd[1] = pathPython;
        cmd[2] = pathImage;

        
        strPredictions ="";
        Runtime r = Runtime.getRuntime();
        System.out.println("Runtime==>>");
        try {
            Process p = r.exec(cmd);
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while((strOfOutpotPhyton=in.readLine()) != null){
                //Notification.show(strOfOutpotPhyton,10000,Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                System.out.println("java = "+strOfOutpotPhyton);
                strPredictions = strOfOutpotPhyton;
            }
        } catch (Exception e) {
            System.out.println("sendToNN ERROR  Process p = r.exec(cmd);===>>"+e.toString());
        }

        Notification.show(strPredictions, 5000, Position.BOTTOM_START);

        try {
            double p = Double.parseDouble((String)strPredictions.toString());
            System.out.println("double = "+p);
            if(p>50)
            {
                //Notification.show("Realism", 5000, Position.BOTTOM_START);
                strPredictions = "Realism";
            }
            else{
                //Notification.show("Abstract", 5000, Position.BOTTOM_START);
                strPredictions = "Abstract";
            }

            add(new H2("The model classified your image into a category: "+strPredictions));
            if(p>50)
            {
                add(new H2("with an accuracy of: "+p));
            }
            else{
                double p2 = 100-p;
                add(new H2("with an accuracy of: "+p2));
            }

            boolean b =photoService.setClassification(uploadPhoto, strPredictions);
                if(b==true){
                    System.out.println("secsses");
                    //remove(this.photoIDofMongo);
                    //photoService.addPhoto(uploadPhoto, uploadPhoto.getIdOfUser());
                }
                else
                    System.out.println("not work");
            
          
           

        } catch (Exception e) {
            System.out.println(e.toString());
            System.out.println("Cnut convert");
            System.out.println(strPredictions);
            }

            File f = new File("C:\\Users\\user\\Desktop\\savePhoto\\"+photoIDofMongo+".jpg");
            try {
                f.delete();
                System.out.println("\n"+f.getName() + " deleted");
                
            } catch (Exception e) {
                System.out.println("\nfailed to deleted the file"+e.toString());
            }
        
    }
    private void sendToCNN2() {
        if (this.photoIDofMongo == null) {
            Notification.show("You must upload a photo before sending for evaluation", 10000, Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_WARNING);
            return;
        }
    
        Photo photo = photoService.getPhotoById(this.photoIDofMongo);
        byte[] photoFileContent = photo.getContend();
    
        StreamResource resource = new StreamResource("stam.jpg", () -> new ByteArrayInputStream(photoFileContent));
    
        Image image = new Image(resource, photo.getName());
    
        try {
            String outputPath = "C:\\Users\\user\\Desktop\\savePhoto\\" + photoIDofMongo + ".jpg";
            try (OutputStream out = new FileOutputStream(outputPath)) {
                out.write(photoFileContent);
            }
    
            System.out.println("File saved successfully at: " + outputPath);
        } catch (Exception e) {
            System.out.println("OutputStream error: " + e.toString());
        }
    
        String pathPython = "C:\\Users\\user\\Documents\\VSProj\\milestone2\\src\\main\\java\\israela\\milestone2\\CNN.py";
        String pathImage = "C:\\Users\\user\\Desktop\\savePhoto\\" + this.photoIDofMongo + ".jpg";
    
        Thread t = new Thread(() -> {
            try {
                String[] cmd = {"python", pathPython, pathImage};
                Process process = Runtime.getRuntime().exec(cmd);
    
                try (BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    strPredictions = in.lines().collect(Collectors.joining(System.lineSeparator()));
                }
    
                UI.getCurrent().access(() -> {
                    Notification.show(strPredictions, 5000, Position.BOTTOM_START);
    
                    try {
                        double p = Double.parseDouble(strPredictions);
                        String category = (p > 50) ? "Realism" : "Abstract";
                        double accuracy = (p > 50) ? p : 100 - p;
    
                        add(new H2("The model classified your image into a category: " + category));
                        add(new H2("with an accuracy of: " + accuracy));
    
                        Long idUser = Long.parseLong(String.valueOf(VaadinSession.getCurrent().getSession().getAttribute("userId")));
                        boolean b = photoService.setClassification(uploadPhoto, category);
    
                        synchronized (photoIDofMongo) {
                            photoIDofMongo = photoService.addPhoto(uploadPhoto, idUser);
                        }
    
                        if (b) {
                            System.out.println("Success");
                        } else {
                            System.out.println("Not successful");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Failed to convert predictions to double: " + strPredictions);
                    }
                });
    
            } catch (IOException e) {
                System.out.println("Error executing Python script: " + e.toString());
            }
        });
    
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted: " + e.toString());
            Thread.currentThread().interrupt();
        }
    
        UI.getCurrent().access(() -> {
            String filePath = "C:\\Users\\user\\Desktop\\savePhoto\\" + this.photoIDofMongo + ".jpg";
            File fileToDelete = new File(filePath);
    
            if (fileToDelete.delete()) {
                System.out.println(fileToDelete.getName() + " deleted successfully");
            } else {
                System.out.println("Failed to delete the file: " + fileToDelete.getName());
            }
        });
}

private void creatPhotoUpload2() {


    MemoryBuffer memoryBuffer = new MemoryBuffer();
    singleFileUpload = new Upload(memoryBuffer);
    singleFileUpload.setAcceptedFileTypes("image/*");

    singleFileUpload.addSucceededListener(event -> {
        Notification.show("Photo Upload to Server Succeeded!", 5000, Position.TOP_CENTER);

        try {
            this.photoFileContend = memoryBuffer.getInputStream().readAllBytes();

            Long idUser = Long.parseLong(String.valueOf(VaadinSession.getCurrent().getSession().getAttribute("userId")));
            User user = userServise.getUserById(idUser);

            uploadPhoto = new Photo(event.getFileName(), user.getName(), photoFileContend);
            this.photoIDofMongo = photoService.addPhoto(uploadPhoto, idUser);

            add(new Button("Send to Evaluation", e -> sendToCNN()));
            add(new Button("Remove Photo", e -> remove(String.valueOf(VaadinSession.getCurrent().getSession().getAttribute("userId")))));

            try {
                ArrayList<Photo> list = photoService.getPhotoByUserId(idUser);
                System.out.println("Size of photoUser = " + list.size());

                showPhotoOnPage(uploadPhoto.getContend(), uploadPhoto);
            } catch (Exception e) {
                System.out.println("Error retrieving photos from the service: " + e.toString());
            }
        } catch (IOException e) {
            System.out.println("Error reading photo content: " + e.toString());
            this.photoIDofMongo = null;
        }
    });
}
    

private void sendToCNN3(){

        if(this.photoIDofMongo==null)
        {
            Notification.show("You must upload a photo before sending for evaluation",10000,Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_WARNING);
            return;
        }
        
        
        Photo photo = photoService.getPhotoById(this.photoIDofMongo);
        System.out.println("sendToNN==>>"+photo.getName());
        byte[] photoFileContend = photo.getContend();
        System.out.println("========================>>"+photoFileContend.toString());
       // Notification.show("The model start working",5000, Position.TOP_CENTER);
        StreamResource resource = new StreamResource("stam.jpg", new InputStreamFactory() 
        {
            public java.io.InputStream createInputStream() 
            {
                return new ByteArrayInputStream(photoFileContend);
            };
        });
       
        Image image = new Image(resource, photo.getName());
        try {
            OutputStream out = new FileOutputStream("C:\\Users\\user\\Desktop\\savePhoto\\"+photoIDofMongo+".jpg");
            out.write(photoFileContend);
            out.flush();
            out.close();
            
            System.out.println("YESSSSSSSSSSSSS");
        } catch (Exception e) {
            System.out.println("OutputStream =====>>"+e.toString());
        }
        
        
        String pathPython = "C:\\Users\\user\\Documents\\VSProj\\milestone2\\src\\main\\java\\israela\\milestone2\\CNN.py";
        //String pathImage = "C:\\Users\\user\\Desktop\\savePhoto\\"+photoID+".png";
        String pathImage = "C:\\Users\\user\\Desktop\\savePhoto\\"+photoIDofMongo+".jpg";
        //String pathImage ="C:\\Users\\user\\Desktop\\savePhoto\\00c5774bc9883453a565f949e4b1e19b.jpg";
        
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
               // Notification.show("The model start working2",5000, Position.TOP_CENTER);
                
                String [] cmd = new String[3];
                cmd[0] = "python";
                cmd[1] = pathPython;
                cmd[2] = pathImage;
        
                
                strPredictions ="";
                Runtime r = Runtime.getRuntime();
                System.out.println("Runtime==>>");
                try {
                    Process p = r.exec(cmd);
                    BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    while((strOfOutpotPhyton=in.readLine()) != null){
                        //Notification.show(strOfOutpotPhyton,10000,Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                        System.out.println("java = "+strOfOutpotPhyton);
                        strPredictions = strOfOutpotPhyton;
                    }
                } catch (Exception e) {
                    System.out.println("sendToNN ERROR  Process p = r.exec(cmd);===>>"+e.toString());
                }
        
                Notification.show(strPredictions, 5000, Position.BOTTOM_START);
        
                
            }
            });
            try {
                t.start();
                
            t.join();
            try {
                double p = Double.parseDouble((String)strPredictions.toString());
                System.out.println("double = "+p);
                if(p>50)
                {
                    strPredictions = "Realism";
                }
                else{
                    strPredictions = "Abstract";
                }
    
                add(new H2("The model classified your image into a category: "+strPredictions));
                if(p>50)
                {
                    add(new H2("with an accuracy of: "+p));
                }
                else{
                    double p2 = 100-p;
                    add(new H2("with an accuracy of: "+p2));
                }
                Long idUser = Long.parseLong((String)VaadinSession.getCurrent().getSession().getAttribute("userId"));
                boolean b =photoService.setClassification(uploadPhoto, strPredictions);
                
                if(b==true){
                        System.out.println("secsses");
    
                        // Long idUser = Long.parseLong((String)VaadinSession.getCurrent().getSession().getAttribute("userId"));
                        
                        // this.photoIDofMongo = photoService.addPhoto(uploadPhoto, idUser);
    
                        // if(this.photoIDofMongo!=null)
                        // {
                        //     Notification.show("Photo Upload to Server Succeeded!", 5000, Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS);;
                        // }
                        // else
                        // {
                        //     Notification.show("Photo Failde Upload to Server", 5000, Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_ERROR);;
                        // }
                        //remove(this.photoIDofMongo);
                        //photoService.addPhoto(uploadPhoto, uploadPhoto.getIdOfUser());
                        //Long idUser = Long.parseLong((String)VaadinSession.getCurrent().getSession().getAttribute("userId"));
                        
                        //uploadPhoto = new Photo("event.getFileName()", "user.getName()", photoFileContend);
                        //this.photoIDofMongo = photoService.addPhoto(uploadPhoto, idUser);
                    }
                    else
                        System.out.println("not work");
                
              
               
    
            } catch (Exception e) {
                System.out.println(e.toString());
                System.out.println("Cnut convert");
                System.out.println(strPredictions);
                }
        
            
            File f = new File("C:\\Users\\user\\Desktop\\savePhoto\\"+this.photoIDofMongo+".jpg");
            if(f.delete())
            {
                System.out.println("\n"+f.getName() + " deleted");
            }
            else{
                System.out.println("\nfailed to deleted the file"); 
            }
                
            } catch (Exception e) {
                System.out.println("NOT WORK ==>>"+e.toString());
            }
            
            
        
        
    }

    private void creatPhotoUpload() {

        /* Example for MemoryBuffer */

        MemoryBuffer memoryBuffer = new MemoryBuffer();//מאגר נתונים (או סתם מאגר) הוא אזור בזיכרון המשמש לאחסון נתונים באופן זמני בזמן שהם מועברים ממקום אחד לאחר
        singleFileUpload = new Upload(memoryBuffer);
        singleFileUpload.setAcceptedFileTypes("image/*");
        //singleFileUpload.setMaxFileSize(0);//הגבלה של הגודל

        singleFileUpload.addSucceededListener(event -> {
            Notification.show("Photo Upload to Server Succeeded!", 5000, Position.TOP_CENTER);

            // Get information about the uploaded file
            //InputStream fileData = memoryBuffer.getInputStream();
            //String fileName = event.getFileName();
            //long contentLength = event.getContentLength();
            //String mimeType = event.getMIMEType();

            // Do something with the file data
            // processFile(fileData, fileName, contentLength, mimeType);

            System.out.println("File name: "+event.getFileName());
            System.out.println("File size: "+event.getContentLength());
            System.out.println("File type: "+event.getMIMEType());
            System.out.println("");
        
            try {
                photoFileContend =  memoryBuffer.getInputStream().readAllBytes();
                
                
                //showPhotoOnPage(photoFileContend, uploadPhoto);
                Long idUser = Long.parseLong((String)VaadinSession.getCurrent().getSession().getAttribute("userId"));
                User user = userServise.getUserById(idUser);
                
                //uploadPhoto.setPhoto(event.getFileName(), user.getName(), photoFileContend);
                uploadPhoto = new Photo(event.getFileName(), user.getName(), photoFileContend);
                
                //this.photoIDofMongo = photoService.addPhoto(uploadPhoto, idUser);
                this.photoIDofMongo = photoService.addPhoto(uploadPhoto, idUser);
                
                add(new Button("Send to Evaluation", e -> sendToCNN()));
                add(new Button("Remove Photo", e -> remove((String)VaadinSession.getCurrent().getSession().getAttribute("userId"))));

                //Notification.show("Photo Upload to DB Succeeded!", 5000, Position.TOP_CENTER);
               
                //showPhotoOnPage(photoFileContend, uploadPhoto);
                try{
                ArrayList<Photo> list = photoService.getPhotoByUserId(idUser);
                System.out.println("*****************************");
                System.out.println("Size of photoUser = "+list.size());
                System.out.println("*****************************\n");
                //showPhotoOnPage(list.get(list.size()-1).getContend(), uploadPhoto);
                showPhotoOnPage(uploadPhoto.getContend(), uploadPhoto);
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
        add(image);
  
    }

    private boolean isUserAuthorized()
    {
        // try to get 'username' from session cookie (was created in the Welcome(login) page).
        String userName = (String)VaadinSession.getCurrent().getSession().getAttribute("username");

        return (userName == null) ? false : true;
    }


}

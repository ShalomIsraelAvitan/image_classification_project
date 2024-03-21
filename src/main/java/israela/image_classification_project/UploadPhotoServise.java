package israela.image_classification_project;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.springframework.stereotype.Service;

@Service
public class UploadPhotoServise {
    public  String strPredictions;
    
    // private PhotoServise photoServise;
    // public UploadPhotoServise(PhotoServise photoServise)
    // {
    //     this.photoServise = photoServise;
    // }

    public interface LongTasklistener
   {
      public void onStart(String msg);

      public void onUpdate(String msg);

      public void onInterupt(String msg);

      public void onFinish(String msg);

      //public void updateUIOnFinish(String msg);

      //public void model();
   }


    public Thread doLongTask1(String pathPython, String pathImage, LongTasklistener listener)
   {
      Thread thread = new Thread(new Runnable()
      {
         @Override
         public void run()
         {
            
            listener.onStart(Thread.currentThread().getName() + " run() Started.");
            
            //listener.model();
            String [] cmd = new String[3];
            cmd[0] = "python";
            cmd[1] = pathPython;
            cmd[2] = pathImage;
            
            strPredictions ="";
            String strOfOutpotPhyton= "";
            Runtime r = Runtime.getRuntime();
            System.out.println("Runtime==>>");
            try {
                //מייצגת תהליך מערכת
                //כאשר פקודה מבוצעת באמצעות exec(), היא מחזירה אובייקט Process המייצג את התהליך החדש שנוצר.
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

            
            //int sec = 0;
            // while (sec < seconds)
            // {
            //    sec++;
            //    listener.onUpdate(Thread.currentThread().getName() + " run() Update " + sec + "/" + seconds);
            //    try
            //    {
            //       Thread.sleep(1000);
            //    }
            //    catch (InterruptedException e)
            //    {
            //       listener.onInterupt(Thread.currentThread().getName() + " run() Interrupted!");
            //       break;
            //    }
            // }
            listener.onFinish(strPredictions);
            
         }
      });
      

     return thread;
     
   }
    
}

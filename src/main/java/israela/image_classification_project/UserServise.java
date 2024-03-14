package israela.image_classification_project;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class UserServise {
    
    private UserRepository userRepo;
    private ArrayList<UserGridChangeListener> listeners;
    private ArrayList<User> usersList;

    // create chat change listener using "Observer" Desing Pattern.
   public interface UserGridChangeListener
   {
      public void onChange();
   }

    public UserServise(UserRepository userRepo) {
        this.userRepo = userRepo;
        listeners = new ArrayList<UserGridChangeListener>();
        usersList = new ArrayList<>();
    }

    
    public User getUserById(Long id)
    {
        return userRepo.findUserById(id);
    }

    public boolean addUser(User newUser)
    {
        try {
            Long id = Long.parseLong("111111111");
            if(newUser.getId().equals(id))
            {
                newUser.setAdmin(true);
            }
            else{
                newUser.setAdmin(false);
            }
            userRepo.insert(newUser);

           // usersList.add(newUser);
           synchronized(listeners){
            for (UserGridChangeListener listener : listeners)
                    listener.onChange();
           }
            return true;
            
        } catch (Exception e) {
            
            System.out.println("\nERROR======>addUser\n"+e.toString());
            return false;
        }
    }

    public boolean deletUser(User user)
    {
        try {
            userRepo.delete(user);
            return true;
        } catch (Exception e) {
            
            return false;
        }
    }


    public boolean isUserExists(String name, Long id, int pw)
    {
        User user = userRepo.findUserById(id);
        //User user = userRepo.findByName(name);
        if(user!=null)
            if(user.getName()==name && user.getPw()==pw)
                return true;
        return false;
    }

    public boolean isIdUsd(Long id)
    {
        User user = userRepo.findUserById(id);

        if(user!=null)
        {
            return true;

        }
        return false;
    }


    public boolean isUserExistsLogin(String userName, Long idUser, int password) {
        User user = userRepo.findByName(userName);
        if(user!=null)
        {
            if(user.getName().equals(userName))
            {
                if(user.getPw()==password)
                return true;
            }
        }
        return false;

    }


    public List<User> getAllUser() {
        List<User> arrayListUser = userRepo.findAll();
        System.out.println("arrayListUser = "+arrayListUser.size());
        
        return arrayListUser;
    }

    // public ArrayList<User> DrawingPoints() {
    //     synchronized(listeners) {
    //         for (UserGridChangeListener listener : listeners) {
    //             listener.onChange();
    //         }
    //         return usersList;
    //     }
    // }
    // add listener to listener-list


   public void addUserChangeListener(UserGridChangeListener listener)
   {
      synchronized (listeners)
      {
         listeners.add(listener);
      }
   }

}

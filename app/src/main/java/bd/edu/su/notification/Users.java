package bd.edu.su.notification;

import com.google.firebase.firestore.Exclude;

public class Users extends UserId{

    private String name,image;
    @Exclude private String user_id;

    public Users(){}

    public Users(String name, String image) {
        this.name = name;
        this.image = image;
    }


    public String getUser_id() {
        return user_id;
    }
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

package ru.javajava.mechanics.avatar;

import org.jetbrains.annotations.NotNull;
import ru.javajava.model.UserProfile;

/**
 * Created by ivan on 14.11.16.
 */
public class GameUser {
    private final UserProfile userProfile;
   // private final TimingPart timingPart;
   // private final Square square;

    //TODO: Collider


    public GameUser(@NotNull UserProfile userProfile) {
        this.userProfile = userProfile;
   //     square = new Square();
   //     this.timingPart = new TimingPart();
    }



    public UserProfile getUserProfile() {
        return userProfile;
    }



    public long getId() {
        return userProfile.getId();
    }

//    @NotNull
//    public ServerPlayerSnap generateSnap() {
//
//        final ServerPlayerSnap result = new ServerPlayerSnap();
//        result.setUserId(getId());
//        result.setPlayerSquare(square.getSnap());
//        return result;
//    }
}


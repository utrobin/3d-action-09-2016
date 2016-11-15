package ru.javajava.mechanics.avatar;

import org.jetbrains.annotations.NotNull;
import ru.javajava.mechanics.base.Coords;
import ru.javajava.mechanics.base.ServerPlayerSnap;
import ru.javajava.model.UserProfile;

/**
 * Created by ivan on 14.11.16.
 */
public class GameUser {
    private final UserProfile userProfile;
   // private final TimingPart timingPart;
    private final Sphere sphere;

    //TODO: Collider


    public GameUser(UserProfile userProfile) {
        this.userProfile = userProfile;
        sphere = new Sphere();
   //     this.timingPart = new TimingPart();
    }



    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setCoords (Coords coords) {
        this.sphere.setCoords(coords);
    }

    public Coords getCoords () {
        return sphere.getCoords();
    }



    public long getId() {
        return userProfile.getId();
    }

    public ServerPlayerSnap generateSnap() {
        final ServerPlayerSnap result = new ServerPlayerSnap();
        result.setUserId(getId());
        result.setPlayerCoords(sphere.getCoords());
        return result;
    }
}


import java.awt.*;

public class LimitedPlayerState {
    public Color avatar;
    public Position home;
    public Position current;

    public LimitedPlayerState(Color avatar, Position home, Position current) {
        this.avatar = avatar;
        this.home = home;
        this.current = current;
    }

    public Color getAvatar() {
        return avatar;
    }

    public Position getHome() {
        return home;
    }

    public Position getCurrent() {
        return current;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LimitedPlayerState)) {
            return false;
        }
        LimitedPlayerState toCompare = (LimitedPlayerState) o;

        return this.avatar.equals(toCompare.getAvatar()) && this.home.equals(toCompare.getHome())
                && this.current.equals(toCompare.getCurrent());
    }
}

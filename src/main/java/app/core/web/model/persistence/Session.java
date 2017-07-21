package app.core.web.model.persistence;

import app.core.business.model.mapping.Token;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by alexandremasanes on 03/03/2017.
 */
@Component
@Scope(scopeName = WebApplicationContext.SCOPE_SESSION,
       proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Session {

    private User                user;

    private Map<Object, Object> flashAttributes;

    private ArrayList<String> notifications;

    {
        notifications   = new ArrayList<>();
        flashAttributes = new HashMap<>();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void pushNotification(String notification) {
        notifications.add(notification);
    }

    public void pushFlashAttibute(Object key, Object value) {
        flashAttributes.put(key, value);
    }

    public void pushFlashAttibute(Object value) {
        flashAttributes.put(value.getClass(), value);
    }

    public Object flushFlashAttribute(Object key) {
        Object value;

        value = flashAttributes.get(key);

        flashAttributes.remove(key);

        return value;
    }

    public Map<Object, Object> flushFlashAttributes() {
       Map<Object, Object> flashAttributes;

       flashAttributes = new HashMap<>(this.flashAttributes);

       this.flashAttributes.clear();

       return  flashAttributes;
    }

    public ArrayList<String> flushNotifications() {
        ArrayList<String> notifications;
        notifications = new ArrayList<String>(this.notifications);
        this.notifications.clear();
        return notifications;
    }
}

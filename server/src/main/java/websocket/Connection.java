package websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.Objects;

public class Connection {

    public String auth;
    public Session session;

    public Connection(String auth, Session session) {
        this.auth = auth;
        this.session = session;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Connection that)) return false;
        return Objects.equals(auth, that.auth) && Objects.equals(session, that.session);
    }

    @Override
    public int hashCode() {
        return Objects.hash(auth, session);
    }
}

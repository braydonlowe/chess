package websocket;


import org.eclipse.jetty.websocket.api.Session;
import server.JsonUtil;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    private final ConcurrentHashMap<String, Set<Connection>> game = new ConcurrentHashMap<>();

    public void add(String gameID, String auth, Session session) {
        game.computeIfAbsent(gameID, k -> new HashSet<>()).add(new Connection(auth, session));
    }

    public void remove(String gameID, String auth) {
        Set<Connection> users = game.get(gameID);
        if (users != null) {
            users.removeIf(user -> Objects.equals(user.auth, auth));
        }
    }

    public void broadcast(String gameID, String auth, ServerMessage notification) throws IOException {
        Set<Connection> allUsers = game.get(gameID);
        if (allUsers != null) {
            var removeList = new ArrayList<Connection>();
            for (Connection user : allUsers) {
                if (user.session.isOpen()) {
                    if (!user.auth.equals(auth)) {
                        user.send(JsonUtil.toJson(notification));
                    }
                } else {
                    removeList.add(user);
                }
            }
            allUsers.removeAll(removeList);
        }
    }

    public void broadcastAll(String gameID, ServerMessage notification) throws IOException {
        Set<Connection> users = game.get(gameID);
        if (users != null) {
            var removeList = new ArrayList<Connection>();
            for (Connection user : users) {
                if (user.session.isOpen()) {
                    user.send(JsonUtil.toJson(notification));
                } else {
                    removeList.add(user);
                }
            }
            users.removeAll(removeList);
        }
    }
}

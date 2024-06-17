package websocket;

import com.google.gson.Gson;
import com.sun.nio.sctp.NotificationHandler;

import javax.websocket.*;
import java.net.URI;

public class WebSocket extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;


    public WebSocket(String url, NotificationHandler notificationHandler) throws Exception {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String string) {
                    //PetShop turned their notification here into a GSON
                    notificationHandler.notify();
                }
            });
            this.notificationHandler = notificationHandler;
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void send(String s) throws Exception {
        this.session.getBasicRemote().sendText(s);
    }
}
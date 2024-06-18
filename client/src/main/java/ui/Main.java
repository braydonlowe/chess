package ui;

import com.google.gson.Gson;
import model.Auth;
import websocket.NoteHandler;
import websocket.WebSocket;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class Main {

    public static final NoteHandler handler = new NoteHandler() {
        private GameplayUI ui;

        @Override
        public void notify(String message) {
            ServerMessage serverMess = new Gson().fromJson(message, ServerMessage.class);
            ServerMessage.ServerMessageType type = serverMess.getServerMessageType();
            switch (type) {
                case NOTIFICATION:
                    Notification messageNote = new Gson().fromJson(message, Notification.class);
                    UIUtils.printOneLiners(new PrintStream(System.out, true, StandardCharsets.UTF_8), messageNote.getMessageText());
                    break;
                case ERROR:
                    ErrorMessage errorNote = new Gson().fromJson(message, ErrorMessage.class);
                    UIUtils.printOneLiners(new PrintStream(System.out, true, StandardCharsets.UTF_8), errorNote.getMessageText());
                    break;
                case LOAD_GAME:
                    LoadGameMessage loadNote = new Gson().fromJson(message, LoadGameMessage.class);
                    if (loadNote.getTeam().equals("WHITE")) {
                        ui.printBoardWhite(loadNote.getGameData().game().getBoard());
                    } else {
                        ui.printBoardBlack(loadNote.getGameData().game().getBoard());
                    }
                    break;
            }
            ;
        }
    };

        private static WebSocket socket;

        static {
            try {
                socket = new WebSocket("http://localhost:8080", handler);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public static void main(String[] args) throws Exception {
            //Later I'll add something to this function to not run on local.
            String localhost = "http://localhost:8080";
            ServerFacade facade = new ServerFacade(localhost);
            PreLoginUI login = new PreLoginUI();
            boolean mainToggleLoop = false;
            while (!mainToggleLoop) {
                mainToggleLoop = login.menuLoop(facade);
                Auth auth = login.getAuth();
                if (auth == null) {
                    return;
                }
                PostLoginUI post = new PostLoginUI(auth, socket);
                post.menuLoop(facade, auth);
                login.setAuthNull();
            }
        }
    }

package ui;

import model.*;

public class ServerFacade {

    private final String url;

    public ServerFacade(String url) {
        this.url = url;
    }

    //register
    public Auth register(User request) throws Exception {
        return ServerFacadeUtils.makeRequest("POST", "/user", request, Auth.class, url, null);
    }

    //login
    public Auth login(User request) throws Exception {
        return ServerFacadeUtils.makeRequest("POST", "/session", request, Auth.class, url, null);
    }

    //logout
    public void logout(String auth) throws Exception{
        ServerFacadeUtils.makeRequest("DELETE", "/session", null, null, url, auth);
    }

    //listgames
    public ListOfGamesRecord listGames() throws Exception {
        return ServerFacadeUtils.makeRequest("GET","/game", null, ListOfGamesRecord.class , url, null);
    }

    //create game
    public CreateGameRecord createGame(String auth, CreateGameRecord request) throws Exception {
        return ServerFacadeUtils.makeRequest("POST", "/game", request, CreateGameRecord.class, url, auth);
    }
    //join game
    public void joinGame(String auth, JoinGameRecord request) throws Exception {
        ServerFacadeUtils.makeRequest("PUT", "/game", request, null, url, auth);
    }

    //clear data
    public void clearData() throws Exception {
        ServerFacadeUtils.makeRequest("DELETE", "/db", null, null,url,null);
    }
}

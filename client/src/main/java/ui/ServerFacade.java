package ui;

import model.ListOfGamesRecord;

public class ServerFacade {

    private final String url;

    public ServerFacade(String url) {
        this.url = url;
    }

    //register
    //login
    //logout
    //listgames
    public ListOfGamesRecord listGames() throws Exception {
        ServerFacadeUtils.makeRequest("GET","/game", null, ListOfGamesRecord.class , url, null);
    }

    //create game
    //join game

    //clear data
    public void clearData() throws Exception {
        ServerFacadeUtils.makeRequest("DELETE", "/db", null, null,url,null);
    }
}

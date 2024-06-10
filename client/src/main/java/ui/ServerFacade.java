package ui;

import ui.ServerFacadeUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

public class ServerFacade {

    private final String url;

    public ServerFacade(String url) {
        this.url = url;
    }


}

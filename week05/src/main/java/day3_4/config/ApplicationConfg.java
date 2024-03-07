package day3_4.config;

import io.javalin.Javalin;

public class ApplicationConfg {
    private static Javalin app;
    private static ApplicationConfg insanse;

    public static ApplicationConfg startServer(int port){
        app.start(port);
        return insanse;
    }


    public static ApplicationConfg initateServer(){
        app.create(config -> {
            config.http.defaultContentType = "application/json";
        });

        return insanse;
    }
}

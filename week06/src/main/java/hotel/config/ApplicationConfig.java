package hotel.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import hotel.controlers.SecurityController;
import hotel.dtos.UserDTO;
import hotel.exceptions.ApiException;
import io.javalin.Javalin;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.http.staticfiles.Location;
import io.javalin.plugin.bundled.RouteOverviewPlugin;
import jakarta.persistence.EntityManagerFactory;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static io.javalin.apibuilder.ApiBuilder.path;
//import io.javalin.plugin.bundled.

public class ApplicationConfig {
    private ObjectMapper jsonMapper = new ObjectMapper();
    private static ApplicationConfig appConfig;
    private static Javalin app;
    private ApplicationConfig() {
    }

    public static ApplicationConfig getInstance() {
        if (appConfig == null) {
            appConfig = new ApplicationConfig();
        }
        return appConfig;
    }

    public ApplicationConfig initiateServer() {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        String separator = System.getProperty("file.separator");
        app = Javalin.create(config -> {

            config.plugins.enableDevLogging(); // enables extensive development logging in terminal
            config.http.defaultContentType = "application/json"; // default content type for requests
            config.routing.contextPath = "/"; // base path for all routes
        });
        return appConfig;
    }

    public ApplicationConfig setRoutes(EndpointGroup routes) {
        app.routes(() -> {
            path("", routes); // e.g. /person
        });
        return appConfig;
    }


    public ApplicationConfig setCORS() {
        app.before(ctx -> {
            setCorsHeaders(ctx);
        });
        app.options("/*", ctx -> { // Burde nok ikke være nødvendig?
            setCorsHeaders(ctx);
        });
        return appConfig;
    }

    private static void setCorsHeaders(Context ctx) {
        ctx.header("Access-Control-Allow-Origin", "*");
        ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE, OPTIONS");
        ctx.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
        ctx.header("Access-Control-Allow-Credentials", "true");
    }

    

    public ApplicationConfig startServer(int port) {
        app.start(port);

        return appConfig;
    }

    public ApplicationConfig stopServer(){
        app.stop();
        return appConfig;
    }

    public ApplicationConfig setErrorHandling() {
        // To use this one, just set ctx.status(404) in the controller and add a ctx.attribute("msg", "Your message") to the ctx
        // Look at the PersonController: delete() method for an example
        // Might be better to just use the setApiExceptionHandling method below
        app.error(404, ctx -> {
            String message = ctx.attribute("msg");
            message = "{\"msssssg\": \"" + message + "\"}";
            ctx.json(message);
        });
        return appConfig;
    }

    public ApplicationConfig setGeneralExceptionHandling(){
        app.exception(Exception.class, (e,ctx)->{
            e.printStackTrace();
            ctx.result(e.getMessage());
        });
        return appConfig;
    }

    public ApplicationConfig beforeFilter(){
        app.before(ctx->{
            String pathInfo = ctx.req().getPathInfo();
            ctx.req().getHeaderNames().asIterator().forEachRemaining(el-> System.out.println(el));
        });
        return appConfig;
    }

    public ApplicationConfig checkSecurityRoles() {
        app.updateConfig(config -> {
            config.accessManager((handler, ctx, rolesSet) -> {
                Set<String> allowedRoles = rolesSet.stream().map(r -> r.toString().toUpperCase()).collect(Collectors.toSet());
                if(allowedRoles.contains("ANYONE") || ctx.method().toString().equals("OPTIONS")) {
                    handler.handle(ctx);
                    return;
                }
                UserDTO user = ctx.attribute("user");
                if(user == null){
                    ctx.status(HttpStatus.FORBIDDEN)
                                .json(jsonMapper.createObjectNode()
                                        .put("msg","Not authorized. No username were added from the token"));
                    return;
                }

                if (SecurityController.getInstance().authorize(user, allowedRoles))
                    handler.handle(ctx);
                else
                    throw new ApiException(HttpStatus.FORBIDDEN.getCode(), "Unauthorized with roles: " + user.getRoles());
            });
        });
        return appConfig;
    }

}
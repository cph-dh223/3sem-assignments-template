package hotel.config;

import java.util.Set;
import java.util.stream.Collectors;

import hotel.controlers.ISecurityController;
import hotel.controlers.SecurityController;
import hotel.daos.UserDAO;
import hotel.dtos.UserDTO;
import hotel.exceptions.ApiException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.javalin.Javalin;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;

/**
 * Purpose:
 *
 * @author: Thomas Hartmann
 */
public class ApplicationConfig {
    ObjectMapper om = new ObjectMapper();

    private Javalin app;

    private ISecurityController securityController;
    private static ApplicationConfig instance;
    private ApplicationConfig(EntityManagerFactory emf){
        securityController = new SecurityController(new UserDAO(emf));
    }
    public static ApplicationConfig getInstance(EntityManagerFactory emf){
        if(instance == null){
            instance = new ApplicationConfig(emf);
        }
        return instance;
    }
    
    public static ApplicationConfig getInstance(){
        if(instance == null){
            //todo
        }
        return instance;
    }
    public ApplicationConfig initiateServer(){
        app = Javalin.create(config->{
        config.http.defaultContentType = "application/json";
        config.routing.contextPath = "/api";
        });

        return instance;
    }
    public ApplicationConfig startServer(int port){
        app.start(port);
        return instance;
    }
    public ApplicationConfig setRoute(EndpointGroup route){
        app.routes(route);
        return instance;
    }

    public ApplicationConfig setExceptionHandling(){
        app.exception(Exception.class, (e,ctx)->{
            if(e instanceof ApiException) {
                ApiException apiException = (ApiException)e;

                ctx.status(apiException.getStatusCode()).json(om.createObjectNode().put("errrorMessage",apiException.getMessage()));
                return;
            }
            ObjectNode node = om.createObjectNode().put("errrorMessage",e.getMessage());
            ctx.status(500).json(node);
        });
        return instance;
    }
    public void stopServer(){
        app.stop();
    }
    public ApplicationConfig checkSecurityRoles() {
        // Check roles on the user (ctx.attribute("username") and compare with permittedRoles using securityController.authorize()
        app.updateConfig(config -> {

            config.accessManager((handler, ctx, permittedRoles) -> {
                // permitted roles are defined in the last arg to routes: get("/", ctx -> ctx.result("Hello World"), Role.ANYONE);

                Set<String> allowedRoles = permittedRoles.stream().map(role -> role.toString().toUpperCase()).collect(Collectors.toSet());
                if(allowedRoles.contains("ANYONE") || ctx.method().toString().equals("OPTIONS")) {
                    // Allow requests from anyone and OPTIONS requests (preflight in CORS)
                    handler.handle(ctx);
                    return;
                }

                UserDTO user = ctx.attribute("user");
                System.out.println("USER IN CHECK_SEC_ROLES: "+user);
                if(user == null)
                    ctx.status(HttpStatus.FORBIDDEN)
                            .json(om.createObjectNode()
                                    .put("msg","Not authorized. No username were added from the token"));

                if (securityController.authorize(user, allowedRoles))
                    handler.handle(ctx);
                else
                {
                    String userRoles =user.getRoles().stream().filter(r -> r.length() > 0).reduce("", (acc, r) -> acc + " " + r.toUpperCase() + ",").strip().replaceAll(",$", "");
                    throw new ApiException(HttpStatus.FORBIDDEN.getCode(), "Unauthorized with roles: [" + userRoles + "]");
                }
            });
        });                

        return instance;
    }
}

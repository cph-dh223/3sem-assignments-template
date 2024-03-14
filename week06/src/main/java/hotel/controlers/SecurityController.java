package hotel.controlers;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import hotel.config.HibernateConfig;
import hotel.daos.UserDAO;
import hotel.dtos.UserDTO;
import hotel.exceptions.ApiException;
import hotel.exceptions.ValidationException;
import hotel.ressources.Role;
import hotel.ressources.User;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;

public class SecurityController implements IController{
    //TODO: Bedere SECRET_KEY and bedere way to stor it
    private final String SECRET_KEY = "0B352CAE153F3C55156CF7C42A267F44A510B2C5E2750B3B09FD528FF234F403BFA9AE071A9D2C14C281DE1F37C94AE82204082076746DC60ACEB74E38C37DE689E31DC0FFA657474ED6281ABFBA6BB991C063217B14FBA209E9ABBC4304E3A8040DF0A45D8E51DE0EE7DCD0877CDB1CC81073DDB607C229E3556788988BFA74";
    
    private static SecurityController instanse;

    private Map<String, User> users;
    private static UserDAO userDAO;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private SecurityController() {
    }

    private static SecurityController instance;

    public static SecurityController getInstance() { // Singleton because we don't want multiple instances of the same class
        if (instance == null) {
            instance = new SecurityController();
        }
        userDAO = new UserDAO(HibernateConfig.getEntityManagerFactory());
        return instance;
    }


    @Override
    public Handler getAll() {
        return ctx -> {
            List<String> users = userDAO.getAllUsers().stream().map(u -> createUserToken(new UserDTO(u), SECRET_KEY)).collect(Collectors.toList());
            String json = objectMapper.writeValueAsString(users);
            ctx.status(HttpStatus.OK).json(json);
        };
    }

    @Override
    public Handler getById() {
        return ctx -> {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'getById'");
        };

    }

    @Override
    public Handler create() {
        return ctx -> {
            UserDTO newUserDTO = ctx.bodyAsClass(UserDTO.class);
            userDAO.createUser(newUserDTO.getUsername(), newUserDTO.getPassword());
            String createToken = createUserToken(newUserDTO, SECRET_KEY); 
            ctx.status(HttpStatus.CREATED).json(createToken);
        };

    }

    @Override
    public Handler delete() {
        return ctx -> {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'delete'");
        };

    }

    @Override
    public Handler update() {
        return ctx -> {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'update'");
        };

    }

    
    public Handler login() {
        return ctx -> {
            UserDTO loginUserDTO = ctx.bodyAsClass(UserDTO.class);
            try {
                User user = userDAO.getVerifiedUser(loginUserDTO.getUsername(), loginUserDTO.getPassword());
                String json = createUserToken(new UserDTO(user), SECRET_KEY);
                ctx.status(HttpStatus.ACCEPTED).json(json);
            } catch (ValidationException e) {
                e.printStackTrace();
                ctx.status(404).result("user not found");
            }
            
        };
    }

    


    public Handler getUserRoles() {
        return ctx -> {
            UserDTO currentUser = (UserDTO) ctx.attribute("user");
            String json = objectMapper.writeValueAsString(currentUser.getRoles());
            ctx.status(HttpStatus.OK).json(json);
        };
    }

    
    public Handler authenticateUser() {
        return ctx -> {
            ObjectNode node = objectMapper.createObjectNode();
            String header = ctx.header("Authorization");
            if(header == null){
                ctx.status(HttpStatus.FORBIDDEN).json(node.put("msg", "Authorization header missing"));
                return;
            }
            String token = header.split(" ")[1];
            if (token == null){
                ctx.status(HttpStatus.FORBIDDEN).json(node.put("msg", "Authorization header not right"));
                return;
            }
            UserDTO userDTO = verifyToken(token);
            if(userDTO == null){
                ctx.status(HttpStatus.FORBIDDEN).json(node.put("msg", "Invalid User or Token"));
            }
            if (userDTO.getRoles().stream().filter(r -> r.equals("user")).findFirst().get() == null) {
                ctx.status(HttpStatus.FORBIDDEN).json(node.put("msg", "Acces not allowed"));
                
            }

            System.out.println("USER IN AUTHENTICATE: " + userDTO);
            ctx.attribute("user", userDTO);
        };
    }

    public Handler authenticateAdmin() {
        return ctx -> {
            ObjectNode node = objectMapper.createObjectNode();
            String header = ctx.header("Authorization");
            if(header == null){
                ctx.status(HttpStatus.FORBIDDEN).json(node.put("msg", "Authorization header missing"));
                return;
            }
            
            String token = header.split(" ")[1];
            if (token == null){
                ctx.status(HttpStatus.FORBIDDEN).json(node.put("msg", "Authorization header not right"));
                return;
            }
            
            UserDTO userDTO = verifyToken(token);
            if(userDTO == null){
                ctx.status(HttpStatus.FORBIDDEN).json(node.put("msg", "Invalid User or Token"));
                return;
            }

            if (!userDTO.getRoles().stream().filter(r -> r.equals("admin")).findFirst().isPresent()) {
                ctx.status(HttpStatus.FORBIDDEN).json(node.put("msg", "Acces not allowed"));
                return;
            }

            System.out.println("USER IN AUTHENTICATE: " + userDTO);
            ctx.attribute("user", userDTO);
        };
    }

    private String createUserToken(UserDTO user, String SECRET_KEY){
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
            .subject(user.getUsername())
            .claim("userName", user.getUsername())
            .claim("roles", user.getRoles().stream().reduce("", (r1, r2) -> r1 + "," + r2))
            .build();
                
        Payload payload = new Payload(claimsSet.toJSONObject());

        JWSSigner signer;
        try {
            signer = new MACSigner(SECRET_KEY);
            JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);
            JWSObject jwsObject = new JWSObject(jwsHeader, payload);
            jwsObject.sign(signer);
            return jwsObject.serialize();
        } catch (JOSEException e) {
            e.printStackTrace();
            throw new ApiException(500, "Could not create token");
        }
    }

    private UserDTO verifyToken(String token) throws ParseException, JOSEException {
        SignedJWT jwt = SignedJWT.parse(token);
        if(jwt.verify(new MACVerifier(SECRET_KEY))){
            JWTClaimsSet claims = jwt.getJWTClaimsSet();
            String username = claims.getClaim("userName").toString();
            Set<String> roles = Arrays.stream(claims.getClaim("roles").toString().split(",")).collect(Collectors.toSet());
            return new UserDTO(username, roles);
        }
        return null;
        
    }


    public boolean authorize(UserDTO user, Set<String> allowedRoles) {
        if(user == null) return false;
        return user.getRoles().stream().filter(r -> allowedRoles.contains(r.toUpperCase())).findFirst().isPresent();
    }
}

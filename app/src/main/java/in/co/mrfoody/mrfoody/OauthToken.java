package in.co.mrfoody.mrfoody;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import java.util.Scanner;

/**
 * Created by om on 15/1/16.
 */
public class OauthToken extends DefaultApi10a{
    private static final String BASE_URL = "http://dev.mrfoody.co.in/";

    @Override
    public String getRequestTokenEndpoint() {
        return BASE_URL + "oauth/initiate";
    }

    @Override
    public String getAccessTokenEndpoint() {
        return BASE_URL + "oauth/token";

    }

    @Override
    public String getAuthorizationUrl(Token requestToken) {
        return BASE_URL + "admin/oauth_authorize?oauth_token="
                + requestToken.getToken(); //this implementation is for admin roles only...

    }
    public static void oauthCall(){
        final String MAGENTO_API_KEY = "1f46c6a95d4949c979e929acccc254b4";
        final String MAGENTO_API_SECRET = "615cefc15e548d28fe29c29e94f5c6dc";
        final String MAGENTO_REST_API_URL = "http://dev.mrfoody.co.in/api/rest";
        // three-legged oauth
        OAuthService service = new ServiceBuilder()
                .provider(OauthToken.class)
                .apiKey(MAGENTO_API_KEY)
                .apiSecret(MAGENTO_API_SECRET)
                .debug()
                .build();

        // start
        Scanner in = new Scanner(System.in);
        System.out.println("Magento'srkflow");
        System.out.println();
        // Obtain the Request Token
        System.out.println("FetchingRequest Token...");
        Token requestToken = service.getRequestToken();
        System.out.println("GotRequest Token!");
        System.out.println();

        // Obtain the Authorization URL
        System.out.println("FetchingAuthorization URL...");
        String authorizationUrl = service.getAuthorizationUrl(requestToken);
        System.out.println("GotAuthorization URL!");
        System.out.println("Nownd authorize Main here:");
        System.out.println(authorizationUrl);
        System.out.println("Ande the authorization code here");
        System.out.print(">>");
        Verifier verifier = new Verifier(in.nextLine());
        System.out.println();

        // Trade the Request Token and Verfier for the Access Token
        System.out.println("TradingRequest Token for an Access Token...");
        Token accessToken = service.getAccessToken(requestToken, verifier);
        System.out.println("GotAccess Token!");
        System.out.println("(if curious it looks like this: "
                + accessToken + " )");
        System.out.println();

        // Now let's go and ask for a protected resource!
        OAuthRequest request = new OAuthRequest(Verb.GET, MAGENTO_REST_API_URL+ "/products?limit=2");
        service.signRequest(accessToken, request);
        Response response = request.send();
        System.out.println();
        System.out.println(response.getCode());
        System.out.println(response.getBody());
        System.out.println();
    }
}

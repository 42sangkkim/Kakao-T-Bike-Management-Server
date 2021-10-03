package Kakao_T_Bike_Management.Server;

public class Authorization {
    private static final String X_AUTH_TOKEN = "example-x-auth-token";
    private static String AUTH_KEY = null;

    public static String getAuth(String x_auth_token) {
        if (!X_AUTH_TOKEN.equals(x_auth_token))
            return null;

        AUTH_KEY = "example-auth-key";
        return AUTH_KEY;
    }

    public static void resetAuth() {
        AUTH_KEY = null;
    }

    public static boolean isAuth(String auth_key) {
        if(AUTH_KEY == null || !AUTH_KEY.equals(auth_key))
            return false;
        return true;
    }
}

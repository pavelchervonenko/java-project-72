package hexlet.code.util;

public class NamedRoutes {

    public static String rootPath() {
        return "/";
    }

    public static String urlsPath() {
        return "/urls";
    }

    public static String urlPath() {
        return "/urls/{id}";
    }

    public static String urlPath(Long id) {
        return "/urls/" + id;
    }
}

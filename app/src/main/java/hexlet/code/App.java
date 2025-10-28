package hexlet.code;

import io.javalin.Javalin;

//import lombok.extern.slf4j.Slf4j;

//@Slf4j
public final class App {

    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "7070");
        return Integer.valueOf(port);
    }

    public static void main(String[] args) {
        var app = getApp();
        app.start(getPort());
    }

    public static Javalin getApp() {
        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
        });

       app.get("/", ctx -> {
           ctx.result("Hello World");
       });

        return app;
    }


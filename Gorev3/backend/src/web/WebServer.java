import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import controllers.UserController;

public class WebServer {
    // Web sunucusunu başlatır
    private int port;
    // Sunucu nesnesi
    private HttpServer server;

    // Port numarası alır
    public WebServer(int port) {
        this.port = port;
    }
    // Sunucuyu başlatır
    public void start() {
        /* Aşağıdaki kod satırının açıklaması:
        1. Sunucu nesnesi oluşturulur
        2. UserController sınıfı oluşturulur
        3. Sunucu nesnesine istekler eklenir
        4. Sunucu başlatılır */
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
            UserController userController = new UserController();
            server.createContext("/api/v1/users/getAll", userController::handleGetAllUsers);
            server.createContext("/api/v1/users/create", userController::handleCreateUser);
            server.createContext("/api/v1/users/getById", userController::handleGetUserById);
            server.createContext("/api/v1/users/update", userController::handleUpdateUser);
            server.createContext("/api/v1/users/delete", userController::handleDeleteUser);
            server.createContext("/api/v1/users/restore", userController::handleRestoreUser);
            server.createContext("/", exchange -> {
                String response = "Hatalı istek";
                exchange.sendResponseHeaders(400, response.getBytes().length);
                exchange.getResponseBody().write(response.getBytes());
                exchange.close();
            });
            server.setExecutor(null);
            System.out.println("Server is running on port " + port);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package core.utilities;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import exceptions.*;

public class RequestHelper {

    // Gelen isteği işler ve response döndürür
    public static void handleRequest(HttpExchange exchange, String expectedMethod, RequestHandler handler) throws IOException {
        /* Aşağıdaki kod satırının açıklaması:
        1. Eğer gelen istek OPTIONS ise handleOptionsRequest metodu çalıştırılır
        2. Eğer gelen istek OPTIONS değilse ve beklenen metod ile uyuşmuyorsa IllegalArgumentException hatası fırlatılır 
        3. İsteği işler ve response döndürür
        4. Eğer kullanıcı bulunamazsa UserNotFoundException hatası fırlatılır
        5. Eğer kullanıcı zaten varsa UserAlreadyExistsException hatası fırlatılır
        6. Eğer veritabanı hatası oluşursa DatabaseException hatası fırlatılır
        7. Eğer başka bir hata oluşursa Exception hatası fırlatılır */
        try {
            if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                handleOptionsRequest(exchange);
                return;
            }

            if (!exchange.getRequestMethod().equalsIgnoreCase(expectedMethod))
                throw new IllegalArgumentException("Metod desteklenmiyor");

            String response = handler.handle();
            sendResponse(exchange, 200, response);
        } catch (UserNotFoundException e) {
            sendResponse(exchange, 404, "{\"error\":\"" + e.getMessage() + "\"}");
        } catch (UserAlreadyExistsException e) {
            sendResponse(exchange, 409, "{\"error\":\"" + e.getMessage() + "\"}");
        } catch (DatabaseException e) {
            sendResponse(exchange, 500, "{\"error\":\"" + e.getMessage() + "\"}");
        } catch (IllegalArgumentException e) {
            sendResponse(exchange, 405, "{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            sendResponse(exchange, 500, "{\"error\":\"Sunucu Hatası\"}");
        }
    }

    // OPTIONS isteğini işler ve response döndürür
    private static void handleOptionsRequest(HttpExchange exchange) throws IOException {
        /* Aşağıdaki kod satırının açıklaması:
        1. OPTIONS isteğine response döndürür
        2. Access-Control-Allow-Origin, Access-Control-Allow-Methods ve Access-Control-Allow-Headers başlıklarını ekler
        3. Response kodunu 204 olarak ayarlar çünkü OPTIONS isteği için içerik döndürülmez */
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");
        exchange.sendResponseHeaders(204, -1);
    }

    // Response döndürür ve response kodunu ayarlar
    private static void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        /* Aşağıdaki kod satırının açıklaması:
        1. Response döndürür
        2. Content-Type başlığını ekler
        3. Access-Control-Allow-Origin başlığını ekler
        4. Response kodunu ve response boyutunu ayarlar
        5. Response gövdesine response döndürür */
        byte[] responseBytes = response.getBytes();
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        try (var os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }

    // RequestHandler fonksiyonel arayüzü tanımlar ve handle metodu tanımlar 
    @FunctionalInterface
    public interface RequestHandler {
        // İsteği işler ve response döndürür
        String handle() throws Exception;
    }
}
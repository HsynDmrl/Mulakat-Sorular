package core.utilities;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

public class JsonHelper {

    // Listeyi JSON formatına çevirir
    public static String toJson(Object object) {
        // Eğer gelen nesne bir liste ise listeyi JSON formatına çevirir ve döndürür 
        if (object instanceof List<?>) {
            /* Aşağıdaki kod satırının açıklaması:
            1. Gelen nesneyi List tipine dönüştürür
            2. Stream API ile listeyi dönüştürür
            3. Her bir elemanı JSON formatına dönüştürür
            4. Her bir elemanı virgül ile ayırır
            5. Listeyi JSON formatına dönüştürür
            Örnek çıktı: [{"id":"1","name":"John"},{"id":"2","name":"Doe"}] */
            List<?> list = (List<?>) object;
            return "[" + list.stream()
                    .map(JsonHelper::simpleJsonFromObject)
                    .collect(Collectors.joining(",")) + "]";
        }
        return simpleJsonFromObject(object);
    }

    // JSON formatını nesneye çevirir
    public static <T> T fromJson(String json, Class<T> classOfT) {

        /* Aşağıdaki kod satırının açıklaması:
        1. Gelen JSON formatındaki veriyi parseJson metodu ile Map tipine dönüştürür
        2. T tipinde bir nesne oluşturur
        3. T tipindeki nesnenin tüm alanlarını dolaşır
        4. Alanın adı Map'te varsa alanın değerini Map'ten alır ve nesneye set eder
        5. Nesneyi döndürür
        Örnek çıktı: CreateUserDTO{id='1', name='John'} */
        try {
            T instance = classOfT.getConstructor().newInstance();
            Map<String, String> parsedData = parseJson(json);

            for (var field : classOfT.getDeclaredFields()) {
                field.setAccessible(true);
                if (parsedData.containsKey(field.getName())) {
                    field.set(instance, parsedData.get(field.getName()));
                }
            }
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("JSON dönüşüm hatası: " + e.getMessage());
        }
    }

    // JSON formatındaki veriyi Map tipine dönüştürür
    private static Map<String, String> parseJson(String json) {
        /* Aşağıdaki kod satırının açıklaması:
        1. Gelen JSON formatındaki veriyi işler
        2. JSON formatındaki veriyi Map tipine dönüştürür
        Örnek çıktı: {id=1, name=John} */
        Map<String, String> map = new HashMap<>();
        json = json.replace("{", "").replace("}", "").replace("\"", "");
        String[] pairs = json.split(",");
        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            if (keyValue.length == 2) {
                map.put(keyValue[0].trim(), keyValue[1].trim());
            }
        }
        return map;
    }

    // Nesneyi JSON formatına çevirir
    private static String simpleJsonFromObject(Object dto) {
        /* Aşağıdaki kod satırının açıklaması:
        1. Gelen nesneyi JSON formatına dönüştürür
        Örnek çıktı: {"id":"1","name":"John"} */
        StringBuilder sb = new StringBuilder("{");
        var fields = dto.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            try {
                fields[i].setAccessible(true);
                sb.append("\"").append(fields[i].getName()).append("\":\"").append(fields[i].get(dto)).append("\"");
                if (i < fields.length - 1) sb.append(",");
            } catch (IllegalAccessException ignored) {}
        }
        sb.append("}");
        return sb.toString();
    }
}

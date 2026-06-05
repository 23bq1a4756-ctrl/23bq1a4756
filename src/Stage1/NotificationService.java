package Stage1;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class NotificationService {
    private static final String URL =
            "https://4.224.186.213/evalution-service/notifications";

    private static final Map<String, Integer> PRIORITY = Map.of(
            "Placement", 2,
            "Result", 1,
            "Event", 0
    );

    public static String fetch() throws Exception {

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    public static List<Notification> parse(String json) {

        List<Notification> list = new ArrayList<>();

        json = json.trim();
        json = json.substring(1, json.length() - 1);

        String[] objects = json.split("\\},\\{");

        for (String obj : objects) {

            obj = obj.replace("{", "")
                    .replace("}", "")
                    .replace("\"", "");

            String[] fields = obj.split(",");

            int id = 0;
            String type = "";
            String message = "";
            String timestamp = "";

            for (String f : fields) {

                String[] kv = f.split(":");
                if (kv.length < 2) continue;

                String key = kv[0].trim();
                String value = kv[1].trim();

                switch (key) {
                    case "id":
                        id = Integer.parseInt(value);
                        break;
                    case "type":
                        type = value;
                        break;
                    case "message":
                        message = value;
                        break;
                    case "timestamp":
                        timestamp = value;
                        break;
                }
            }

            list.add(new Notification(id, type, message, timestamp));
        }

        return list;
    }

    public static List<Notification> topN(List<Notification> list, int n) {

        list.sort((a, b) -> Integer.compare(
                PRIORITY.getOrDefault(b.type, 0),
                PRIORITY.getOrDefault(a.type, 0)
        ));

        return list.subList(0, Math.min(n, list.size()));
    }

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter N (top N notifications): ");
        int n = sc.nextInt();

        String json = fetch();

        List<Notification> list = parse(json);

        List<Notification> result = topN(list, n);

        System.out.println("\nTop " + n + " Notifications:\n");

        for (Notification s : result) {
            System.out.println(s);
        }

        sc.close();
    }
}
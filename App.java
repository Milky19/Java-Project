import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class App {

    public static void main(String[] args) throws Exception {

        int port = 8081;

        HttpServer server =
                HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/", App::handleRequest);

        server.setExecutor(null);

        System.out.println("Calculator application started");
        System.out.println("Application running on port " + port);

        server.start();
    }

    private static void handleRequest(HttpExchange exchange)
            throws IOException {

        String query = exchange.getRequestURI().getQuery();

        String html;

        if (query != null && query.contains("first")) {

            String[] values = query.split("&");

            int first = Integer.parseInt(
                    URLDecoder.decode(values[0].split("=")[1],
                            StandardCharsets.UTF_8)
            );

            int second = Integer.parseInt(
                    URLDecoder.decode(values[1].split("=")[1],
                            StandardCharsets.UTF_8)
            );

            if (second == 0) {

                html = createPage(
                        "Division by zero is not allowed"
                );

            } else {

                int add = first + second;
                int sub = first - second;
                int mul = first * second;
                int div = first / second;

                html = createPage(
                        "<h2>Calculation Result</h2>" +
                        "<p>Addition: " + add + "</p>" +
                        "<p>Subtraction: " + sub + "</p>" +
                        "<p>Multiplication: " + mul + "</p>" +
                        "<p>Division: " + div + "</p>"
                );
            }

        } else {

            html = createPage("");
        }

        exchange.getResponseHeaders()
                .set("Content-Type", "text/html");

        exchange.sendResponseHeaders(
                200,
                html.getBytes(StandardCharsets.UTF_8).length
        );

        OutputStream output = exchange.getResponseBody();

        output.write(
                html.getBytes(StandardCharsets.UTF_8)
        );

        output.close();
    }


    private static String createPage(String result) {

        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Java Calculator</title>

                    <style>
                        body {
                            font-family: Arial;
                            text-align: center;
                            margin-top: 50px;
                        }

                        input {
                            padding: 10px;
                            margin: 8px;
                        }

                        button {
                            padding: 10px 25px;
                            cursor: pointer;
                        }

                        .result {
                            margin-top: 25px;
                            font-size: 20px;
                        }
                    </style>
                </head>

                <body>

                    <h1>Java Calculator</h1>

                    <form method="GET">

                        <input
                            type="number"
                            name="first"
                            placeholder="First Number"
                            required
                        >

                        <br>

                        <input
                            type="number"
                            name="second"
                            placeholder="Second Number"
                            required
                        >

                        <br>

                        <button type="submit">
                            Calculate
                        </button>

                    </form>

                    <div class="result">
                        %s
                    </div>

                </body>
                </html>
                """.formatted(result);
    }
}

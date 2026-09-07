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

        System.out.println("=================================");
        System.out.println("Java Calculator Started");
        System.out.println("Application running on port " + port);
        System.out.println("=================================");

        server.start();
    }

    private static void handleRequest(HttpExchange exchange)
            throws IOException {

        String query = exchange.getRequestURI().getQuery();

        String html;

        if (query != null && query.contains("first")) {

            String[] values = query.split("&");

            int first = Integer.parseInt(
                    URLDecoder.decode(
                            values[0].split("=")[1],
                            StandardCharsets.UTF_8
                    )
            );

            int second = Integer.parseInt(
                    URLDecoder.decode(
                            values[1].split("=")[1],
                            StandardCharsets.UTF_8
                    )
            );

            if (second == 0) {

                html = createPage(
                        "<div class=\"error\">" +
                        "⚠️ Division by zero is not allowed" +
                        "</div>"
                );

            } else {

                int add = first + second;
                int sub = first - second;
                int mul = first * second;
                int div = first / second;

                html = createPage(
                        "<h2>📊 Calculation Result</h2>" +
                        "<p>➕ Addition: <strong>" + add +
                        "</strong></p>" +
                        "<p>➖ Subtraction: <strong>" + sub +
                        "</strong></p>" +
                        "<p>✖️ Multiplication: <strong>" + mul +
                        "</strong></p>" +
                        "<p>➗ Division: <strong>" + div +
                        "</strong></p>"
                );
            }

        } else {

            html = createPage("");
        }

        byte[] response =
                html.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders()
                .set("Content-Type", "text/html; charset=UTF-8");

        exchange.sendResponseHeaders(
                200,
                response.length
        );

        OutputStream output =
                exchange.getResponseBody();

        output.write(response);

        output.close();
    }

    private static String createPage(String result) {

        return """
                <!DOCTYPE html>

                <html lang="en">

                <head>

                    <meta charset="UTF-8">

                    <meta name="viewport"
                          content="width=device-width, initial-scale=1.0">

                    <title>Java Calculator</title>

                    <style>

                        * {
                            margin: 0;
                            padding: 0;
                            box-sizing: border-box;
                        }

                        body {
                            font-family: Arial, Helvetica, sans-serif;

                            min-height: 100vh;

                            display: flex;
                            justify-content: center;
                            align-items: center;

                            background:
                            linear-gradient(
                                135deg,
                                #667eea,
                                #764ba2
                            );

                            padding: 20px;
                        }

                        .calculator {
                            width: 100%;
                            max-width: 450px;

                            background: white;

                            padding: 35px;

                            border-radius: 25px;

                            box-shadow:
                            0 20px 50px
                            rgba(0,0,0,0.25);

                            text-align: center;
                        }

                        .icon {
                            width: 75px;
                            height: 75px;

                            margin: 0 auto 15px;

                            border-radius: 50%;

                            display: flex;
                            align-items: center;
                            justify-content: center;

                            background:
                            linear-gradient(
                                135deg,
                                #667eea,
                                #764ba2
                            );

                            color: white;

                            font-size: 38px;
                        }

                        h1 {
                            color: #222;
                            font-size: 30px;
                            margin-bottom: 8px;
                        }

                        .subtitle {
                            color: #777;
                            font-size: 14px;
                            margin-bottom: 30px;
                        }

                        .input-group {
                            text-align: left;
                            margin-bottom: 18px;
                        }

                        label {
                            display: block;
                            margin-bottom: 7px;
                            font-size: 14px;
                            font-weight: bold;
                            color: #444;
                        }

                        input {
                            width: 100%;

                            padding: 15px;

                            border: 2px solid #e1e1e1;

                            border-radius: 12px;

                            font-size: 17px;

                            outline: none;
                        }

                        input:focus {
                            border-color: #667eea;
                        }

                        button {
                            width: 100%;

                            margin-top: 10px;

                            padding: 15px;

                            border: none;

                            border-radius: 12px;

                            background:
                            linear-gradient(
                                135deg,
                                #667eea,
                                #764ba2
                            );

                            color: white;

                            font-size: 17px;

                            font-weight: bold;

                            cursor: pointer;
                        }

                        button:hover {
                            transform: translateY(-2px);
                        }

                        .result {
                            margin-top: 28px;

                            padding: 20px;

                            border-radius: 15px;

                            background: #f7f8ff;

                            border: 1px solid #e2e5ff;

                            color: #333;

                            font-size: 18px;

                            text-align: left;
                        }

                        .result h2 {
                            text-align: center;

                            color: #667eea;

                            margin-bottom: 15px;

                            font-size: 21px;
                        }

                        .result p {
                            padding: 10px 12px;

                            margin: 7px 0;

                            background: white;

                            border-radius: 8px;
                        }

                        .error {
                            color: #d93025;

                            background: #fff1f0;

                            border: 1px solid #ffd6d2;

                            padding: 15px;

                            border-radius: 10px;

                            text-align: center;

                            font-weight: bold;
                        }

                        .footer {
                            margin-top: 25px;

                            font-size: 12px;

                            color: #999;
                        }

                    </style>

                </head>

                <body>

                    <div class="calculator">

                        <div class="icon">
                            🧮
                        </div>

                        <h1>
                            Java Calculator
                        </h1>

                        <p class="subtitle">
                            Simple Web Calculator powered by Java
                        </p>

                        <form method="GET">

                            <div class="input-group">

                                <label>
                                    First Number
                                </label>

                                <input
                                    type="number"
                                    name="first"
                                    placeholder="Enter first number"
                                    required
                                >

                            </div>

                            <div class="input-group">

                                <label>
                                    Second Number
                                </label>

                                <input
                                    type="number"
                                    name="second"
                                    placeholder="Enter second number"
                                    required
                                >

                            </div>

                            <button type="submit">
                                Calculate ✨
                            </button>

                        </form>

                        <div class="result">

                            %s

                        </div>

                        <div class="footer">

                            Java HTTP Server • Port 8081

                        </div>

                    </div>

                </body>

                </html>
                """.formatted(result);
    }
}

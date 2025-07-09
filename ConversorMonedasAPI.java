
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;

public class ConversorMonedasAPI {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese el monto a convertir: ");
        double monto = scanner.nextDouble();
        scanner.nextLine();  // limpiar buffer

        System.out.print("Moneda base (ej: USD): ");
        String monedaBase = scanner.nextLine().toUpperCase();

        System.out.print("Moneda destino (ej: EUR): ");
        String monedaDestino = scanner.nextLine().toUpperCase();

        try {
            String apiKey = "d91c54249e214f65f1f5a8b2";
            String urlStr = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + monedaBase;
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder respuesta = new StringBuilder();
            String linea;

            while ((linea = in.readLine()) != null) {
                respuesta.append(linea);
            }
            in.close();

            JSONObject json = new JSONObject(respuesta.toString());

            if (!json.getString("result").equals("success")) {
                System.out.println("Error al obtener datos de la API.");
                return;
            }

            JSONObject tasas = json.getJSONObject("conversion_rates");

            if (!tasas.has(monedaDestino)) {
                System.out.println("Moneda destino no válida o no encontrada.");
                return;
            }

            double tasa = tasas.getDouble(monedaDestino);
            double resultado = monto * tasa;

            System.out.printf("%.2f %s equivalen a %.2f %s\n", monto, monedaBase, resultado, monedaDestino);

        } catch (Exception e) {
            System.out.println("Ocurrió un error: " + e.getMessage());
        }

        scanner.close();
    }
}

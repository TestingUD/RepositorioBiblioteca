// API Módulo Préstamos (sin frameworks) – com.sun.net.httpserver
// Endpoints:
//  GET /                -> {"status":"ok","endpoints":["/prestamos/calcular","/prestamos/crear","/prestamos/listar"]}
//  GET /prestamos/calcular?monto=10000&meses=12&tasa=0.2   -> calcula cuota (método francés)
//  GET /prestamos/crear?monto=10000&meses=12&tasa=0.2      -> crea y devuelve préstamo (id, cuota, totales)
//  GET /prestamos/listar                                   -> lista préstamos creados en memoria (por ejecución)

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Api {

    // ----- Modelo y "repo" en memoria -----
    static class Prestamo {
        long id;
        double monto;
        int meses;
        double tasaAnual;   // ej: 0.2 para 20%
        double cuota;       // mensual
        double totalInteres;
        double totalPagado;
        Prestamo(long id, double monto, int meses, double tasaAnual, double cuota, double totalInteres, double totalPagado) {
            this.id = id; this.monto = monto; this.meses = meses; this.tasaAnual = tasaAnual;
            this.cuota = cuota; this.totalInteres = totalInteres; this.totalPagado = totalPagado;
        }
    }
    private static final Map<Long, Prestamo> DB = new ConcurrentHashMap<>();
    private static long NEXT_ID = 1;
    private static synchronized long nextId() { return NEXT_ID++; }

    // ----- Lógica de cálculo (método francés) -----
    // r = tasaAnual/12; cuota = P*r / (1 - (1+r)^-n); si r=0 => P/n
    public static double cuotaMensual(double monto, int meses, double tasaAnual) {
        if (meses <= 0) throw new IllegalArgumentException("meses debe ser > 0");
        if (monto <= 0) throw new IllegalArgumentException("monto debe ser > 0");
        if (tasaAnual < 0) throw new IllegalArgumentException("tasa debe ser >= 0");
        double r = tasaAnual / 12.0;
        if (r == 0) return monto / meses;
        return monto * r / (1.0 - Math.pow(1.0 + r, -meses));
    }

    public static void main(String[] args) throws Exception {
        int port = 8080; // cambia a 8081 si estuviera ocupado
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        // Root/health
        server.createContext("/", ex -> {
            sendJson(ex, 200, "{\"status\":\"ok\",\"endpoints\":[\"/prestamos/calcular\",\"/prestamos/crear\",\"/prestamos/listar\"]}");
        });

        // GET /prestamos/calcular?monto=&meses=&tasa=
        server.createContext("/prestamos/calcular", ex -> {
            Map<String,String> q = qparams(ex.getRequestURI().getRawQuery());
            try {
                if (!q.containsKey("monto") || !q.containsKey("meses") || !q.containsKey("tasa")) {
                    sendError(ex, 400, "faltan parámetros: monto, meses, tasa"); return;
                }
                double monto = Double.parseDouble(q.get("monto"));
                int meses = Integer.parseInt(q.get("meses"));
                double tasa = Double.parseDouble(q.get("tasa")); // ej: 0.2 = 20%
                double cuota = cuotaMensual(monto, meses, tasa);
                double total = cuota * meses;
                double interes = total - monto;
                String body = String.format(Locale.US,
                    "{\"monto\":%.2f,\"meses\":%d,\"tasa\":%.6f,\"cuota\":%.6f,\"totalInteres\":%.6f,\"totalPagado\":%.6f}",
                    monto, meses, tasa, cuota, interes, total);
                sendJson(ex, 200, body);
            } catch (NumberFormatException e) {
                sendError(ex, 400, "parámetros numéricos inválidos");
            } catch (IllegalArgumentException e) {
                sendError(ex, 400, e.getMessage());
            }
        });

        // GET /prestamos/crear?monto=&meses=&tasa=
        server.createContext("/prestamos/crear", ex -> {
            Map<String,String> q = qparams(ex.getRequestURI().getRawQuery());
            try {
                if (!q.containsKey("monto") || !q.containsKey("meses") || !q.containsKey("tasa")) {
                    sendError(ex, 400, "faltan parámetros: monto, meses, tasa"); return;
                }
                double monto = Double.parseDouble(q.get("monto"));
                int meses = Integer.parseInt(q.get("meses"));
                double tasa = Double.parseDouble(q.get("tasa"));
                double cuota = cuotaMensual(monto, meses, tasa);
                double total = cuota * meses;
                double interes = total - monto;
                long id = nextId();
                Prestamo p = new Prestamo(id, monto, meses, tasa, cuota, interes, total);
                DB.put(id, p);
                String body = String.format(Locale.US,
                    "{\"id\":%d,\"monto\":%.2f,\"meses\":%d,\"tasa\":%.6f,\"cuota\":%.6f,\"totalInteres\":%.6f,\"totalPagado\":%.6f}",
                    id, monto, meses, tasa, cuota, interes, total);
                sendJson(ex, 200, body);
            } catch (NumberFormatException e) {
                sendError(ex, 400, "parámetros numéricos inválidos");
            } catch (IllegalArgumentException e) {
                sendError(ex, 400, e.getMessage());
            }
        });

        // GET /prestamos/listar
        server.createContext("/prestamos/listar", ex -> {
            StringBuilder sb = new StringBuilder();
            sb.append("{\"loans\":[");
            boolean first = true;
            for (Prestamo p : DB.values()) {
                if (!first) sb.append(",");
                first = false;
                sb.append(String.format(Locale.US,
                  "{\"id\":%d,\"monto\":%.2f,\"meses\":%d,\"tasa\":%.6f,\"cuota\":%.6f}",
                  p.id, p.monto, p.meses, p.tasaAnual, p.cuota));
            }
            sb.append("]}");
            sendJson(ex, 200, sb.toString());
        });

        server.setExecutor(null);
        System.out.println("API Préstamos en http://localhost:"+port+"  (/prestamos/calcular, /prestamos/crear, /prestamos/listar)");
        server.start();
    }

    // -------- utilidades ----------
    private static Map<String,String> qparams(String raw) {
        Map<String,String> m = new HashMap<>();
        if (raw == null || raw.isEmpty()) return m;
        for (String p : raw.split("&")) {
            int i = p.indexOf('=');
            String k = i>=0 ? p.substring(0,i) : p;
            String v = i>=0 ? p.substring(i+1) : "";
            m.put(urlDecode(k), urlDecode(v));
        }
        return m;
    }
    private static String urlDecode(String s) {
        return URLDecoder.decode(s, StandardCharsets.UTF_8);
    }
    private static void sendError(HttpExchange ex, int status, String msg) throws IOException {
        sendJson(ex, status, "{\"error\":\""+msg.replace("\"","\\\"")+"\"}");
    }
    private static void sendJson(HttpExchange ex, int status, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        ex.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = ex.getResponseBody()) { os.write(bytes); }
    }
}

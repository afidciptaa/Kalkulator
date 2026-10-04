import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages calculation history persistence and in-memory list.
 */
public class HistoryManager {
    public static class Entry {
        public final String expression;
        public final String result;
        public final String time;

        public Entry(String expression, String result, String time) {
            this.expression = expression;
            this.result = result;
            this.time = time;
        }

        @Override
        public String toString() {
            return time + "\t" + expression + "\t" + result;
        }
    }

    private final String filepath;
    private final int maxEntries;
    private final List<Entry> entries = new ArrayList<>();
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss");

    public HistoryManager(String filepath, int maxEntries) {
        this.filepath = filepath;
        this.maxEntries = maxEntries;
        load();
    }

    public HistoryManager() {
        this("calc_history.txt", 50);
    }

    public synchronized void addEntry(String expression, String result) {
        String time = LocalTime.now().format(TIME_FMT);
        entries.add(0, new Entry(expression, result, time));
        if (entries.size() > maxEntries) {
            entries.remove(entries.size() - 1);
        }
        save();
    }

    public synchronized List<Entry> getEntries() {
        return new ArrayList<>(entries);
    }

    public synchronized void clear() {
        entries.clear();
        save();
    }

    private void save() {
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(filepath), StandardCharsets.UTF_8))) {
            for (Entry e : entries) {
                writer.println(e.toString());
            }
        } catch (IOException ignored) {}
    }

    private void load() {
        File file = new File(filepath);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length >= 3) {
                    entries.add(new Entry(parts[1], parts[2], parts[0]));
                }
            }
        } catch (IOException ignored) {}
    }
}

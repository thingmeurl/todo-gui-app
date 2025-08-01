package task;



import java.time.LocalDate;
import java.util.Optional;

public class Task {
    private String title;
    private boolean done;
    private Priority priority;
    private LocalDate dueDate;

    public Task(String title, boolean done, Priority priority, LocalDate dueDate) {
        this.title = title;
        this.done = done;
        this.priority = priority;
        this.dueDate = dueDate;
    }
    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public boolean isDone(){
        return this.done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public Priority getPriority(){
        return priority;
    }

    public void setPriority(Priority priority){
        this.priority = priority;
    }

    public LocalDate getDueDate(){
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate){
        this.dueDate = dueDate;
    }

    public void toggleDone(){
        this.done = !this.done;
    }
    // checkbox だけ担当するヘルパー関数
    private String getCheckbox() {
        return done ? "[✓]" : "[ ]";
    }

    public boolean isDueToday(){
        return LocalDate.now().equals(this.dueDate);
    }

    @Override
    public String toString(){
        return String.format("%s %-20s 優先度: %-2s 締切： %s",
                getCheckbox(),
                title,
                priority,
                dueDate != null ? dueDate.toString() : "なし");
    }

    // CSV形式に変換
    String toCSV() {
        return String.join(",",
        title,
        Boolean.toString(done),
        priority != null ? priority.toString() : "",
        dueDate != null ? dueDate.toString() : "");
    }

    // CSVからTask生成
    static Optional<Task> fromCSV(String line) {
        try {
            String[] parts = line.split(",", -1);
            if (parts.length < 4) return Optional.empty();
            String title = parts[0];
            boolean isDone = Boolean.parseBoolean(parts[1]);
            Priority priority = parts[2].isEmpty() ? null : Priority.fromString(parts[2]);
            LocalDate dueDate = parts[3].isEmpty() ? null : LocalDate.parse(parts[3]);
            return Optional.of(new Task(title, isDone, priority, dueDate));

        } catch (Exception e) {
            System.out.println("CSV読み込みエラー：" + e.getMessage());
            return Optional.empty();
        }
    }
}

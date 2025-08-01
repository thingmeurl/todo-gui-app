package task;

import javax.swing.table.AbstractTableModel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;

public class TaskTableModel extends AbstractTableModel {
    private final String[] columnNames = {"完了","タスク名","期日","優先度"};
    private final List<Task> tasks;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public TaskTableModel(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public int getRowCount() {
        return tasks.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "完了";
            case 1: return "タスク名";
            case 2: return "期日";
            case 3: return "優先度";
            default: return "";
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0: return Boolean.class;   // チェックボックス用
            case 1: return String.class;    // タスク名
            case 2: return LocalDate.class; // 期日
            case 3: return Priority.class;  // 優先度
            default: return Object.class;
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Task task = tasks.get(rowIndex);
        switch (columnIndex) {
            case 0: return task.isDone();
            case 1: return task.getTitle();
            case 2:
                LocalDate dueDate = task.getDueDate();
                return (dueDate != null) ? formatter.format(dueDate) : "";
            case 3: return task.getPriority();
            default: return null;
        }
    }

    public Task getTaskAt(int rowIndex) {
        return tasks.get(rowIndex);
    }

    public void removeTaskAt(int rowIndex) {
        tasks.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public void addTask(Task task) {
        tasks.add(task);
        fireTableRowsInserted(tasks.size() - 1, tasks.size() -1);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Task task = tasks.get(rowIndex);

        switch (columnIndex) {
            case 0: // 完了チェック
                if (aValue instanceof Boolean) {
                    task.setDone((Boolean) aValue);
                }
                break;
            case 1: // タスク名
                if (aValue != null) {
                    task.setTitle(aValue.toString());
                }
                break;
            case 2: // 期日
                if (aValue == null || aValue.toString().isBlank()) {
                    task.setDueDate(null);
                } else {
                    try {
                        task.setDueDate(LocalDate.parse(aValue.toString()));
                    } catch (DateTimeParseException e) {
                        System.out.println("Invalid date format: " + aValue.toString());
                    }
                }
                break;
            case 3: // 優先度
                if (aValue instanceof Priority) {
                    task.setPriority((Priority) aValue);
                }
                break;
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 0; //完了列のみ編集可
    }


}

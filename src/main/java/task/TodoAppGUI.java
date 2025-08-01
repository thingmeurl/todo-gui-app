package task;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TodoAppGUI extends JFrame {

    private TaskManager taskManager;
    private JTable taskTable;
    private TaskTableModel tableModel;

    public TodoAppGUI(){
        super("ToDoアプリ");
        taskManager = new TaskManager();
        List<Task> loadedTasks = taskManager.getTasks();

        // モデルとテーブルの準備
        tableModel = new TaskTableModel(loadedTasks);
        System.out.println(tableModel.getRowCount());
        taskTable = new JTable(tableModel);
        TableRowSorter<TaskTableModel> sorter = new TableRowSorter<>(tableModel);
        taskTable.setRowSorter(sorter);
        taskTable.setFont(new Font("Yu Gothic UI",Font.PLAIN,16));
        taskTable.setRowHeight(30);

        // 完了列の幅を固定
        taskTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        taskTable.getColumnModel().getColumn(0).setMaxWidth(50);
        taskTable.getColumnModel().getColumn(0).setMinWidth(50);

        // タスク名、期日、優先度の列幅
        taskTable.getColumnModel().getColumn(1).setPreferredWidth(250);
        taskTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        taskTable.getColumnModel().getColumn(3).setPreferredWidth(80);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addButton = new JButton("追加");
        JButton editButton = new JButton("編集");
        JButton deleteButton = new JButton("削除");
        JCheckBox filterCheckBox = new JCheckBox("完了済みタスクを非表示");

        topPanel.add(addButton);
        topPanel.add(editButton);
        topPanel.add(deleteButton);
        topPanel.add(filterCheckBox);
        add(topPanel, BorderLayout.NORTH);


        // 完了済みタスクの非表示
        filterCheckBox.addActionListener(e -> {
            if (filterCheckBox.isSelected()){
                sorter.setRowFilter(new RowFilter<TaskTableModel, Integer>() {
                    @Override
                    public boolean include(Entry<? extends TaskTableModel, ? extends Integer> entry) {
                        Task task = tableModel.getTaskAt(entry.getIdentifier());
                        return !task.isDone();
                    }
                });
            } else {
                sorter.setRowFilter(null);
            }
        });

        addButton.addActionListener(e -> {
            TaskEditorDialog dialog = new TaskEditorDialog(this, null);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                Task newTask = dialog.getTask();
                taskManager.addTask(newTask);
                refreshTaskTable();
                JOptionPane.showMessageDialog(this, "タスクを追加しました");
                taskManager.saveTasksToFile();
            }
        });

        editButton.addActionListener(e -> {
            int selectedRow = taskTable.getSelectedRow();
            if (selectedRow >= 0) {
                Task selectedTask = tableModel.getTaskAt(selectedRow);

                // JFrame
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(taskTable);

                // 編集用ダイアログを開く（既存タスクを渡す）
                TaskEditorDialog dialog = new TaskEditorDialog(parentFrame, selectedTask);
                dialog.setVisible(true);

                if (dialog.isConfirmed()) {
                    // TaskEditorDialog で task は直接更新済みなので再取得は不要
                    refreshTaskTable();

                    JOptionPane.showMessageDialog(this,"タスクを編集しました");
                    taskManager.saveTasksToFile();
                }
            } else {
                JOptionPane.showMessageDialog(this,"編集するタスクを選択してください");
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = taskTable.getSelectedRow();
            if (selectedRow >= 0) {
                Task selectedTask = tableModel.getTaskAt(selectedRow);

                int confirm = JOptionPane.showConfirmDialog(this,"選択したタスクを削除しますか？","確認",JOptionPane.YES_NO_CANCEL_OPTION);

                if (confirm == JOptionPane.YES_NO_OPTION) {
                    taskManager.removeTask(selectedTask);
                    refreshTaskTable();

                    JOptionPane.showMessageDialog(this,"タスクを削除しました");
                    taskManager.saveTasksToFile();

                } else {
                    JOptionPane.showMessageDialog(this,"削除するタスクを選択してください");
                }
            }
        });

        tableModel.addTableModelListener(e -> {
            // 値が変更された時
            if (e.getType() == TableModelEvent.UPDATE) {
                taskManager.saveTasksToFile(); // 自動保存
            }
        });

        JScrollPane scrollPane = new JScrollPane(taskTable);
        add(scrollPane, BorderLayout.CENTER);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void refreshTaskTable() {
        tableModel.fireTableDataChanged();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TodoAppGUI gui = new TodoAppGUI();
            gui.setTitle("Todo アプリ");
            gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            gui.setSize(600,400);
            gui.setVisible(true);
        });
    }
}

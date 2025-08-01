package task;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.Locale;


public class TaskEditorDialog extends JDialog {
    private final JFrame parentFrame;
    private DatePicker datePicker;
    private JPanel mainPanel;
    private JPanel titlePanel;
    private JPanel deadLinePanel;
    private JPanel priorityPanel;
    private JPanel completedPanel;
    private JButton okButton;
    private JButton cancelButton;
    private JComboBox<Priority> priorityBox;
    private JCheckBox completedCheckBox;
    private JTextField titleField;
    private JTextField dueDateField;
    private LocalDate dueDate;
    private boolean confirmed = false;
    private boolean completed = false;
    private boolean isNewTask;
    private Task task;
    private DatePicker dueDatePicker;
    private static final Font COMMON_FONT = new Font("Yu Gothic UI",Font.PLAIN,16);

    public TaskEditorDialog(JFrame parent, Task task) {
        super(parent, "タスク編集", true);
        this.parentFrame = parent;
        this.task = task;
        this.isNewTask = (task == null);
        initializeGUI();
        setResizable(false);
        setPreferredSize(new Dimension(450,250));
        pack();
        setLocationRelativeTo(null);
        addEventHandlers();
        if (task != null) {
            setFieldFromTask(task);
        }

//        // GlidLayoutモーダル
//        setLayout(new GridLayout(0,2,10,10));
//        UIManager.put("Label.font", new Font("Yu Gothic UI",Font.PLAIN,14));
//        UIManager.put("TextField.font", new Font("Yu Gothic UI",Font.PLAIN,14));
//
//        // タスク名
//        add(new JLabel("タスク名"));
//        titleField = new JTextField(task != null ? task.getTitle() : "");
//        add(titleField);
//
//        // 日付ピッカー設定
//        DatePickerSettings dateSettings = new DatePickerSettings();
//        dateSettings.setAllowEmptyDates(true);
//        dueDatePicker = new DatePicker(dateSettings);
//
//        // 初期値の設定
//        if (task != null && task.getDueDate() != null) {
//            dueDatePicker.setDate(task.getDueDate());
//        }
//
//        // 期日
//        add(new JLabel("期日（yyyy-MM-dd）"));
//        dueDateField = new JTextField();
//        add(dueDatePicker);
//
//        // 優先度セレクタ
//        add(new JLabel("優先度"));
//        priorityBox = new JComboBox<>(Priority.values());
//        priorityBox.setSelectedItem(task != null ? task.getPriority() : Priority.MEDIUM); // 直接 Priority を取得
//        add(priorityBox);
//
//        // 完了チェックボックス
//        add(new JLabel("完了"));
//        completedCheckBox = new JCheckBox();
//        completedCheckBox.setSelected(task != null && task.isDone());
//        add(completedCheckBox);
//
//        // OKボタン
//        JButton okButton = new JButton("OK");
//        okButton.addActionListener(e -> {
//            String title = titleField.getText();
//
//            // バリデーション：タスク名が空欄
//            if(title.isEmpty()){
//                JOptionPane.showMessageDialog(this,"<html><b>タスク名を入力してください</b></html>","入力エラー",JOptionPane.ERROR_MESSAGE);
//                return;
//            }
//
//            // LocalDateとしてそのまま取得
//            LocalDate parsedDueDate = dueDatePicker.getDate();
//            if (parsedDueDate == null) {
//                int result = JOptionPane.showConfirmDialog(this,"<html><b>期日が未入力です。空欄のままにしますか？</b></html>","確認",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.ERROR_MESSAGE);
//                if (result != JOptionPane.YES_OPTION) {
//                    return;
//                }
//            }
//
//            Priority priority = (Priority) priorityBox.getSelectedItem();
//            boolean completed = completedCheckBox.isSelected();
//
//            if (isNewTask) {
//                this.task = new Task(title, completed, priority, parsedDueDate);
//            } else {
//                this.task.setTitle(title);
//                this.task.setPriority(priority);
//                this.task.setDone(completed);
//                this.task.setDueDate(parsedDueDate);
//            }
//
//            confirmed = true;
//            dispose();
//        });
//
//        // キャンセルボタン
//        JButton cancelButton = new JButton("キャンセル");
//        cancelButton.addActionListener(e -> {
//            confirmed = false;
//            dispose();
//        });
//
//        add(okButton);
//        add(cancelButton);
//
//        pack();
//        setLocationRelativeTo(parent);
    }

    private void initializeGUI() {
        // メインパネルを垂直方向に並べる BoxLayout で構成
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        // ボタン生成と配置
        okButton = new JButton("OK");
        okButton.setFont(COMMON_FONT);
        cancelButton = new JButton("キャンセル");
        cancelButton.setFont(COMMON_FONT);

        // タスク名行
        titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        titlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleLabel = new JLabel("タスク名：");
        titleLabel.setFont(COMMON_FONT);
        titleLabel.setPreferredSize(new Dimension(80,25));

        titleField = new JTextField();
        titleField.setFont(COMMON_FONT);
        Dimension titleFieldSize = new Dimension(300, 20);
        titleField.setPreferredSize(titleFieldSize);
        titleField.setMaximumSize(titleFieldSize);
        titleField.setMinimumSize(titleFieldSize);
        titleField.setBackground(Color.WHITE);
        titleField.setForeground(Color.BLACK);
        titleField.setDisabledTextColor(Color.BLACK);
        titleField.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(26,0)));
        titlePanel.add(titleField);
//        titlePanel.add(Box.createHorizontalGlue());

        mainPanel.add(titlePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0,10))); // 縦スぺ―ス

        // 期日行
        deadLinePanel = new JPanel();
        deadLinePanel.setLayout(new BoxLayout(deadLinePanel, BoxLayout.X_AXIS));
        deadLinePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ラベル（他と同じ幅に調整）
        JLabel deadLineLabel = new JLabel("期日：");
        deadLineLabel.setFont(COMMON_FONT);
        deadLineLabel.setPreferredSize(new Dimension(80,25));
        deadLinePanel.add(deadLineLabel);
        deadLinePanel.add(Box.createRigidArea(new Dimension(46,0)));

        // 日付表示用のフィールド
        Dimension dateFieldSize = new Dimension(100,25);
        dueDateField = new JTextField(10);
        dueDateField.setFont(COMMON_FONT);
        dueDateField.setPreferredSize(dateFieldSize);
        dueDateField.setMaximumSize(dateFieldSize);
        dueDateField.setMinimumSize(dateFieldSize);
        dueDateField.setEnabled(false);
        dueDateField.setBackground(Color.WHITE);
        dueDateField.setForeground(Color.BLACK);
        dueDateField.setDisabledTextColor(Color.BLACK);
        dueDateField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        deadLinePanel.add(dueDateField);

        // DatePicker の設定と追加
        DatePickerSettings settings = new DatePickerSettings(Locale.JAPAN);
        settings.setVisibleDateTextField(false);
        dueDatePicker = new DatePicker(settings);
        Dimension pickerSize = dueDatePicker.getPreferredSize();
        dueDatePicker.setMaximumSize(pickerSize);
        dueDatePicker.setMinimumSize(pickerSize);
        dueDatePicker.addDateChangeListener(e ->{
            if (e.getNewDate() != null) {
                dueDateField.setText(e.getNewDate().toString());
            } else {
                dueDateField.setText("");
            }
        });
        deadLinePanel.add(Box.createRigidArea(new Dimension(10,0))); // Picker との余白
        deadLinePanel.add(dueDatePicker);

        mainPanel.add(deadLinePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0,10)));

        // 優先度行
        JPanel priorityPanel = new JPanel();
        priorityPanel.setLayout(new BoxLayout(priorityPanel, BoxLayout.X_AXIS));
        priorityPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel priorityLabel = new JLabel("優先度：");
        priorityLabel.setFont(COMMON_FONT);
        priorityLabel.setPreferredSize(new Dimension(80,25));

        // サイズ固定
        priorityBox = new JComboBox<>(Priority.values());
        priorityBox.setSelectedItem(Priority.MEDIUM);
        priorityBox.setFont(COMMON_FONT);
        priorityBox.setPreferredSize(new Dimension(50,25));
        priorityBox.setMaximumSize(new Dimension(50,25));
        priorityBox.setMinimumSize(new Dimension(50,25));

        priorityPanel.add(priorityLabel);
        priorityPanel.add(Box.createRigidArea(new Dimension(31,0))); // ラベルとボックスの間の余白
        priorityPanel.add(priorityBox);

        mainPanel.add(priorityPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0,10)));

        // 完了行
        completedPanel = new JPanel();
        completedCheckBox = new JCheckBox();

        completedPanel.setLayout(new BoxLayout(completedPanel,BoxLayout.X_AXIS));
        completedPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel completeLabel = new JLabel("完了：");
        completeLabel.setFont(COMMON_FONT);
        completeLabel.setPreferredSize(new Dimension(60,25));
        completeLabel.setMaximumSize(new Dimension(60,25));

        completedCheckBox.setPreferredSize(new Dimension(25,25));
        completedCheckBox.setMaximumSize(new Dimension(25,25));
        completedCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        completedPanel.add(completeLabel);
        completedPanel.add(Box.createRigidArea(new Dimension(30,0)));
        completedPanel.add(completedCheckBox);

        mainPanel.add(completedPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0,10)));

        // ボタン行
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.X_AXIS));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        okButton = new JButton("OK");
        okButton.setFont(COMMON_FONT);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(okButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10,0)));
        cancelButton = new JButton(("キャンセル"));
        cancelButton.setFont(COMMON_FONT);
        buttonPanel.add(cancelButton);
        buttonPanel.add(Box.createHorizontalGlue());

        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(buttonPanel);

        // ダイアログにセット
        System.out.println("mainPanel = " + mainPanel);
        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }

    private void addEventHandlers() {
        // OKボタン
        okButton.addActionListener(e -> {
            String title = titleField.getText();

            // バリデーション：タスク名が空欄
            if(title.isEmpty()) {
                JOptionPane.showMessageDialog(TaskEditorDialog.this,"<html><b>タスク名を入力してください","入力エラー",JOptionPane.ERROR_MESSAGE);
                return;
            }

            // LocalDateとしてそのまま取得
            LocalDate parsedDueDate = dueDatePicker.getDate();
            if (parsedDueDate == null) {
                int result = JOptionPane.showConfirmDialog(TaskEditorDialog.this,"<html><b>期日が未入力です。空欄のままにしますか？</b></html>","確認",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.ERROR_MESSAGE);
                if (result != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            Priority priority = (Priority) priorityBox.getSelectedItem();
            boolean completed = completedCheckBox.isSelected();

            if (isNewTask) {
                this.task = new Task(title, completed, priority, parsedDueDate);
            } else {
                this.task.setTitle(title);
                this.task.setPriority(priority);
                this.task.setDone(completed);
                this.task.setDueDate(parsedDueDate);
            }

            confirmed = true;
            dispose();
        });

        // キャンセルボタン
        cancelButton.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

    }

    private void setFieldFromTask(Task task) {
        titleField.setText(task.getTitle());
        dueDateField.setText(task.getDueDate().toString());
        priorityBox.setSelectedItem(task.getPriority());
        completedCheckBox.setSelected(task.isDone());
    }


    public boolean isConfirmed() {
        return confirmed;
    }

    public Priority getTaskPriority() {
        return (Priority) priorityBox.getSelectedItem();
    }

    public String getTaskTitle() {
        return titleField.getText();
    }

    public boolean isTaskCompleted() {
        return completedCheckBox.isSelected();
    }

    public Task getTask() {
        return confirmed ? task : null;
    }

}

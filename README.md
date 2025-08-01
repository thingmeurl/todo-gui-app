# 🖥️ GUI版 ToDoアプリ（Java Swing）

このディレクトリには、Java Swing を用いて作成した GUI版のToDoアプリが格納されています。  
CLI版（コマンドラインインターフェース）とは別に、GUI操作でタスクの追加・編集・削除・完了切替などが行えます。

---

## 📁 ディレクトリ構成

```
gui/
└── src/
    └── main/
        └── java/
            └── task/
                ├── Task.java
                ├── TaskManager.java
                ├── Priority.java
                ├── TodoAppGUI.java
                ├── TaskEditorDialog.java
                └── TaskTableModel.java
```

---

## 🚀 実行方法

IntelliJ IDEA等のIDEで `TodoAppGUI.java` を実行してください。  
または、以下のようにコンパイル＆実行することも可能です。

```bash
# クラスパスのルートまで移動
cd gui/src/main/java

# コンパイル
javac task/*.java

# 実行
java task.TodoAppGUI
```

---

## ✅ 主な機能

- タスクの追加・削除・編集
- 優先度（高・中・低）の設定
- タスクの完了チェック
- タスクの一覧表示（JTable）
- データのファイル保存・読み込み（`tasks.csv`）

---

## ⚠️ 注意点

- 同一プロジェクト内にCLI版とGUI版が共存しています。クラスパスや依存関係が重複しないようにご注意ください。
- GUI版に不要なCLI専用クラス（例：`ConsoleInputProvider`）はビルド対象から除外してください。

---

## ✍️ 開発メモ

- GUI構成には Swing を使用しています。
- ファイル保存形式は CSVで、CLI版と共有可能です（フォーマット互換性あり）。
- 今後、フィルタリング機能や日付ソートなどの拡張も検討中です。

---

## 📌 作者メモ

学習目的で構築したJavaポートフォリオアプリです。  
「とにかく動く」ことを重視しながら、リファクタリング・MVC的構造の分離など段階的に取り組んでいます。


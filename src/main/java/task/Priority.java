package task;

public enum Priority {
    HIGH,
    MEDIUM,
    LOW;

    @Override
    public String toString() {
        switch (this) {
            case HIGH: return "高";
            case MEDIUM: return "中";
            case LOW: return "低";
            default: return super.toString();
        }
    }

    // 文字列からEnumを返すメソッド（柔軟な入力対応）
    public static Priority fromString(String input) {
        switch (input.trim().toLowerCase()) {
            case "high":
            case "高":
                return HIGH;
            case "medium":
            case "中":
                return MEDIUM;
            case "low":
            case "低":
                return LOW;
            default:
                throw new IllegalArgumentException("無効な優先度：" + input);
        }
    }
}

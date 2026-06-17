package network;

public final class Action {
    public static final String LOGIN = "LOGIN";
    public static final String REGISTER = "REGISTER";
    public static final String RESET_PASSWORD = "RESET_PASSWORD";
    public static final String CATEGORY_FIND_ALL = "CATEGORY_FIND_ALL";
    public static final String TYPE_FIND_ALL = "TYPE_FIND_ALL";
    public static final String TRANSACTION_FIND_BY_USER = "TRANSACTION_FIND_BY_USER";
    public static final String TRANSACTION_FIND_ALL = "TRANSACTION_FIND_ALL";
    public static final String TRANSACTION_CREATE = "TRANSACTION_CREATE";
    public static final String TRANSACTION_UPDATE = "TRANSACTION_UPDATE";
    public static final String TRANSACTION_DELETE = "TRANSACTION_DELETE";
    public static final String TRANSACTION_SEARCH = "TRANSACTION_SEARCH";
    public static final String REPORT_BY_DAY = "REPORT_BY_DAY";
    public static final String REPORT_FAMILY_BY_DAY = "REPORT_FAMILY_BY_DAY";
    public static final String EXPORT_TRANSACTIONS_CSV = "EXPORT_TRANSACTIONS_CSV";
    public static final String IMPORT_TRANSACTIONS_CSV = "IMPORT_TRANSACTIONS_CSV";
    public static final String USER_FIND_ALL = "USER_FIND_ALL";
    public static final String USER_CREATE = "USER_CREATE";
    public static final String USER_UPDATE = "USER_UPDATE";
    public static final String USER_DELETE = "USER_DELETE";
    public static final String CATEGORY_CREATE = "CATEGORY_CREATE";
    public static final String CATEGORY_UPDATE = "CATEGORY_UPDATE";
    public static final String CATEGORY_DELETE = "CATEGORY_DELETE";

    private Action() {
    }
}

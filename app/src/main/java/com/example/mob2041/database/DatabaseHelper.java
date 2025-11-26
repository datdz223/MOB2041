package com.example.mob2041.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MOB2041.db";
    private static final int DATABASE_VERSION = 1;

    // Bảng Users (Nhân viên)
    public static final String TABLE_USERS = "users";
    public static final String COL_USER_ID = "user_id";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";
    public static final String COL_FULL_NAME = "full_name";
    public static final String COL_ROLE = "role"; // "admin" hoặc "staff"
    public static final String COL_EMAIL = "email";
    public static final String COL_PHONE = "phone";
    public static final String COL_CREATED_DATE = "created_date";

    // Bảng ProductCategories (Danh mục sản phẩm)
    public static final String TABLE_PRODUCT_CATEGORIES = "product_categories";
    public static final String COL_CATEGORY_ID = "category_id";
    public static final String COL_CATEGORY_NAME = "category_name";
    public static final String COL_CATEGORY_DESCRIPTION = "description";

    // Bảng Products (Sản phẩm)
    public static final String TABLE_PRODUCTS = "products";
    public static final String COL_PRODUCT_ID = "product_id";
    public static final String COL_PRODUCT_NAME = "product_name";
    public static final String COL_PRODUCT_DESCRIPTION = "description";
    public static final String COL_PRICE = "price";
    public static final String COL_UNIT = "unit";
    public static final String COL_IMAGE = "image";

    // Bảng Customers (Khách hàng)
    public static final String TABLE_CUSTOMERS = "customers";
    public static final String COL_CUSTOMER_ID = "customer_id";
    public static final String COL_CUSTOMER_NAME = "customer_name";
    public static final String COL_CUSTOMER_PHONE = "phone";
    public static final String COL_CUSTOMER_EMAIL = "email";
    public static final String COL_CUSTOMER_ADDRESS = "address";

    // Bảng SalesOrders (Hóa đơn bán hàng)
    public static final String TABLE_SALES_ORDERS = "sales_orders";
    public static final String COL_ORDER_ID = "order_id";
    public static final String COL_ORDER_DATE = "order_date";
    public static final String COL_TOTAL_AMOUNT = "total_amount";
    public static final String COL_STATUS = "status"; // "pending", "completed", "cancelled"
    public static final String COL_STAFF_ID = "staff_id";

    // Bảng OrderDetails (Chi tiết hóa đơn)
    public static final String TABLE_ORDER_DETAILS = "order_details";
    public static final String COL_ORDER_DETAIL_ID = "order_detail_id";
    public static final String COL_QUANTITY = "quantity";
    public static final String COL_UNIT_PRICE = "unit_price";
    public static final String COL_SUBTOTAL = "subtotal";

    // Bảng WarehouseReceipts (Phiếu nhập kho)
    public static final String TABLE_WAREHOUSE_RECEIPTS = "warehouse_receipts";
    public static final String COL_RECEIPT_ID = "receipt_id";
    public static final String COL_RECEIPT_DATE = "receipt_date";
    public static final String COL_SUPPLIER = "supplier";
    public static final String COL_RECEIPT_NOTE = "note";

    // Bảng Inventory (Tồn kho)
    public static final String TABLE_INVENTORY = "inventory";
    public static final String COL_INVENTORY_ID = "inventory_id";
    public static final String COL_STOCK_QUANTITY = "stock_quantity";
    public static final String COL_LAST_UPDATED = "last_updated";

    // Bảng Payments (Thanh toán)
    public static final String TABLE_PAYMENTS = "payments";
    public static final String COL_PAYMENT_ID = "payment_id";
    public static final String COL_PAYMENT_DATE = "payment_date";
    public static final String COL_PAYMENT_METHOD = "payment_method"; // "cash", "card", "transfer"
    public static final String COL_PAYMENT_AMOUNT = "amount";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng Users
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT UNIQUE NOT NULL, " +
                COL_PASSWORD + " TEXT NOT NULL, " +
                COL_FULL_NAME + " TEXT NOT NULL, " +
                COL_ROLE + " TEXT NOT NULL, " +
                COL_EMAIL + " TEXT, " +
                COL_PHONE + " TEXT, " +
                COL_CREATED_DATE + " TEXT DEFAULT CURRENT_TIMESTAMP" +
                ")";
        db.execSQL(createUsersTable);

        // Tạo bảng ProductCategories
        String createCategoriesTable = "CREATE TABLE " + TABLE_PRODUCT_CATEGORIES + " (" +
                COL_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CATEGORY_NAME + " TEXT NOT NULL, " +
                COL_CATEGORY_DESCRIPTION + " TEXT" +
                ")";
        db.execSQL(createCategoriesTable);

        // Tạo bảng Products
        String createProductsTable = "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                COL_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PRODUCT_NAME + " TEXT NOT NULL, " +
                COL_PRODUCT_DESCRIPTION + " TEXT, " +
                COL_PRICE + " REAL NOT NULL, " +
                COL_CATEGORY_ID + " INTEGER, " +
                COL_UNIT + " TEXT DEFAULT 'cái', " +
                COL_IMAGE + " TEXT, " +
                "FOREIGN KEY(" + COL_CATEGORY_ID + ") REFERENCES " + TABLE_PRODUCT_CATEGORIES + "(" + COL_CATEGORY_ID + ")" +
                ")";
        db.execSQL(createProductsTable);

        // Tạo bảng Customers
        String createCustomersTable = "CREATE TABLE " + TABLE_CUSTOMERS + " (" +
                COL_CUSTOMER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CUSTOMER_NAME + " TEXT NOT NULL, " +
                COL_CUSTOMER_PHONE + " TEXT, " +
                COL_CUSTOMER_EMAIL + " TEXT, " +
                COL_CUSTOMER_ADDRESS + " TEXT" +
                ")";
        db.execSQL(createCustomersTable);

        // Tạo bảng SalesOrders
        String createSalesOrdersTable = "CREATE TABLE " + TABLE_SALES_ORDERS + " (" +
                COL_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CUSTOMER_ID + " INTEGER, " +
                COL_ORDER_DATE + " TEXT DEFAULT CURRENT_TIMESTAMP, " +
                COL_TOTAL_AMOUNT + " REAL NOT NULL, " +
                COL_STATUS + " TEXT DEFAULT 'pending', " +
                COL_STAFF_ID + " INTEGER, " +
                "FOREIGN KEY(" + COL_CUSTOMER_ID + ") REFERENCES " + TABLE_CUSTOMERS + "(" + COL_CUSTOMER_ID + "), " +
                "FOREIGN KEY(" + COL_STAFF_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + ")" +
                ")";
        db.execSQL(createSalesOrdersTable);

        // Tạo bảng OrderDetails
        String createOrderDetailsTable = "CREATE TABLE " + TABLE_ORDER_DETAILS + " (" +
                COL_ORDER_DETAIL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ORDER_ID + " INTEGER NOT NULL, " +
                COL_PRODUCT_ID + " INTEGER NOT NULL, " +
                COL_QUANTITY + " INTEGER NOT NULL, " +
                COL_UNIT_PRICE + " REAL NOT NULL, " +
                COL_SUBTOTAL + " REAL NOT NULL, " +
                "FOREIGN KEY(" + COL_ORDER_ID + ") REFERENCES " + TABLE_SALES_ORDERS + "(" + COL_ORDER_ID + "), " +
                "FOREIGN KEY(" + COL_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCTS + "(" + COL_PRODUCT_ID + ")" +
                ")";
        db.execSQL(createOrderDetailsTable);

        // Tạo bảng WarehouseReceipts
        String createWarehouseReceiptsTable = "CREATE TABLE " + TABLE_WAREHOUSE_RECEIPTS + " (" +
                COL_RECEIPT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_RECEIPT_DATE + " TEXT DEFAULT CURRENT_TIMESTAMP, " +
                COL_SUPPLIER + " TEXT, " +
                COL_STAFF_ID + " INTEGER, " +
                COL_RECEIPT_NOTE + " TEXT, " +
                "FOREIGN KEY(" + COL_STAFF_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + ")" +
                ")";
        db.execSQL(createWarehouseReceiptsTable);

        // Tạo bảng Inventory
        String createInventoryTable = "CREATE TABLE " + TABLE_INVENTORY + " (" +
                COL_INVENTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PRODUCT_ID + " INTEGER NOT NULL UNIQUE, " +
                COL_STOCK_QUANTITY + " INTEGER DEFAULT 0, " +
                COL_LAST_UPDATED + " TEXT DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY(" + COL_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCTS + "(" + COL_PRODUCT_ID + ")" +
                ")";
        db.execSQL(createInventoryTable);

        // Tạo bảng Payments
        String createPaymentsTable = "CREATE TABLE " + TABLE_PAYMENTS + " (" +
                COL_PAYMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ORDER_ID + " INTEGER NOT NULL, " +
                COL_PAYMENT_DATE + " TEXT DEFAULT CURRENT_TIMESTAMP, " +
                COL_PAYMENT_METHOD + " TEXT NOT NULL, " +
                COL_PAYMENT_AMOUNT + " REAL NOT NULL, " +
                "FOREIGN KEY(" + COL_ORDER_ID + ") REFERENCES " + TABLE_SALES_ORDERS + "(" + COL_ORDER_ID + ")" +
                ")";
        db.execSQL(createPaymentsTable);

        // Chèn dữ liệu mẫu
        insertSampleData(db);
    }

    private void insertSampleData(SQLiteDatabase db) {
        // Tạo tài khoản Admin mặc định
        db.execSQL("INSERT INTO " + TABLE_USERS + " (" + COL_USERNAME + ", " + COL_PASSWORD + ", " + COL_FULL_NAME + ", " + COL_ROLE + ") VALUES ('admin', 'admin123', 'Quản trị viên', 'admin')");
        
        // Tạo tài khoản Staff mặc định
        db.execSQL("INSERT INTO " + TABLE_USERS + " (" + COL_USERNAME + ", " + COL_PASSWORD + ", " + COL_FULL_NAME + ", " + COL_ROLE + ") VALUES ('staff', 'staff123', 'Nhân viên bán hàng', 'staff')");

        // Tạo một số danh mục mẫu
        db.execSQL("INSERT INTO " + TABLE_PRODUCT_CATEGORIES + " (" + COL_CATEGORY_NAME + ", " + COL_CATEGORY_DESCRIPTION + ") VALUES ('Điện tử', 'Các sản phẩm điện tử')");
        db.execSQL("INSERT INTO " + TABLE_PRODUCT_CATEGORIES + " (" + COL_CATEGORY_NAME + ", " + COL_CATEGORY_DESCRIPTION + ") VALUES ('Quần áo', 'Thời trang và quần áo')");
        db.execSQL("INSERT INTO " + TABLE_PRODUCT_CATEGORIES + " (" + COL_CATEGORY_NAME + ", " + COL_CATEGORY_DESCRIPTION + ") VALUES ('Thực phẩm', 'Đồ ăn và thức uống')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVENTORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WAREHOUSE_RECEIPTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SALES_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }
}


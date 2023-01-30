package com.xxl.job.admin.core.datax;

public enum DataTransferTypeEnum {

    ORACLE_TO_ORACLE("ORACLE_TO_ORACLE", "oracle.json"),
    ORACLE_TO_MYSQL("ORACLE_TO_MYSQL", "oracle2mysql.json"),
    MYSQL_TO_MYSQL("MYSQL_TO_MYSQL", "mysql.json"),
    MYSQL_TO_ORACLE("MYSQL_TO_ORACLE", "mysql2oracle.json");


    private String title;
    private String file;

    DataTransferTypeEnum(String title, String file) {
        this.title = title;
        this.file = file;
    }

    public String getTitle() {
        return title;
    }

    public String getFile() {
        return file;
    }

    public static DataTransferTypeEnum match(String name, DataTransferTypeEnum defaultItem){
        for (DataTransferTypeEnum item: DataTransferTypeEnum.values()) {
            if (item.name().equals(name)) {
                return item;
            }
        }
        return defaultItem;
    }

}

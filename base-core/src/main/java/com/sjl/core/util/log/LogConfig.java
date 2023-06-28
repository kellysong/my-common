package com.sjl.core.util.log;


/**
 * 日志打印配置类
 *
 * @author Kelly
 * @version 1.0.0
 * @filename LogConfig
 * @time 2023/6/28 21:37
 * @copyright(C) 2023 song
 */
public class LogConfig {

    private String tag;
    private boolean logDebugMode;
    private boolean writeFileFlag;
    private String logPath;
    private int saveDay;
    private int singleFileSize;

    public LogConfig(LogConfig.Builder builder) {
        this.tag = builder.tag;
        this.logDebugMode = builder.logDebugMode;
        this.writeFileFlag = builder.writeFileFlag;
        this.logPath = builder.logPath;
        this.saveDay = builder.saveDay;
        this.singleFileSize = builder.singleFileSize;


    }

    public static final class Builder {
        private String tag;
        private boolean logDebugMode;
        private boolean writeFileFlag;
        /**
         * 自定义日志保存路径
         */
        private String logPath;
        /**
         * 设置文件保留天数
         */
        private int saveDay;
        /**
         * 设置单个文件大小,单位B
         */
        private int singleFileSize;

        public Builder setTag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder setLogDebugMode(boolean logDebugMode) {
            this.logDebugMode = logDebugMode;
            return this;
        }

        public Builder setWriteFileFlag(boolean writeFileFlag) {
            this.writeFileFlag = writeFileFlag;
            return this;
        }

        public Builder setLogPath(String logPath) {
            this.logPath = logPath;
            return this;
        }

        public Builder setSaveDay(int saveDay) {
            this.saveDay = saveDay;
            return this;
        }

        public Builder setSingleFileSize(int singleFileSize) {
            this.singleFileSize = singleFileSize;
            return this;
        }

        public LogConfig build() {
            return new LogConfig(this);
        }
    }

    public String getTag() {
        return tag;
    }

    public boolean isLogDebugMode() {
        return logDebugMode;
    }

    public boolean isWriteFileFlag() {
        return writeFileFlag;
    }

    public String getLogPath() {
        return logPath;
    }

    public int getSaveDay() {
        return saveDay;
    }

    public int getSingleFileSize() {
        return singleFileSize;
    }
}

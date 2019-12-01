package com.sjl.core.widget.update;

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename UpdateDialogListener.java
 * @time 2019/1/24 13:57
 * @copyright(C) 2019 song
 */
public interface UpdateDialogListener {
    /**
     * 开始更新
     * @param updateDialog
     */
    void onUpdate(UpdateDialog updateDialog);

    /**
     * 取消
     * @param updateDialog
     */
    void onCancel(UpdateDialog updateDialog);
}

package com.chy.dialoglibrary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.chy.dialoglibrary.R;
import com.chy.dialoglibrary.bean.ColorBean;
import com.chy.dialoglibrary.bean.ContentBean;
import com.chy.dialoglibrary.bean.SizeBean;
import com.chy.dialoglibrary.databinding.DialogRegularBinding;
import com.chy.dialoglibrary.listener.CHYOnCancelClickListener;
import com.chy.dialoglibrary.listener.CHYOnRightClickListener;

/**
 * @author chenhongye
 */
public class LocalRegularDialog extends Dialog {

    private DialogRegularBinding mBinding = null;
    private Context mContext;

    /**
     * 窗口类型枚举
     */
    public enum DIALOG_TYPE {
        RIGHT_DIALOG, ERROR_DIALOG, WARNING_DIALOG, INFORMATION_DIALOG
    }

    public LocalRegularDialog(@NonNull Context context) {
        this(context, R.style.CHYDialog);
    }

    private LocalRegularDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        this.setCanceledOnTouchOutside(true);
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dialog_regular, null, false);
        setContentView(mBinding.getRoot());
        setTextColor(new ColorBean());
        setTextSize(new SizeBean());
        setDialogSize();
    }

    /**
     * 设置窗口尺寸
     */
    private void setDialogSize() {
        Resources resources = mContext.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        WindowManager.LayoutParams at = this.getWindow().getAttributes();
        at.height = (int) (height * 0.4);
        at.width = (int) (width * 0.8);
        this.getWindow().setAttributes(at);
    }

    /**
     * 创建对话框
     *
     * @param dialogType           对话框类型
     * @param bean                 数据源
     * @param cancelClickListener  取消按钮的监听
     * @param onRightClickListener 确定按钮的监听
     */
    public void createDialog(DIALOG_TYPE dialogType, ContentBean bean, final CHYOnCancelClickListener cancelClickListener, final CHYOnRightClickListener onRightClickListener) {
        if (bean == null) {
            throw new NullPointerException("the second params is null,method:ContentBean(String title, String content,  String cancelButton, String rightButton) or ContentBean(String content, String rightButton)");
        }
        switch (dialogType) {
            case INFORMATION_DIALOG:
                setDialogContent(R.mipmap.ic_infomation, bean);
                break;
            case WARNING_DIALOG:
                setDialogContent(R.mipmap.ic_warning, bean);
                break;
            case RIGHT_DIALOG:
                setDialogContent(R.mipmap.ic_right, bean);
                break;
            case ERROR_DIALOG:
                setDialogContent(R.mipmap.ic_error, bean);
                break;
        }
        mBinding.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (cancelClickListener != null)
                    cancelClickListener.onCancelClick(v);
            }
        });
        mBinding.tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onRightClickListener != null)
                    onRightClickListener.onRightClick(v);
            }
        });

        this.show();
    }

    /**
     * 设置提示框内容
     *
     * @param id   icon的图片
     * @param bean 数据源
     */
    private void setDialogContent(int id, ContentBean bean) {
        mBinding.imgIcon.setImageResource(id);
        mBinding.setContent(bean);
    }

    /**
     * 设置文字颜色
     * @param color 文字颜色
     */
    public void setTextColor(ColorBean color) {
        mBinding.setColor(color);
    }

    /**
     * 设置文字大小
     * @param size 文字大小
     */
    public void setTextSize(SizeBean size) {
        mBinding.setSize(size);
    }

    /**
     * 设置对话框背景
     *
     * @param drawable   drawable的res
     * @param showStorke 是否显示分割线
     */
    public void setBackgroundResource(@DrawableRes int drawable, boolean showStorke) {
        mBinding.getRoot().setBackgroundResource(drawable);
        if (!showStorke) {
            mBinding.strokeHorizontal.setVisibility(View.GONE);
            mBinding.strokeVertical.setVisibility(View.GONE);
        }
    }
}

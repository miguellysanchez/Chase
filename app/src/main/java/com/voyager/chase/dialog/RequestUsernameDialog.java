package com.voyager.chase.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.voyager.chase.R;
import com.voyager.chase.utility.PreferenceUtility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by miguellysanchez on 6/29/16.
 */
public class RequestUsernameDialog extends Dialog {

    @BindView(R.id.chase_dialog_request_username_edittext_input)
    EditText mEditTextInput;
    @BindView(R.id.chase_dialog_request_username_textview_info)
    TextView mTextViewInfo;

    public RequestUsernameDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.chase_dialog_request_username);
        ButterKnife.bind(this);
        initializeViews();
    }

    private void initializeViews() {
        mEditTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mTextViewInfo.setText("Please input a username.");
            }
        });
    }

    @OnClick(R.id.chase_dialog_request_username_button_confirm)
    public void confirmUsername() {
        String input = mEditTextInput.getText().toString().trim();
        if(TextUtils.isEmpty(input)){
            mTextViewInfo.setText("Username cannot be empty!!");
        } else if(input.contains(" ")){
            mTextViewInfo.setText("Username cannot contain spaces");
        } else {
            PreferenceUtility.getInstance(getContext()).setMqttClientId(input);
            dismiss();
        }
    }


}
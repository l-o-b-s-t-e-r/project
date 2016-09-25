package com.android.project.signup;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.project.R;
import com.android.project.cofig.WhichOneApp;

import java.net.HttpURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Lobster on 05.09.16.
 */

public class SignUpDialog extends DialogFragment implements SignUpPresenter.View {

    @BindView(R.id.editTextName)
    EditText editTextName;
    @BindView(R.id.editTextEmail)
    EditText editTextEmail;
    @BindView(R.id.editTextPassword)
    EditText editTextPassword;
    @BindView(R.id.editTextPasswordRepeat)
    EditText editTextPasswordRepeat;

    private String mName;
    private String mEmail;
    private String mPassword;
    private String mPasswordRepeat;
    private SignUpPresenter.ActionListener mActionListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionListener = new SignUpPresenterImpl(this, ((WhichOneApp) getActivity().getApplication()).getMainComponent());
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.fragment_sign_up, null);

        Dialog dialog = new AlertDialog.Builder(getActivity(), R.style.AppThemeDialogSignUp)
                .setView(view)
                .create();

        ButterKnife.bind(this, view);

        editTextName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    mActionListener.checkName(editTextName.getText().toString().trim());
                }
            }
        });

        editTextEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (Patterns.EMAIL_ADDRESS.matcher(editTextEmail.getText().toString().trim()).matches()) {
                        mActionListener.checkEmail(editTextEmail.getText().toString().trim());
                    } else {
                        editTextEmail.setError(getString(R.string.incorrect_email));
                    }
                }
            }
        });

        return dialog;
    }

    @OnClick(R.id.btn_sign_up)
    public void onClick() {
        mName = editTextName.getText().toString().trim();
        mEmail = editTextEmail.getText().toString().trim();
        mPassword = editTextPassword.getText().toString().trim();
        mPasswordRepeat = editTextPasswordRepeat.getText().toString().trim();

        if (!mName.matches("[a-z0-9]+")) {
            editTextName.setError(getString(R.string.invalid_characters));
        }

        if (mName.contains(" ")) {
            editTextName.setError(getString(R.string.space_existence));
            return;
        }

        if (mPassword.contains(" ")) {
            editTextPassword.setError(getString(R.string.space_existence));
            return;
        }

        if (mName.isEmpty()) {
            editTextName.setError(getString(R.string.empty_field));
            return;
        }

        if (mEmail.isEmpty()) {
            editTextEmail.setError(getString(R.string.empty_field));
            return;
        }

        if (mPassword.isEmpty()) {
            editTextPassword.setError(getString(R.string.empty_field));
            return;
        }

        if (mPasswordRepeat.isEmpty()) {
            editTextPasswordRepeat.setError(getString(R.string.empty_field));
            return;
        }

        if (!mPassword.equals(mPasswordRepeat)) {
            editTextPasswordRepeat.setError(getString(R.string.password_not_match));
            return;
        }

        mActionListener.signUp(mName, mPassword, mEmail);
        Toast.makeText(getContext(), "We have sent a verification to your email.", Toast.LENGTH_SHORT).show();
        dismiss();
    }

    @Override
    public void showCheckNameResult(Integer requestCode) {
        if (requestCode == HttpURLConnection.HTTP_BAD_REQUEST) {
            editTextName.setError(getString(R.string.existing_name));
        }
    }

    @Override
    public void showCheckEmailResult(Integer requestCode) {
        if (requestCode == HttpURLConnection.HTTP_BAD_REQUEST) {
            editTextEmail.setError(getString(R.string.existing_email));
        }
    }
}
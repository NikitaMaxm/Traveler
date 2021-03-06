package we.itschool.project.traveler.presentation.fragment.registration;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import traveler.module.data.travelerapi.errors.UserNetAnswers;
import traveler.module.domain.entity.UserEntity;
import traveler.module.domain.entity.UserInfo;
import we.itschool.project.traveler.R;
import we.itschool.project.traveler.app.AppStart;
import we.itschool.project.traveler.presentation.activity.LoginActivity;
import we.itschool.project.traveler.presentation.activity.MainActivity;

public class RegistrationFragment extends Fragment {

    private static final String defaultFlag = "Waiting";
    private EditText et_first_name;
    private EditText et_second_name;
    private EditText et_email;
    private EditText et_password;
    private EditText et_password_check;
    private EditText et_birth_date;
    private EditText et_phone;
    private EditText et_social_cont;
    private ProgressBar pb_reg;
    private String gender;
    private String flag = defaultFlag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        et_first_name = view.findViewById(R.id.et_reg_field_firs_name);
        et_second_name = view.findViewById(R.id.et_reg_field_second_name);
        et_email = view.findViewById(R.id.et_reg_field_email);
        et_password = view.findViewById(R.id.et_reg_field_password);
        et_password_check = view.findViewById(R.id.et_reg_field_password_two);
        et_birth_date = view.findViewById(R.id.et_reg_field_date_of_birth);
        et_phone = view.findViewById(R.id.et_reg_field_phone_number);
        et_social_cont = view.findViewById(R.id.et_reg_field_social_contacts);


        //default gender value
        gender = getResources().getStringArray(R.array.genders)[0];
        RadioButton rb_male = view.findViewById(R.id.rb_reg_man);
        rb_male.setOnClickListener(v -> gender = getResources().getStringArray(R.array.genders)[1]);
        RadioButton rb_female = view.findViewById(R.id.rb_reg_woman);
        rb_female.setOnClickListener(v -> gender = getResources().getStringArray(R.array.genders)[2]);

        pb_reg = view.findViewById(R.id.pb_registration);

        Button bt_register = view.findViewById(R.id.bt_reg_register_new_user);
        bt_register.setOnClickListener(v -> {
            if (checkAllData()) {
                pb_reg.setVisibility(View.VISIBLE);
                final Handler handler = new Handler();
                handler.postDelayed(() -> {
                    while (true) if (!defaultFlag.equals(flag)) break;
                    pb_reg.setVisibility(View.INVISIBLE);

                    if (UserNetAnswers.userOtherError.equals(flag)) {
                        Toast.makeText(this.requireActivity().getBaseContext(),
                                R.string.reg_error,
                                Toast.LENGTH_LONG).show();
                    } else if (UserNetAnswers.userAlreadyExists.equals(flag)) {
                        Toast.makeText(this.requireActivity().getBaseContext(),
                                R.string.reg_error_email_exist,
                                Toast.LENGTH_LONG).show();
                    } else if (UserNetAnswers.userSuccessRegistration.equals(flag)) {
                        startMainActivity();
                    } else {
                        Toast.makeText(this.requireActivity().getBaseContext(),
                                R.string.reg_some_error,
                                Toast.LENGTH_LONG).show();
                    }

                }, 2500);
            }
        });
    }

    private boolean checkAllData() {
        boolean check = true;

        String name = et_first_name.getText().toString().trim();
        String surname = et_second_name.getText().toString().trim();
        String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        String password2 = et_password_check.getText().toString().trim();
        String birth_date = et_birth_date.getText().toString().trim();
        String phone = et_phone.getText().toString().trim();
        String social_contacts = et_social_cont.getText().toString().trim();

        if (social_contacts.isEmpty()) {
            check = false;
            et_social_cont.setError(requireContext().getResources().getString(R.string.reg_required_social));
            et_social_cont.requestFocus();
        }
        if (phone.isEmpty()) {
            check = false;
            et_phone.setError(requireContext().getResources().getString(R.string.reg_required_phone));
            et_phone.requestFocus();
        } else if (!Patterns.PHONE.matcher(phone).matches()) {
            check = false;
            et_phone.setText("");
            et_phone.setError(requireContext().getResources().getString(R.string.reg_invalid_phone));
            et_phone.requestFocus();
        }
        if (birth_date.isEmpty()) {
            check = false;
            et_birth_date.setError(requireContext().getResources().getString(R.string.reg_required_birth));
            et_birth_date.requestFocus();
        } else if (!isValid(birth_date)) {
            check = false;
            et_birth_date.setText("");
            et_birth_date.setError(requireContext().getResources().getString(R.string.reg_invalid_birth));
            et_birth_date.requestFocus();
        }
        boolean can1 = true, can2 = true;
        if (password2.isEmpty()) {
            can2 = false;
            check = false;
            et_password_check.setError(requireContext().getResources().getString(R.string.reg_required_password));
            et_password_check.requestFocus();
        }
        if (password.isEmpty()) {
            can1 = false;
            check = false;
            et_password.setError(requireContext().getResources().getString(R.string.reg_required_password));
            et_password.requestFocus();
        } else if (password.length() < 6) {
            can1 = false;
            check = false;
            et_password.setError(requireContext().getResources().getString(R.string.reg_not_long_password));
            et_password.requestFocus();
        }
        if (can1 && can2 && !et_password.getText().toString().equals(et_password_check.getText().toString())) {
            check = false;
            et_password.setText("");
            et_password_check.setText("");
            et_password_check.setError(requireContext().getResources().getString(R.string.reg_dont_confirm_pass));
            et_password_check.requestFocus();
        }
        if (email.isEmpty()) {
            check = false;
            et_email.setError(requireContext().getResources().getString(R.string.reg_required_email));
            et_email.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(et_email.getText()).matches()) {
            check = false;
            et_email.setError(requireContext().getResources().getString(R.string.reg_invalid_email));
            et_email.requestFocus();
        }
        if (surname.isEmpty()) {
            check = false;
            et_second_name.setError(requireContext().getResources().getString(R.string.reg_required_second_name));
            et_second_name.requestFocus();
        }
        if (name.isEmpty()) {
            check = false;
            et_first_name.setError(requireContext().getResources().getString(R.string.reg_required_name));
            et_first_name.requestFocus();
        }
        if (gender.equals(getResources().getStringArray(R.array.genders)[0])) {
            if (check) {
                Toast.makeText(this.getContext(), R.string.reg_genders_error, Toast.LENGTH_LONG).show();
            }
            check = false;
        }
        if (check) {
            //send request to register new user
            registerUser(
                    new UserEntity(
                            new UserInfo(
                                    et_first_name.getText().toString() + "",
                                    et_second_name.getText().toString() + "",
                                    et_email.getText().toString() + "",
                                    et_phone.getText().toString() + "",
                                    et_social_cont.getText().toString() + "",
                                    "null",
                                    et_birth_date.getText().toString() + "",
                                    getResources().getStringArray(R.array.genders)[1].equals(gender),
                                    "",
                                    "",
                                    null,
                                    null
                            ),
                            -1
                    ),
                    et_password.getText().toString()
            );
            savePrefs();
        }
        return check;
    }

    private void registerUser(UserEntity user, String pass) {
        new Thread(() -> flag = AppStart.uRegUC.regNew(user, pass)).start();
    }

    private boolean isValid(String dateStr) {
        try {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate.parse(dateStr, dateFormatter);
        } catch (DateTimeParseException e) {
            try {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                LocalDate.parse(dateStr, dateFormatter);
            } catch (DateTimeParseException e1) {
                try {
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                    LocalDate.parse(dateStr, dateFormatter);
                } catch (DateTimeParseException e2) {
                    try {
                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("ddMMyyyy");
                        LocalDate.parse(dateStr, dateFormatter);
                    } catch (DateTimeParseException e3) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void savePrefs() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(requireActivity().getApplicationContext());

        SharedPreferences.Editor editor = pref.edit();
        editor.putString(LoginActivity.KEY_PREF_USER_PASSWORD, et_password.getText().toString());
        editor.putString(LoginActivity.KEY_PREF_USER_EMAIL, et_email.getText().toString());

        editor.apply();
    }

    private void startMainActivity() {
        FragmentManager fragmentManager = getParentFragmentManager();
        fragmentManager.popBackStack();
        Intent intent = new Intent(this.requireActivity().getBaseContext(), MainActivity.class);
        startActivity(intent);
        //close activity in case there is no more need in it
        closeActivity();
    }

    private void closeActivity() {
        this.requireActivity().finish();
    }
}
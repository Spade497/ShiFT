package mmu.edu.my.shift;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    EditText regEmail, regPassword, conPassword;
    Button regBtn;
    int check;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regEmail=findViewById(R.id.editTextTextEmail);
        regPassword=findViewById(R.id.editTextTextPassword);
        conPassword=findViewById(R.id.editTextConfirmPassword);

        fAuth=FirebaseAuth.getInstance();
        regBtn=findViewById(R.id.btnRegister);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check=0;
                checkDataEntered();
                if (check==0) {
                    String emailValue= regEmail.getText().toString().trim();
                    String passwordValue= regPassword.getText().toString().trim();
                    fAuth.createUserWithEmailAndPassword(emailValue,passwordValue).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "User created", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                            } else {
                                Toast.makeText(RegisterActivity.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    boolean isEmail(EditText text){
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    boolean isEmpty(EditText text){
        CharSequence str=text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    void checkDataEntered(){
        if (isEmpty(regPassword)) {
            regPassword.setError("Password is required");
            check++; }

        if (isEmpty(conPassword)) {
            conPassword.setError("Confirmation of password is required");
            check++;
        }

        if (!(regPassword.getText().toString().trim()).equals(conPassword.getText().toString().trim())) {
            conPassword.setError("The password entered is different");
            check++;
        }

        if (isEmail(regEmail)==false) {
            regEmail.setError("Enter valid email");
            check++; }
    }
}
package com.example.onch.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onch.R;
import com.example.onch.ModelClass.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistrationActivity extends AppCompatActivity {

    TextView txt_signin,btn_signup;
    //CircleImageView profile_image;
    EditText reg_name,reg_email,reg_password,reg_cpassword;
    FirebaseAuth auth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Uri imageUri;
    FirebaseDatabase database;
    FirebaseStorage storage;
    String imageURI;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();

        if(auth.getCurrentUser()!=null){
            Intent intent=new Intent(RegistrationActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();
        }


        txt_signin=findViewById(R.id.txt_signin);
      //  profile_image=findViewById(R.id.profile_image);
        reg_email=findViewById(R.id.reg_email);
        reg_name=findViewById(R.id.reg_name);
        reg_password=findViewById(R.id.reg_pass);
        reg_cpassword=findViewById(R.id.reg_cpass);
        btn_signup=findViewById(R.id.btn_signup);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.dismiss();
                String name=reg_name.getText().toString();
                String email=reg_email.getText().toString();
                String password=reg_password.getText().toString();
                String cpassword=reg_cpassword.getText().toString();
                String status="Hey There I'm Using This Application";


                if(TextUtils.isEmpty(name)|| TextUtils.isEmpty(email)||
                        TextUtils.isEmpty(password) || TextUtils.isEmpty(cpassword))
                {
                    Toast.makeText(RegistrationActivity.this, "Please Enter Valid Data", Toast.LENGTH_SHORT).show();
                }else if(!email.matches(emailPattern))
                {
                    reg_email.setError("Please Enter Valide Email");
                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, "Please Enter Valide Email", Toast.LENGTH_SHORT).show();
                }else if(!password.equals(cpassword) )
                {
                    Toast.makeText(RegistrationActivity.this, "Password dose not Match", Toast.LENGTH_SHORT).show();
                }else if(password.length()<6)
                {
                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, "Enter 6 Cheracter Password", Toast.LENGTH_SHORT).show();
                }else{
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            DatabaseReference reference =database.getReference().child("user").child(auth.getUid());
                            StorageReference storageReference=storage.getReference().child("upload").child(auth.getUid());

                                if(imageUri!=null)
                                {
                                    storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                if(task.isSuccessful())

                                                {
                                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            imageURI=uri.toString();
                                                            Users users=new Users(auth.getUid(),name,email,imageURI);
                                                            reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful())
                                                                    {
                                                                        progressDialog.dismiss();
                                                                        Toast.makeText(RegistrationActivity.this, "user create", Toast.LENGTH_SHORT).show();
                                                                          startActivity(new Intent(RegistrationActivity.this,HomeActivity.class));
                                                                    }else
                                                                        Toast.makeText(RegistrationActivity.this, "Error in Creating New user", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });

                                                        }
                                                    });
                                                }
                                        }
                                    });
                                }else{
                                    imageURI="https://firebasestorage.googleapis.com/v0/b/onch-dd87d.appspot.com/o/profile_iamge.jfif?alt=media&token=6f1a6af4-7f20-473f-8d68-39f1ccc4b46c";
                                    Users users=new Users(auth.getUid(),name,email,imageURI);
                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                Intent intent = new Intent (RegistrationActivity.this,HomeActivity.class);
                                                startActivity(intent);
                                            }else
                                                Toast.makeText(RegistrationActivity.this, "Error in Creating New user", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(RegistrationActivity.this,"Something Went Wrong",Toast.LENGTH_SHORT);

                        }
                    }
                });
               }
            }
        });
//        profile_image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                intent.setType("image/*");
//                startActivityForResult(intent, 2);
//            }
//        });

     txt_signin.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             Intent intent = new Intent(RegistrationActivity.this,LoginActivity.class);
             startActivity(intent);

         }
     });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==10)
        {
            if(data!=null)
            {
//                imageUri=data.getData();
//                profile_image.setImageURI(imageUri);
            }
        }
    }
}
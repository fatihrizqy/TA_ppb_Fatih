package com.example.TAppbFATIH;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.TAppbFATIH.lokalDatabase.InitDatabase;
import com.example.TAppbFATIH.retrofit.APIClient;
import com.example.TAppbFATIH.retrofit.APIService;
import com.example.TAppbFATIH.retrofit.modelResponseData.StatusResponse;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    EditText mUsernameEdit, mPasswordEdit;
    Button mLoginButton, mRegistrasiButton;
    private final static int RC_SIGN_IN = 2;
    LinearLayout googleBtn,facebookBtn;
    LoginButton login_button;
    private FirebaseAuth mAuth;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    public static final String session = "session";
    private ProgressDialog mDialog;
    private CallbackManager callbackManager;
    GoogleSignInClient mGoogleSignInClient;
    private static final String EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUsernameEdit = (EditText) findViewById(R.id.editUsername);
        mPasswordEdit = (EditText) findViewById(R.id.editPassword);
        mLoginButton = (Button) findViewById(R.id.btnLogin);
        mRegistrasiButton = (Button) findViewById(R.id.btnRegistrasi);
        googleBtn = findViewById(R.id.googleBtn);
        mDialog = new ProgressDialog(this);
//        facebookBtn = findViewById(R.id.facebookBtn);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("37721388676-0uh7tr2h38t3vdb70vr2cuera0ftnh9c.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();

//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                signIn();
            }
        });
        login_button = (LoginButton) findViewById(R.id.login_button);
        login_button.setReadPermissions(Arrays.asList("email"));
        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
//                loginResult.getAccessToken();
                Log.e("sukses","sukses");
            }

            @Override
            public void onCancel() {
                Log.e("cancel","cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("eror","eror");
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String user = mUsernameEdit.getText().toString();
//                String pass = mPasswordEdit.getText().toString();
//                pref = getSharedPreferences(session, MODE_PRIVATE);
//                boolean username = user.equals(pref.getString("username",
//                        "null"));
//                boolean password = pass.equals(pref.getString("password",
//                        "null"));
//                if(username && password)
//                {
//                    Intent moveHalamanUtama = new Intent(MainActivity.this, Dashboard.class);
//                    startActivity(moveHalamanUtama);
//                }else{
//                    Toast.makeText(MainActivity.this, "Username dan Password SALAH !!!", Toast.LENGTH_SHORT).show();
//                }
                if (mUsernameEdit.getText().toString().isEmpty()){
                    mUsernameEdit.setError("Username harus diisi");
                }else if (mUsernameEdit.getText().toString().isEmpty()){
                    mUsernameEdit.setError("Password harus diisi");
                }else {
                    loginWithForm();
                }
            }
        });
        mRegistrasiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveRegis = new Intent(MainActivity.this,regis.class);
                startActivity(moveRegis);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            firebaseAuthWithGoogle(task);
        }
    }

    public void loginWithForm(){
        InitDatabase initDatabase = new InitDatabase(MainActivity.this);
        mDialog.setMessage("Please wait...");
        mDialog.show();
        APIService service = APIClient.getClient().create(APIService.class);
        Call<StatusResponse> res = service.login(mUsernameEdit.getText().toString(),mPasswordEdit.getText().toString());
        res.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus()==1){
                        Toast.makeText(MainActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                        if (initDatabase.getTotalInit()>0){
                            initDatabase.delete("user");
                        }
                        if (initDatabase.insert(response.body().getData().get(0))>0){
                            Intent moveHalamanUtama = new Intent(MainActivity.this, DashboardList.class);
                            startActivity(moveHalamanUtama);
                        }else {
                            Toast.makeText(MainActivity.this, "gagal simpan data lokal", Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(MainActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
                mDialog.dismiss();
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "eror connection", Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void token(){
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
    }

    private void firebaseAuthWithGoogle(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
//            Log.e("TAG", account.getDisplayName());
//            Log.e("TAG", account.getEmail());
//            Log.e("TAG", account.getIdToken());
            loginWithGoogle(account.getEmail(),account.getDisplayName(),account.getIdToken());
            // Signed in successfully, show authenticated UI.

        } catch (ApiException e) {
            Toast.makeText(MainActivity.this, "signInResult:failed code=" + e.getStatusCode(), Toast.LENGTH_SHORT).show();
            Log.e("Eror", "signInResult:failed code=" + e.getStatusCode());
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
//            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());

        }
    }

    public void loginWithGoogle(String email, String displayname,String token){
        InitDatabase initDatabase = new InitDatabase(MainActivity.this);
        mDialog.setMessage("Please wait...");
        mDialog.show();
        APIService service = APIClient.getClient().create(APIService.class);
        Call<StatusResponse> res = service.registerGoogle("",displayname,"",email,"google",token,"","");
        res.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus()==1){
                        Toast.makeText(MainActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                        Log.e("database",""+initDatabase.getTotalInit());
                        if (initDatabase.getTotalInit()>0){
                            initDatabase.delete("user");
                        }
                        if (initDatabase.insert(response.body().getData().get(0))>0){
                            Intent moveHalamanUtama = new Intent(MainActivity.this, DashboardList.class);
                            startActivity(moveHalamanUtama);
                        }else {
                            Toast.makeText(MainActivity.this, "gagal simpan data lokal", Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(MainActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
                mDialog.dismiss();
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "eror connection", Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }
        });
    }

    private void handlefaceBookAccesToken(final AccessToken accessToken) {
        Log.e("Accestoken", "handlefaceBookAccesToken: " + accessToken);
        final AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d("TAG", "signWithCredentialIs: success ");
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            user.getIdToken(true)
                                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                                            if (task.isSuccessful()) {
                                                String idToken = task.getResult().getToken();
//                                                Gtoken = idToken;
                                            }
                                        }
                                    });
                            user.linkWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                                    DatabaseReference userRecord =
//                                            FirebaseDatabase.getInstance().getReference()
//                                                    .child("Users")
//                                                    .child(user.getUid());
//                                    userRecord.child("last_signin_at").setValue(ServerValue.TIMESTAMP);

                                }
                            });

//                            updateUI(user);

                        } else {
//                            Log.w(TAG_FB, "signInWithCredential:failure", task.getException());
//                            Toast.makeText(LoginActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                    }
                });


    }

    private void loginFaceBook() {
        Log.e("login","clicked");
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
//                handlefaceBookAccesToken(loginResult.getAccessToken());
//                setFacebookData(loginResult);


            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("eror","eror");
                if (error instanceof FacebookAuthorizationException) {
                    if (AccessToken.getCurrentAccessToken() != null) {
                        LoginManager.getInstance().logOut();
                    }
                }
                error.printStackTrace();
            }
        });

    }

//    private void setFacebookData(final LoginResult loginResult) {
//        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
//                new GraphRequest.GraphJSONObjectCallback() {
//                    @Override
//                    public void onCompleted(JSONObject object, GraphResponse response) {
//                        try {
//
//                            Log.e("facebook",response.getJSONObject().getString("email"));
//                            Log.e("facebook",response.getJSONObject().getString("name"));
////                            Log.i(TAG_FB, response.toString());
////                            Gemail = response.getJSONObject().getString("email");
////                            Gnama = response.getJSONObject().getString("name");
////                            Gprovider = "Facebook";
////
////                            Profile profile = Profile.getCurrentProfile();
////                            // Guid = profile.getId();
////                            Gphoto = "https://graph.facebook.com/" + profile.getId() + "/picture?width=400";
////                            // Gphoto = profile.getLinkUri().toString();
////                            Log.i("LINK ::===>>>>", Gphoto);
////                            if (Profile.getCurrentProfile() != null) {
////                                Log.i("Login", "ProfilePic" + Profile.getCurrentProfile().getProfilePictureUri(200, 200));
////                            }
////                            Log.i("Login" + "Email", Gemail);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//        Bundle parameters = new Bundle();
//        parameters.putString("fields", "id,email,first_name,last_name,gender, name, picture");
//        request.setParameters(parameters);
//        request.executeAsync();
//
////        goSignUpData("0000");
//    }


//    @Override
//    public boolean onCreateOptionsMenu(android.view.Menu menu) {
//        getMenuInflater().inflate(R.menu.main_menu,menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item){
//        int id = item.getItemId();
//        switch (id)
//        {
//            case R.id.id_about:
//                Intent intent = new Intent(MainActivity.this, Menu.class);
//                startActivity(intent);
//                Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }


}
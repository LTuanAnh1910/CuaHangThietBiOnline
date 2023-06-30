package Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cuahangthietbionline.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import until.Server;

public class DangNhapActivity extends AppCompatActivity {

    TextView txtdangki;
    private EditText edtEmail, edtPassword;
    private Button btnLogin;
    private String apiUrl = Server.Dangnhap;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        initView();
        initControll();
    }

    private void initControll() {
        txtdangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DangKiActivity.class);
                startActivity(intent);
            }
        });
        // Xử lý sự kiện khi nhấn nút Đăng nhập
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy thông tin từ các trường nhập liệu
                final String email = edtEmail.getText().toString().trim();
                final String password = edtPassword.getText().toString();

                // Kiểm tra xem các trường có được nhập đầy đủ hay không
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(DangNhapActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Tạo yêu cầu HTTP POST đến API đăng nhập
                StringRequest stringRequest = new StringRequest(Request.Method.POST, apiUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");
                                    String message = jsonObject.getString("message");

                                    if (success) {
                                        // Đăng nhập thành công, chuyển sang màn hình chính
                                        Toast.makeText(DangNhapActivity.this, message, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(DangNhapActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // Đăng nhập thất bại, hiển thị thông báo lỗi
                                        Toast.makeText(DangNhapActivity.this, message, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(DangNhapActivity.this, "Đã xảy ra lỗi trong quá trình gửi yêu cầu", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("username", email);
                        params.put("password", password);
                        return params;
                    }
                };

                // Thêm yêu cầu vào hàng đợi RequestQueue
                RequestQueue requestQueue = Volley.newRequestQueue(DangNhapActivity.this);
                requestQueue.add(stringRequest);
            }
        });

    }

    private void initView() {
        txtdangki = findViewById(R.id.txtdangki);
        // Ánh xạ các view từ layout
        edtEmail = findViewById(R.id.email);
        edtPassword = findViewById(R.id.pass);
        btnLogin = findViewById(R.id.btndangnhap);
    }
}
package Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class DangKiActivity extends AppCompatActivity {

    private EditText edtEmail, edtUsername, edtPassword, edtConfirmPassword, edtMobile;
    private Button btnRegister;
    private String apiUrl = Server.Dangki;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ki);

        // Ánh xạ các view từ layout
//        edtEmail = findViewById(R.id.email);
        edtUsername = findViewById(R.id.username);
        edtPassword = findViewById(R.id.pass);
        edtConfirmPassword = findViewById(R.id.repass);
//        edtMobile = findViewById(R.id.mobile);
        btnRegister = findViewById(R.id.btndangki);

        // Xử lý sự kiện khi nhấn nút Đăng ký
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy thông tin từ các trường nhập liệu
//                final String email = edtEmail.getText().toString().trim();
                final String username = edtUsername.getText().toString().trim();
                final String password = edtPassword.getText().toString();
                String confirmPassword = edtConfirmPassword.getText().toString();
//                final String mobile = edtMobile.getText().toString().trim();

                // Kiểm tra xem các trường có được nhập đầy đủ hay không
                if ( username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ) {
                    Toast.makeText(DangKiActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kiểm tra xem mật khẩu và xác nhận mật khẩu có khớp nhau hay không
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(DangKiActivity.this, "Mật khẩu và Xác nhận mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Tạo yêu cầu HTTP POST đến API
                StringRequest stringRequest = new StringRequest(Request.Method.POST, apiUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("status");
                                    String message = jsonObject.getString("message");

                                    if (status.equals("success")) {
                                        Toast.makeText(DangKiActivity.this, message, Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(DangKiActivity.this, message, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(DangKiActivity.this, "Đã xảy ra lỗi trong quá trình gửi yêu cầu", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
//                        params.put("email", email);
                        params.put("username", username);
                        params.put("password", password);
//                        params.put("mobile", mobile);
                        return params;
                    }
                };

                // Thêm yêu cầu vào hàng đợi RequestQueue
                RequestQueue requestQueue = Volley.newRequestQueue(DangKiActivity.this);
                requestQueue.add(stringRequest);
            }
        });
    }
}


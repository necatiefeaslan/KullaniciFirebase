package tr.com.necatiefeaslan.kullanicifirebase

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import tr.com.necatiefeaslan.kullanicifirebase.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance() // FirebaseAuth nesnesini başlatıyoruz

        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, DetayActivity::class.java))
            finish() // MainActivity'yi sonlandırıyoruz
        }

        // Giriş butonuna tıklama işlemi
        binding.btnLogin.setOnClickListener {
            val eposta = binding.etEmail.text.toString()
            val sifre = binding.etPassword.text.toString()

            // Alanları kontrol etme
            if (eposta.isEmpty() || sifre.isEmpty()) {
                Toast.makeText(this, "Boş alanları doldurunuz", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Firebase Auth ile giriş işlemi
            auth.signInWithEmailAndPassword(eposta, sifre)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Giriş başarılıysa DetayActivity'ye geçiş yapıyoruz
                        Toast.makeText(this, "Giriş Başarılı", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, DetayActivity::class.java)
                        startActivity(intent)
                        finish() // MainActivity'yi sonlandırıyoruz
                    } else {
                        // Giriş başarısızsa hata mesajı gösteriyoruz
                        Toast.makeText(this, "Giriş Başarısız", Toast.LENGTH_LONG).show()
                        Toast.makeText(
                            baseContext,
                            "Authentication failed: ${task.exception}",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }

        // Kayıt butonuna tıklama işlemi
        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, KayitActivity::class.java))
        }
    }
}


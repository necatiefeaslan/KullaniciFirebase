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

        binding.btnLogin.setOnClickListener {
            val eposta = binding.etEmail.text.toString()
            val sifre = binding.etPassword.text.toString()

            if(eposta.isEmpty() || sifre.isEmpty()){
                Toast.makeText(this, "Boş alanları doldurunuz", Toast.LENGTH_LONG).show()

            }

            auth.signInWithEmailAndPassword(eposta, sifre)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                       Toast.makeText(this, "Giriş Başarılı", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, DetayActivity::class.java)
                        startActivity(intent)

                    } else {
                        Toast.makeText(this, "Giriş Başarısız", Toast.LENGTH_LONG).show()
                        Toast.makeText(
                            baseContext,
                            "Authentication failed."+task.exception,
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }
        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, KayitActivity::class.java))
        }

    }
}

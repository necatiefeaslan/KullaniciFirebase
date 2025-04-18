package tr.com.necatiefeaslan.kullanicifirebase

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import tr.com.necatiefeaslan.kullanicifirebase.databinding.ActivityDetayBinding

class DetayActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetayBinding
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        val currentUser = auth.currentUser

        // Kullanıcı e-posta bilgilerini alıyoruz
        binding.tvEmail.text = currentUser?.email.toString()

        // Firestore'dan kullanıcı bilgilerini alıyoruz
        val userId = currentUser?.uid
        if (userId != null) {
            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val eposta = document.getString("email")
                        val ad = document.getString("ad")
                        val soyad = document.getString("soyad")
                        val ogrenciNo = document.getString("ogrenciNo")

                        // Ad, Soyad ve Öğrenci Numarasını TextView'lere atıyoruz
                        binding.tvEmail.text = eposta
                        binding.tvName.text = ad
                        binding.tvSurname.text = soyad
                        binding.tvStudentNo.text = ogrenciNo
                    } else {
                        Toast.makeText(this, "Kullanıcı bilgileri bulunamadı", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Veritabanı hatası", Toast.LENGTH_SHORT).show()
                }
        }

        // Çıkış yapma işlemi
        binding.btnExit.setOnClickListener {
            if (currentUser != null) {
                auth.signOut()
                Toast.makeText(this, "Çıkış Yapıldı", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish() // Aktiviteleri bitiriyoruz
            }
        }
    }
}



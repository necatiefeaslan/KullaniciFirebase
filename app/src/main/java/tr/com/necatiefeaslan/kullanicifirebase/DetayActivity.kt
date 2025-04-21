package tr.com.necatiefeaslan.kullanicifirebase

import android.content.Intent
import android.os.Bundle
import android.view.View
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
        binding.textView.text = currentUser?.email.toString()

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
                        binding.textView.text = eposta
                        binding.textView2.text = ad
                        binding.textView3.text = soyad
                        binding.textView4.text = ogrenciNo
                    } else {
                        Toast.makeText(this, "Kullanıcı bilgileri bulunamadı", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Veritabanı hatası", Toast.LENGTH_SHORT).show()
                }
        }

        // Çıkış yapma işlemi
        binding.button4.setOnClickListener {
            if (currentUser != null) {
                auth.signOut()
                Toast.makeText(this, "Çıkış Yapıldı", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish() // Aktiviteleri bitiriyoruz
            }
        }
    }
    fun onClickDeleteAccount(view: View) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Firestore'dan kullanıcı verilerini sil
            val userId = currentUser.uid
            val db = Firebase.firestore

            db.collection("users").document(userId)
                .delete()
                .addOnSuccessListener {
                    // Firestore'dan silme başarılı, şimdi Firebase Auth'dan kullanıcıyı sil
                    currentUser.delete()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Hesap başarıyla silindi", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this, "Hesap silinirken bir hata oluştu: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Veri silinirken bir hata oluştu: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Kullanıcı oturum açmamış", Toast.LENGTH_SHORT).show()
        }
    }

    fun onClickUpdateAccount(view: View) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val db = Firebase.firestore

            // EditText'lerden güncel verileri al
            val yeniParola = binding.editTextParola.text.toString()
            val yeniTamAd = binding.editTextTamAd.text.toString()
            val yeniOgrenciNo = binding.editTextOgrenciNo.text.toString()

            // Güncellenecek verileri bir map'te topla
            val updates = hashMapOf<String, Any>()
            if (yeniParola.isNotEmpty()) updates["parola"] = yeniParola
            if (yeniTamAd.isNotEmpty()) updates["tamad"] = yeniTamAd
            if (yeniOgrenciNo.isNotEmpty()) updates["ogrenciNo"] = yeniOgrenciNo

            // Firestore'da güncelleme yap
            db.collection("users").document(userId)
                .update(updates)
                .addOnSuccessListener {
                    Toast.makeText(this, "Bilgiler başarıyla güncellendi", Toast.LENGTH_SHORT).show()

                    // Eğer parola güncellendiyse, Firebase Auth'da da güncelle
                    if (yeniParola.isNotEmpty()) {
                        currentUser.updatePassword(yeniParola)
                            .addOnCompleteListener { task ->
                                if (!task.isSuccessful) {
                                    Toast.makeText(this, "Parola güncellenirken hata oluştu", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }

                    // Güncellenmiş verileri göster
                    refreshUserData()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Güncelleme sırasında hata oluştu: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Kullanıcı oturum açmamış", Toast.LENGTH_SHORT).show()
        }
    }

    private fun refreshUserData() {
        val currentUser = auth.currentUser
        val db = Firebase.firestore
        val userId = currentUser?.uid

        if (userId != null) {
            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val eposta = document.getString("eposta")
                        val parola = document.getString("parola")
                        val tamad = document.getString("tamad")
                        val ogrenciNo = document.getString("ogrenciNo")

                        binding.textView.text = eposta
                        binding.textView2.text = parola
                        binding.textView3.text = tamad
                        binding.textView4.text = ogrenciNo
                    }
                }
        }
    }
}




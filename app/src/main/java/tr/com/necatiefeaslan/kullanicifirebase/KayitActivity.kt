package tr.com.necatiefeaslan.kullanicifirebase

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import tr.com.necatiefeaslan.kullanicifirebase.databinding.ActivityKayitBinding

class KayitActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKayitBinding
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKayitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth


        binding.btnCompleteRegister.setOnClickListener {
            val eposta = binding.etRegisterEmail.text.toString()
            val sifre = binding.etRegisterPassword.text.toString()
            val ad = binding.etName.text.toString()
            val soyad = binding.etSurname.text.toString()
            val ogrenciNo = binding.etStudentNo.text.toString()

            auth.createUserWithEmailAndPassword(eposta, sifre)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid
                        val userMap = hashMapOf(
                            "email" to eposta,
                            "ad" to ad,
                            "soyad" to soyad,
                            "ogrenciNo" to ogrenciNo
                        )

                        if (userId != null) {
                            db.collection("users").document(userId).set(userMap)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Kayıt Başarılı", Toast.LENGTH_LONG).show()
                                    startActivity(Intent(this, MainActivity::class.java))
                                    finish()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(
                                        this,
                                        "Firestore kaydı başarısız.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                    } else {
                        Toast.makeText(
                            baseContext,
                            "Authentication failed." + task.exception,
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }
    }
}


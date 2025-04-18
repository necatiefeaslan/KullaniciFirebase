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
        val db = Firebase.firestore

        binding.tvEmail.text = currentUser?.email.toString()
        binding.tvName.text = currentUser?.displayName.toString()
        binding.tvSurname.text = currentUser?.displayName.toString()
        binding.tvStudentNo.text = currentUser?.displayName.toString()

        binding.btnExit.setOnClickListener {
            if (currentUser != null) {
                auth.signOut()
                Toast.makeText(this, "Çıkış Yapıldı", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, MainActivity::class.java))
            }
        }

    }

  }


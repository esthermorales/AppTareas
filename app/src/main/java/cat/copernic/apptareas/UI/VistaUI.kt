package cat.copernic.apptareas.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.Navigation
import cat.copernic.apptareas.MainActivity
import cat.copernic.apptareas.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class VistaUI : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vista_ui)
    }

    override fun onCreateOptionsMenu(menuu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menuu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var id = item.itemId

        if (id == R.id.cierreSesion) {
            Firebase.auth.signOut()
            val toInit = Intent(this, MainActivity::class.java)
            startActivity(toInit)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.fragmentContainerView)
            .navigateUp() || super.onSupportNavigateUp()
    }
}
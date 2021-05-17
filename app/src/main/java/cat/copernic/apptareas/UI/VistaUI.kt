package cat.copernic.apptareas.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import cat.copernic.apptareas.R

class VistaUI : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vista_ui)
    }

    override fun onCreateOptionsMenu(menuu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menuu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var id = item.itemId

        if (id == R.id.verUsuario){

        }
        return super.onOptionsItemSelected(item)
    }
}
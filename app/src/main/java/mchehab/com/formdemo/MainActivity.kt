package mchehab.com.formdemo

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.WindowManager
import android.widget.Button
import android.widget.CheckedTextView
import android.widget.EditText
import android.widget.Spinner

class MainActivity : AppCompatActivity() {

    private val editTextName: EditText by lazy{
        findViewById<EditText>(R.id.editTextName)
    }
    private val editTextEmail: EditText by lazy{
        findViewById<EditText>(R.id.editTextEmail)
    }
    private val editTextPhone: EditText by lazy{
        findViewById<EditText>(R.id.editTextPhone)
    }

    private val spinnerPizzaSize: Spinner by lazy{
        findViewById<Spinner>(R.id.spinnerToppings)
    }

    private val checkboxBacon: CheckedTextView by lazy{
        findViewById<CheckedTextView>(R.id.checkboxBacon)
    }
    private val checkboxExtraCheese: CheckedTextView by lazy{
        findViewById<CheckedTextView>(R.id.checkboxExtraCheese)
    }
    private val checkboxOnion: CheckedTextView by lazy{
        findViewById<CheckedTextView>(R.id.checkboxOnion)
    }
    private val checkboxMushroom: CheckedTextView by lazy{
        findViewById<CheckedTextView>(R.id.checkboxMushroom)
    }

    private val editTextTime: EditText by lazy{
        findViewById<EditText>(R.id.editTextTime)
    }
    private val editTextDelivery: EditText by lazy{
        findViewById<EditText>(R.id.editTextDelivery)
    }

    private val buttonPost: Button by lazy{
        findViewById<Button>(R.id.button)
    }

    val editText: EditText by lazy {
        findViewById<EditText>(R.id.editTextDelivery)
    }

    private var isJSONPosting = false
    private val broadcastReceiverPost = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val bundle = intent.extras
            if (bundle != null) {
                val result = bundle.getString("result")
                AlertDialog.Builder(this@MainActivity)
                        .setTitle("Result")
                        .setMessage(result)
                        .setPositiveButton("Ok", null)
                        .create()
                        .show()
                isJSONPosting = false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiverPost, IntentFilter(BroadcastConstants.JSON_POST))
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        checkboxExtraCheese.setOnClickListener { e -> checkboxExtraCheese.toggle() }
        checkboxBacon.setOnClickListener { e -> checkboxBacon.toggle() }
        checkboxMushroom.setOnClickListener { e -> checkboxMushroom.toggle() }
        checkboxOnion.setOnClickListener { e -> checkboxOnion.toggle() }

        if (savedInstanceState != null) {
            checkboxBacon.isChecked = savedInstanceState.getBoolean("checkboxBacon")
            checkboxExtraCheese.isChecked = savedInstanceState.getBoolean("checkboxExtraCheese")
            checkboxOnion.isChecked = savedInstanceState.getBoolean("checkboxOnion")
            checkboxMushroom.isChecked = savedInstanceState.getBoolean("checkboxMushroom")
        }
    }

    override fun onSaveInstanceState(bundle: Bundle?) {
        super.onSaveInstanceState(bundle)
        bundle?.putBoolean("checkboxBacon", checkboxBacon.isChecked)
        bundle?.putBoolean("checkboxExtraCheese", checkboxExtraCheese.isChecked)
        bundle?.putBoolean("checkboxOnion", checkboxOnion.isChecked)
        bundle?.putBoolean("checkboxMushroom", checkboxMushroom.isChecked)
    }

}
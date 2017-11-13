package mchehab.com.formdemo

import kotlinx.android.synthetic.main.activity_main.*

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.WindowManager
import android.widget.CheckedTextView
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity() {

    private val listCheckedTextView: MutableList<CheckedTextView> by lazy {
        mutableListOf(checkboxOnion, checkboxBacon, checkboxExtraCheese, checkboxMushroom)
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
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiverPost, IntentFilter
        (BroadcastConstants.JSON_POST))
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiverPost)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        listCheckedTextView.forEach { it.setOnClickListener{ _ -> it.toggle() } }

        if (savedInstanceState != null)
            listCheckedTextView.forEach { it.isChecked =  savedInstanceState.getBoolean(it.text.toString())}

        setButtonOnClickListener()
    }

    private fun setButtonOnClickListener() {
        buttonPost.setOnClickListener { e ->
            try {
                if(!isFormValid()){
                    return@setOnClickListener;
                }
                val jsonObject = JSONObject()
                val jsonArrayToppings = JSONArray()

                val jsonObjectForm = JSONObject()
                jsonObject.put("custname", editTextName.text.toString())
                jsonObject.put("custemail", editTextEmail.text.toString())
                jsonObject.put("custtel", editTextPhone.text.toString())
                jsonObject.put("size", spinnerPizzaSize.selectedItem)
                jsonObject.put("delivery", editTextTime.text.toString())
                jsonObject.put("comments", editTextDelivery.text.toString())

                listCheckedTextView.forEach { if(it.isChecked) jsonArrayToppings.put(it.text.toString()) }

                if (jsonArrayToppings.length() > 0)
                    jsonObject.put("toppings", jsonArrayToppings)

                jsonObjectForm.put("form", jsonObject)

                HttpAsyncTask(WeakReference(this), BroadcastConstants
                        .JSON_POST, HTTP.POST, jsonObjectForm.toString())
                        .execute("http://httpbin.org/post")
                isJSONPosting = true
            } catch (jsonException: JSONException) {
                jsonException.printStackTrace()
            }
        }
    }

    private fun isFormValid(): Boolean {
        if (!isValidName(editTextName.text.toString())) {
            editTextName.error = "Invalid name"
            return false
        }
        if (!isValidPhone(editTextPhone.text.toString())) {
            editTextPhone.error = "Invalid phone"
            return false
        }
        if (!isValidEmail(editTextEmail.text.toString())) {
            editTextEmail.error = "Invalid email address"
            return false
        }
        if (editTextTime.text.toString().length == 0) {
            editTextTime.error = "Invalid time"
            return false
        } else {
            editTextTime.error = null
        }
        return true
    }

    private fun isValidPhone(phone: String): Boolean {
        return android.util.Patterns.PHONE.matcher(phone).matches()
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidName(name: String): Boolean {
        val regex = "^[\\p{L} .'-]+$"
        return name.matches(regex.toRegex())
    }

    override fun onSaveInstanceState(bundle: Bundle?) {
        super.onSaveInstanceState(bundle)
        listCheckedTextView.forEach { bundle?.putBoolean(it.text.toString(), it.isChecked) }
    }
}
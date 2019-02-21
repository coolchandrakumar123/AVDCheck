package com.zoho.desk.avdcheck

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.util.TypedValue
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        startAnim()

        fab.setOnClickListener { view ->
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG) .setAction("Action", null).show()
            openTicketInRadar(648638721, 196608000004495117L)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when(item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun startAnim() {
        val animated = AnimatedVectorDrawableCompat.create(this, R.drawable.deeplink_loader)
        animated?.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
            override fun onAnimationEnd(drawable: Drawable?) {
                imageLoader.post { animated.start() }
            }

        })
        imageLoader.setImageDrawable(animated)
        animated?.start()
    }

    private fun openTicketInRadar(orgId: Long, ticketId: Long) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("zohodeskradar://?orgId=$orgId&ticketId=$ticketId"))
        val activities = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        val isIntentSafe = activities.size > 0
        if (isIntentSafe) {
            startActivity(intent)
            overridePendingTransition(0, 0)
        } else {
            val builder = AlertDialog.Builder(this)
            builder.setPositiveButton("Ok", null)
            builder.setView(TextView(this).apply {
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                val string = SpannableString("Download Radar from PlayStore\n\nhttps://play.google.com/store/apps/details?id=com.zoho.desk.radar")  // No I18N
                Linkify.addLinks(string, Linkify.WEB_URLS)
                setPadding(16, 16, 16, 16)
                text = string
                movementMethod = LinkMovementMethod.getInstance()
            })
            builder.setCancelable(true)
            builder.show()
        }
    }
}

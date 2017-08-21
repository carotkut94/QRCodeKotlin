package com.death.movieticket

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.annotations.SerializedName

import org.json.JSONObject

class TicketResultActivity : AppCompatActivity() {

    private var txtName: TextView? = null
    private var txtDuration: TextView? = null
    private var txtDirector: TextView? = null
    private var txtGenre: TextView? = null
    private var txtRating: TextView? = null
    private var txtPrice: TextView? = null
    private var txtError: TextView? = null
    private var imgPoster: ImageView? = null
    private var btnBuy: Button? = null
    private var progressBar: ProgressBar? = null
    private var ticketView: TicketView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_result)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        txtName = findViewById<TextView>(R.id.name)
        txtDirector = findViewById<TextView>(R.id.director)
        txtDuration = findViewById<TextView>(R.id.duration)
        txtPrice = findViewById<TextView>(R.id.price)
        txtRating = findViewById<TextView>(R.id.rating)
        imgPoster = findViewById<ImageView>(R.id.poster)
        txtGenre = findViewById<TextView>(R.id.genre)
        btnBuy = findViewById<Button>(R.id.btn_buy)
        imgPoster = findViewById<ImageView>(R.id.poster)
        txtError = findViewById<TextView>(R.id.txt_error)
        ticketView = findViewById<TicketView>(R.id.layout_ticket)
        progressBar = findViewById<ProgressBar>(R.id.progressBar)

        val barcode = intent.getStringExtra("code")

        // close the activity in case of empty barcode
        if (TextUtils.isEmpty(barcode)) {
            Toast.makeText(applicationContext, "Barcode is empty!", Toast.LENGTH_LONG).show()
            finish()
        }
        Log.e("Barcode", barcode)
        // search the barcode
        searchBarcode(barcode)
    }

    /**
     * Searches the barcode by making http call
     * Request was made using Volley network library but the library is
     * not suggested in production, consider using Retrofit
     */
    private fun searchBarcode(barcode: String) {
        // making volley's json request
        Log.e("URL TO HIT", URL + barcode)
        val jsonObjReq = JsonObjectRequest(Request.Method.GET,
                URL + barcode, null, Response.Listener { response ->
            Log.e("Response", response.toString())
            if(response.has("error"))
            {
                showNoTicket()
            }else{
                renderMovie(response)
            }
        }, Response.ErrorListener { error ->
            if(error!=null)
                showNoTicket()
            else
                showNoTicket()
        })

        MyApp.instance?.addToRequestQueue(jsonObjReq)
    }

    private fun showNoTicket() {
        txtError!!.visibility = View.VISIBLE
        ticketView!!.visibility = View.GONE
        progressBar!!.visibility = View.GONE
    }

    /**
     * Rendering movie details on the ticket
     */
    private fun renderMovie(response: JSONObject) {
        try {

            // converting json to movie object
            val movie = Gson().fromJson(response.toString(), Movie::class.java)
            Log.d("Data", movie.toString())
            if (movie != null) {
                txtName?.text = movie.name
                txtDirector?.text = movie.director
                txtDuration?.text = movie.duration
                txtGenre?.text = movie.genre
                txtRating?.text = "" + movie.rating
                txtPrice?.text = movie.price
                Glide.with(this).load(movie.poster).into(imgPoster)

                if (movie.isReleased) {
                    btnBuy?.text = getString(R.string.btn_buy_now)
                    btnBuy?.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
                } else {
                    btnBuy?.text = getString(R.string.btn_coming_soon)
                    btnBuy?.setTextColor(ContextCompat.getColor(this, R.color.btn_disabled))
                }
                ticketView?.visibility = View.VISIBLE
                progressBar?.visibility = View.GONE
            } else {
                // movie not found
                showNoTicket()
            }
        } catch (e: JsonSyntaxException) {
            Log.e(TAG, "JSON Exception: " + e.message)
            showNoTicket()
            Toast.makeText(applicationContext, "Error occurred. Check your LogCat for full report", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            // exception
            showNoTicket()
            Toast.makeText(applicationContext, "Error occurred. Check your LogCat for full report", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private inner class Movie {
        var name: String? = null
            internal set
        var director: String? = null
            internal set
        var poster: String? = null
            internal set
        var duration: String? = null
            internal set
        var genre: String? = null
            internal set
        var price: String? = null
            internal set
        var rating: Float = 0.toFloat()
            internal set

        @SerializedName("released")
        var isReleased: Boolean = false
            internal set
    }

    companion object {
        private val TAG = TicketResultActivity::class.java.simpleName

        // url to search barcode
        private val URL = "https://api.androidhive.info/barcodes/search.php?code="
    }
}
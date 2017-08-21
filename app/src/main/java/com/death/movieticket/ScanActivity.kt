package com.death.movieticket

/**
 * Created by sidhantrajora on 21/08/17.
 * REF: Ravi Tamada's code from https://www.androidhive.info/2017/08/android-barcode-scanner-using-google-mobile-vision-building-movie-tickets-app/
 */

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.SparseArray
import android.widget.Toast

import com.google.android.gms.vision.barcode.Barcode


import info.androidhive.barcode.BarcodeReader

class ScanActivity : AppCompatActivity(), BarcodeReader.BarcodeReaderListener {
    lateinit var  barcodeReader : BarcodeReader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
        barcodeReader = supportFragmentManager.findFragmentById(R.id.barcode_scanner) as BarcodeReader
    }

    override fun onScannedMultiple(barcodes: MutableList<Barcode>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onScanned(barcode: Barcode?) {
        barcodeReader.playBeep()
        val intent = Intent(this@ScanActivity,TicketResultActivity::class.java)
        intent.putExtra("code", barcode?.displayValue)
        startActivity(intent)
    }

    override fun onScanError(errorMessage: String?) {
        Toast.makeText(this,"Scan error"+errorMessage,Toast.LENGTH_SHORT).show()
    }

    override fun onBitmapScanned(sparseArray: SparseArray<Barcode>?) {

    }
}

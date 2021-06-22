package com.appyhigh.converterproject

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.pdftron.pdf.PDFViewCtrl
import com.pdftron.pdf.controls.DocumentActivity
import com.pdftron.pdf.utils.AppUtils

class MainActivity : AppCompatActivity() {

    lateinit var pdfViewCtrl: PDFViewCtrl

    private val pickDocumentFile = registerForActivityResult(ActivityResultContracts.OpenDocument()) {
        //DocumentActivity.openDocument(this, it)
        pdfViewCtrl.openNonPDFUri(it , null)
    }

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted)
                pickDocumentFile.launch(arrayOf("application/pdf", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation"))
            else {
                Toast.makeText(this, "This permission is necessary, exiting...", Toast.LENGTH_LONG).show()
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pdfViewCtrl = findViewById(R.id.pdfViewCtrl)
        AppUtils.setupPDFViewCtrl(pdfViewCtrl)

        findViewById<Button>(R.id.button).setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                pickDocumentFile.launch(arrayOf("application/pdf", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation"))
            else
                requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    override fun onPause() {
        super.onPause()
        pdfViewCtrl.pause()
        pdfViewCtrl.purgeMemory()
    }

    override fun onResume() {
        super.onResume()
        pdfViewCtrl.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        pdfViewCtrl.destroy()
    }
}
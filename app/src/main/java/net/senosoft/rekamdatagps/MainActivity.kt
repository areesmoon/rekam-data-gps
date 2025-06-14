package net.senosoft.rekamdatagps

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

import net.senosoft.rekamdatagps.data.DataModel
import net.senosoft.rekamdatagps.data.DatabaseHelper
private lateinit var dbHelper: DatabaseHelper

private var currentBitmap: Bitmap? = null

class MainActivity : AppCompatActivity() {

    private lateinit var etNama: EditText
    private lateinit var etDeskripsi: EditText
    private lateinit var etAlamat: EditText
    private lateinit var ivFoto: ImageView
    private lateinit var tvLokasi: TextView
    private lateinit var btnAmbilFoto: Button
    private lateinit var btnAmbilLokasi: Button
    private lateinit var btnSimpan: Button
    private lateinit var btnLihatData: Button

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val imageBitmap: Bitmap? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    data?.getParcelableExtra("data", Bitmap::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    data?.extras?.get("data") as? Bitmap
                }

                imageBitmap?.let {
                    currentBitmap = it // ✅ Simpan bitmap yang valid
                    ivFoto.setImageBitmap(it)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)

        etNama = findViewById(R.id.etNama)
        etDeskripsi = findViewById(R.id.etDeskripsi)
        etAlamat = findViewById(R.id.etAlamat)
        ivFoto = findViewById(R.id.ivFoto)
        tvLokasi = findViewById(R.id.tvLokasi)
        btnAmbilFoto = findViewById(R.id.btnAmbilFoto)
        btnAmbilLokasi = findViewById(R.id.btnAmbilLokasi)
        btnSimpan = findViewById(R.id.btnSimpan)
        btnLihatData = findViewById(R.id.btnLihatData)

        // Permissions
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            1
        )

        btnAmbilFoto.setOnClickListener { bukaKamera() }
        btnAmbilLokasi.setOnClickListener { ambilLokasi() }
        btnSimpan.setOnClickListener { simpanData() }
        btnLihatData.setOnClickListener {
            val intent = Intent(this, ListDataActivity::class.java)
            startActivity(intent)
        }
    }

    private fun bukaKamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(intent)
    }

    private fun ambilLokasi() {
        // Simulasi lokasi (nanti kita ganti jadi real GPS)
        latitude = -6.200000
        longitude = 106.816666
        tvLokasi.text = "Lokasi: $latitude, $longitude"
    }

    private fun simpanData() {
        val nama = etNama.text.toString()
        val deskripsi = etDeskripsi.text.toString()
        val alamat = etAlamat.text.toString()

        ivFoto.isDrawingCacheEnabled = true
        ivFoto.buildDrawingCache()

        val bitmap = currentBitmap
        if (bitmap == null) {
            Toast.makeText(this, "Foto belum diambil", Toast.LENGTH_SHORT).show()
            return
        }
        val stream = java.io.ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()

        val data = DataModel(
            nama = nama,
            deskripsi = deskripsi,
            alamat = alamat,
            latitude = latitude,
            longitude = longitude,
            foto = byteArray
        )

        val sukses = dbHelper.insertData(data)

        etNama.setText("")
        etDeskripsi.setText("")
        etAlamat.setText("")
        ivFoto.setImageResource(0)

        currentBitmap = null

        if (sukses) {
            Toast.makeText(this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show()
        }
    }
}

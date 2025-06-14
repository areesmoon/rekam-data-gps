package net.senosoft.rekamdatagps.data

data class DataModel(
    val id: Int = 0,
    val nama: String,
    val deskripsi: String,
    val alamat: String,
    val latitude: Double,
    val longitude: Double,
    val foto: ByteArray?
)

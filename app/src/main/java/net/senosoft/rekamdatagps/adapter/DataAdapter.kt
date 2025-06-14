package net.senosoft.rekamdatagps.adapter

import net.senosoft.rekamdatagps.R
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import net.senosoft.rekamdatagps.data.DataModel

class DataAdapter(private val dataList: List<DataModel>) :
    RecyclerView.Adapter<DataAdapter.DataViewHolder>() {

    class DataViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNama: TextView = view.findViewById(R.id.tvItemNama)
        val tvAlamat: TextView = view.findViewById(R.id.tvItemAlamat)
        val tvDeskripsi: TextView = view.findViewById(R.id.tvItemDeskripsi)
        val ivFoto: ImageView = view.findViewById(R.id.ivItemFoto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_data, parent, false)
        return DataViewHolder(view)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val data = dataList[position]
        holder.tvNama.text = data.nama
        holder.tvAlamat.text = data.alamat
        holder.tvDeskripsi.text = data.deskripsi

        if (data.foto != null) {
            val bitmap = BitmapFactory.decodeByteArray(data.foto, 0, data.foto.size)
            holder.ivFoto.setImageBitmap(bitmap)
        } else {
            // holder.ivFoto.setImageResource(R.drawable.placeholder_image) // Ganti dgn drawable yg ada
        }
    }

    override fun getItemCount(): Int = dataList.size
}

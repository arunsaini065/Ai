package com.rocks.ui.ratio

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rocks.ui.R
import com.rocks.ui.beGone
import com.rocks.ui.beVisible
import com.rocks.ui.databinding.CropRatioItemListBinding
import com.rocks.ui.ratio.ratiolayout.RatioDatumMode
import com.rocks.ui.setTintColor
import com.rocks.ui.toBinding

class CropRatioRecyclerView : RecyclerView {

    private val _ratioMutableList = mutableListOf<Ratio>()

    var iChangeRatioListener: IChangeRatioListener? = null

    private var selectedItem: Int = -1

    data class Ratio(val name: String?,  val width: Int,val height: Int,val src:Int=0 )

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)

    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context,
        attrs,
        defStyleAttr) {
        init(context)

    }

    fun init(context: Context) {

        _ratioMutableList.add(Ratio("1:1", 1, 1))

        _ratioMutableList.add(Ratio("4:5", 4, 5))

        _ratioMutableList.add(Ratio("4:6", 4, 6))

        _ratioMutableList.add(Ratio("3:4", 3, 4))

        _ratioMutableList.add(Ratio("4:3", 4, 3))

        _ratioMutableList.add(Ratio("5:7", 7, 5))

        _ratioMutableList.add(Ratio("10:8", 10, 8))

        _ratioMutableList.add(Ratio("4:3", 4, 3))

        _ratioMutableList.add(Ratio("16:9", 16, 9))

        _ratioMutableList.add(Ratio("8:10", 8,10))

        _ratioMutableList.add(Ratio("16:9", 16, 9))

        _ratioMutableList.add(Ratio("2:3", 2, 3))

        _ratioMutableList.add(Ratio("3:1", 3, 1))

        _ratioMutableList.add(Ratio("16:9", 16, 9))

        _ratioMutableList.add(Ratio("2:1", 2, 1))


        _ratioMutableList.add(Ratio("1:2", 1, 2))

        layoutManager = LinearLayoutManager(context, HORIZONTAL, false)

        adapter = CropRatioViewAdapter()

    }

    private inner class CropRatioViewAdapter : Adapter<CropRatioViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CropRatioViewHolder {

            return CropRatioViewHolder(parent.toBinding())

        }

        override fun onBindViewHolder(holder: CropRatioViewHolder, position: Int) {

            val item = _ratioMutableList[position]

            val ratioDatumMode: RatioDatumMode = if (item.height > item.width) {

                RatioDatumMode.DATUM_HEIGHT

            } else if (item.height < item.width) {

                RatioDatumMode.DATUM_WIDTH

            } else {

                RatioDatumMode.DATUM_AUTO
            }


            holder.mBinding.mRatioLayout.setRatio(ratioDatumMode, item.width.toFloat(), item.height.toFloat())

            holder.mBinding.name.text = item.name


            if (selectedItem == position) {

                holder.mBinding.mRatioLayout.setBackgroundResource(R.drawable.ratio_background_selected)

                holder.mBinding.name.setTextColor(ContextCompat.getColor(context,R.color.black))

                holder.mBinding.ratio.setTintColor(R.color.black)

            } else {

                holder.mBinding.mRatioLayout.setBackgroundResource(R.drawable.ratio_background_unselected)

                holder.mBinding.name.setTextColor(ContextCompat.getColor(context,R.color.white))

                holder.mBinding.ratio.setTintColor(R.color.white)


            }

            if (item.src != 0) {

                holder.mBinding.name.beGone()

            } else {

                holder.mBinding.name.beVisible()

            }

            holder.mBinding.ratio.setImageResource(item.src)

            holder.itemView.setOnClickListener {


                runCatching {

                    val i = _ratioMutableList[holder.adapterPosition]

                    iChangeRatioListener?.onChangeRatio(i.width, i.height)

                    selectedItem=holder.adapterPosition

                    adapter?.notifyDataSetChanged()

                }


            }
        }

        override fun getItemCount(): Int {

            return _ratioMutableList.size

        }


    }

    private class CropRatioViewHolder(val mBinding: CropRatioItemListBinding) : ViewHolder(mBinding.root)


    interface IChangeRatioListener {

        fun onChangeRatio(width: Int, height: Int)

    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearRatio(){
        selectedItem=-1
        adapter?.notifyDataSetChanged()
    }
    fun isRatio()=selectedItem!=-1

}
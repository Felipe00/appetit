package io.felipe.appetit.ui.new_sale.step3

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import io.felipe.appetit.R
import io.felipe.appetit.database.Database
import io.felipe.appetit.database.PrefsDb
import io.felipe.appetit.ui.new_sale.NewSaleActivity
import kotlinx.android.synthetic.main.fragment_finish_sale.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class FinishSaleFragment : Fragment() {

    private lateinit var database: Database
    private var datePicked = ""
    private var listener: OnFragmentInteractionListener? = null
    private var isPaid: Boolean? = null
    private val calendar: Calendar = Calendar.getInstance()
    private var onDateSetListener: OnDateSetListener =
        OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = PrefsDb.init(activity!!).getDatabase()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_finish_sale, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        finishSaleRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioBtnYes -> {
                    isPaid = true
                }
                R.id.radioBtnNo -> {
                    isPaid = false
                }
            }
        }

        finishSaleDateContainer.setOnClickListener {
            DatePickerDialog(
                activity!!,
                onDateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        finishSaleAmemButton.setOnClickListener {
            if (datePicked.isEmpty()) {
                Toast.makeText(
                    activity,
                    getString(R.string.finish_sale_date_error_toast),
                    Toast.LENGTH_LONG
                )
                    .show()
                return@setOnClickListener
            }
            if (isPaid == null) {
                Toast.makeText(
                    activity,
                    getString(R.string.finish_sale_is_paid_error_toast),
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            val map = HashMap<String, Any?>()
            map["isPaid"] = isPaid
            map["date"] = datePicked
            listener?.onFragmentInteraction(map, NewSaleActivity.FINISH)
        }
    }

    private fun updateLabel() {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        datePicked = sdf.format(calendar.time)
        finishSaleDateChosen.text = datePicked
        finishSaleAmemButton.alpha = 1F
        finishSaleDateChosen.setTextColor(ContextCompat.getColor(activity!!, R.color.colorBlack))
        DrawableCompat.setTint(
            DrawableCompat.wrap(finishSaleCalendarIcon.drawable),
            ContextCompat.getColor(activity!!, R.color.colorPrimary)
        )
        finishSaleCalendarIcon.alpha = 1F
        finishSaleDateContainer.background =
            ContextCompat.getDrawable(activity!!, R.drawable.outline_border_selected)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(any: Any, typ: Int)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment FinishSaleFragment.
         */
        @JvmStatic
        fun newInstance() = FinishSaleFragment().apply { }
    }
}

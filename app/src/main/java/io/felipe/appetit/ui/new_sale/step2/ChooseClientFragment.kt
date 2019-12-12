package io.felipe.appetit.ui.new_sale.step2

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import io.felipe.appetit.R
import io.felipe.appetit.database.Client
import io.felipe.appetit.database.Database
import io.felipe.appetit.database.PrefsDb
import io.felipe.appetit.ui.new_sale.NewSaleActivity
import kotlinx.android.synthetic.main.fragment_choose_client.*
import java.util.*

class ChooseClientFragment : Fragment() {

    private lateinit var clientAdapter: ListClientAdapter
    private lateinit var database: Database
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = PrefsDb.init(activity!!).getDatabase()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_client, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(chooseClientList) {
            clientAdapter = ListClientAdapter(database.clients ?: emptyList()) {
                listener?.onFragmentInteraction(it, NewSaleActivity.CLIENT)
            }
            adapter = clientAdapter
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
        }

        chooseClientSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                peformSearch(s.toString())
            }
        })
        chooseClientNextContainer.setOnClickListener {
            Toast.makeText(activity, "Chamar step 3", Toast.LENGTH_LONG).show()
            // (activity as NewSaleActivity).callClientsFragment()
        }
    }

    private fun peformSearch(text: String) {
        clientAdapter.update(database.clients?.filter {
            it.name?.toUpperCase(Locale.getDefault())?.contains(text.toUpperCase(Locale.getDefault())) == true
        })
    }

    fun updateList(clients: ArrayList<Client>) {
        clientAdapter.update(emptyList())
        clientAdapter.update(
            database.clients?.map {
                it.isSelected = it in clients
                it
            }
        )
        updateTotalClients()
    }

    private fun updateTotalClients() {
        val selectedItems = (activity as NewSaleActivity).getClientList()
        if (selectedItems.isEmpty()) {
            turnTotalClientsViewOff()
        } else {
            if (selectedItems.size == 1) {
                chooseClientTotal.text = "${selectedItems.size} cliente selecionado"
            } else {
                chooseClientTotal.text = "${selectedItems.size} clientes selecionados"
            }
            turnTotalClientsViewOn()
        }
    }

    private fun turnTotalClientsViewOn() {
        chooseClientTotalContainer.visibility = View.VISIBLE
    }

    private fun turnTotalClientsViewOff() {
        chooseClientTotalContainer.visibility = View.GONE
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
        fun onFragmentInteraction(item: Any, type: Int)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment ChooseClientFragment.
         */
        @JvmStatic
        fun newInstance() = ChooseClientFragment().apply {}
    }
}

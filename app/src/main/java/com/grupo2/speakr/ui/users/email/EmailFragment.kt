package com.grupo2.speakr.ui.users.email

import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.grupo2.speakr.R
import com.grupo2.speakr.data.MailAuth
import com.grupo2.speakr.data.PasswordAuth
import com.grupo2.speakr.data.repository.remote.RemoteUserDataSource
import com.grupo2.speakr.ui.users.password.PasswordViewModel
import com.grupo2.speakr.ui.users.password.PasswordViewModelFactory
import com.grupo2.speakr.utils.Resource

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EmailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EmailFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private val userRepository = RemoteUserDataSource()
    private val viewModel: EmailViewModel by viewModels { EmailViewModelFactory(userRepository) }
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_email, container, false)

        // Assuming you have EditText views with IDs "editText1" and "editText2"
        val editText1 = view.findViewById<EditText>(R.id.editTextCurrentEmail)
        val editText2 = view.findViewById<EditText>(R.id.editTextNewEmail)
        val editText3 = view.findViewById<EditText>(R.id.editTextConfirmEmail)
        val buttonView = view.findViewById<Button>(R.id.btnChangeEmail)

        buttonView.setOnClickListener {
            // Collect data from the EditText views
            val oldEmail = editText1.text.toString()
            val newEmail = editText2.text.toString()
            val newEmailComprob = editText3.text.toString()


            if (newEmail == newEmailComprob) {
                val emailAuth = MailAuth(oldEmail, newEmail)

                viewModel.updateMail(emailAuth)
                Log.i("password", emailAuth.oldMail )
                Log.i("password", emailAuth.newMail )

                viewModel.emails.observe(this) { result ->
                    Log.i("result", result?.status.toString())
                    Log.i("result", "Received data: ${result?.data}")
                    if (result != null) {
                        when (result.status) {
                            Resource.Status.SUCCESS -> {
                                // Handle successful password change
//                                if (result != null) {
                                Log.i("cambio", "success")
                                editText1.setText("")
                                editText2.setText("")
                                editText3.setText("")
                                Toast.makeText(requireContext(), "Email changed successfully!", Toast.LENGTH_SHORT).show()

//                                closeFragment()


//                                    }
                            }

                            Resource.Status.ERROR -> {
                                // Handle password change error
                                // Show an error toast message
                                Toast.makeText(requireContext(), "Error changing Email. Please try again.", Toast.LENGTH_SHORT).show()
                            }

                            Resource.Status.LOADING -> {
                                // Handle loading state (optional)
                                // You can show a loading indicator or perform other actions while waiting
                            }
                        }
                    }
                }
            } else {
                // Passwords do not match
                // Show a toast message indicating the mismatch

                editText3.setText("")
                Toast.makeText(
                    requireContext(),
                    "New mails do not match. Please check again.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            // Do something with the collected data
            // For example, you can log it or pass it to your ViewModel

            // viewModel.updateData(text1, text2)
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EmailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EmailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
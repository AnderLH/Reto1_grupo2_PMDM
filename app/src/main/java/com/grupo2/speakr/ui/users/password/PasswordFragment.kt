package com.grupo2.speakr.ui.users.password

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import com.grupo2.speakr.R
import com.grupo2.speakr.data.PasswordAuth
import com.grupo2.speakr.data.repository.remote.RemoteUserDataSource
import com.grupo2.speakr.ui.songs.all.HomeFragment

import com.grupo2.speakr.utils.Resource

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [PasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PasswordFragment : Fragment()  {

    private val userRepository = RemoteUserDataSource()
    private val viewModel: PasswordViewModel by viewModels { PasswordViewModelFactory(userRepository) }
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

        val view = inflater.inflate(R.layout.fragment_password, container, false)

        // Assuming you have EditText views with IDs "editText1" and "editText2"
        val editText1 = view.findViewById<EditText>(R.id.editTextCurrentPassword)
        val editText2 = view.findViewById<EditText>(R.id.editTextNewPassword)
        val editText3 = view.findViewById<EditText>(R.id.editTextConfirmPassword)
        val buttonView = view.findViewById<Button>(R.id.btnChangePassword)

        buttonView.setOnClickListener {
            // Collect data from the EditText views
            val oldPassword = editText1.text.toString()
            val newPassword = editText2.text.toString()
            val newPasswordComprob = editText3.text.toString()


            if (newPassword == newPasswordComprob) {
                val passwordAuth = PasswordAuth(oldPassword, newPassword)

                viewModel.updatePassword(passwordAuth)
                Log.i("password", passwordAuth.oldPassword )
                Log.i("password", passwordAuth.newPassword )

                viewModel.passwords.observe(this) { result ->
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
                                Toast.makeText(requireContext(), "Password changed successfully!", Toast.LENGTH_SHORT).show()

//                                closeFragment()


//                                    }
                            }

                            Resource.Status.ERROR -> {
                                // Handle password change error
                                // Show an error toast message
                                Toast.makeText(requireContext(), "Error changing password. Please try again.", Toast.LENGTH_SHORT).show()
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
                    "New passwords do not match. Please check again.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            // Do something with the collected data
            // For example, you can log it or pass it to your ViewModel

            // viewModel.updateData(text1, text2)
        }

        return view
    }

//    private fun closeFragment() {
//        fragmentManager?.let {
//            it.beginTransaction().remove(this).commit()
//        }
//    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PasswordFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

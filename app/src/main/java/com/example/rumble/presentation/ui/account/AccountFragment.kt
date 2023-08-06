package com.example.rumble.presentation.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rumble.R
import com.example.rumble.databinding.FragmentAccountBinding
import com.example.rumble.domain.model.ProfileInfo
import com.example.rumble.presentation.ui.main.MainActivity
import com.example.rumble.presentation.ui.main.MainActivity.Companion.KEY_USER

class AccountFragment : Fragment() {

    private var rootView : FragmentAccountBinding? = null
    private val binding get() = rootView!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val profile = arguments?.getParcelable<ProfileInfo>(KEY_USER)
        profile?.let {
            binding.nameTitle.text = requireContext().getString(R.string.name_title)
            binding.name.text = "${profile.firstname}  ${profile.lastname}"

            binding.userIdTitle.text = requireContext().getString(R.string.userId_title)
            binding.userId.text = profile.userId.toString()

            binding.addressTitle.text = requireContext().getString(R.string.address_title)
            binding.address.text = "${getString(R.string.name_title)} ${profile.address.street}, ${profile.address.street}, ${profile.address.zip}"

            binding.emailTitle.text = requireContext().getString(R.string.email_title)
            binding.email.text = profile.email

            binding.activeSubscriptionsTitle.text = requireContext().getString(R.string.active_subscriptions_title)
            binding.activeSubscriptions.text = profile.activeSubscriptions.joinToString(", ") { it }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        rootView = FragmentAccountBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rootView = null
    }
}
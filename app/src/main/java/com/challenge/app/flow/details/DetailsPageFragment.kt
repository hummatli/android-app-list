package com.challenge.app.flow.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.challenge.app.R
import com.challenge.app.base.BaseFragment
import com.challenge.app.databinding.FragmentDetailsPageBinding
import com.challenge.app.utils.setSafeOnClickListener
import android.content.Intent
import android.net.Uri
import com.challenge.app.extensions.makeGone
import com.challenge.app.utils.Utils
import org.koin.android.ext.android.inject
import java.lang.Exception


class DetailsPageFragment : BaseFragment() {

    private val viewModel by inject<DetailsPageViewModel>()

    private val args: DetailsPageFragmentArgs by navArgs()
    private lateinit var binding: FragmentDetailsPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initFavorite(args.airline.code)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupViews(rootView: View) = with(binding) {
        ivIconBack.setOnClickListener {
            findNavController().popBackStack()
        }

        ivFavorite.setOnClickListener {
            viewModel.toggleFavorite(args.airline.code)
        }

        viewModel.favoriteLiveData.observe(viewLifecycleOwner) {
            ivFavorite.setImageResource(viewModel.getFavoriteImage(it))
        }

        args.airline.let { item ->
            Glide.with(ivLogo)
                .load(item.fullLogoURL)
                .centerCrop()
                .placeholder(R.drawable.bg_image_place_holder)
                .into(ivLogo)

            tvToolbar.text = item.name
            tvName.text = item.name

            item.site.let { site ->
                if (site.isNullOrBlank()) {
                    tvSite.makeGone()
                    ivSite.makeGone()
                } else {
                    tvSite.text = site
                    tvSite.setSafeOnClickListener {
                        try {
                            startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(Utils.correctWebUrl(site))
                                )
                            )
                        } catch (ex: Exception) {
                            showError(ex, binding.lytRoot)
                        }
                    }
                }
            }

            item.phone.let { phone ->
                if (phone.isNullOrBlank()) {
                    tvPhone.makeGone()
                    ivPhone.makeGone()
                } else {
                    tvPhone.text = phone
                    tvPhone.setSafeOnClickListener {
                        try {
                            startActivity(
                                Intent(
                                    Intent.ACTION_DIAL,
                                    Uri.fromParts("tel", phone, null)
                                )
                            )
                        } catch (ex: Exception) {
                            showError(ex, binding.lytRoot)
                        }
                    }
                }
            }
        }
    }
}

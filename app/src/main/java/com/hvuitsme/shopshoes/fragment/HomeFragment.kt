package com.hvuitsme.shopshoes.fragment

import android.graphics.Outline
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.hvuitsme.shopshoes.R
import com.hvuitsme.shopshoes.adapter.BrandAdapter
import com.hvuitsme.shopshoes.adapter.SlideAdapter
import com.hvuitsme.shopshoes.databinding.FragmentHomeBinding
import com.hvuitsme.shopshoes.model.SlideModel
import com.hvuitsme.shopshoes.viewmodel.MainViewModel
import kotlinx.coroutines.Runnable

//// TODO: Rename parameter arguments, choose names that match
//// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel = MainViewModel()
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        intitBanner()
        initBrand()
        startAutoScroll()
        stopAutoScroll()
    }

    private fun intitBanner() {
        binding.progressBar.visibility = View.VISIBLE
        viewModel.banners.observe(viewLifecycleOwner, Observer {
            banners(it)
            binding.progressBar.visibility = View.GONE
        })
        viewModel.loadBanners()
    }

    private fun banners(image: List<SlideModel>) {
        binding.pagerView.adapter = SlideAdapter(image, binding.pagerView)
        binding.pagerView.clipToPadding = false
        binding.pagerView.clipChildren = false
        binding.pagerView.offscreenPageLimit = 3
        binding.pagerView.getChildAt(0)?.overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        binding.pagerView.outlineProvider = object : ViewOutlineProvider(){
            override fun getOutline(view: View, outline: Outline) {
                val cornerRadius = resources.getDimension(R.dimen.size_x5)
                outline.setRoundRect(0,0, view.width, view.height, cornerRadius)
            }
        }

        binding.pagerView.clipToOutline = true

        val compositePageTransformer = CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40))
        }

        binding.pagerView.setPageTransformer(compositePageTransformer)
    }

    private fun initBrand() {
        binding.progressBarBrand.visibility = View.VISIBLE
        viewModel.brands.observe(viewLifecycleOwner, Observer {
            binding.viewBrand.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.viewBrand.adapter = BrandAdapter(it)
            binding.progressBarBrand.visibility = View.GONE
        })
        viewModel.loadBrands()
    }

    private fun startAutoScroll() {
        val delay: Long = 6000

        val runnable = object : Runnable{
            override fun run() {
                if (binding.pagerView.adapter != null){
                    val itemCount = binding.pagerView.adapter?.itemCount ?: 0
                    val nextPage = (binding.pagerView.currentItem + 1) % itemCount
                    binding.pagerView.setCurrentItem(nextPage, true)
                }
                handler.postDelayed(this, delay)
            }
        }
        handler.postDelayed(runnable, delay)
    }

    private fun stopAutoScroll() {
        handler.removeCallbacksAndMessages(null)
    }

    override fun onResume() {
        super.onResume()
        startAutoScroll()
    }

    override fun onPause() {
        super.onPause()
        stopAutoScroll()
    }

//    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false)
//    }
//
//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment HomeFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            HomeFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}
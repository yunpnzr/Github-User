package com.example.mygithubuser.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mygithubuser.activity.DetailUserActivity
import com.example.mygithubuser.adapter.FollowAdapter
import com.example.mygithubuser.databinding.FragmentFollowBinding
import com.example.mygithubuser.response.FollowResponseItem
import com.example.mygithubuser.viewmodel.MainViewModel

class FollowFragment : Fragment() {

    private var position: Int? = null
    private var username: String? = null

    private lateinit var binding: FragmentFollowBinding
    private lateinit var mainViewModel: MainViewModel

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = ""
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]
        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }

        if (position == 1){
            val username = DetailUserActivity.username
            username.let {
                if (it != null) {
                    mainViewModel.getFollowers(it)
                }
            }
            mainViewModel.follower.observe(viewLifecycleOwner) { showUser: List<FollowResponseItem> ->
                setReviewData(showUser)
            }
        } else {
            val username = DetailUserActivity.username
            username.let {
                if (it != null) {
                    mainViewModel.getFollowing(it)
                }
            }
            mainViewModel.following.observe(viewLifecycleOwner) { showUser: List<FollowResponseItem> ->
                setReviewData(showUser)
            }
        }

        mainViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        binding.fragmentFollow.layoutManager = LinearLayoutManager(requireActivity())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setReviewData(showUser: List<FollowResponseItem>) {
        val adapter = FollowAdapter()
        adapter.listUserFragment = showUser as ArrayList<FollowResponseItem>

        binding.fragmentFollow.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}
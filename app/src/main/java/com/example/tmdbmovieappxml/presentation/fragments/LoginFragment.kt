package com.example.tmdbmovieappxml.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tmdbmovieappxml.R
import com.example.tmdbmovieappxml.databinding.FragmentLoginBinding
import com.example.tmdbmovieappxml.presentation.MoviesActivity
import com.example.tmdbmovieappxml.presentation.MoviesViewModel

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: MoviesViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater,
            container,
            false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MoviesActivity).viewModel
        initListeners()
    }

    private fun initListeners(){
        binding.etEmail.addTextChangedListener {
            enableLoginButton()
        }
        binding.etPassword.addTextChangedListener {
            enableLoginButton()
        }
        binding.btnLogin.setOnClickListener{
            findNavController().navigate(R.id.actionLogin)
        }
    }
    private fun enableLoginButton(){
        binding.apply {
            btnLogin.isEnabled = viewModel.checkLoginButton(etEmail.text.toString(), etPassword.text.toString())
        }
    }
}
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

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding

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
    private fun validateEmail() : Boolean {
        val mail = binding.etEmail.text.toString()
        val pattern = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
        return pattern.matches(mail)
    }

    private fun validatePassword(): Boolean {
        val password = binding.etPassword.text.toString()
        val pattern = Regex("(?=.*[A-Z])(?=.*\\d).{8,}")
        return pattern.matches(password)
    }

    private fun checkLoginButton() = validateEmail() && validatePassword()

    private fun enableLoginButton(){
        binding.btnLogin.isEnabled = checkLoginButton()
    }
}
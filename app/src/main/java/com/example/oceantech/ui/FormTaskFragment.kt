package com.example.oceantech.ui

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.oceantech.R
import com.google.firebase.auth.FirebaseAuth

class FormTaskFragment : Fragment() {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    private lateinit var edtPrimeiroNome: EditText
    private lateinit var edtSegundoNome: EditText
    private lateinit var edtSobrenome: EditText
    private lateinit var edtCidade: EditText
    private lateinit var edtTelefone: EditText
    private lateinit var btnSave: Button
    private lateinit var btnDelete: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_form_task, container, false)

        edtPrimeiroNome = view.findViewById(R.id.edtPrimeiroNome)
        edtSegundoNome = view.findViewById(R.id.edtSegundoNome)
        edtSobrenome = view.findViewById(R.id.edtSobrenome)
        edtCidade = view.findViewById(R.id.edtCidade)
        edtTelefone = view.findViewById(R.id.edtTelefone)
        btnSave = view.findViewById(R.id.btnSave)
        btnDelete = view.findViewById(R.id.btnDelete)

        btnDelete.setOnClickListener {
            deletarConta()
        }

        return view
    }

    private fun deletarConta() {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirmar Exclusão")
            .setMessage("Tem certeza de que deseja excluir sua conta? Essa ação não pode ser desfeita.")
            .setPositiveButton("Excluir") { _, _ -> realizarExclusaoConta() }
            .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun realizarExclusaoConta() {
        val progressDialog = showProgressDialog("Excluindo Conta", "Aguarde enquanto excluímos sua conta...")

        auth.currentUser?.let { user ->
            user.delete().addOnCompleteListener { deleteTask ->
                progressDialog.dismiss()
                if (deleteTask.isSuccessful) {
                    showToast("Conta excluída com sucesso")
                    activity?.finish()
                } else {
                    showToast("Erro ao excluir conta: ${deleteTask.exception?.message}")
                }
            }
        } ?: showToast("Usuário não autenticado")
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showProgressDialog(title: String, message: String): ProgressDialog {
        return ProgressDialog(requireContext()).apply {
            setTitle(title)
            setMessage(message)
            setCancelable(false)
            show()
        }
    }
}

package com.example.thal3.myapplication;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class CadastroEstagiario extends AppCompatActivity {

    Button cadastrar;
    private EditText edtNome;
    private EditText edtEmail;
    private EditText edtCPF;
    private EditText edtSenha;
    private EditText edtConfSenha;
    private EditText edtArea;
    private EditText edtCidade;
    private EditText edtPergunta;
    private EditText edtResposta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_estagiario);

        edtNome = (EditText)findViewById(R.id.editNomeCadastroEst);
        edtEmail = (EditText)findViewById(R.id.editEmailCadastroEst);
        edtCPF = (EditText)findViewById(R.id.editEmailCadastroEst);
        edtSenha = (EditText)findViewById(R.id.editSenhaCadastro);
        edtConfSenha = (EditText)findViewById(R.id.editConfirmaSenha);
        edtCidade = (EditText)findViewById(R.id.editCidade);

        cadastrar = (Button)findViewById(R.id.cadastrar);
        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validaCampos();
                //Intent abreTelaCadastroCurriculo = new Intent(getBaseContext(), Curriculo.class);
               // startActivity(abreTelaCadastroCurriculo);
            }
        });

    }

    private void validaCampos() {
        boolean camposVazios = false;
        ArrayList<String> logError = new ArrayList<>();

        String nome = edtNome.getText().toString();
        String email = edtEmail.getText().toString();
        String cpf = edtCPF.getText().toString();
        String senha = edtSenha.getText().toString();
        String confSenha = edtConfSenha.getText().toString();
        String cidade = edtCidade.getText().toString();

        if (camposVazios = isCampoVazio(nome)) {
            edtNome.requestFocus();
        } else if (camposVazios = isCampoVazio(email)) {
            edtEmail.requestFocus();
        } else if (camposVazios = isCampoVazio(cpf)) {
            edtCPF.requestFocus();
        } else if (camposVazios = isCampoVazio(senha)) {
            edtSenha.requestFocus();
        } else if (camposVazios = isCampoVazio(confSenha)) {
            edtConfSenha.requestFocus();
        }
          else if (camposVazios = isCampoVazio(cidade)) {
            edtCidade.requestFocus();
        }
        if (camposVazios) {
            logError.add("- Preencha todos os campos vazios.");
        }

        if (!isEmailValido(email)) {
            logError.add("- O email não é válido.");
        }

        if (!isSenhaIgual(senha, confSenha)) {
            logError.add("- As senhas não conferem.");
        }

        if (logError.size() > 0) {
            String msg = new String();
            for (String erro : logError) {
                msg = msg.concat(erro + "\n");
            }
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle("Aviso");
            dlg.setMessage(msg);
            dlg.setNeutralButton("OK", null);
            dlg.show();
        }
    }

    private boolean isCampoVazio(String valor) {
        boolean resultado = (TextUtils.isEmpty(valor) || valor.trim().isEmpty());
        return resultado;
    }

    private boolean isEmailValido(String email) {
        boolean resultado = (!isCampoVazio(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
        return resultado;
    }

    private boolean isSenhaIgual(String senha1, String senha2) {
        boolean resultado = senha1.equals(senha2);
        return resultado;
    }
}

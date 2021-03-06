package bsi.mpoo.traineeufrpe.gui.empregador.home.vaga;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import bsi.mpoo.traineeufrpe.R;
import bsi.mpoo.traineeufrpe.dominio.Notificacao;
import bsi.mpoo.traineeufrpe.dominio.pessoa.Pessoa;
import bsi.mpoo.traineeufrpe.dominio.vaga.ControladorVaga;
import bsi.mpoo.traineeufrpe.infra.sessao.SessaoEstagiario;
import bsi.mpoo.traineeufrpe.negocio.InscricaoServices;
import bsi.mpoo.traineeufrpe.negocio.NotificacaoServices;
import bsi.mpoo.traineeufrpe.negocio.PdfViewer;

public class ActPerfilEstagiario4Empregador extends AppCompatActivity {

    private TextView curso;
    private TextView instituicao;
    private TextView area;
    private TextView email;
    private TextView cidade;
    private TextView curriculo;
    private CardView cardViewSelecionar;
    private CardView cardViewInserirCurriculo;
    private LinearLayout lblSim;
    private LinearLayout lblNao;
    private TextView txtStatus;
    private ImageView imagem;
    private InscricaoServices inscricaoServices = new InscricaoServices(this);
    private Toolbar toolbar;
    private boolean selecionado = false;
    private final String strCurso;
    private final String strInstituicao;
    private final String strArea;
    public static ControladorVaga controladorVaga;
    private final Pessoa pessoa;
    private final NotificacaoServices notificationServices = new NotificacaoServices(this);
    private CardView cardView1;
    private CardView cardView2;
    private PdfViewer pdfViewer;

    public ActPerfilEstagiario4Empregador(){
        pessoa = controladorVaga.getPessoa();
        strCurso = pessoa.getEstagiario().getCurriculo().getCurso();
        strInstituicao = pessoa.getEstagiario().getCurriculo().getInstituicao();
        strArea = pessoa.getEstagiario().getCurriculo().getAreaAtuacao();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_estagiario);
        Constroi();
        Bitmap bitmap = getImagem();
        Set(bitmap);
        lblSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!selecionado)
                    sim();
            }
        });
        lblNao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!selecionado)
                    nao();
            }
        });
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!pessoa.getEstagiario().getCurriculo().getLink().equals("")){
                    mostrarUrl(pessoa.getEstagiario().getCurriculo().getLink());
                }else{
                    Snackbar snackbar;
                    snackbar = Snackbar.make(v, "Este usuário não cadastrou um link.", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        });

        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!pessoa.getEstagiario().getCurriculo().getExperiencia().equals("")){
                    PreparaCurriculo();
                    pdfViewer.ViewPdf();
                }else{
                    Snackbar snackbar;
                    snackbar = Snackbar.make(v, "Este usuário não inseriu um curriculo.", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }

            }
        });
    }



    private void PreparaCurriculo() {
        pdfViewer = new PdfViewer(this);
        pdfViewer.open_document();
        pdfViewer.addMetaData("Nome", "Vaga", "Eu");
        pdfViewer.addTitle(pessoa.getNome(),
                pessoa.getEstagiario().getCurriculo().getInstituicao()
                        + " Sede em " + pessoa.getCidade(),
                "Estágio na área de " + pessoa.getEstagiario().getCurriculo().getAreaAtuacao());
        pdfViewer.addText("_________________________________________________________");
        pdfViewer.addParagraph("Sumário");
        pdfViewer.addText(pessoa.getEstagiario().getCurriculo().getExperiencia());
        pdfViewer.addText(pessoa.getEstagiario().getCurriculo().getRelacionamento());
        pdfViewer.addText(pessoa.getEstagiario().getCurriculo().getObjetivo());
        pdfViewer.addText(pessoa.getEstagiario().getCurriculo().getConhcimentos_basicos());
        pdfViewer.addParagraph("Experiência");
        pdfViewer.addText("_________________________________________________________");
        pdfViewer.addText(pessoa.getEstagiario().getCurriculo().getCurso());
        pdfViewer.addText(pessoa.getEstagiario().getCurriculo().getConhecimentos_especificos());
        pdfViewer.addText(pessoa.getEstagiario().getCurriculo().getDisciplinas());

        pdfViewer.close_doc();
    }

    private void mostrarUrl(String url) {
        Intent site = new Intent(Intent.ACTION_VIEW);
        site.setData(Uri.parse(url));
        startActivity(site);
    }

    private void nao() {
        inscricaoServices = new InscricaoServices(ActPerfilEstagiario4Empregador.this);
        controladorVaga.setStatus("dispensado");
        inscricaoServices.setStatusInscricaoByEstagiarioAndVaga(controladorVaga);
        lblSim.setBackgroundColor(Color.parseColor("#999999"));
        lblNao.setBackgroundColor(Color.parseColor("#ff0000"));
        txtStatus.setText("Candidato dispensado.");
        txtStatus.setVisibility(View.VISIBLE);
        notificacaoEstagiarioNaoSelecionado(controladorVaga);
        selecionado = true;
    }

    private void sim() {
        inscricaoServices = new InscricaoServices(ActPerfilEstagiario4Empregador.this);
        controladorVaga.setStatus("selecionado");
        inscricaoServices.setStatusInscricaoByEstagiarioAndVaga(controladorVaga);
        lblNao.setBackgroundColor(Color.parseColor("#999999"));
        lblSim.setBackgroundColor(Color.parseColor("#228b22"));
        txtStatus.setText("Candidato selecionado.");
        notificacaoSelecionadoEstagiario(controladorVaga);
        txtStatus.setVisibility(View.VISIBLE);
        selecionado = true;
    }

    private void Set(Bitmap bitmap) {
        curso.setText(strCurso);
        instituicao.setText(strInstituicao);
        area.setText(strArea);
        email.setText(pessoa.getEstagiario().getEmail());
        cidade.setText(pessoa.getCidade());
        imagem.setContentDescription(pessoa.getNome());
        imagem.setImageBitmap(bitmap);
        toolbar.setTitle(pessoa.getNome());
        setSupportActionBar(toolbar);
        cardViewSelecionar.setVisibility(View.VISIBLE);
        cardViewInserirCurriculo.setVisibility(View.INVISIBLE);
        lblNao.setBackgroundColor(Color.parseColor("#ff0000"));
        lblSim.setBackgroundColor(Color.parseColor("#228b22"));
    }

    private void Constroi() {
        curso =  findViewById(R.id.campo_curso);
        instituicao =  findViewById(R.id.campo_instituicao);
        area =  findViewById(R.id.campo_area);
        email = findViewById(R.id.campo_email);
        cidade =  findViewById(R.id.campo_local);
        imagem =  findViewById(R.id.campo_imagem);
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        cardView1 = findViewById(R.id.cardViewLink);
        cardView2 = findViewById(R.id.cardViewCurriculo);
        cardViewSelecionar = findViewById(R.id.cardViewEmpregador);
        cardViewInserirCurriculo = findViewById(R.id.cardViewEdit);
        lblSim = findViewById(R.id.selectSim);
        lblNao = findViewById(R.id.selectNao);
        txtStatus = findViewById(R.id.txtStatus);
        curriculo = findViewById(R.id.campo_curriculo);
    }

    private Bitmap getImagem(){
        byte[] fotoEstagiario = pessoa.getEstagiario().getFoto();
        return BitmapFactory.decodeByteArray(fotoEstagiario, 0, fotoEstagiario.length);
    }
    private void notificacaoSelecionadoEstagiario(ControladorVaga inscricao){
        Notificacao novaNotificacao = new Notificacao();
        novaNotificacao.setEmpregadorEnvia(inscricao.getEmpregador());
        novaNotificacao.setPessoaRecebe(inscricao.getPessoa());
        String nomeVaga = inscricao.getVaga().getNome();
        novaNotificacao.setMensagem("Você foi selecionado para a vaga " + nomeVaga + ".");
        novaNotificacao.setVagaRelacionada(inscricao.getVaga());

        notificationServices.enviar4Estagiario(novaNotificacao);
    }
    private void notificacaoEstagiarioNaoSelecionado(ControladorVaga inscricao) {
        Notificacao novaNotificacao = new Notificacao();
        novaNotificacao.setEmpregadorEnvia(inscricao.getEmpregador());
        novaNotificacao.setPessoaRecebe(inscricao.getPessoa());
        String nomeVaga = inscricao.getVaga().getNome();
        novaNotificacao.setMensagem("Você não foi selecionado para a vaga " + nomeVaga + ".");
        novaNotificacao.setVagaRelacionada(inscricao.getVaga());
        notificationServices.enviar4Estagiario(novaNotificacao);
    }
}

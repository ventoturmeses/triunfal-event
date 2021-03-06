package br.unicamp.apptriunfalevent.ui.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.Locale;

import br.unicamp.apptriunfalevent.APIconfig.*;
import br.unicamp.apptriunfalevent.APIconfig.Service;
import br.unicamp.apptriunfalevent.Models.*;
import br.unicamp.apptriunfalevent.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IngressarEvento extends AppCompatActivity {

    private Button btnIngressarEvento_ingressarEV, btnHome_ingEvent;
    private EditText edtCodigo_ingressarEV;
    private TextView tvAviso_ingressarEV;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingressar_evento);

        btnIngressarEvento_ingressarEV = (Button) findViewById(R.id.btnIngressarEvento_ingressarEV);
        edtCodigo_ingressarEV = (EditText) findViewById(R.id.edtCodigo_ingressarEV);
        tvAviso_ingressarEV = (TextView) findViewById(R.id.tvAviso_ingressarEV);
        btnHome_ingEvent = (Button) findViewById(R.id.btnHome_ingEvent);

        session = new Session(this);

        btnIngressarEvento_ingressarEV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String codigo = edtCodigo_ingressarEV.getText().toString().toUpperCase(Locale.ROOT);
                if (codigo.length() != 6) {
                    tvAviso_ingressarEV.setText("Código inválido!");
                    return;
                }

                Convidado convidado = new Convidado(session.getusename(), codigo.toString());

                Service service = RetrofitConfig.getRetrofitInstance().create(Service.class);

                Call<Convidado> call = service.postConvidado(convidado);
                call.enqueue(new Callback<Convidado>() {
                    @Override
                    public void onResponse(Call<Convidado> call, Response<Convidado> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(IngressarEvento.this, "SUCESSO", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(IngressarEvento.this, EventoInfo.class);

                            Evento evento = new Evento();
                            evento.setId(codigo);
                            intent.putExtra("sessaoEventoPart", (Serializable) evento);
                            startActivity(intent);
                            
                        }
                        else{
                            Toast.makeText(IngressarEvento.this, "SU", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<Convidado> call, Throwable t) {
                        Toast.makeText(IngressarEvento.this, "SUCEO", Toast.LENGTH_SHORT).show();

                    }
                });

                try {
                    //Response<Usuario> response = call.execute();
                    Toast.makeText(IngressarEvento.this, "SUCESSO", Toast.LENGTH_SHORT).show();
                }
                catch (Exception erro) {
                    Toast.makeText(IngressarEvento.this, erro.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnHome_ingEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(IngressarEvento.this, HomeActivity.class));
            }
        });
    }
}
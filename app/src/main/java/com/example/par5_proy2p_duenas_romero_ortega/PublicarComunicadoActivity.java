package com.example.par5_proy2p_duenas_romero_ortega;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import android.provider.OpenableColumns;
import android.view.View;
import Enums.TipoComunicado;
import Enums.TipoAudiencia;
import Models.Anuncio;
import Models.Comunicado;
import Models.Evento;
import Enums.NivelUrgencia;
import Exceptions.DatosIncompletosException;
import Models.Usuario;
import Persistencia.ComunicadoRepositorio;
import Persistencia.ManejadorArchivo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PublicarComunicadoActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_IMAGE = 1001;
    private ImageButton btnVolver;

    private RadioGroup rgTipo;
    private RadioButton rbAnuncio, rbEvento;
    private Spinner spArea;
    private CheckBox cbEstudiantes, cbProfesores, cbAdministrativo;
    private EditText etTitulo, etLugar, etDescripcion;
    private Button btnFecha, btnImagen, btnPublicar, btnCancelar;
    private TextView tvImagenNombre, tvLugarLabel, tvFechaLabel;

    private String fechaSeleccionada = "";
    private Uri imagenUri = null;
    private String imagenNombre = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicar_comunicado);

        rgTipo = findViewById(R.id.rgTipo);
        rbAnuncio = findViewById(R.id.rbAnuncio);
        rbEvento = findViewById(R.id.rbEvento);
        spArea = findViewById(R.id.spArea);
        cbEstudiantes = findViewById(R.id.cbEstudiantes);
        cbProfesores = findViewById(R.id.cbProfesores);
        cbAdministrativo = findViewById(R.id.cbAdministrativo);
        etTitulo = findViewById(R.id.etTitulo);
        etLugar = findViewById(R.id.etLugar);
        etDescripcion = findViewById(R.id.etDescripcion);
        btnFecha = findViewById(R.id.btnFecha);
        btnImagen = findViewById(R.id.btnImagen);
        btnPublicar = findViewById(R.id.btnPublicar);
        btnCancelar = findViewById(R.id.btnCancelar);
        tvImagenNombre = findViewById(R.id.tvImagenNombre);
        tvLugarLabel = findViewById(R.id.tvLugarLabel);
        tvFechaLabel = findViewById(R.id.tvFechaLabel);

        this.btnVolver = findViewById(R.id.backButton);
        btnVolver.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PublicarComunicadoActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });

        ComunicadoRepositorio.cargarComunicados(this);

        String[] areas = {"Académico","Administrativo","Cultural","General"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, areas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spArea.setAdapter(adapter);


        rgTipo.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbEvento) {
                etLugar.setVisibility(View.VISIBLE);
                btnFecha.setVisibility(View.VISIBLE);
                tvLugarLabel.setVisibility(View.VISIBLE);
                tvFechaLabel.setVisibility(View.VISIBLE);
            } else {
                etLugar.setVisibility(View.GONE);
                btnFecha.setVisibility(View.GONE);
                tvLugarLabel.setVisibility(View.GONE);
                tvFechaLabel.setVisibility(View.GONE);
                fechaSeleccionada = "";
                btnFecha.setText(R.string.seleccionar_fecha);
            }
        });


        btnFecha.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, y, m, d) -> {
                        fechaSeleccionada = String.format("%02d/%02d/%04d", d, m+1, y);
                        btnFecha.setText(fechaSeleccionada);
                    }, year, month, day);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        });


        btnImagen.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
        });

        // Publicar
        btnPublicar.setOnClickListener(v -> {
            try {
                publicar();
            } catch (DatosIncompletosException ex) {
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Cancelar
        btnCancelar.setOnClickListener(v -> finish());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imagenUri = data.getData();
            if (imagenUri != null) {
                imagenNombre = obtenerNombreArchivo(imagenUri);
                tvImagenNombre.setText(imagenNombre);
            }
        }
    }

    private String obtenerNombreArchivo(Uri uri) {
        String result = "";
        try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int idx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (idx >= 0) result = cursor.getString(idx);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result.isEmpty()) {
            result = "imagen_" + System.currentTimeMillis() + ".jpg";
        }
        return result;
    }

    private void publicar() throws DatosIncompletosException, SecurityException {
        // Verificar que el usuario esté autenticado
        if (Usuario.logged_user_id == null || Usuario.logged_user_id.isEmpty()) {
            throw new SecurityException(getString(R.string.debe_iniciar_sesion));
        }

        boolean esEvento = (rgTipo.getCheckedRadioButtonId() == R.id.rbEvento);
        TipoComunicado tipo = esEvento ? TipoComunicado.EVENTO : TipoComunicado.ANUNCIO;
        String area = spArea.getSelectedItem().toString();
        List<TipoAudiencia> audiencia = new ArrayList<>();
        if (cbEstudiantes.isChecked()) audiencia.add(TipoAudiencia.ESTUDIANTES);
        if (cbProfesores.isChecked()) audiencia.add(TipoAudiencia.PROFESORES);
        if (cbAdministrativo.isChecked()) audiencia.add(TipoAudiencia.ADMINISTRATIVO);

        String titulo = etTitulo.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();
        String lugar = etLugar.getText() != null ? etLugar.getText().toString().trim() : "";
        NivelUrgencia nivelPorDefecto = NivelUrgencia.MEDIA;
        if (titulo.isEmpty()) throw new DatosIncompletosException(getString(R.string.titulo_obligatorio));
        if (descripcion.isEmpty()) throw new DatosIncompletosException(getString(R.string.descripcion_obligatoria));
        if (audiencia.isEmpty()) throw new DatosIncompletosException(getString(R.string.seleccione_audiencia));
        if (esEvento) {
            if (lugar.isEmpty()) throw new DatosIncompletosException(getString(R.string.lugar_obligatorio));
            if (fechaSeleccionada.isEmpty()) throw new DatosIncompletosException(getString(R.string.seleccione_fecha));

        }


        int id = ComunicadoRepositorio.generarNuevoId(this);
        String userId = Usuario.logged_user_id;

        Comunicado c;
        if (TipoComunicado.EVENTO.equals(tipo)) {
            c = new Evento(id, userId,area, titulo, audiencia, descripcion, imagenNombre, fechaSeleccionada,lugar);
        } else {
            c = new Anuncio(id,userId, area, titulo, audiencia, descripcion, imagenNombre, nivelPorDefecto);
        }


        if (imagenUri != null) {
            try (InputStream is = getContentResolver().openInputStream(imagenUri)) {
                if (is != null) {
                    byte[] bytes = ManejadorArchivo.readBytesFromInputStream(is);
                    String destNombre = "img_" + id + "_" + imagenNombre;
                    ManejadorArchivo.escribirBinario(this, destNombre, bytes);
                    if (c instanceof Evento) {
                        ((Evento) c).setNombreArchivoImagen(destNombre);
                    } else if (c instanceof Anuncio) {
                        ((Anuncio) c).setNombreArchivoImagen(destNombre);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ComunicadoRepositorio.guardarComunicado(this, c);

        Toast.makeText(this, "Comunicado publicado", Toast.LENGTH_LONG).show();
        limpiarCampos();
    }

    //Limpia todo lo rellenado anteriormente
    private void limpiarCampos() {
        rgTipo.check(R.id.rbAnuncio);
        spArea.setSelection(0);
        
        for (TipoAudiencia audiencia : TipoAudiencia.values()) {
            int viewId = getResources().getIdentifier("cb" + audiencia.name().charAt(0) + audiencia.name().substring(1).toLowerCase(), 
                "id", getPackageName());
            CheckBox checkBox = findViewById(viewId);
            if (checkBox != null) {
                checkBox.setChecked(false);
            }
        }
        etTitulo.setText("");
        etDescripcion.setText("");
        etLugar.setText("");
        fechaSeleccionada = "";
        btnFecha.setText(R.string.seleccionar_fecha);
        imagenUri = null;
        imagenNombre = "";
        tvImagenNombre.setText("");
    }
}

package com.cbd.android.ui.newPost;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.cbd.android.R;
import com.cbd.android.activities.MainActivity;
import com.cbd.android.common.Constants;
import com.cbd.android.common.Utils;
import com.cbd.android.viewModels.PostViewModel;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.single.CompositePermissionListener;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.Objects;

public class NewPostDialogFragment extends DialogFragment implements View.OnClickListener {
    private ImageView postExit, imagenPostPre;
    private Button postPublishButton, uploadPhotoButton;
    private TextView postTitleInput, postDescriptionInput, postPriceInput;
    private PermissionListener allPermissionsListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.new_post_full_dialog, container, false);
        postExit = view.findViewById(R.id.post_exit);
        postPublishButton = view.findViewById(R.id.post_publish_button);
        postTitleInput = view.findViewById(R.id.post_title_input);
        postDescriptionInput = view.findViewById(R.id.post_description_input);
        postPriceInput = view.findViewById(R.id.post_price_input);
        uploadPhotoButton = view.findViewById(R.id.post_upload_photo);
        imagenPostPre = view.findViewById(R.id.imagen_post_pre);
        imagenPostPre.setVisibility(View.GONE);

        // Eventos
        postPublishButton.setOnClickListener(this);
        postExit.setOnClickListener(this);
        uploadPhotoButton.setOnClickListener(this);

        // Seteamos la imagen del post a null
        ((MainActivity) Objects.requireNonNull(getActivity())).setImagenSeleccionada(null);

        return view;
    }

    public void loadImage(Uri uri) throws IOException {
        Bitmap bmp = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getActivity()).getContentResolver(), uri);
        Utils.rescaleBitmap(bmp, 1000, 100);
        Glide.with(this)
                .load(bmp)
                .centerCrop()
                .into(imagenPostPre);
        imagenPostPre.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.post_publish_button) {
            String title = postTitleInput.getText().toString();
            String description = postDescriptionInput.getText().toString();
            String price = postPriceInput.getText().toString();
            Uri imagenSeleccionada = ((MainActivity) Objects.requireNonNull(getActivity())).getImagenSeleccionada();

            if (title.isEmpty()) {
                postTitleInput.setError(Constants.ERROR_TITULO_VACIO);
            }
            if (description.isEmpty()) {
                postDescriptionInput.setError(Constants.ERROR_DESCRIPCION_VACIO);
            }
            if (price.isEmpty()) {
                postPriceInput.setError(Constants.ERROR_PRECIO_VACIO);
            }
            if (!title.isEmpty() && !description.isEmpty() && !price.isEmpty()) {
                PostViewModel postViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()))
                        .get(PostViewModel.class);

            Bitmap bmp = null;
            if (imagenSeleccionada != null) {
                try {
                    bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imagenSeleccionada);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            postViewModel.publishPost(title, description, Double.valueOf(price), bmp);
            Objects.requireNonNull(getDialog()).dismiss();
            }
        } else if (id == R.id.post_exit) {
            showDialogConfirm();
        } else if (id == R.id.post_upload_photo) {
            checkPermissions();
        }
    }

    private void checkPermissions() {
        PermissionListener dialogOnDeniedPermissionListener = DialogOnDeniedPermissionListener.Builder.withContext(getActivity())
                .withTitle("Permisos")
                .withMessage("Los permisos solicitados son necesarios para seleccionar una imagen")
                .withButtonText("Aceptar")
                .withIcon(R.mipmap.ic_launcher)
                .build();

        allPermissionsListener = new CompositePermissionListener((PermissionListener) getActivity(), dialogOnDeniedPermissionListener);
        Dexter.withContext(getActivity()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(allPermissionsListener).check();
    }

    private void showDialogConfirm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("¿Desea realmente no publicar este post?").setTitle("Cancelar");
        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

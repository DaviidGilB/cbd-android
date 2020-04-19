package com.cbd.android.ui.newPost;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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

import com.cbd.android.R;
import com.cbd.android.common.Constants;
import com.cbd.android.viewModels.PostViewModel;

import java.util.Objects;

public class NewPostDialogFragment extends DialogFragment implements View.OnClickListener {
    private ImageView postExit;
    private Button postPublishButton;
    private TextView postTitleInput, postDescriptionInput, postPriceInput;

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

        // Eventos
        postPublishButton.setOnClickListener(this);
        postExit.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.post_publish_button) {
            String title = postTitleInput.getText().toString();
            String description = postDescriptionInput.getText().toString();
            String price = postPriceInput.getText().toString();

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
                postViewModel.publishPost(title, description, Double.valueOf(price));
                Objects.requireNonNull(getDialog()).dismiss();
            }
        } else if (id == R.id.post_exit) {
            showDialogConfirm();
        }
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

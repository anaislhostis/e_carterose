// Generated by view binder compiler. Do not edit!
package com.example.e_carterose.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.e_carterose.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentSelectionTransportAbattoirBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final LinearLayout TextViewElevage;

  @NonNull
  public final TextView TitreEndroitTransfert;

  @NonNull
  public final Button buttonEffacer;

  @NonNull
  public final Button buttonSubmit;

  @NonNull
  public final EditText editTextNumTra;

  @NonNull
  public final TextView textViewNumTra;

  private FragmentSelectionTransportAbattoirBinding(@NonNull LinearLayout rootView,
      @NonNull LinearLayout TextViewElevage, @NonNull TextView TitreEndroitTransfert,
      @NonNull Button buttonEffacer, @NonNull Button buttonSubmit, @NonNull EditText editTextNumTra,
      @NonNull TextView textViewNumTra) {
    this.rootView = rootView;
    this.TextViewElevage = TextViewElevage;
    this.TitreEndroitTransfert = TitreEndroitTransfert;
    this.buttonEffacer = buttonEffacer;
    this.buttonSubmit = buttonSubmit;
    this.editTextNumTra = editTextNumTra;
    this.textViewNumTra = textViewNumTra;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentSelectionTransportAbattoirBinding inflate(
      @NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentSelectionTransportAbattoirBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_selection_transport_abattoir, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentSelectionTransportAbattoirBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      LinearLayout TextViewElevage = (LinearLayout) rootView;

      id = R.id.TitreEndroitTransfert;
      TextView TitreEndroitTransfert = ViewBindings.findChildViewById(rootView, id);
      if (TitreEndroitTransfert == null) {
        break missingId;
      }

      id = R.id.buttonEffacer;
      Button buttonEffacer = ViewBindings.findChildViewById(rootView, id);
      if (buttonEffacer == null) {
        break missingId;
      }

      id = R.id.buttonSubmit;
      Button buttonSubmit = ViewBindings.findChildViewById(rootView, id);
      if (buttonSubmit == null) {
        break missingId;
      }

      id = R.id.editTextNumTra;
      EditText editTextNumTra = ViewBindings.findChildViewById(rootView, id);
      if (editTextNumTra == null) {
        break missingId;
      }

      id = R.id.textViewNumTra;
      TextView textViewNumTra = ViewBindings.findChildViewById(rootView, id);
      if (textViewNumTra == null) {
        break missingId;
      }

      return new FragmentSelectionTransportAbattoirBinding((LinearLayout) rootView, TextViewElevage,
          TitreEndroitTransfert, buttonEffacer, buttonSubmit, editTextNumTra, textViewNumTra);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}

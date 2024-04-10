// Generated by view binder compiler. Do not edit!
package com.example.e_carterose.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.e_carterose.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentIndexBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final Button ButtonEleveur;

  @NonNull
  public final Button ButtonTransporteurEquarisseur;

  @NonNull
  public final Button ButtonVeterinaire;

  @NonNull
  public final LinearLayout TextViewElevage;

  @NonNull
  public final LinearLayout buttonContainer;

  private FragmentIndexBinding(@NonNull LinearLayout rootView, @NonNull Button ButtonEleveur,
      @NonNull Button ButtonTransporteurEquarisseur, @NonNull Button ButtonVeterinaire,
      @NonNull LinearLayout TextViewElevage, @NonNull LinearLayout buttonContainer) {
    this.rootView = rootView;
    this.ButtonEleveur = ButtonEleveur;
    this.ButtonTransporteurEquarisseur = ButtonTransporteurEquarisseur;
    this.ButtonVeterinaire = ButtonVeterinaire;
    this.TextViewElevage = TextViewElevage;
    this.buttonContainer = buttonContainer;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentIndexBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentIndexBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_index, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentIndexBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.ButtonEleveur;
      Button ButtonEleveur = ViewBindings.findChildViewById(rootView, id);
      if (ButtonEleveur == null) {
        break missingId;
      }

      id = R.id.ButtonTransporteurEquarisseur;
      Button ButtonTransporteurEquarisseur = ViewBindings.findChildViewById(rootView, id);
      if (ButtonTransporteurEquarisseur == null) {
        break missingId;
      }

      id = R.id.ButtonVeterinaire;
      Button ButtonVeterinaire = ViewBindings.findChildViewById(rootView, id);
      if (ButtonVeterinaire == null) {
        break missingId;
      }

      LinearLayout TextViewElevage = (LinearLayout) rootView;

      id = R.id.buttonContainer;
      LinearLayout buttonContainer = ViewBindings.findChildViewById(rootView, id);
      if (buttonContainer == null) {
        break missingId;
      }

      return new FragmentIndexBinding((LinearLayout) rootView, ButtonEleveur,
          ButtonTransporteurEquarisseur, ButtonVeterinaire, TextViewElevage, buttonContainer);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}

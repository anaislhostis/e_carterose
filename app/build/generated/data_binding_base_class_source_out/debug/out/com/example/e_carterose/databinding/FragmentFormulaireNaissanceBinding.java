// Generated by view binder compiler. Do not edit!
package com.example.e_carterose.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.e_carterose.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentFormulaireNaissanceBinding implements ViewBinding {
  @NonNull
  private final ScrollView rootView;

  @NonNull
  public final TextView TitreFormNais;

  @NonNull
  public final Button buttonEffacer;

  @NonNull
  public final Button buttonPickDateNais;

  @NonNull
  public final Button buttonSubmit;

  @NonNull
  public final EditText editTextCodePaysMere;

  @NonNull
  public final EditText editTextCodePaysPere;

  @NonNull
  public final EditText editTextCodeRace;

  @NonNull
  public final EditText editTextCodeRaceMere;

  @NonNull
  public final EditText editTextCodeRacePere;

  @NonNull
  public final EditText editTextNom;

  @NonNull
  public final EditText editTextNumNat;

  @NonNull
  public final EditText editTextNumNatMere;

  @NonNull
  public final EditText editTextNumNatPere;

  @NonNull
  public final Spinner spinnerSexe;

  @NonNull
  public final TextView textViewCodePaysMere;

  @NonNull
  public final TextView textViewCodePaysPere;

  @NonNull
  public final TextView textViewCodeRace;

  @NonNull
  public final TextView textViewCodeRaceMere;

  @NonNull
  public final TextView textViewCodeRacePere;

  @NonNull
  public final TextView textViewDateNais;

  @NonNull
  public final TextView textViewNom;

  @NonNull
  public final TextView textViewNumNat;

  @NonNull
  public final TextView textViewNumNatMere;

  @NonNull
  public final TextView textViewNumNatPere;

  @NonNull
  public final TextView textViewSexe;

  private FragmentFormulaireNaissanceBinding(@NonNull ScrollView rootView,
      @NonNull TextView TitreFormNais, @NonNull Button buttonEffacer,
      @NonNull Button buttonPickDateNais, @NonNull Button buttonSubmit,
      @NonNull EditText editTextCodePaysMere, @NonNull EditText editTextCodePaysPere,
      @NonNull EditText editTextCodeRace, @NonNull EditText editTextCodeRaceMere,
      @NonNull EditText editTextCodeRacePere, @NonNull EditText editTextNom,
      @NonNull EditText editTextNumNat, @NonNull EditText editTextNumNatMere,
      @NonNull EditText editTextNumNatPere, @NonNull Spinner spinnerSexe,
      @NonNull TextView textViewCodePaysMere, @NonNull TextView textViewCodePaysPere,
      @NonNull TextView textViewCodeRace, @NonNull TextView textViewCodeRaceMere,
      @NonNull TextView textViewCodeRacePere, @NonNull TextView textViewDateNais,
      @NonNull TextView textViewNom, @NonNull TextView textViewNumNat,
      @NonNull TextView textViewNumNatMere, @NonNull TextView textViewNumNatPere,
      @NonNull TextView textViewSexe) {
    this.rootView = rootView;
    this.TitreFormNais = TitreFormNais;
    this.buttonEffacer = buttonEffacer;
    this.buttonPickDateNais = buttonPickDateNais;
    this.buttonSubmit = buttonSubmit;
    this.editTextCodePaysMere = editTextCodePaysMere;
    this.editTextCodePaysPere = editTextCodePaysPere;
    this.editTextCodeRace = editTextCodeRace;
    this.editTextCodeRaceMere = editTextCodeRaceMere;
    this.editTextCodeRacePere = editTextCodeRacePere;
    this.editTextNom = editTextNom;
    this.editTextNumNat = editTextNumNat;
    this.editTextNumNatMere = editTextNumNatMere;
    this.editTextNumNatPere = editTextNumNatPere;
    this.spinnerSexe = spinnerSexe;
    this.textViewCodePaysMere = textViewCodePaysMere;
    this.textViewCodePaysPere = textViewCodePaysPere;
    this.textViewCodeRace = textViewCodeRace;
    this.textViewCodeRaceMere = textViewCodeRaceMere;
    this.textViewCodeRacePere = textViewCodeRacePere;
    this.textViewDateNais = textViewDateNais;
    this.textViewNom = textViewNom;
    this.textViewNumNat = textViewNumNat;
    this.textViewNumNatMere = textViewNumNatMere;
    this.textViewNumNatPere = textViewNumNatPere;
    this.textViewSexe = textViewSexe;
  }

  @Override
  @NonNull
  public ScrollView getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentFormulaireNaissanceBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentFormulaireNaissanceBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_formulaire_naissance, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentFormulaireNaissanceBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.TitreFormNais;
      TextView TitreFormNais = ViewBindings.findChildViewById(rootView, id);
      if (TitreFormNais == null) {
        break missingId;
      }

      id = R.id.buttonEffacer;
      Button buttonEffacer = ViewBindings.findChildViewById(rootView, id);
      if (buttonEffacer == null) {
        break missingId;
      }

      id = R.id.buttonPickDateNais;
      Button buttonPickDateNais = ViewBindings.findChildViewById(rootView, id);
      if (buttonPickDateNais == null) {
        break missingId;
      }

      id = R.id.buttonSubmit;
      Button buttonSubmit = ViewBindings.findChildViewById(rootView, id);
      if (buttonSubmit == null) {
        break missingId;
      }

      id = R.id.editTextCodePaysMere;
      EditText editTextCodePaysMere = ViewBindings.findChildViewById(rootView, id);
      if (editTextCodePaysMere == null) {
        break missingId;
      }

      id = R.id.editTextCodePaysPere;
      EditText editTextCodePaysPere = ViewBindings.findChildViewById(rootView, id);
      if (editTextCodePaysPere == null) {
        break missingId;
      }

      id = R.id.editTextCodeRace;
      EditText editTextCodeRace = ViewBindings.findChildViewById(rootView, id);
      if (editTextCodeRace == null) {
        break missingId;
      }

      id = R.id.editTextCodeRaceMere;
      EditText editTextCodeRaceMere = ViewBindings.findChildViewById(rootView, id);
      if (editTextCodeRaceMere == null) {
        break missingId;
      }

      id = R.id.editTextCodeRacePere;
      EditText editTextCodeRacePere = ViewBindings.findChildViewById(rootView, id);
      if (editTextCodeRacePere == null) {
        break missingId;
      }

      id = R.id.editTextNom;
      EditText editTextNom = ViewBindings.findChildViewById(rootView, id);
      if (editTextNom == null) {
        break missingId;
      }

      id = R.id.editTextNumNat;
      EditText editTextNumNat = ViewBindings.findChildViewById(rootView, id);
      if (editTextNumNat == null) {
        break missingId;
      }

      id = R.id.editTextNumNatMere;
      EditText editTextNumNatMere = ViewBindings.findChildViewById(rootView, id);
      if (editTextNumNatMere == null) {
        break missingId;
      }

      id = R.id.editTextNumNatPere;
      EditText editTextNumNatPere = ViewBindings.findChildViewById(rootView, id);
      if (editTextNumNatPere == null) {
        break missingId;
      }

      id = R.id.spinnerSexe;
      Spinner spinnerSexe = ViewBindings.findChildViewById(rootView, id);
      if (spinnerSexe == null) {
        break missingId;
      }

      id = R.id.textViewCodePaysMere;
      TextView textViewCodePaysMere = ViewBindings.findChildViewById(rootView, id);
      if (textViewCodePaysMere == null) {
        break missingId;
      }

      id = R.id.textViewCodePaysPere;
      TextView textViewCodePaysPere = ViewBindings.findChildViewById(rootView, id);
      if (textViewCodePaysPere == null) {
        break missingId;
      }

      id = R.id.textViewCodeRace;
      TextView textViewCodeRace = ViewBindings.findChildViewById(rootView, id);
      if (textViewCodeRace == null) {
        break missingId;
      }

      id = R.id.textViewCodeRaceMere;
      TextView textViewCodeRaceMere = ViewBindings.findChildViewById(rootView, id);
      if (textViewCodeRaceMere == null) {
        break missingId;
      }

      id = R.id.textViewCodeRacePere;
      TextView textViewCodeRacePere = ViewBindings.findChildViewById(rootView, id);
      if (textViewCodeRacePere == null) {
        break missingId;
      }

      id = R.id.textViewDateNais;
      TextView textViewDateNais = ViewBindings.findChildViewById(rootView, id);
      if (textViewDateNais == null) {
        break missingId;
      }

      id = R.id.textViewNom;
      TextView textViewNom = ViewBindings.findChildViewById(rootView, id);
      if (textViewNom == null) {
        break missingId;
      }

      id = R.id.textViewNumNat;
      TextView textViewNumNat = ViewBindings.findChildViewById(rootView, id);
      if (textViewNumNat == null) {
        break missingId;
      }

      id = R.id.textViewNumNatMere;
      TextView textViewNumNatMere = ViewBindings.findChildViewById(rootView, id);
      if (textViewNumNatMere == null) {
        break missingId;
      }

      id = R.id.textViewNumNatPere;
      TextView textViewNumNatPere = ViewBindings.findChildViewById(rootView, id);
      if (textViewNumNatPere == null) {
        break missingId;
      }

      id = R.id.textViewSexe;
      TextView textViewSexe = ViewBindings.findChildViewById(rootView, id);
      if (textViewSexe == null) {
        break missingId;
      }

      return new FragmentFormulaireNaissanceBinding((ScrollView) rootView, TitreFormNais,
          buttonEffacer, buttonPickDateNais, buttonSubmit, editTextCodePaysMere,
          editTextCodePaysPere, editTextCodeRace, editTextCodeRaceMere, editTextCodeRacePere,
          editTextNom, editTextNumNat, editTextNumNatMere, editTextNumNatPere, spinnerSexe,
          textViewCodePaysMere, textViewCodePaysPere, textViewCodeRace, textViewCodeRaceMere,
          textViewCodeRacePere, textViewDateNais, textViewNom, textViewNumNat, textViewNumNatMere,
          textViewNumNatPere, textViewSexe);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}

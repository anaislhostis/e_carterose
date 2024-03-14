<ScrollView xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    tools:ignore="MissingDefaultResource">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"

        android:orientation="vertical"
        android:padding="16dp"
        tools:ignore="ExtraText">

        <TextView
            android:id="@+id/num_tra"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Animal n° "
            android:textStyle="bold"
            android:textSize="22dp"
            android:textColor="@color/black"/>

        <Space
            android:layout_width="match_parent"
            android:layout_height="10dp" />

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:textColor="@color/grey"
            android:text="Informations générales :  "
            android:textSize="18dp"/>

        <LinearLayout
            android:id="@+id/animals_details_container"
            style = "@style/AnimalContainer"
            android:backgroundTint="@color/rose_pale"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:paddingLeft="10dp"
            android:textSize="20dp"
            android:background="@drawable/border_shape"
            android:orientation="vertical">

            <Space
                android:layout_width="match_parent"
                android:layout_height="5dp" />


            <TextView
                android:id="@+id/detail_nom"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Nom: "
                android:textColor="@android:color/black"/>

            <TextView
                android:id="@+id/detail_date_naiss"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Date de naissance: "
                android:textColor="@android:color/black"/>

            <TextView
                android:id="@+id/detail_sexe"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Sexe: "
                android:textColor="@android:color/black"/>

            <TextView
                android:id="@+id/detail_race"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Race: "
                android:textColor="@android:color/black"/>
            <Space
                android:layout_width="match_parent"
                android:layout_height="5dp" />

        </LinearLayout>


        <Space
            android:layout_width="match_parent"
            android:layout_height="10dp" />

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:textColor="@color/grey"
            android:text="Génétique :  "
            android:textSize="18dp"/>

        <LinearLayout
            android:id="@+id/animals_genetique_container"
            style = "@style/AnimalContainer"
            android:paddingLeft="10dp"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:backgroundTint="@color/blue"
            android:background="@drawable/border_shape"
            android:orientation="vertical">

            <Space
                android:layout_width="match_parent"
                android:layout_height="5dp" />

            <TextView
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:id="@+id/num_nat_pere"
                android:text="Numéro national du père: "/>

            <TextView
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:id="@+id/race_pere"
                android:text="Race du père: "/>

            <Space
                android:layout_width="match_parent"
                android:layout_height="10dp" />

            <TextView
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:id="@+id/num_nat_mere"
                android:text="Numéro national de la mère: "/>

            <TextView
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:id="@+id/race_mere"
                android:text="Race de la màre: "/>

            <Space
                android:layout_width="match_parent"
                android:layout_height="5dp" />


        </LinearLayout>


        <Space
            android:layout_width="match_parent"
            android:layout_height="30dp" />


        <LinearLayout
            android:id="@+id/buttonContainer"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:gravity="center"
            android:orientation="vertical">

            <Button
                android:id="@+id/buttonQRCode"
                style="@style/ButtonAnimalDetail"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:gravity="center"
                android:text="Afficher le QR Code" />

            <Button
                android:id="@+id/buttonTelecharger"
                style="@style/ButtonAnimalDetail"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:gravity="center"
                android:text="Télécharger la carte rose" />


        </LinearLayout>

    </LinearLayout>
</ScrollView>

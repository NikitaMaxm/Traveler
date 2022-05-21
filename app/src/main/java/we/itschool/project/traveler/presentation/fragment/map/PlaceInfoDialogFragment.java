package we.itschool.project.traveler.presentation.fragment.map;

import static we.itschool.project.traveler.data.api.opentripmapapi.APIConfigOTM.LANGUAGE;
import static we.itschool.project.traveler.presentation.fragment.map.MapFragment.mf;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import retrofit2.Response;
import we.itschool.project.traveler.R;
import we.itschool.project.traveler.data.api.opentripmapapi.ResponseOTMInf.ResponseOTMInfo;

public class PlaceInfoDialogFragment extends DialogFragment {
    private Response<ResponseOTMInfo> response;
    private String dist;
    private String url;
    private String address;
    private String tittle = "Описание ";
    private String text = "отсутствует \n";
    int str_to_fav = R.string.addToFavorite;
    int str_alr_in = R.string.alreadyInFavorite;
    private TableLayout tableLayout;

    private String checkIfNull(String str){ if (str==null){ return ""; }else { return str; } }
    public PlaceInfoDialogFragment(Response<ResponseOTMInfo> card, String distance) {
        this.response = card;
        this.dist = distance;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_fragment, null);
        tableLayout = view.findViewById(R.id.tl_dialog_content);

        String name = response.body().getName();

        if (!LANGUAGE.equals("ru")){
            if (Objects.equals(name, "")) {name = "Unnamed";}
            tittle = "Description ";
            text = "is missing \n";
            dist = "To place(m): " + String.valueOf((int)Double.parseDouble(dist));
            str_to_fav = R.string.addToFavorite_eng;
            str_alr_in = R.string.alreadyInFavorite_eng;
        } else {
            if (Objects.equals(name, "")) {name = "Без названия";}
            dist = "До места(м): " + String.valueOf((int)Double.parseDouble(dist));
        }
        url = checkIfNull(response.body().getWikipedia());
       address = checkIfNull(response.body().getAddress().getRoad()) + ", " +
                checkIfNull(response.body().getAddress().getHouse()) + " " +
                checkIfNull(response.body().getAddress().getHouseNumber()) + "\n";
        try {
            tittle = response.body().getWikipediaExtracts().getTitle();
            text = response.body().getWikipediaExtracts().getText()+ "\n";
        } catch (NullPointerException e){}

        TextView tv_title = new TextView(mf.getContext());
        tv_title.setText(tittle);
        tv_title.setTextColor(Color.BLACK);
        tv_title.setTextSize(20);
        tv_title.setPadding(14,7,14,7);

        TextView tv_text = new TextView(mf.getContext());
        tv_text.setTextSize(19);
        tv_text.setTextColor(Color.BLACK);
        tv_text.setText(text);
        tv_text.setPadding(14,7,14,7);

        TextView tv_dist = new TextView(mf.getContext());
        tv_dist.setText(dist);
        tv_dist.setTextSize(17);
        tv_dist.setPadding(14,7,14,7);

        TextView tv_address = new TextView(mf.getContext());
        if (address.equals(",")){
            address = "...";}
        tv_address.setText(address);
        tv_address.setTextSize(17);
        tv_address.setPadding(14,7,14,7);

        TextView tv_url = new TextView(mf.getContext());
        tv_url.setText(url);
        tv_url.setPadding(14,7,14,7);
        Linkify.addLinks(tv_url, Linkify.ALL);

        tableLayout.addView(tv_title);
        tableLayout.addView(tv_text);
        tableLayout.addView(tv_dist);
        tableLayout.addView(tv_address);
        tableLayout.addView(tv_url);
        tableLayout.setClickable(true);

        String kinds = response.body().getKinds();
        int bit;
        if (kinds.contains("historic")){
                bit = R.drawable.monument;
        }else if (kinds.contains("cultural")){
                bit = R.drawable.historical;
        }else if (kinds.contains("industrial_facilities")){
                bit = R.drawable.industrial;
        }else if (response.body().getName().length() == 0){
                bit = R.drawable.unknown;
        } else if (kinds.contains("natural")){
                bit = R.drawable.nature;
        } else if (kinds.contains("architecture")){
                bit = R.drawable.buildings;
        }else if (kinds.contains("other")){
                bit = R.drawable.forphoto;
        }else{
                bit = R.drawable.unknown;
        }

        AlertDialog.Builder placeInfo = new AlertDialog.Builder(getActivity());
        placeInfo.setTitle(name);
        placeInfo.setIcon(bit);
        placeInfo.setView(view);

        return placeInfo.create();
    }

}
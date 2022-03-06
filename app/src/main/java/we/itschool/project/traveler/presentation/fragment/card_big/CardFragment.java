package we.itschool.project.traveler.presentation.fragment.card_big;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import we.itschool.project.traveler.R;
import we.itschool.project.traveler.app.AppStart;
import we.itschool.project.traveler.domain.entity.Card;

public class CardFragment extends Fragment {

    private static final String ARGUMENT_CARD_GSON = "card Gson";

    private Card card;

    public static CardFragment newInstance(
            String cardGson
    ) {
        Bundle args = new Bundle();
        args.putString(ARGUMENT_CARD_GSON, cardGson);
        CardFragment fragment = new CardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void parseParams() {
        Bundle args = requireArguments();
        if (!args.containsKey(ARGUMENT_CARD_GSON))
            throw new RuntimeException("Argument '\''card gson'\'' is absent");
        String cardGson = args.getString(ARGUMENT_CARD_GSON);
        card = (new Gson()).fromJson(cardGson, Card.class);
        if (AppStart.isLog) {
            Log.w("AboutPeople", "Incoming parseParams:   " + cardGson + "\n");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseParams();
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(
                R.layout.fragment_card_big_view,
                container,
                false
        );
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        String mDrawableCard = card.getCardInfo().getPathToPhoto();
        int resIDCard = view.getContext().getResources().getIdentifier(
                mDrawableCard,
                "drawable",
                view.getContext().getPackageName()
        );
        String mDrawablePerson = card.getCardInfo()
                .getPerson().getPersonInfo().getPathToPhoto();
        int resIDPerson = view.getContext().getResources().getIdentifier(
                mDrawablePerson,
                "drawable",
                view.getContext().getPackageName()
        );

        ((ImageView) view.findViewById(R.id.iv_main_image_big_card))
                .setImageResource(resIDCard);
        ((ImageView) view.findViewById(R.id.iv_avatar_image_big_card))
                .setImageResource(resIDPerson);
        ((TextView) view.findViewById(R.id.tv_avatar_profile_data_small_FN))
                .setText(
                        card.getCardInfo()
                                .getPerson().getPersonInfo().getFirstName()
                        );
        ((TextView) view.findViewById(R.id.tv_avatar_profile_data_small_SN))
                .setText(
                        card.getCardInfo()
                                        .getPerson().getPersonInfo().getSecondName()
                );
        ((TextView) view.findViewById(R.id.tv_description_long))
                .setText(card.getCardInfo().getFullDescription());
    }
}
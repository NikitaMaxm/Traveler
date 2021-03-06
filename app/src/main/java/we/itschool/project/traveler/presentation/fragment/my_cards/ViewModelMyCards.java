package we.itschool.project.traveler.presentation.fragment.my_cards;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import traveler.module.domain.entity.CardEntity;
import traveler.module.domain.usecases.user.UserGetUserCardsUseCase;
import we.itschool.project.traveler.app.AppStart;

public class ViewModelMyCards extends ViewModel {

    private final MutableLiveData<ArrayList<CardEntity>> cardsLiveDataList;
    UserGetUserCardsUseCase getCardsUC = AppStart.uGetUserCardsUC;

    public ViewModelMyCards() {
        cardsLiveDataList = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<CardEntity>> getCardList() {
        cardsLiveDataList.postValue(getCardsUC.getUserCards());
        return cardsLiveDataList;
    }

}

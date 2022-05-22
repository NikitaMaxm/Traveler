package we.itschool.project.traveler.data.repositoryImpl;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import we.itschool.project.traveler.app.AppStart;
import we.itschool.project.traveler.data.api.travelerapi.service.APIServiceTravelerConstructor;
import we.itschool.project.traveler.data.api.travelerapi.entityserv.CardServ;
import we.itschool.project.traveler.data.api.travelerapi.mapper.CardEntityMapper;
import we.itschool.project.traveler.data.api.travelerapi.service.APIServiceCard;
import we.itschool.project.traveler.data.api.travelerapi.service.APIServiceStorage;
import we.itschool.project.traveler.data.datamodel.CardModelPOJO;
import we.itschool.project.traveler.domain.entity.CardEntity;
import we.itschool.project.traveler.domain.repository.CardDomainRepository;

public class CardArrayListRepositoryImpl implements CardDomainRepository {

    private final MutableLiveData<ArrayList<CardEntity>> dataLiveData;
    private final MutableLiveData<ArrayList<CardEntity>> dataLiveDataUserCards;
    private final ArrayList<CardEntity> data;
    private final ArrayList<CardEntity> dataUserCards;

    {
        data = new ArrayList<>();
        dataUserCards = new ArrayList<>();
        dataLiveData = new MutableLiveData<>();
        dataLiveData.postValue(data);
        dataLiveDataUserCards = new MutableLiveData<>();
        dataLiveDataUserCards.postValue(dataUserCards);
    }

    private static int autoIncrementId = 0;

    //only for test!!!
    private static int USER_ID_FOR_TEST = 22;

    private static int NUM_OF_CARDS_TO_DO;

    static {
        NUM_OF_CARDS_TO_DO = 0;
    }

    /**
     * Sends photo to created card as a resource.
     * Then sends http request to add new card to user cards for MyCardsFragment
     *
     * @param path path to photo in user app
     * @param cid  card id
     */
    public void uploadPhotoToCard(String path, int cid) {
        //called from retrofit.execute so no need for extra thread!
//        AsyncTask.execute(() -> {

            APIServiceStorage service = APIServiceTravelerConstructor.CreateService(APIServiceStorage.class);
            //pass it like this
//            Log.v("retrofitLogger", "get file from image");
            File file = new File(path);
//            Log.v("retrofitLogger", "fill it in request Body");
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

//          MultipartBody.Part is used to send also the actual file name
//            Log.v("retrofitLogger", "fill it in multipart Body");
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("file", file.getName(), requestFile);
//            Log.v("retrofitLogger", "Make call");
            RequestBody cidBody = RequestBody.create(MediaType.parse("multipart/from-data"), String.valueOf(cid));
            Call<String> call = service.uploadPhotoToCard2(body, cidBody);
//            Log.v("retrofitLogger", "trying to send data");
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.body() != null) {
                        Log.v("retrofitLogger", "IMAGE_SENT:" + response.body().toString());
                        //add new card to users account
                        loadCardToUser(cid);

                    } else {
                        Log.v("retrofitLogger", "null response.body on IMAGE_SENT");
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                    Log.v("retrofitLogger", "some error!!! on failure IMAGE_SENT id:" + cid +
                            " t:" + t.getMessage());
                }
            });
//        });
    }

    /**
     * loads new card from server for user cards
     *
     * @param id card id
     */
    private void loadCardToUser(int id) {
//        AsyncTask.execute(() -> {

        APIServiceCard service = APIServiceTravelerConstructor.CreateService(APIServiceCard.class);
        Call<String> call = service.getOneCardById(id);
        //Log.v(MainActivity.TAG, "start catching the answer");
        call.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.body() != null) {
                    String str = response.body().toString();
                    Log.v("retrofitLogger", "card.toString():" + str);
                    addCardToMutableDataUserCards(CardEntityMapper.toCardEntityFormCardServ(
                            (new Gson()).fromJson(str, CardServ.class)
                            , true));

                } else {
                    Log.v("retrofitLogger", "null response.body on loadDataRetrofit");
                }
            }

            @Override
            public void onFailure(retrofit2.Call<String> call, Throwable t) {
                Log.v("retrofitLogger", "some error!!! on failure id:" + id +
                        " t:" + t.getMessage());
            }
        });
//        });
    }

    /**
     * loads new card from server for main feed
     *
     * @param id card id to load
     */
    private void loadDataRetrofit(int id) {
//        AsyncTask.execute(() -> {

        APIServiceCard service = APIServiceTravelerConstructor.CreateService(APIServiceCard.class);
        Call<String> call = service.getOneCardById(id);
        //Log.v(MainActivity.TAG, "start catching the answer");
        call.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.body() != null) {
                    String str = response.body().toString();
                    Log.v("retrofitLogger", "card.toString():" + str);
                    addCardToMutableData(CardEntityMapper.toCardEntityFormCardServ(
                            (new Gson()).fromJson(str, CardServ.class)
                            , true));

                } else {
                    Log.v("retrofitLogger", "null response.body on loadDataRetrofit");
                }
            }

            @Override
            public void onFailure(retrofit2.Call<String> call, Throwable t) {
                Log.v("retrofitLogger", "some error!!! on failure id:" + id +
                        " t:" + t.getMessage());
            }
        });
//        });
    }


    private void updateLiveData() {
//        AsyncTask.execute(() -> {
        dataLiveData.postValue(data);
//        });
    }

    private void updateLiveDataUserCards() {
//        AsyncTask.execute(() -> {
        dataLiveDataUserCards.postValue(dataUserCards);
//        });
    }

    /**
     * Method creates new card. All data represent using CardModel as a POJO
     *
     * @param model of card created by user
     */
    @Override
    public void cardCreateNew(CardModelPOJO model) {
        AsyncTask.execute(() -> {
            APIServiceCard service = APIServiceTravelerConstructor.CreateService(APIServiceCard.class);
            Call<String> call = service.addNewCardGson(AppStart.getUser().get_id(), CardEntityMapper.toCardServFromCardModelPOJO(model));
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.body() != null) {
                        Log.v("retrofitLogger", "card.toString():" + response.body().toString());
                        CardServ cardServ = (new Gson()).fromJson(response.body().toString(), CardServ.class);
                        int cid = cardServ.getId();
                        uploadPhotoToCard(model.getPathToPhoto(), cid);

                    } else {
                        Log.v("retrofitLogger", "null response.body on sendNewCardToSeverRetrofit");
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.v("retrofitLogger", "some error!!! on failure, Cannot add new Card toServer:" +
                            " t:" + t.getMessage());
                }
            });
        });
    }

    private void addCardToMutableData(CardEntity card){
        data.add(card);
        updateLiveData();
    }

    protected void addCardToMutableDataUserCards(CardEntity card) {
        dataUserCards.add(card);
        updateLiveDataUserCards();
    }

    @Override
    public void cardAddNew() {
//        if (NUM_OF_CARDS_TO_DO == 2) NUM_OF_CARDS_TO_DO = 13;
        loadDataRetrofit(++NUM_OF_CARDS_TO_DO);
    }

    @Override
    public void cardAddUserCardToMutableList(CardEntity card){
        addCardToMutableData(card);
    }

    @Override
    public void cardDeleteById(CardEntity card) {
        data.remove(card);
        updateLiveData();
    }

    @Override
    public void cardEditById(CardEntity card) {
        CardEntity card_old = cardGetById(card.get_id());
        cardDeleteById(card_old);
        addCardToMutableData(card);
    }

    @Override
    public MutableLiveData<ArrayList<CardEntity>> cardGetAll() {
        return dataLiveData;
    }

    @Override
    public MutableLiveData<ArrayList<CardEntity>> getUserCards() {
        return dataLiveDataUserCards;
    }

    @Override
    public CardEntity cardGetById(int id) {
        CardEntity card = data.get(id);
        if (card != null)
            return card;
        else throw new RuntimeException("There is no element with id = " + id);
    }
}

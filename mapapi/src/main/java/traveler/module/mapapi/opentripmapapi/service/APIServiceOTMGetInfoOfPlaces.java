package traveler.module.mapapi.opentripmapapi.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import traveler.module.mapapi.opentripmapapi.ResponseOTMInf.ResponseOTMInfo;

public interface APIServiceOTMGetInfoOfPlaces {

    @GET("0.1/{lang}/places/xid/{xid}")
    Call<ResponseOTMInfo> getInfo(
            @Path(value = "xid", encoded = true) String xid,
            @Path(value = "lang", encoded = true) String lang,
            @Query("apikey") String key
    );

}

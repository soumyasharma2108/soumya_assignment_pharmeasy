package practice.soumya.com.soumya_assignment.networking;


import practice.soumya.com.soumya_assignment.models.Result;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;


public interface NetworkService {
    @GET("api/users")
    Observable<Result> getTopResults(
            @Query("page") int pageIndex
    );

}
